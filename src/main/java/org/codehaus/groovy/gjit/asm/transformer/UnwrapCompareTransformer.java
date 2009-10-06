package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.asm.PartialDefUseAnalyser;
import org.codehaus.groovy.gjit.asm.ReverseStackDistance;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class UnwrapCompareTransformer implements Transformer, Opcodes {

//    ILOAD 0
//    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//    LDC 0
//    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//    INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.compareLessThan(Ljava/lang/Object;Ljava/lang/Object;)Z
//    IFEQ L2

//    ILOAD 0
//    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//    CHECKCAST java/lang/Integer
//    INVOKESTATIC unbox
//    LDC 0
//    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//    CHECKCAST java/lang/Integer
//    INVOKESTATIC unbox
//    INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.compareLessThan(Ljava/lang/Object;Ljava/lang/Object;)Z
//    IFEQ L2


    private enum ComparingMethod {
    	compareNotEqual,
        compareEqual,
        compareLessThan,
        compareGreaterThan,
        compareLessThanEqual,
        compareGreaterThanEqual
    };

    private static final String SCRIPT_BYTECODE_ADAPTER = "org/codehaus/groovy/runtime/ScriptBytecodeAdapter";
    private static final String COMPARE_METHOD_DESC = "(Ljava/lang/Object;Ljava/lang/Object;)Z";

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        // Steps
        // 1. find ScriptBytecodeAdapter.compareXXX
        // 2. get starting point
        // 3. doing partial def-use
        // 4. check types in used map
        // if type of op1 and op2 are same, do conversion
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != INVOKESTATIC) { s = s.getNext(); continue; }

            MethodInsnNode m = (MethodInsnNode)s;
            if(m.owner.equals(SCRIPT_BYTECODE_ADAPTER) &&
               m.name.startsWith("compare")            &&
               m.desc.equals(COMPARE_METHOD_DESC))
            {
                ComparingMethod compare;
                try {
                    compare = ComparingMethod.valueOf(m.name);
                } catch (IllegalArgumentException e) {
                    System.out.println("get compare method failed: " + m.name );
                    s = s.getNext();
                    continue;
                }

                ReverseStackDistance rvd = new ReverseStackDistance(m);
                AbstractInsnNode start = rvd.findStartingNode();
                PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(body, start, m);
                Map<AbstractInsnNode, AbstractInsnNode[]> usedMap = pdua.analyse();
                AbstractInsnNode[] array = usedMap.get(m);
                assert array.length == 2;
                Type t0 = Utils.getType(array[0]);
                Type t1 = Utils.getType(array[1]);
                // System.out.println(t0);
                // System.out.println(t1);
                // not doing anything for java/lang/Object
                if(t0.equals(t1) && (t0.getDescriptor().equals("Ljava/lang/Object;") == false)) {
                    units.insert(array[0], Utils.getUnboxNodes(t0.getDescriptor()));
                    units.insert(array[1], Utils.getUnboxNodes(t1.getDescriptor()));

                    if(t0.getDescriptor().equals("Ljava/lang/Integer;")) {
                        if(compare == ComparingMethod.compareEqual) {
                        	try {
	                            JumpInsnNode oldIf = (JumpInsnNode)(s.getNext());

	                            AbstractInsnNode trueLabel = oldIf.getNext();
	                            while(trueLabel instanceof LabelNode == false) {
	                            	trueLabel = trueLabel.getNext();
	                            }

	                            int cmp = -1;
	                            if(oldIf.getOpcode() == IFEQ) cmp = IF_ICMPNE;
	                            else if(oldIf.getOpcode() == IFNE) cmp = IF_ICMPEQ;

	                            if(cmp == -1) throw new RuntimeException("NYI");
	                            AbstractInsnNode newS;
	                            if(oldIf.getOpcode() == IFEQ) {
		                            newS = new JumpInsnNode(cmp, (LabelNode)trueLabel);
		                            units.set(s, newS);
		                            units.insert(newS, new JumpInsnNode(GOTO, oldIf.label));
		                            units.remove(oldIf);
	                            } else {
	                            	newS = new JumpInsnNode(cmp, oldIf.label);
	                            	units.set(s, newS);
	                            	units.remove(oldIf);
	                            }
	                            s = newS.getNext();
	                            continue;
                        	} catch(ClassCastException e) {
                        		s = s.getNext();
                        		continue;
                        	}
                        } else if(compare == ComparingMethod.compareNotEqual) {
                        	try {
	                            JumpInsnNode oldIf = (JumpInsnNode)(s.getNext());
	                            LabelNode falseLabel = (LabelNode)(oldIf.getNext());
	                            AbstractInsnNode newS = new JumpInsnNode(IF_ICMPNE, falseLabel);
	                            units.set(s, newS);
	                            units.insert(newS, new JumpInsnNode(GOTO, oldIf.label));
	                            units.remove(oldIf);
	                            s = newS.getNext();
	                            continue;
                        	} catch(ClassCastException e) {
                        		s = s.getNext();
                        		continue;
                        	}
                        } else {
                            AbstractInsnNode newS = convertCompareForInt(compare, s);
                            units.set(s, newS);
                            AbstractInsnNode oldIf = newS.getNext();
                            s = oldIf.getNext();
                            units.remove(oldIf);
                            continue;
                        }
                    } else if(t0.getDescriptor().equals("Ljava/lang/Double;")) {
                        AbstractInsnNode[] newNodes = convertCompare(DCMPL, compare, s);
                        units.set(s, newNodes[0]);
                        units.insert(newNodes[0], newNodes[1]);
                        AbstractInsnNode oldIf = newNodes[1].getNext();
                        s = oldIf.getNext();
                        units.remove(oldIf);
                        continue;
                    } else
                        throw new RuntimeException("NYI "+ t0 + ", " +t1);

//                    switch(t0.getSort()) {
//                        case Type.INT: {
//                            }
//                            continue;
//                        default:
//                            throw new RuntimeException("NYI");
                        // TODO: case Type.LONG:   convertCompare(LCMP, compare, s); break;
                        // TODO: case Type.FLOAT:  convertCompare(FCMPL, compare, s); break;
                        // TODO: case Type.DOUBLE: convertCompare(DCMPL, compare, s); break;
                }
            }
            s = s.getNext();
        }
    }

    private JumpInsnNode convertCompareForInt(ComparingMethod compare, AbstractInsnNode s) {
        JumpInsnNode s1 = (JumpInsnNode) s.getNext();
        switch (compare) {
            case compareGreaterThan:
                return new JumpInsnNode(IF_ICMPLE, s1.label);
            case compareGreaterThanEqual:
                return new JumpInsnNode(IF_ICMPLT, s1.label);
            case compareLessThan:
                return new JumpInsnNode(IF_ICMPGE, s1.label);
            case compareLessThanEqual:
                return new JumpInsnNode(IF_ICMPGT, s1.label);
            default:
                throw new RuntimeException("NYI");
        }
    }


    private AbstractInsnNode[] convertCompare(int opcode, ComparingMethod compare, AbstractInsnNode s) {
        AbstractInsnNode[] result = new AbstractInsnNode[2];
        JumpInsnNode s1 = (JumpInsnNode) s.getNext();
        result[0] = new InsnNode(opcode);
        //units.set(s, );
        switch (compare) {
            case compareGreaterThan:
                result[1] = new JumpInsnNode(IFLE, s1.label);
                break;
            case compareGreaterThanEqual:
                result[1] = new JumpInsnNode(IFLT, s1.label);
                break;
            case compareLessThan:
                result[1] = new JumpInsnNode(IFGE, s1.label);
                break;
            case compareLessThanEqual:
                result[1] = new JumpInsnNode(IFGT, s1.label);
                break;
        }
        return result;
    }

}
