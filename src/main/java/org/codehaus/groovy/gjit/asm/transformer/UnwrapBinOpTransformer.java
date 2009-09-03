package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.asm.CallSiteNameHolder;
import org.codehaus.groovy.gjit.asm.PartialDefUseAnalyser;
import org.codehaus.groovy.gjit.asm.ReverseStackDistance;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.AbstractVisitor;

/**
 * This class unwraps binary operators for a generated type advised class.
 *
 * @author chanwit
 *
 */
public class UnwrapBinOpTransformer implements Transformer, Opcodes {

    private enum BinaryOperator {
        minus, plus, multiply, div, leftShift, rightShift
    }

//    INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
//    ASTORE 1
//
//    ALOAD 1
//    LDC 2
//    AALOAD
//    ILOAD 0
//    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//    ICONST_1
//    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

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
            throw new RuntimeException("No call site name array");
        }

        //
        // locate bin op
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
            BinaryOperator op = null;
            try {
                op = BinaryOperator.valueOf(callSiteName);
            } catch (Exception e) {
                op = null;
            }
            if (op == null) {s = s.getNext(); continue; }
            System.out.println(op.toString() + ": " + callSiteIndex);
            PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(body, start, m);
            Map<AbstractInsnNode, AbstractInsnNode[]> usedMap = pdua.analyse();
            AbstractInsnNode[] array = usedMap.get(m);
            System.out.println(array.length);
            for (int i = 0; i < array.length; i++) {
                System.out.println(AbstractVisitor.OPCODES[array[i].getOpcode()]);
            }
            Type t1 = Utils.getType(array[1]);
            Type t2 = Utils.getType(array[2]);
            if(t1.equals(t2)){
                int offset = 0;
                if(t1.getDescriptor().equals("Ljava/lang/Long;"))   offset = 1;
                if(t1.getDescriptor().equals("Ljava/lang/Float;"))  offset = 2;
                if(t1.getDescriptor().equals("Ljava/lang/Double;")) offset = 3;
                AbstractInsnNode newS = null;
                switch(op) {
                    case plus:       newS = new InsnNode(IADD + offset); break;
                    case minus:      newS = new InsnNode(ISUB + offset); break;
                    case multiply:   newS = new InsnNode(IMUL + offset); break;
                    case div:        newS = new InsnNode(IDIV + offset); break;
                    case leftShift:  newS = new InsnNode(ISHL + offset); break;
                    case rightShift: newS = new InsnNode(ISHR + offset); break;
                }
                //
                // we always assume that inputs are object,
                // so that unboxing them before proceed.
                //
                units.insert(array[1], Utils.getUnboxNodes(t1.getDescriptor()));
                units.insert(array[2], Utils.getUnboxNodes(t2.getDescriptor()));

                //
                // replace with native op, and box it back to an object
                //
                units.set(s, newS);
                units.insert(newS, Utils.getBoxNode(t1.getDescriptor()));

                s = newS.getNext();
                continue;
            }
            s = s.getNext();
        }
    }

}
