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

import org.slf4j.*; 

/**
 * This class unwraps binary operators for a generated type advised class.
 *
 * @author chanwit
 *
 */
public class UnwrapBinOpTransformer implements Transformer, Opcodes {

    private final Logger log = LoggerFactory.getLogger(UnwrapBinOpTransformer.class);

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
        log.info("Finding CallSiteArray");
        while(s != null) {
            if(s.getOpcode() != INVOKESTATIC) { s = s.getNext(); continue; }

            MethodInsnNode m = (MethodInsnNode)s;
            if(m.name.equals("$getCallSiteArray") &&
               m.desc.equals("()[Lorg/codehaus/groovy/runtime/callsite/CallSite;")) {
                owner = m.owner;
                AbstractInsnNode s0 = m.getNext();
                assert s0.getOpcode() == ASTORE;
                callSiteArray = ((VarInsnNode)s0).var;
                log.info("CallSiteArray is found {}", callSiteArray);
                break;
            } else {
                s = s.getNext();
            }
        }

        //CallSite
        log.info("Obtaining call site name array of {}", owner);
        if(CallSiteNameHolder.v().containsKey(owner)) {
            names = CallSiteNameHolder.v().get(owner);
            log.info("... Obtained {}", names);
        } else {
            throw new RuntimeException("No call site name array of class: " + owner);
        }

        //
        // locate bin op
        //
        log.info("Locating binary op");
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
            log.info("Current callSiteIndex {}", callSiteIndex);
            String callSiteName = names[callSiteIndex];
            log.info("Current callSiteName {}", callSiteName);
            BinaryOperator op = null;
            try {
                op = BinaryOperator.valueOf(callSiteName);
            } catch (Exception e) {
                op = null;
            }
            if (op == null) {s = s.getNext(); continue; }

            log.info("Opcode of starting node got from RSD {}", AbstractVisitor.OPCODES[start.getOpcode()]);
            
            PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(body, start, m);
            Map<AbstractInsnNode, AbstractInsnNode[]> usedMap = pdua.analyse();
            AbstractInsnNode[] array = usedMap.get(m);
            log.info("Used Map size {}", array.length);
//            for (int i = 0; i < array.length; i++) {
//                System.out.println(AbstractVisitor.OPCODES[array[i].getOpcode()]);
//                AbstractInsnNode[] x = usedMap.get(array[i]);
//                if(x != null) {
//                    for(int j = 0;j < x.length; j++) {
//                        System.out.println("  >>" + x[j]);                        
//                        // System.out.println("  >>" + AbstractVisitor.OPCODES[x[j].getOpcode()]);
//                    }
//                }
//            }
            Type t1 = Utils.getType(array[1]);
            Type t2 = Utils.getType(array[2]);

            // not doing unwrap on Object
            if(t1.getDescriptor().equals("Ljava/lang/Object;")) {s = s.getNext(); continue; }
            if(t2.getDescriptor().equals("Ljava/lang/Object;")) {s = s.getNext(); continue; }

            int offset = 0;
            AbstractInsnNode newS = null;
            AbstractInsnNode conv1 = null;
            AbstractInsnNode conv2 = null;
            Type resultType;

            if(t1.equals(t2)) {
                if(t1.getDescriptor().equals("Ljava/lang/Long;"))   offset = 1;
                if(t1.getDescriptor().equals("Ljava/lang/Float;"))  offset = 2;
                if(t1.getDescriptor().equals("Ljava/lang/Double;")) offset = 3;
                resultType = t1;
            } else {
                String bin = getPrimitiveSignatures(t1, t2);
                if(bin.equals("IJ")) {
                    offset = 1; // use Long operation
                    conv1  = new InsnNode(I2L);
                    conv2  = null;
                    resultType = Type.getType("Ljava/lang/Long;");
                } else if (bin.equals("JI")) {
                    offset = 1; // use Long operation
                    conv1  = null;                    
                    conv2  = new InsnNode(I2L);
                    resultType = Type.getType("Ljava/lang/Long;");
                } else {
                    s = s.getNext();
                    continue;
                    // throw new RuntimeException("NYI for signature: " + bin);
                }
            }

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
            AbstractInsnNode mayBeDup;
            mayBeDup = array[1].getNext();
            if(mayBeDup.getOpcode() == DUP) {
                units.insert(mayBeDup, Utils.getBoxNode(t1.getDescriptor()));
            }
            mayBeDup = array[2].getNext();
            if(mayBeDup.getOpcode() == DUP) {
                units.insert(mayBeDup, Utils.getBoxNode(t2.getDescriptor()));
            }

            if(conv1 != null) {
                units.insert(array[1], conv1);
            }
            units.insert(array[1], Utils.getUnboxNodes(t1.getDescriptor()));            
            if(conv2 != null) {
                units.insert(array[2], conv2);
            }            
            units.insert(array[2], Utils.getUnboxNodes(t2.getDescriptor()));

            //
            // replace with native op, and box it back to an object
            //
            units.set(s, newS);
            units.insert(newS, Utils.getBoxNode(resultType));

            //
            // clean unused ALOAD, LDC, AALOAD
            //
            units.remove(start.getNext().getNext());
            units.remove(start.getNext());
            units.remove(start);

            s = newS.getNext();
            continue;
        }
    }

    private String getPrimitiveSignatures(Type t1, Type t2) {
        //Ljava/lang/Integer;
        //Ljava/lang/Long;
        //Ljava/lang/Byte;
        //Ljava/lang/Boolean;
        //Ljava/lang/Short;
        //Ljava/lang/Double;
        //Ljava/lang/Character;
        //Ljava/lang/Float;
        char p1 = t1.getDescriptor().charAt(11);
        if(p1 == 'L') p1 = 'J';
        else if(p1 == 'B' && t1.getDescriptor().charAt(12)=='o') p1 = 'Z';

        char p2 = t2.getDescriptor().charAt(11);
        if(p2 == 'L') p2 = 'J';
        else if(p2 == 'B' && t2.getDescriptor().charAt(12)=='o') p2 = 'Z';
        
        return p1 + p2;
    }
}
