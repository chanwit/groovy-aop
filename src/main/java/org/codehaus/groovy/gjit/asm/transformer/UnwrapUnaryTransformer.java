package org.codehaus.groovy.gjit.asm.transformer;

import java.util.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.*;
import org.codehaus.groovy.gjit.asm.*;

public class UnwrapUnaryTransformer implements Transformer, Opcodes {

    private static final int PREVIOUS = -1;
    private static final int NEXT     =  1;

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
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
        // locate unary op
        //
        s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != INVOKEINTERFACE) { s = s.getNext(); continue; }
            MethodInsnNode m = (MethodInsnNode)s;
            if(m.name.equals("call")==false) { s = s.getNext(); continue; }
            if(m.desc.equals("(Ljava/lang/Object;)Ljava/lang/Object;")==false) { s = s.getNext(); continue; }
            if(m.owner.equals("org/codehaus/groovy/runtime/callsite/CallSite")==false) { s = s.getNext(); continue; }

            ReverseStackDistance rsd = new ReverseStackDistance(m);
            AbstractInsnNode start = rsd.findStartingNode();
            if(start.getOpcode() != ALOAD) { s = s.getNext(); continue; }

            AbstractInsnNode callSiteIndexHolder = start.getNext();
            if(callSiteIndexHolder.getOpcode() != LDC) { s = s.getNext(); continue; }

            int callSiteIndex = (Integer)((LdcInsnNode)callSiteIndexHolder).cst;
            String callSiteName = names[callSiteIndex];
            int op = 0;
            if(callSiteName.equals("previous"))  op = PREVIOUS;
            else if(callSiteName.equals("next")) op = NEXT;

            if(op == 0) { s = s.getNext(); continue; }

            PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(body, start, m);
            Map<AbstractInsnNode, AbstractInsnNode[]> usedMap = pdua.analyse();
            AbstractInsnNode[] array = usedMap.get(m);
            Type type = Utils.getType(array[1]);
            System.out.println("unary " + array[1]);
            System.out.println("unary type: " + type);
            System.out.println("call site index: " + callSiteIndex);
            if(type.getDescriptor().equals("Ljava/lang/Integer;")) {

                // TODO DUP
                // manage DUP
//                AbstractInsnNode mayBeDup = s.getNext();
//                if(mayBeDup.getOpcode() == DUP) {
//                    units.insert(mayBeDup, Utils.getBoxNode(int.class));
//                }
                //
                // unbox(int)
                // ICONST_x
                // IADD
                // box(int)
                units.insert(array[1], Utils.getUnboxNodes(int.class));
                AbstractInsnNode newS = new InsnNode(IADD);
                units.set(s, newS);
                units.insertBefore(newS, new InsnNode(ICONST_0 + op));
                units.insert(newS, Utils.getBoxNode(int.class));

                //
                // clean unused ALOAD, LDC, AALOAD
                //
                units.remove(start.getNext().getNext());
                units.remove(start.getNext());
                units.remove(start);

                s = newS.getNext();
                continue;
            } else {
            	s = s.getNext();
            	continue;
            }
        }
    }
}
