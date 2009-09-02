package org.codehaus.groovy.gjit.asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.gjit.asm.transformer.AutoBoxEliminatorTransformer;
import org.codehaus.groovy.gjit.asm.transformer.TypePropagateTransformer;
import org.codehaus.groovy.gjit.asm.transformer.UnwrapCompareTransformer;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

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
        //
        // if it's not a static method, adding simulated "this"
        //
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
        // Prepare required information and
        // Perform a set of transformation
        //
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("advisedTypes",      advisedTypes);
        options.put("advisedReturnType", advisedReturnType);

        new TypePropagateTransformer().internalTransform(targetMN, options);
        new UnwrapCompareTransformer().internalTransform(targetMN, options);
        new AutoBoxEliminatorTransformer().internalTransform(targetMN, options);
        optimiseBinaryOperators(targetMN);

        // generate a new class
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

    /**
     * Check if there is a bytecode pattern that use typed local variable.
     * If so, tranform it with heuristic rules to be native xADD etc.
     *
     * @param m
     */
    private void optimiseBinaryOperators(MethodNode m) {

    }

}
