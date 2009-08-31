package org.codehaus.groovy.gjit.asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class AsmTypeAdvisedClassGenerator implements Opcodes {

    private Class<?>   advisedReturnType;
    private Class<?>[] advisedTypes;

    public Class<?> getAdvisedReturnType() {
        return advisedReturnType;
    }

    public void setAdvisedReturnType(Class<?> advisedReturnType) {
        this.advisedReturnType = advisedReturnType;
    }

    public Class<?>[] getAdvisedTypes() {
        return advisedTypes;
    }

    public void setAdvisedTypes(Class<?>[] advisedTypes) {
        this.advisedTypes = advisedTypes;
    }

    public static class Result {
        public final String methodSignature;
        public final byte[] body;
        public Result(String methodSignature, byte[] body) {
            super();
            this.methodSignature = methodSignature;
            this.body = body;
        }
    }

    @SuppressWarnings("unchecked")
    private MethodNode findMethod(ClassNode targetCN, String name) {
        List<MethodNode> methods = targetCN.methods;
        for (Iterator<MethodNode> iterator = methods.iterator(); iterator.hasNext();) {
            MethodNode methodNode = iterator.next();
            if(methodNode.name.equals(name))
                return methodNode;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private MethodNode findMethod(ClassNode targetCN, String name, String desc) {
        List<MethodNode> methods = targetCN.methods;
        for (Iterator<MethodNode> iterator = methods.iterator(); iterator.hasNext();) {
            MethodNode methodNode = iterator.next();
            if((name + desc).equals(methodNode.name + methodNode.desc)) {
                return methodNode;
            }
        }
        return null;
    }

    public Result perform(CallSite callSite) {
        String[] targetNames = callSite.getClass().getName().split("\\$");
        ClassReader cr;
        ClassNode targetCN = new ClassNode();
        try {
            cr = new ClassReader(targetNames[0]);
            cr.accept(targetCN, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO use desc to exactly find target method
        // MethodNode targetMN = findMethod(targetCN, targetNames[1], desc);
        //
        MethodNode targetMN = findMethod(targetCN, targetNames[1]);
        ArrayList<Type> typeList = new ArrayList<Type>();

        //
        // targetCN is the target class obtained from targetNames[0]
        // it is added to be the first argument to simulate "this".
        //
        Type[] targetMN_types = Type.getArgumentTypes(targetMN.desc);
        if((targetMN.access & ACC_STATIC) == 0) {
            typeList.add(Type.getType("L" + targetCN.name + ";"));
        }
        for (int i = 0; i < advisedTypes.length; i++) {
            Class<?> advisedParamType = advisedTypes[i];
            if(advisedParamType == null)
                typeList.add( targetMN_types[i] );
            else
                typeList.add( Type.getType(advisedParamType) );
        }
        Type[] argumentTypes = typeList.toArray(new Type[typeList.size()]);
        //
        // Advise return type, if available
        //
        Type returnType = Type.getReturnType(targetMN.desc);
        if(advisedReturnType != null) {
            returnType = Type.getType(advisedReturnType);
        }

        //
        // Perform a set of transformation
        //
        typePropagate(targetMN);
        optimiseBinaryOperators(targetMN);

        String newClassName = Type.getInternalName(callSite.getClass()) + "$x";
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_5, ACC_PUBLIC + ACC_SYNTHETIC,
            newClassName, null, "sun/reflect/GroovyAOPMagic", null);

        String methodDescriptor = Type.getMethodDescriptor(returnType, argumentTypes);
        MethodVisitor mv = cw.visitMethod( ACC_PUBLIC + ACC_STATIC + ACC_SYNTHETIC ,
            targetMN.name, methodDescriptor, null, new String[]{"java/lang/Throwable"});
        targetMN.accept(mv); // copy targetMN to mv
        cw.visitEnd();
        return new Result(newClassName + "." + targetMN.name + methodDescriptor, cw.toByteArray());
    }

    private void typePropagate(MethodNode m) {
        final boolean staticMethod = (m.access & ACC_STATIC) != 0;
        InsnList units = m.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() == ALOAD || s.getOpcode() == ASTORE) {
                VarInsnNode v = (VarInsnNode)s;
                Class<?> type;
                if(staticMethod) {
                    if(v.var >= advisedTypes.length) {
                        s = s.getNext();
                        continue;
                    }
                    type = advisedTypes[v.var];
                }
                else {
                    if(v.var-1 >= advisedTypes.length) {
                        s = s.getNext();
                        continue;
                    }
                    type = advisedTypes[v.var - 1];
                }
                if(type != null && type.isPrimitive()) {
                    int offset = 4;
                    if(type == int.class)    offset = 4; else
                    if(type == long.class)   offset = 3; else
                    if(type == float.class)  offset = 2; else
                    if(type == double.class) offset = 1;
                    VarInsnNode newS = new VarInsnNode(v.getOpcode()-offset, v.var);
                    units.set(s, newS);
                    units.insert(newS, getBoxNode(type));
                    s = newS.getNext().getNext();
                    continue;
                } else if(type != null) {
                    throw new RuntimeException("NYI");
                }
            } else if (s.getOpcode() == ARETURN) {
                if(advisedReturnType != null && advisedReturnType.isPrimitive()) {
                    int offset = 4;
                    if(advisedReturnType == int.class)    offset = 4; else
                    if(advisedReturnType == long.class)   offset = 3; else
                    if(advisedReturnType == float.class)  offset = 2; else
                    if(advisedReturnType == double.class) offset = 1;
                    InsnNode newS = new InsnNode(ARETURN - offset);
                    units.set(s, newS);
                    units.insertBefore(newS, getUnboxNodes(advisedReturnType));
                    s = newS.getNext();
                    continue;
                } else if (advisedReturnType != null) {
                    throw new RuntimeException("NYI");
                }
            }
            s = s.getNext();
        }
    }

    /**
     * Check if there is a bytecode pattern that use typed local variable.
     * If so, tranform it with heuristic rules to be native xADD etc.
     *
     * @param m
     */
    private void optimiseBinaryOperators(MethodNode m) {

    }

    private MethodInsnNode getBoxNode(Class<?> type) {
        String name=null;
        String desc=null;
        if(type == int.class)     {name = "Integer";  desc = "I"; } else
        if(type == long.class)    {name = "Long";     desc = "J"; } else
        if(type == byte.class)    {name = "Byte";     desc = "B"; } else
        if(type == boolean.class) {name = "Boolean";  desc = "Z"; } else
        if(type == short.class)   {name = "Short";    desc = "S"; } else
        if(type == double.class)  {name = "Double";   desc = "D"; } else
        if(type == float.class)   {name = "Float";    desc = "F"; } else
        if(type == char.class)    {name = "Character";desc = "C"; }
        if(name == null) throw new RuntimeException("No box for " + type);
        return new MethodInsnNode(INVOKESTATIC, "java/lang/" + name, "valueOf", "(" + desc + ")Ljava/lang/" + name + ";");
    }

    private InsnList getUnboxNodes(Class<?> type) {
        InsnList result = new InsnList();
        String name=null;
        String shortName = type.getName();
        String desc=null;
        if(type == int.class)     {name = "Integer";  desc = "I"; } else
        if(type == long.class)    {name = "Long";     desc = "J"; } else
        if(type == byte.class)    {name = "Byte";     desc = "B"; } else
        if(type == boolean.class) {name = "Boolean";  desc = "Z"; } else
        if(type == short.class)   {name = "Short";    desc = "S"; } else
        if(type == double.class)  {name = "Double";   desc = "D"; } else
        if(type == float.class)   {name = "Float";    desc = "F"; } else
        if(type == char.class)    {name = "Character";desc = "C"; }
        if(name == null) throw new RuntimeException("No unbox for " + type);
        result.add(new TypeInsnNode(CHECKCAST, "java/lang/" + name));
        result.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/" + name, shortName + "Value", "()" + desc));
        return result;
    }
}
