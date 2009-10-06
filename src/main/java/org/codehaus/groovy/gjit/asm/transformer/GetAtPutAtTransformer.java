package org.codehaus.groovy.gjit.asm.transformer;

import java.util.*;

import org.objectweb.asm.*;
import org.objectweb.asm.util.*;
import org.objectweb.asm.tree.*;
import org.codehaus.groovy.gjit.asm.*;

public class GetAtPutAtTransformer implements Transformer, Opcodes {

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        Type[] argTypes   = (Type[])options.get("argTypes");
        Type   returnType = (Type)  options.get("returnType");

        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();

        String owner = null;
        int callSiteArray = -1;
        String[] names = null;

        //
        // finding call site array
        //
        while(s != null) {
            if(s.getOpcode() != INVOKESTATIC) { s = s.getNext(); continue; }

            MethodInsnNode m = (MethodInsnNode)s;
            if(m.name.equals("$getCallSiteArray") &&
               m.desc.equals("()[Lorg/codehaus/groovy/runtime/callsite/CallSite;")) {
                owner = m.owner;
                AbstractInsnNode s0 = m.getNext();
                assert s0.getOpcode() == ASTORE;
                callSiteArray = ((VarInsnNode)s0).var;
                break;
            } else {
                s = s.getNext();
            }
        }

        //CallSite
        if(CallSiteNameHolder.v().containsKey(owner)) {
            names = CallSiteNameHolder.v().get(owner);
        } else {
            throw new RuntimeException("No call site name array of class: " + owner);
        }

        Type[] localTypes = new Type[body.maxLocals];
        //
        // locate getAt, putAt
        //
        s = units.getFirst();
        while(s != null) {
            collectLocalTypes(localTypes, s);

            if(s.getOpcode() != INVOKEINTERFACE) { s = s.getNext(); continue; }
            MethodInsnNode m = (MethodInsnNode)s;
            if(m.name.equals("call")==false) { s = s.getNext(); continue; }
            if(m.owner.equals("org/codehaus/groovy/runtime/callsite/CallSite")==false) { s = s.getNext(); continue; }

            ReverseStackDistance rsd = new ReverseStackDistance(m);
            AbstractInsnNode start = rsd.findStartingNode();
            if(start.getOpcode() != ALOAD) { s = s.getNext(); continue; }

            AbstractInsnNode callSiteIndexHolder = start.getNext();
            if(callSiteIndexHolder.getOpcode() != LDC) { s = s.getNext(); continue; }

            int callSiteIndex = (Integer)((LdcInsnNode)callSiteIndexHolder).cst;
            String callSiteName = names[callSiteIndex];

            // not getAt, not putAt then continue
            if(!(callSiteName.equals("getAt") || callSiteName.equals("putAt"))) { s = s.getNext(); continue; }

            // doing analysis to get
            // - array[0], the call site object
            // - array[1], the object
            // - array[2], the index
            PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(body, start, m);
            Map<AbstractInsnNode, AbstractInsnNode[]> usedMap = pdua.analyse();
            AbstractInsnNode[] array = usedMap.get(m);

            // special call of getType to also obtain type information from argTypes
            // for example, if it's [D then
            Type t0 = resolveType(array[1], argTypes, localTypes);
            if(t0.getSort() == Type.ARRAY) {
                // [D -> elemType is D
                boolean convert = false;
                int load=0, store=0;
                Type elemType = t0.getElementType();
                switch(elemType.getSort()) {
                    case Type.BOOLEAN:
                    case Type.BYTE:
                        load  = BALOAD;
                        store = BASTORE;
                        convert = true;
                        break;
                    case Type.INT:
                    	load  = IALOAD;
                    	store = IASTORE;
                    	convert = true;
                    	break;
                    case Type.LONG:
                    	load  = LALOAD;
                    	store = LASTORE;
                    	convert = true;
                    	break;
                    case Type.DOUBLE:
                        load  = DALOAD;
                        store = DASTORE;
                        convert = true;
                        break;
                }
                if(convert == false) { s = s.getNext(); continue; }

                if(callSiteName.equals("getAt")) {
                	// INDEXING
                    // array[2] must be unboxed to "int" for indexing
                    // if it's an object, do boxing
                    if(Utils.getType(array[2]).getSort() == Type.OBJECT) {
                        AbstractInsnNode dup = array[2].getNext();
                        // TODO DUP special case, when it also makes itself into TOS
                        // need checking for anther opcode
//                        if(dup.getOpcode() == DUP) {
//                            units.insert(dup, Utils.getBoxNode(int.class));
//                        }
                        units.insert(array[2], Utils.getUnboxNodes(int.class));
                    } else {
                        throw new RuntimeException("NYI");
                    }

                    //
                    // sometime the result of of the call gets dupped.
                    // BUT it should be OK as we ALWAYS box the result
                    //

                    InsnNode newS = new InsnNode(load);
                    units.set(m, newS);
                    units.insert(newS, Utils.getBoxNode(elemType));

                    // clean up
                    units.remove(start.getNext().getNext());
                    units.remove(start.getNext());
                    units.remove(start);

                    s = newS.getNext();
                    continue;
                } else if (callSiteName.equals("putAt")) {
                    // array[2] must be the "int" index
                    // array[3] must be a value of elemType, if it's [D, then D
                    if(Utils.getType(array[2]).getSort() == Type.OBJECT) {
                        units.insert(array[2], Utils.getUnboxNodes(int.class));
                    } else {
                        throw new RuntimeException("NYI");
                    }
                    InsnNode newS = new InsnNode(store);
                    units.set(m, newS);
                    units.insert(array[3], Utils.getUnboxNodes(elemType));

                    // clean up
                    units.remove(start.getNext().getNext());
                    units.remove(start.getNext());
                    units.remove(start);

                    s = newS.getNext();
                    // this POP is the result from calling "putAt"
                    // as the "putAt"'s signature returns Ljava/lang/Object;
                    // it's always discarded,
                    // but xDSTORE does not need it.
                    if(s.getOpcode() == POP) { // unused POP
                        AbstractInsnNode oldS = s;
                        s = s.getNext();
                        units.remove(oldS);
                    }
                    continue;
                }
            }
            s = s.getNext();
        }
    }

    private Type resolveType(AbstractInsnNode a, Type[] argTypes, Type[] localTypes) {
        Type t = Utils.getType(a, argTypes);
        if(t.getDescriptor().equals("Ljava/lang/Object;")) {
            if(a.getOpcode() == ALOAD) {
                VarInsnNode v = (VarInsnNode)a;
                t = localTypes[v.var];
            }
        }
        return t;
    }

    private void collectLocalTypes(Type[] localTypes, AbstractInsnNode s) {
        if(s.getOpcode() >= ISTORE && s.getOpcode() <= ASTORE) {
            VarInsnNode v = (VarInsnNode)s;
            if(v.getOpcode() == ISTORE) localTypes[v.var] = Type.INT_TYPE;
            else if(v.getOpcode() == LSTORE) localTypes[v.var] = Type.LONG_TYPE;
            else if(v.getOpcode() == FSTORE) localTypes[v.var] = Type.FLOAT_TYPE;
            else if(v.getOpcode() == DSTORE) localTypes[v.var] = Type.DOUBLE_TYPE;
            else if(v.getOpcode() == ASTORE) {
                AbstractInsnNode p = v.getPrevious();
                if(p.getOpcode() == MULTIANEWARRAY) {
                    MultiANewArrayInsnNode mna = (MultiANewArrayInsnNode)p;
                    localTypes[v.var] = Type.getType(mna.desc);
                } else {
                    localTypes[v.var] = Type.getType("Ljava/lang/Object;");
                }
            }
        }
    }
}
