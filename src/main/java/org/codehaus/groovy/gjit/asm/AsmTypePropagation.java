package org.codehaus.groovy.gjit.asm;

import java.io.IOException;
import java.lang.reflect.Modifier;
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
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class AsmTypePropagation implements Opcodes {

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

    private MethodNode findMethod(ClassNode targetCN, String name) {
        List<MethodNode> methods = targetCN.methods;
        for (Iterator<MethodNode> iterator = methods.iterator(); iterator.hasNext();) {
            MethodNode methodNode = iterator.next();
            if(methodNode.name.equals(name))
                return methodNode;
        }
        return null;
    }

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

    public Result typePropagate(CallSite callSite) {
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

        transform(targetMN);

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

    private void transform(MethodNode m) {
        final boolean staticMethod = (m.access & ACC_STATIC) != 0;
        InsnList units = m.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() == ALOAD || s.getOpcode() == ASTORE) {
                VarInsnNode v = (VarInsnNode)s;
                Class<?> type;
                if(staticMethod)
                    type = advisedTypes[v.var];
                else
                    type = advisedTypes[v.var - 1];
                if(type != null && type.isPrimitive()) {
                    int offset = 4;
                    if(type == int.class)   offset = 4; else
                    if(type == long.class)  offset = 3; else
                    if(type == float.class) offset = 2; else
                    if(type == double.class)offset = 1;
                    VarInsnNode newS = new VarInsnNode(v.getOpcode()-offset, v.var);
                    units.set(s, newS);
                    units.insert(newS, getBoxNode(type));
                    s = newS.getNext().getNext();
                    continue;
                } else if(type != null) {
                    throw new RuntimeException("NYI");
                }
            }
            s = s.getNext();
        }
    }

    private MethodInsnNode getBoxNode(Class<?> type) {
        String name=null;
        if(type == int.class)     name = "Integer"; else
        if(type == long.class)    name = "Long";    else
        if(type == byte.class)    name = "Byte";    else
        if(type == boolean.class) name = "Boolean"; else
        if(type == short.class)   name = "Short";   else
        if(type == double.class)  name = "Double";  else
        if(type == float.class)   name = "Float";   else
        if(type == char.class)    name = "Character";
        if(name == null) throw new RuntimeException("No box for " + type);
        return new MethodInsnNode(INVOKESTATIC, "java/lang/" + name, "valueOf", "(I)Ljava/lang/" + name + ";");
    }
}
