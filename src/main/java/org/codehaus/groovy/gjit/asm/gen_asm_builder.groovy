#!/usr/bin/env groovy

//
// Run the following line to generate:
// $ groovy ./gen_asm_builder.groovy > AsmNodeBuilder.java
//
def header = '''
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
'''
println header

["INVOKEVIRTUAL", "INVOKESPECIAL", "INVOKESTATIC", "INVOKEINTERFACE"].each {
    println "public void ${it.toLowerCase()}(String owner, String name, String desc) {"
    println "    list.add(new MethodInsnNode($it, owner, name, desc));"
    println "}"
    println "public void ${it.toLowerCase()}(String owner, String name, List args, Class<?> retType) {"
    println "    Type[] types = new Type[args.size()];"
    println "    for (int i = 0; i < types.length; i++) {"
    println "        types[i] = Type.getType((Class<?>)args.get(i));"
    println "    }"
    println "    String desc = Type.getMethodDescriptor(Type.getType(retType), types);"
    println "    ${it.toLowerCase()}(owner, name, desc);"
    println "}"
    println "public void ${it.toLowerCase()}(Class<?> ownerClass, String name, List args, Class<?> retType) {"
    println "    Type[] types = new Type[args.size()];"
    println "    for (int i = 0; i < types.length; i++) {"
    println "        types[i] = Type.getType((Class<?>)args.get(i));"
    println "    }"
    println "    String desc = Type.getMethodDescriptor(Type.getType(retType), types);"
    println "    String owner = Type.getInternalName(ownerClass);"
    println "    ${it.toLowerCase()}(owner, name, desc);"
    println "}"
    println()
}

["_NEW", "ANEWARRAY", "CHECKCAST", "INSTANCE_OF"].each {
    def name = it
    if(name=="INSTANCE_OF") name = "INSTANCEOF"
    else if(name=="_NEW") name = "NEW"

    println "public void ${it.toLowerCase()}(String cls) {"
    println "    list.add(new TypeInsnNode($name, cls));"
    println "}"
    println "public void ${it.toLowerCase()}(Class<?> cls) {"
    println "    list.add(new TypeInsnNode($name, Type.getInternalName(cls)));"
    println "}"
    println()
}

["IFEQ", "IFNE", "IFLT", "IFGE", "IFGT", "IFLE", "IF_ICMPEQ",
"IF_ICMPNE", "IF_ICMPLT", "IF_ICMPGE", "IF_ICMPGT", "IF_ICMPLE", "IF_ACMPEQ",
"IF_ACMPNE", "_GOTO", "JSR", "IFNULL", "IFNONNULL"].each {
    println "public void ${it.toLowerCase()}(LabelNode node) {"
    if(it == "_GOTO")
        println "    list.add(new JumpInsnNode(${it[1..-1]}, node));"
    else
        println "    list.add(new JumpInsnNode($it, node));"
    println "}"
    println()
}

["BIPUSH", "SIPUSH", "NEWARRAY"].each {
    println "public void ${it.toLowerCase()}(int i) {"
    println "    list.add(new IntInsnNode($it, i));"
    println "}"
    println()
}

["NOP", "ACONST_NULL", "ICONST_M1", "ICONST_0", "ICONST_1",
"ICONST_2", "ICONST_3", "ICONST_4", "ICONST_5", "LCONST_0", "LCONST_1",
"FCONST_0", "FCONST_1", "FCONST_2", "DCONST_0", "DCONST_1", "IALOAD", "LALOAD",
"FALOAD", "DALOAD", "AALOAD", "BALOAD", "CALOAD", "SALOAD", "IASTORE", "LASTORE",
"FASTORE", "DASTORE", "AASTORE", "BASTORE", "CASTORE", "SASTORE", "POP", "POP2",
"DUP", "DUP_X1", "DUP_X2", "DUP2", "DUP2_X1", "DUP2_X2", "SWAP", "IADD", "LADD",
"FADD", "DADD", "ISUB", "LSUB", "FSUB", "DSUB", "IMUL", "LMUL", "FMUL", "DMUL", "IDIV",
"LDIV", "FDIV", "DDIV", "IREM", "LREM", "FREM", "DREM", "INEG", "LNEG", "FNEG", "DNEG",
"ISHL", "LSHL", "ISHR", "LSHR", "IUSHR", "LUSHR", "IAND", "LAND", "IOR", "LOR", "IXOR",
"LXOR", "I2L", "I2F", "I2D", "L2I", "L2F", "L2D", "F2I", "F2L", "F2D", "D2I", "D2L", "D2F",
"I2B", "I2C", "I2S", "LCMP", "FCMPL", "FCMPG", "DCMPL", "DCMPG", "IRETURN", "LRETURN",
"FRETURN", "DRETURN", "ARETURN", "_RETURN", "ARRAYLENGTH", "ATHROW",
"MONITORENTER", "MONITOREXIT"].each {
    println "public Object get${it[0]+it[1..-1].toLowerCase()}() {"
    if(it=="_RETURN")
        println "   list.add(new InsnNode(${it[1..-1]})); return null;"
    else
        println "   list.add(new InsnNode($it)); return null;"
    println "}"
    println()
}

["GETSTATIC", "PUTSTATIC", "GETFIELD", "PUTFIELD"].each {
    println "public void ${it.toLowerCase()}(Object owner, String name, Object desc) {"
    println "    if(owner instanceof Class) owner = Type.getInternalName((Class)owner);"
    println "    if(desc  instanceof Class) desc =  Type.getDescriptor((Class)desc);"
    println "    list.add(new FieldInsnNode($it,"
    println "            (String)owner, name, (String)desc));"
    println "}"
    //println "public void ${it.toLowerCase()}(Class<?> owner, String name, Class<?> desc) {"
    //println "   list.add(new FieldInsnNode($it,"
    //println "           Type.getInternalName(owner),"
    //println "           name, Type.getDescriptor(desc)));"
    //println "}"
    println()
}

["ILOAD", "LLOAD", "FLOAD", "DLOAD",
"ALOAD", "ISTORE", "LSTORE", "FSTORE", "DSTORE", "ASTORE", "RET"].each {
    println "public void ${it.toLowerCase()}(int i) {"
    println "    list.add(new VarInsnNode($it, i));"
    println "}"
    println()
}

println '}'