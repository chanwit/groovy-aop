package org.codehaus.groovy.gjit.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.util.AbstractVisitor;

import org.slf4j.*; 

public class ReverseStackDistance implements Opcodes {

    private final Logger log = LoggerFactory.getLogger(ReverseStackDistance.class);

    private MethodInsnNode start;
    private int stacks = 0;

    public ReverseStackDistance(MethodInsnNode s) {
        this.start = s;
    }

    private final int[] STACK_SIZE = {
            0, // nop, 0
            1, // aconst_null, 1
            1, // iconst_m1, 2
            1, // iconst_0, 3
            1, // iconst_1, 4
            1, // iconst_2, 5
            1, // iconst_3, 6
            1, // iconst_4, 7
            1, // iconst_5, 8
            2, // lconst_0, 9
            2, // lconst_1, 10
            1, // fconst_0, 11
            1, // fconst_1, 12
            1, // fconst_2, 13
            2, // dconst_0, 14
            2, // dconst_1, 15
            1, // bipush, 16
            1, // sipush, 17
            1, // ldc, 18
            1, // ldc_w, 19
            2, // ldc2_w, 20
            1, // iload, 21
            2, // lload, 22
            1, // fload, 23
            2, // dload, 24
            1, // aload, 25
            1, // iload_0, 26
            1, // iload_1, 27
            1, // iload_2, 28
            1, // iload_3, 29
            2, // lload_0, 30
            2, // lload_1, 31
            2, // lload_2, 32
            2, // lload_3, 33
            1, // fload_0, 34
            1, // fload_1, 35
            1, // fload_2, 36
            1, // fload_3, 37
            2, // dload_0, 38
            2, // dload_1, 39
            2, // dload_2, 40
            2, // dload_3, 41
            1, // aload_0, 42
            1, // aload_1, 43
            1, // aload_2, 44
            1, // aload_3, 45
            -1, // iaload, 46
            0, // laload, 47
            -1, // faload, 48
            0, // daload, 49
            -1, // aaload, 50
            -1, // baload, 51
            -1, // caload, 52
            -1, // saload, 53
            -1, // istore, 54
            -2, // lstore, 55
            -1, // fstore, 56
            -2, // dstore, 57
            -1, // astore, 58
            -1, // istore_0, 59
            -1, // istore_1, 60
            -1, // istore_2, 61
            -1, // istore_3, 62
            -2, // lstore_0, 63
            -2, // lstore_1, 64
            -2, // lstore_2, 65
            -2, // lstore_3, 66
            -1, // fstore_0, 67
            -1, // fstore_1, 68
            -1, // fstore_2, 69
            -1, // fstore_3, 70
            -2, // dstore_0, 71
            -2, // dstore_1, 72
            -2, // dstore_2, 73
            -2, // dstore_3, 74
            -1, // astore_0, 75
            -1, // astore_1, 76
            -1, // astore_2, 77
            -1, // astore_3, 78
            -3, // iastore, 79
            -4, // lastore, 80
            -3, // fastore, 81
            -4, // dastore, 82
            -3, // aastore, 83
            -3, // bastore, 84
            -3, // castore, 85
            -3, // sastore, 86
            -1, // pop, 87
            -2, // pop2, 88
            1, // dup, 89
            1, // dup_x1, 90
            1, // dup_x2, 91
            2, // dup2, 92
            2, // dup2_x1, 93
            // 2, // dup2_x2, 94
            0, // TODO a quick hack for DUP2_X2 - real value should be 2
            0, // swap, 95
            -1, // iadd, 96
            -2, // ladd, 97
            -1, // fadd, 98
            -2, // dadd, 99
            -1, // isub, 100
            -2, // lsub, 101
            -1, // fsub, 102
            -2, // dsub, 103
            -1, // imul, 104
            -2, // lmul, 105
            -1, // fmul, 106
            -2, // dmul, 107
            -1, // idiv, 108
            -2, // ldiv, 109
            -1, // fdiv, 110
            -2, // ddiv, 111
            -1, // irem, 112
            -2, // lrem, 113
            -1, // frem, 114
            -2, // drem, 115
            0, // ineg, 116
            0, // lneg, 117
            0, // fneg, 118
            0, // dneg, 119
            -1, // ishl, 120
            -1, // lshl, 121
            -1, // ishr, 122
            -1, // lshr, 123
            -1, // iushr, 124
            -1, // lushr, 125
            -1, // iand, 126
            -2, // land, 127
            -1, // ior, 128
            -2, // lor, 129
            -1, // ixor, 130
            -2, // lxor, 131
            0, // iinc, 132
            1, // i2l, 133
            0, // i2f, 134
            1, // i2d, 135
            -1, // l2i, 136
            -1, // l2f, 137
            0, // l2d, 138
            0, // f2i, 139
            1, // f2l, 140
            1, // f2d, 141
            -1, // d2i, 142
            0, // d2l, 143
            -1, // d2f, 144
            0, // i2b, 145
            0, // i2c, 146
            0, // i2s, 147
            -3, // lcmp, 148
            -1, // fcmpl, 149
            -1, // fcmpg, 150
            -3, // dcmpl, 151
            -3, // dcmpg, 152
            -1, // ifeq, 153
            -1, // ifne, 154
            -1, // iflt, 155
            -1, // ifge, 156
            -1, // ifgt, 157
            -1, // ifle, 158
            -2, // if_icmpeq, 159
            -2, // if_icmpne, 160
            -2, // if_icmplt, 161
            -2, // if_icmpge, 162
            -2, // if_icmpgt, 163
            -2, // if_icmple, 164
            -2, // if_acmpeq, 165
            -2, // if_acmpne, 166
            0, // goto, 167
            1, // jsr, 168
            0, // ret, 169
            -1, // tableswitch, 170
            -1, // lookupswitch, 171
            -1, // ireturn, 172
            -2, // lreturn, 173
            -1, // freturn, 174
            -2, // dreturn, 175
            -1, // areturn, 176
            0, // return, 177
            0, // getstatic, 178            depends on the type
            0, // putstatic, 179            depends on the type
            0, // getfield, 180             depends on the type
            0, // putfield, 181             depends on the type
            0, // invokevirtual, 182        depends on the type
            0, // invokespecial, 183        depends on the type
            0, // invokestatic, 184         depends on the type
            0, // invokeinterface, 185      depends on the type
            0, // undefined, 186
            1, // new, 187
            0, // newarray, 188
            0, // anewarray, 189
            0, // arraylength, 190
            -1, // athrow, 191              stack is cleared
            0, // checkcast, 192
            0, // instanceof, 193
            -1, // monitorenter, 194
            -1, // monitorexit, 195
            0, // wide, 196                 depends on the following opcode
            0, // multianewarray, 197       depends on the dimensions
            -1, // ifnull, 198
            -1, // ifnonnull, 199
            0, // goto_w, 200
            1 // jsr_w, 201
        };


    private int argStackSize(String d) {
        Type[] argTypes = Type.getArgumentTypes(d);
        //sysout
        int growth=0;
        for(int i=0;i<argTypes.length; i++) {
            growth = growth + argTypes[i].getSize();
        }
        return growth;
    }

    private int retStackSize(String d) {
        Type retType = Type.getReturnType(d);
        if(retType == Type.VOID_TYPE) return 0;
        return retType.getSize();
    }

    public AbstractInsnNode findStartingNode() {
    	log.info("Start");
        int growth = argStackSize(start.desc);
        if(start.getOpcode() == INVOKESTATIC) {
            stacks = growth;
        } else {
            stacks = growth + 1; // include obj ref
        }
        AbstractInsnNode p = start;
        while(true) {
            if(p.getPrevious()==null) return p;
            p = p.getPrevious();
            log.info("Current insn {}", AbstractVisitor.OPCODES[p.getOpcode()]);
            switch(p.getOpcode()) {
                case LDC:
                    Object cst = ((LdcInsnNode)p).cst;
                    if(cst instanceof Long || cst instanceof Double) {
                        stacks -= 2;
                    } else {
                        stacks -= 1;
                    }
                    break;
                case GETSTATIC:
                case PUTSTATIC:
                case GETFIELD:
                case PUTFIELD:
                    stacks -= Type.getType(((FieldInsnNode)p).desc).getSize();
                    break;
                case INVOKEINTERFACE:
                case INVOKESPECIAL:
                case INVOKEVIRTUAL:
                    growth = retStackSize(((MethodInsnNode)p).desc) - (1 + argStackSize(((MethodInsnNode)p).desc));
                    stacks -= growth;
                    break;
                case INVOKESTATIC:
                    growth = retStackSize(((MethodInsnNode)p).desc) - argStackSize(((MethodInsnNode)p).desc);
                    stacks -= growth;
                    break;
                default:
                    // - because reverse
                    if(p.getOpcode()!=-1) {
                        stacks -= STACK_SIZE[p.getOpcode()];
                    }
                    break;
            }
            if(stacks == 0) return p;
        }
    }

}
