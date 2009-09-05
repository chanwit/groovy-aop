
package org.codehaus.groovy.gjit.asm;

import java.util.List;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class AsmNodeBuilder implements Opcodes {

    private InsnList list = new InsnList();

    public InsnList getList() {
        return list;
    }

    public void ldc(Object cst) {
        list.add(new LdcInsnNode(cst));
    }

    public LabelNode getLabel() {
        LabelNode result = new LabelNode();
        list.add(result);
        return result;
    }

    public void iinc(int var) {
        iinc(var, 1);
    }

    public void iinc(int var, int incr) {
        list.add(new IincInsnNode(var, incr));
    }

public void invokevirtual(String owner, String name, String desc) {
    list.add(new MethodInsnNode(INVOKEVIRTUAL, owner, name, desc));
}
public void invokevirtual(String owner, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    invokevirtual(owner, name, desc);
}
public void invokevirtual(Class<?> ownerClass, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    String owner = Type.getInternalName(ownerClass);
    invokevirtual(owner, name, desc);
}

public void invokespecial(String owner, String name, String desc) {
    list.add(new MethodInsnNode(INVOKESPECIAL, owner, name, desc));
}
public void invokespecial(String owner, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    invokespecial(owner, name, desc);
}
public void invokespecial(Class<?> ownerClass, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    String owner = Type.getInternalName(ownerClass);
    invokespecial(owner, name, desc);
}

public void invokestatic(String owner, String name, String desc) {
    list.add(new MethodInsnNode(INVOKESTATIC, owner, name, desc));
}
public void invokestatic(String owner, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    invokestatic(owner, name, desc);
}
public void invokestatic(Class<?> ownerClass, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    String owner = Type.getInternalName(ownerClass);
    invokestatic(owner, name, desc);
}

public void invokeinterface(String owner, String name, String desc) {
    list.add(new MethodInsnNode(INVOKEINTERFACE, owner, name, desc));
}
public void invokeinterface(String owner, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    invokeinterface(owner, name, desc);
}
public void invokeinterface(Class<?> ownerClass, String name, List args, Class<?> retType) {
    Type[] types = new Type[args.size()];
    for (int i = 0; i < types.length; i++) {
        types[i] = Type.getType((Class<?>)args.get(i));
    }
    String desc = Type.getMethodDescriptor(Type.getType(retType), types);
    String owner = Type.getInternalName(ownerClass);
    invokeinterface(owner, name, desc);
}

public void _new(String cls) {
    list.add(new TypeInsnNode(NEW, cls));
}
public void _new(Class<?> cls) {
    list.add(new TypeInsnNode(NEW, Type.getInternalName(cls)));
}

public void anewarray(String cls) {
    list.add(new TypeInsnNode(ANEWARRAY, cls));
}
public void anewarray(Class<?> cls) {
    list.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(cls)));
}

public void checkcast(String cls) {
    list.add(new TypeInsnNode(CHECKCAST, cls));
}
public void checkcast(Class<?> cls) {
    list.add(new TypeInsnNode(CHECKCAST, Type.getInternalName(cls)));
}

public void instance_of(String cls) {
    list.add(new TypeInsnNode(INSTANCEOF, cls));
}
public void instance_of(Class<?> cls) {
    list.add(new TypeInsnNode(INSTANCEOF, Type.getInternalName(cls)));
}

public void ifeq(LabelNode node) {
    list.add(new JumpInsnNode(IFEQ, node));
}

public void ifne(LabelNode node) {
    list.add(new JumpInsnNode(IFNE, node));
}

public void iflt(LabelNode node) {
    list.add(new JumpInsnNode(IFLT, node));
}

public void ifge(LabelNode node) {
    list.add(new JumpInsnNode(IFGE, node));
}

public void ifgt(LabelNode node) {
    list.add(new JumpInsnNode(IFGT, node));
}

public void ifle(LabelNode node) {
    list.add(new JumpInsnNode(IFLE, node));
}

public void if_icmpeq(LabelNode node) {
    list.add(new JumpInsnNode(IF_ICMPEQ, node));
}

public void if_icmpne(LabelNode node) {
    list.add(new JumpInsnNode(IF_ICMPNE, node));
}

public void if_icmplt(LabelNode node) {
    list.add(new JumpInsnNode(IF_ICMPLT, node));
}

public void if_icmpge(LabelNode node) {
    list.add(new JumpInsnNode(IF_ICMPGE, node));
}

public void if_icmpgt(LabelNode node) {
    list.add(new JumpInsnNode(IF_ICMPGT, node));
}

public void if_icmple(LabelNode node) {
    list.add(new JumpInsnNode(IF_ICMPLE, node));
}

public void if_acmpeq(LabelNode node) {
    list.add(new JumpInsnNode(IF_ACMPEQ, node));
}

public void if_acmpne(LabelNode node) {
    list.add(new JumpInsnNode(IF_ACMPNE, node));
}

public void _goto(LabelNode node) {
    list.add(new JumpInsnNode(GOTO, node));
}

public void jsr(LabelNode node) {
    list.add(new JumpInsnNode(JSR, node));
}

public void ifnull(LabelNode node) {
    list.add(new JumpInsnNode(IFNULL, node));
}

public void ifnonnull(LabelNode node) {
    list.add(new JumpInsnNode(IFNONNULL, node));
}

public void bipush(int i) {
    list.add(new IntInsnNode(BIPUSH, i));
}

public void sipush(int i) {
    list.add(new IntInsnNode(SIPUSH, i));
}

public void newarray(int i) {
    list.add(new IntInsnNode(NEWARRAY, i));
}

public Object getNop() {
   list.add(new InsnNode(NOP)); return null;
}

public Object getAconst_null() {
   list.add(new InsnNode(ACONST_NULL)); return null;
}

public Object getIconst_m1() {
   list.add(new InsnNode(ICONST_M1)); return null;
}

public Object getIconst_0() {
   list.add(new InsnNode(ICONST_0)); return null;
}

public Object getIconst_1() {
   list.add(new InsnNode(ICONST_1)); return null;
}

public Object getIconst_2() {
   list.add(new InsnNode(ICONST_2)); return null;
}

public Object getIconst_3() {
   list.add(new InsnNode(ICONST_3)); return null;
}

public Object getIconst_4() {
   list.add(new InsnNode(ICONST_4)); return null;
}

public Object getIconst_5() {
   list.add(new InsnNode(ICONST_5)); return null;
}

public Object getLconst_0() {
   list.add(new InsnNode(LCONST_0)); return null;
}

public Object getLconst_1() {
   list.add(new InsnNode(LCONST_1)); return null;
}

public Object getFconst_0() {
   list.add(new InsnNode(FCONST_0)); return null;
}

public Object getFconst_1() {
   list.add(new InsnNode(FCONST_1)); return null;
}

public Object getFconst_2() {
   list.add(new InsnNode(FCONST_2)); return null;
}

public Object getDconst_0() {
   list.add(new InsnNode(DCONST_0)); return null;
}

public Object getDconst_1() {
   list.add(new InsnNode(DCONST_1)); return null;
}

public Object getIaload() {
   list.add(new InsnNode(IALOAD)); return null;
}

public Object getLaload() {
   list.add(new InsnNode(LALOAD)); return null;
}

public Object getFaload() {
   list.add(new InsnNode(FALOAD)); return null;
}

public Object getDaload() {
   list.add(new InsnNode(DALOAD)); return null;
}

public Object getAaload() {
   list.add(new InsnNode(AALOAD)); return null;
}

public Object getBaload() {
   list.add(new InsnNode(BALOAD)); return null;
}

public Object getCaload() {
   list.add(new InsnNode(CALOAD)); return null;
}

public Object getSaload() {
   list.add(new InsnNode(SALOAD)); return null;
}

public Object getIastore() {
   list.add(new InsnNode(IASTORE)); return null;
}

public Object getLastore() {
   list.add(new InsnNode(LASTORE)); return null;
}

public Object getFastore() {
   list.add(new InsnNode(FASTORE)); return null;
}

public Object getDastore() {
   list.add(new InsnNode(DASTORE)); return null;
}

public Object getAastore() {
   list.add(new InsnNode(AASTORE)); return null;
}

public Object getBastore() {
   list.add(new InsnNode(BASTORE)); return null;
}

public Object getCastore() {
   list.add(new InsnNode(CASTORE)); return null;
}

public Object getSastore() {
   list.add(new InsnNode(SASTORE)); return null;
}

public Object getPop() {
   list.add(new InsnNode(POP)); return null;
}

public Object getPop2() {
   list.add(new InsnNode(POP2)); return null;
}

public Object getDup() {
   list.add(new InsnNode(DUP)); return null;
}

public Object getDup_x1() {
   list.add(new InsnNode(DUP_X1)); return null;
}

public Object getDup_x2() {
   list.add(new InsnNode(DUP_X2)); return null;
}

public Object getDup2() {
   list.add(new InsnNode(DUP2)); return null;
}

public Object getDup2_x1() {
   list.add(new InsnNode(DUP2_X1)); return null;
}

public Object getDup2_x2() {
   list.add(new InsnNode(DUP2_X2)); return null;
}

public Object getSwap() {
   list.add(new InsnNode(SWAP)); return null;
}

public Object getIadd() {
   list.add(new InsnNode(IADD)); return null;
}

public Object getLadd() {
   list.add(new InsnNode(LADD)); return null;
}

public Object getFadd() {
   list.add(new InsnNode(FADD)); return null;
}

public Object getDadd() {
   list.add(new InsnNode(DADD)); return null;
}

public Object getIsub() {
   list.add(new InsnNode(ISUB)); return null;
}

public Object getLsub() {
   list.add(new InsnNode(LSUB)); return null;
}

public Object getFsub() {
   list.add(new InsnNode(FSUB)); return null;
}

public Object getDsub() {
   list.add(new InsnNode(DSUB)); return null;
}

public Object getImul() {
   list.add(new InsnNode(IMUL)); return null;
}

public Object getLmul() {
   list.add(new InsnNode(LMUL)); return null;
}

public Object getFmul() {
   list.add(new InsnNode(FMUL)); return null;
}

public Object getDmul() {
   list.add(new InsnNode(DMUL)); return null;
}

public Object getIdiv() {
   list.add(new InsnNode(IDIV)); return null;
}

public Object getLdiv() {
   list.add(new InsnNode(LDIV)); return null;
}

public Object getFdiv() {
   list.add(new InsnNode(FDIV)); return null;
}

public Object getDdiv() {
   list.add(new InsnNode(DDIV)); return null;
}

public Object getIrem() {
   list.add(new InsnNode(IREM)); return null;
}

public Object getLrem() {
   list.add(new InsnNode(LREM)); return null;
}

public Object getFrem() {
   list.add(new InsnNode(FREM)); return null;
}

public Object getDrem() {
   list.add(new InsnNode(DREM)); return null;
}

public Object getIneg() {
   list.add(new InsnNode(INEG)); return null;
}

public Object getLneg() {
   list.add(new InsnNode(LNEG)); return null;
}

public Object getFneg() {
   list.add(new InsnNode(FNEG)); return null;
}

public Object getDneg() {
   list.add(new InsnNode(DNEG)); return null;
}

public Object getIshl() {
   list.add(new InsnNode(ISHL)); return null;
}

public Object getLshl() {
   list.add(new InsnNode(LSHL)); return null;
}

public Object getIshr() {
   list.add(new InsnNode(ISHR)); return null;
}

public Object getLshr() {
   list.add(new InsnNode(LSHR)); return null;
}

public Object getIushr() {
   list.add(new InsnNode(IUSHR)); return null;
}

public Object getLushr() {
   list.add(new InsnNode(LUSHR)); return null;
}

public Object getIand() {
   list.add(new InsnNode(IAND)); return null;
}

public Object getLand() {
   list.add(new InsnNode(LAND)); return null;
}

public Object getIor() {
   list.add(new InsnNode(IOR)); return null;
}

public Object getLor() {
   list.add(new InsnNode(LOR)); return null;
}

public Object getIxor() {
   list.add(new InsnNode(IXOR)); return null;
}

public Object getLxor() {
   list.add(new InsnNode(LXOR)); return null;
}

public Object getI2l() {
   list.add(new InsnNode(I2L)); return null;
}

public Object getI2f() {
   list.add(new InsnNode(I2F)); return null;
}

public Object getI2d() {
   list.add(new InsnNode(I2D)); return null;
}

public Object getL2i() {
   list.add(new InsnNode(L2I)); return null;
}

public Object getL2f() {
   list.add(new InsnNode(L2F)); return null;
}

public Object getL2d() {
   list.add(new InsnNode(L2D)); return null;
}

public Object getF2i() {
   list.add(new InsnNode(F2I)); return null;
}

public Object getF2l() {
   list.add(new InsnNode(F2L)); return null;
}

public Object getF2d() {
   list.add(new InsnNode(F2D)); return null;
}

public Object getD2i() {
   list.add(new InsnNode(D2I)); return null;
}

public Object getD2l() {
   list.add(new InsnNode(D2L)); return null;
}

public Object getD2f() {
   list.add(new InsnNode(D2F)); return null;
}

public Object getI2b() {
   list.add(new InsnNode(I2B)); return null;
}

public Object getI2c() {
   list.add(new InsnNode(I2C)); return null;
}

public Object getI2s() {
   list.add(new InsnNode(I2S)); return null;
}

public Object getLcmp() {
   list.add(new InsnNode(LCMP)); return null;
}

public Object getFcmpl() {
   list.add(new InsnNode(FCMPL)); return null;
}

public Object getFcmpg() {
   list.add(new InsnNode(FCMPG)); return null;
}

public Object getDcmpl() {
   list.add(new InsnNode(DCMPL)); return null;
}

public Object getDcmpg() {
   list.add(new InsnNode(DCMPG)); return null;
}

public Object getIreturn() {
   list.add(new InsnNode(IRETURN)); return null;
}

public Object getLreturn() {
   list.add(new InsnNode(LRETURN)); return null;
}

public Object getFreturn() {
   list.add(new InsnNode(FRETURN)); return null;
}

public Object getDreturn() {
   list.add(new InsnNode(DRETURN)); return null;
}

public Object getAreturn() {
   list.add(new InsnNode(ARETURN)); return null;
}

public Object get_return() {
   list.add(new InsnNode(RETURN)); return null;
}

public Object getArraylength() {
   list.add(new InsnNode(ARRAYLENGTH)); return null;
}

public Object getAthrow() {
   list.add(new InsnNode(ATHROW)); return null;
}

public Object getMonitorenter() {
   list.add(new InsnNode(MONITORENTER)); return null;
}

public Object getMonitorexit() {
   list.add(new InsnNode(MONITOREXIT)); return null;
}

public void getstatic(String owner, String name, String desc) {
    list.add(new FieldInsnNode(GETSTATIC,
            owner, name, desc));
}
public void getstatic(Class<?> owner, String name, Class<?> desc) {
   list.add(new FieldInsnNode(GETSTATIC,
           Type.getInternalName(owner),
           name, Type.getDescriptor(desc)));
}

public void putstatic(String owner, String name, String desc) {
    list.add(new FieldInsnNode(PUTSTATIC,
            owner, name, desc));
}
public void putstatic(Class<?> owner, String name, Class<?> desc) {
   list.add(new FieldInsnNode(PUTSTATIC,
           Type.getInternalName(owner),
           name, Type.getDescriptor(desc)));
}

public void getfield(String owner, String name, String desc) {
    list.add(new FieldInsnNode(GETFIELD,
            owner, name, desc));
}
public void getfield(Class<?> owner, String name, Class<?> desc) {
   list.add(new FieldInsnNode(GETFIELD,
           Type.getInternalName(owner),
           name, Type.getDescriptor(desc)));
}

public void putfield(String owner, String name, String desc) {
    list.add(new FieldInsnNode(PUTFIELD,
            owner, name, desc));
}
public void putfield(Class<?> owner, String name, Class<?> desc) {
   list.add(new FieldInsnNode(PUTFIELD,
           Type.getInternalName(owner),
           name, Type.getDescriptor(desc)));
}

public void iload(int i) {
    list.add(new VarInsnNode(ILOAD, i));
}

public void lload(int i) {
    list.add(new VarInsnNode(LLOAD, i));
}

public void fload(int i) {
    list.add(new VarInsnNode(FLOAD, i));
}

public void dload(int i) {
    list.add(new VarInsnNode(DLOAD, i));
}

public void aload(int i) {
    list.add(new VarInsnNode(ALOAD, i));
}

public void istore(int i) {
    list.add(new VarInsnNode(ISTORE, i));
}

public void lstore(int i) {
    list.add(new VarInsnNode(LSTORE, i));
}

public void fstore(int i) {
    list.add(new VarInsnNode(FSTORE, i));
}

public void dstore(int i) {
    list.add(new VarInsnNode(DSTORE, i));
}

public void astore(int i) {
    list.add(new VarInsnNode(ASTORE, i));
}

public void ret(int i) {
    list.add(new VarInsnNode(RET, i));
}

}
