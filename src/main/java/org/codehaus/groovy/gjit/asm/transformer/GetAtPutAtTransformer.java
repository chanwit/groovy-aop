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

        //
        // locate getAt, putAt
        //
        s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != INVOKEINTERFACE) { s = s.getNext(); continue; }
            MethodInsnNode m = (MethodInsnNode)s;
            if(m.name.equals("call")==false) { s = s.getNext(); continue; }
            if(m.desc.equals("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")==false) { s = s.getNext(); continue; }
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
            Type t0 = Utils.getType(array[1], argTypes);
            if(t0.getSort() == Type.ARRAY) {
                // [D -> elemType is D
                Type elemType = t0.getElementType();
                switch(elemType.getSort()) {
                    case Type.DOUBLE: {
                        if(callSiteName.equals("getAt")) {
                            // array[2] must be unboxed to "int" for indexing
                            // if it's an object, do boxing
                            if(Utils.getType(array[2]).getSort() == Type.OBJECT) {
                                units.insert(array[2], Utils.getUnboxNodes(int.class));
                            } else {
                                throw new RuntimeException("NYI");
                            }
                            InsnNode newS = new InsnNode(DALOAD);
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
                            InsnNode newS = new InsnNode(DASTORE);
                            units.set(m, newS);                            
                            units.insert(array[3], Utils.getUnboxNodes(elemType));

                            // clean up
                            units.remove(start.getNext().getNext());
                            units.remove(start.getNext());
                            units.remove(start);
                            
                            s = newS.getNext();
                            continue;
                        }
                    }
                }
            }
            s = s.getNext();
        }

    }

}
