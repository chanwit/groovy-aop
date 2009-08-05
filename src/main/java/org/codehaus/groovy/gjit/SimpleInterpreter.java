package org.codehaus.groovy.gjit;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.AbstractVisitor;

public class SimpleInterpreter implements Opcodes {

//    ALOAD 1
//    LDC 26
//    AALOAD

//    ILOAD 8

//    ALOAD 1
//    LDC 27
//    AALOAD

//    ALOAD 1
//    LDC 28
//    AALOAD

//    INVOKESTATIC MicroTreeNode2.$get$$class$TreeNode()Ljava/lang/Class;
//    ALOAD 13
//    ILOAD 10
//    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;)Ljava/lang/Object;
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    private Stack<DefValue> stack = new Stack<DefValue>();
    private Map<AbstractInsnNode, AbstractInsnNode[]> used = new HashMap<AbstractInsnNode, AbstractInsnNode[]>();
    private AbstractInsnNode start;
    private AbstractInsnNode stop;
    private int maxStack;
    private InsnList units;

    // for debugging
    protected int currentIndex;

    public SimpleInterpreter(MethodNode methodNode, AbstractInsnNode s0, AbstractInsnNode s) {
        units = methodNode.instructions;
        maxStack = methodNode.maxStack;
        start = s0;
        stop = s;
    }

    private static final DefValue NULL_VALUE = new DefValue(null, Type.VOID_TYPE);

    private void prepareStack(AbstractInsnNode s0) {
        for(int i=0;i<maxStack;i++) {
            stack.push(NULL_VALUE);
        }
    }

    public Map<AbstractInsnNode, AbstractInsnNode[]> analyse() {
        AbstractInsnNode s0 = start;
        prepareStack(s0);
        // prepare fake stack entry for executing s0
        // DebugUtils.print(" stop at::: ");
        // DebugUtils.dump(stop);
        while(s0 != stop) {
            // DebugUtils.print(" simple::: ");
            // DebugUtils.dump(s0);
            execute(s0);
            s0 = s0.getNext();
        }
        execute(s0);
        return used;
    }

    private DefValue pop() {
        return stack.pop();
    }

    private DefValue peek() {
        return stack.peek();
    }

    private void push(DefValue defValue) {
        stack.push(defValue);
    }

    public void execute(AbstractInsnNode insn) {
        currentIndex = units.indexOf(insn);
        if(insn instanceof InsnNode) {
            execute0((InsnNode)insn);
        } else if(insn instanceof IntInsnNode) {
            execute0((IntInsnNode)insn);
        } else if(insn instanceof VarInsnNode) {
            execute0((VarInsnNode)insn);
        } else if(insn instanceof TypeInsnNode) {
            execute0((TypeInsnNode)insn);
        } else if(insn instanceof FieldInsnNode) {
            execute0((FieldInsnNode)insn);
        } else if(insn instanceof MethodInsnNode){
            execute0((MethodInsnNode)insn);
        } else if(insn instanceof JumpInsnNode) {
            execute0((JumpInsnNode)insn);
        } else if(insn instanceof LdcInsnNode) {
            execute0((LdcInsnNode)insn);
        } else if(insn instanceof IincInsnNode) {
            execute0((IincInsnNode)insn);
        }
    }

    private void execute0(TypeInsnNode insn) {
        switch(insn.getOpcode()) {
            case NEW:
                push(new DefValue(insn, Type.getType(Object.class)));
                break;
            case ANEWARRAY: {
                DefValue size = pop();
                used.put(insn, new AbstractInsnNode[]{size.source});
                push(new DefValue(insn, Type.getType(Object.class)));
            }
                break;
            case CHECKCAST: {
                DefValue obj = pop();
                used.put(insn, new AbstractInsnNode[]{obj.source});
                push(new DefValue(insn, Type.getType(Object.class)));
            }
                break;
            case INSTANCEOF: {
                DefValue obj = pop();
                used.put(insn, new AbstractInsnNode[]{obj.source});
                push(new DefValue(insn, Type.INT_TYPE));
            }
                break;
            default :
                throw new RuntimeException("not implemented yet: " + AbstractVisitor.OPCODES[insn.getOpcode()]);
        }
    }

    private void execute0(FieldInsnNode insn) {
        // GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
//        DebugUtils.toggle();
//        DebugUtils.dump(insn);
//        DebugUtils.toggle();
        switch(insn.getOpcode()) {
            case GETFIELD:
                used.put(insn, new AbstractInsnNode[]{pop().source});
                push(new DefValue(insn, Type.getType(insn.desc)));
                break;
            case PUTFIELD:
                used.put(insn, new AbstractInsnNode[]{pop().source, pop().source});
                break;
            case GETSTATIC:
                push(new DefValue(insn, Type.getType(insn.desc)));
                break;
            case PUTSTATIC:
                used.put(insn, new AbstractInsnNode[]{pop().source});
                break;
        }
        //DebugUtils.dump(insn);
        //throw new RuntimeException("not implemented yet: " + AbstractVisitor.OPCODES[insn.getOpcode()]);
    }

    private void execute0(MethodInsnNode insn) {
        Type[] argTypes = Type.getArgumentTypes(insn.desc);
        Type retType = Type.getReturnType(insn.desc);
        AbstractInsnNode[] paramSource;
        switch(insn.getOpcode()) {
            case INVOKESTATIC:
                paramSource = new AbstractInsnNode[argTypes.length];
                for(int i=argTypes.length-1;i >= 0;i--) {
                    DefValue v = pop();
                    paramSource[i] = v.source;
                }
                used.put(insn, paramSource);
                if(retType != Type.VOID_TYPE) push(new DefValue(insn, retType));
                break;
            case INVOKEINTERFACE:
            case INVOKESPECIAL:
            case INVOKEVIRTUAL:
                paramSource = new AbstractInsnNode[argTypes.length + 1];
                for(int i=argTypes.length-1;i >= 0;i--) {
                    DefValue v = pop();
                    paramSource[i+1] = v.source;
                }
                paramSource[0] = pop().source; // ref object
                used.put(insn, paramSource);
                if(retType != Type.VOID_TYPE) push(new DefValue(insn, retType));
                break;
        }
    }

    private void execute0(JumpInsnNode insn) {
        switch(insn.getOpcode()) {
            case IFEQ:
            case IFNE:
            case IFGE:
            case IFGT:
            case IFLE:
            case IFLT:
                used.put(insn, new AbstractInsnNode[]{pop().source});
                break;
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
            case IF_ICMPLT:
                // treat all of it as object,
                used.put(insn, new AbstractInsnNode[]{pop().source});
                break;

            case GOTO:
                break;
            default:
                throw new RuntimeException("not implemented yet: " + AbstractVisitor.OPCODES[insn.getOpcode()]);
        }
//        DebugUtils.dump(insn);
//        throw new RuntimeException("not implemented yet: " + AbstractVisitor.OPCODES[insn.getOpcode()]);
    }

    private void execute0(LdcInsnNode insn) {
        if(insn.cst instanceof Integer) push(new DefValue(insn, Type.INT_TYPE));
        else if(insn.cst instanceof Long) push(new DefValue(insn, Type.LONG_TYPE));
        else if(insn.cst instanceof Float) push(new DefValue(insn, Type.FLOAT_TYPE));
        else if(insn.cst instanceof Double) push(new DefValue(insn, Type.DOUBLE_TYPE));
        else push(new DefValue(insn, Type.getType(insn.cst.getClass())));
    }

    private void execute0(IincInsnNode insn) {
        // TODO Auto-generated method stub

    }

    private void execute0(VarInsnNode insn) {
        // TODO Auto-generated method stub
        switch(insn.getOpcode()) {
            case  ILOAD:
                    push(new DefValue(insn, Type.INT_TYPE));
                    break;
            case  LLOAD:
                    push(new DefValue(insn, Type.LONG_TYPE));
                    break;
            case  FLOAD:
                    push(new DefValue(insn, Type.FLOAT_TYPE));
                    break;
            case  DLOAD:
                    push(new DefValue(insn, Type.DOUBLE_TYPE));
                    break;
            case  ALOAD:
                    push(new DefValue(insn, Type.getType(Object.class)));
                    break;
            case  ISTORE:
            case  LSTORE:
            case  FSTORE:
            case  DSTORE:
            case  ASTORE:
                    DefValue v = pop();
                    used.put(insn, new AbstractInsnNode[]{v.source});
                    break;
        }
    }

    private void execute0(IntInsnNode insn) {
        switch(insn.getOpcode()){
            case BIPUSH:
                push(new DefValue(insn, Type.INT_TYPE));
                break;
            case SIPUSH:
                push(new DefValue(insn, Type.INT_TYPE));
                break;
            case NEWARRAY:
                throw new RuntimeException("not implemented yet");
        }
    }

    private void execute0(InsnNode insn) {
        /*
        NOP, ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1,
         *        ICONST_2, ICONST_3, ICONST_4, ICONST_5, LCONST_0, LCONST_1,
         *        FCONST_0, FCONST_1, FCONST_2, DCONST_0, DCONST_1, IALOAD, LALOAD,
         *        FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IASTORE, LASTORE,
         *        FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE, POP, POP2,
         *        DUP, DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP, IADD, LADD,
         *        FADD, DADD, ISUB, LSUB, FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV,
         *        LDIV, FDIV, DDIV, IREM, LREM, FREM, DREM, INEG, LNEG, FNEG, DNEG,
         *        ISHL, LSHL, ISHR, LSHR, IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR,
         *        LXOR, I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F,
         *        I2B, I2C, I2S, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN,
         *        FRETURN, DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW,
         *        MONITORENTER, or MONITOREXIT.
*/
        switch(insn.getOpcode()) {
            case ICONST_M1:
            case ICONST_0:
            case ICONST_1:
            case ICONST_2:
            case ICONST_3:
            case ICONST_4:
            case ICONST_5:
                push(new DefValue(insn, Type.INT_TYPE));
                break;
            case AASTORE: {
                DefValue val = pop();
                DefValue index = pop();
                DefValue aref = pop();
                used.put(insn, new AbstractInsnNode[]{val.source, index.source, aref.source});
                push(new DefValue(insn, Type.getType(Object.class)));
                }
                break;
            case AALOAD: {
                DefValue index = pop();
                DefValue aref = pop();
                used.put(insn, new AbstractInsnNode[]{index.source, aref.source});
                push(new DefValue(insn, Type.getType(Object.class)));
                }
                break;
            case POP: {
                DefValue tos = pop();
                used.put(insn, new AbstractInsnNode[]{tos.source});
                }
                break;
            case DUP:
            case DUP2: {
                DefValue tos = peek();
                used.put(insn, new AbstractInsnNode[]{tos.source});
                push(tos);
                }
                break;
            case IADD:
            case ISUB:
            case IMUL:
            case IDIV: {
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[]{arg0.source, arg1.source});
                push(new DefValue(insn, Type.INT_TYPE));
                }
                break;
            case LADD:
            case LSUB:
            case LMUL:
            case LDIV: {
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[]{arg0.source, arg1.source});
                push(new DefValue(insn, Type.LONG_TYPE));
                }
                break;
            case DADD:
            case DSUB:
            case DMUL:
            case DDIV: {
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[]{arg0.source, arg1.source});
                push(new DefValue(insn, Type.DOUBLE_TYPE));
                }
                break;
            case FADD:
            case FSUB:
            case FMUL:
            case FDIV: {
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[]{arg0.source, arg1.source});
                push(new DefValue(insn, Type.FLOAT_TYPE));
                }
                break;
            case ACONST_NULL:
                push(new DefValue(insn, Type.VOID_TYPE));
                break;
            case RETURN:
                break;
            case DCMPG:
            case DCMPL: {
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[] { arg0.source, arg1.source });
                push(new DefValue(insn, Type.INT_TYPE));
                }
                break;
            case L2D: {
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[] {arg0.source});
                push(new DefValue(insn, Type.DOUBLE_TYPE));
                }break;
            case DUP_X2: {
                DefValue arg2 = pop();
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[] {arg0.source, arg1.source, arg2.source});
                push(new DefValue(insn, Type.VOID_TYPE));
                push(new DefValue(insn, Type.VOID_TYPE));
                push(new DefValue(insn, Type.VOID_TYPE));
                push(new DefValue(insn, Type.VOID_TYPE));
                }break;
            case DUP2_X1: {
                DefValue arg2 = pop();
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[] {arg0.source, arg1.source, arg2.source});
                push(new DefValue(insn, Type.VOID_TYPE));
                push(new DefValue(insn, Type.VOID_TYPE));
                push(new DefValue(insn, Type.VOID_TYPE));
                push(new DefValue(insn, Type.VOID_TYPE));
                push(new DefValue(insn, Type.VOID_TYPE));
                }break;
            case POP2: {
                DefValue arg1 = pop();
                DefValue arg0 = pop();
                used.put(insn, new AbstractInsnNode[] {arg0.source, arg1.source});
                }break;
            default:
                throw new RuntimeException("not implemented yet: " + AbstractVisitor.OPCODES[insn.getOpcode()]);
        }
    }



}
