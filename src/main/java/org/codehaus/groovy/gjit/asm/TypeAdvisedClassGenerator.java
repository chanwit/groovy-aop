package org.codehaus.groovy.gjit.asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.gjit.asm.transformer.*;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TypeAdvisedClassGenerator implements Opcodes {

    private Class<?>   advisedReturnType;
    private Class<?>[] advisedTypes;
    private Transformer[] transformers;

    public TypeAdvisedClassGenerator() {
        this.transformers = new Transformer[] {
            new TypePropagateTransformer(),
            new DeConstantTransformer(),
            new WhileTrueEliminatorTransformer(),

            new UnwrapCompareTransformer(),
            new UnwrapBinOpTransformer(),
            new UnwrapUnaryTransformer(),
            new GetAtPutAtTransformer(),
            new DupAstorePopEliminatorTransformer(),
            new InferLocalsTransformer(),
            new NullInitToZeroTransformer(),
            new AutoBoxEliminatorTransformer(),
            new IincTransformer(),
            new XLoadBoxPopEliminatorTransformer(),
            new UnusedCSARemovalTransformer(),
        };
    }

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
        public final String owner;
        public final String name;
        public final String desc;
        public final byte[] body;
        public final boolean firstTime;
        public Result(String owner, String name, String desc, byte[] body, boolean firstTime) {
            super();
            this.owner = owner;
            this.name = name;
            this.desc = desc;
            this.body = body;
            this.firstTime = firstTime;
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
        //
        // current name is in Fib$fib$x format
        // this must be changed to Fib_fib_x to allow re-transformation
        // because Fib_fib_x will get a call site Fib_fib_x$fib
        //
        String newInternalClassName = Type.getInternalName(callSite.getClass()) + "_x";
        newInternalClassName = newInternalClassName.replace('$', '_');

        String[] targetNames = callSite.getClass().getName().split("\\$");

        ClassReader cr;
        ClassNode targetCN = new ClassNode();
        try {
            String targetInternalName = targetNames[0].replace('.', '/');
            if(ClassBodyCache.v().containsKey(targetInternalName)) {
                byte[] bytes = ClassBodyCache.v().get(targetInternalName);
                cr = new ClassReader(bytes);
            } else {
                cr = new ClassReader(targetNames[0]);
            }
            cr.accept(targetCN, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //
        // prepare constant and call site name array
        //
        Utils.prepareClassInfo(targetCN);

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
        if((targetMN.access & ACC_STATIC) == 0)
            typeList.add(Type.getType("L" + targetCN.name + ";"));

        for (int i = 0; i < advisedTypes.length; i++) {
            Class<?> advisedParamType = advisedTypes[i];
            if(advisedParamType == null)
                typeList.add(targetMN_types[i]);
            else
                typeList.add(Type.getType(advisedParamType));
        }
        Type[] argumentTypes = typeList.toArray(new Type[typeList.size()]);

        //
        // Advise return type, if available
        //
        Type returnType = Type.getReturnType(targetMN.desc);
        if(advisedReturnType != null) {
            returnType = Type.getType(advisedReturnType);
        }
        System.out.println("advised return type : " + advisedReturnType);
        System.out.println("return type : " + returnType);

        //
        // Prepare required information and
        // Perform a set of transformations
        // The main transformation is TypePropagateTransformer
        //
        Map<String, Object> options = new HashMap<String, Object>();
        // TODO: unify them
        // The followings give the similar information
        // They should be merged together
        //
        // 1) used by type propagatation
        options.put("advisedTypes", advisedTypes);
        options.put("advisedReturnType", advisedReturnType);
        //
        // 2) used by getAt/putAt
        options.put("argTypes",   argumentTypes);
        options.put("returnType", returnType);
        for (int i = 0; i < transformers.length; i++) {
            transformers[i].internalTransform(targetMN, options);
        }

        //
        // Generate a new class
        //
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_5, ACC_PUBLIC + ACC_SYNTHETIC, newInternalClassName, null,
            "sun/reflect/GroovyAOPMagic", null);
        cw.visitField(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC,
            "$callSiteArray", "Ljava/lang/ref/SoftReference;", null, null)
            .visitEnd();
        //
        // TODO The generated class requires its own call site arrays
        // to correctly support recursive
        //
        // 1. copy static field of the array
        // 2. copy get call site array
        // need to checkout the real structure for this
        // 3. change the first line of GETSTATIC to get field from the new class

        MethodNode createCallSiteArray = findMethod(targetCN, "$createCallSiteArray");
        transformCreateCallSiteArray(createCallSiteArray, newInternalClassName);
        {
            MethodVisitor mv = cw.visitMethod(
                createCallSiteArray.access,
                createCallSiteArray.name,
                createCallSiteArray.desc,
                createCallSiteArray.signature,
                (String[]) createCallSiteArray.exceptions.toArray(new String[createCallSiteArray.exceptions.size()])
            );
            createCallSiteArray.accept(mv);
        }
        MethodNode getCallSiteArray = findMethod(targetCN, "$getCallSiteArray");
        transformGetCallSiteArray(getCallSiteArray, newInternalClassName);
        {
            MethodVisitor mv = cw.visitMethod(
                getCallSiteArray.access,
                getCallSiteArray.name,
                getCallSiteArray.desc,
                getCallSiteArray.signature,
                (String[]) getCallSiteArray.exceptions.toArray(new String[getCallSiteArray.exceptions.size()])
            );
            getCallSiteArray.accept(mv);
        }
        relocateGetCallSiteArray(targetMN, newInternalClassName);

        //
        // Generate a new method based on "targetMN"
        //
        String methodDescriptor = Type.getMethodDescriptor(returnType, argumentTypes);
        {
            MethodVisitor mv = cw.visitMethod( ACC_PUBLIC + ACC_STATIC + ACC_SYNTHETIC ,
                targetMN.name, methodDescriptor,
                null, new String[]{"java/lang/Throwable"});
            targetMN.accept(mv); // copy targetMN to mv
        }

        cw.visitEnd();

        //
        // cached for further optimisation
        //
        byte[] bytes = cw.toByteArray();
        boolean firstTime = true;
        if(ClassBodyCache.v().containsKey(newInternalClassName)) {
            firstTime = false;
        }
        ClassBodyCache.v().put(newInternalClassName, bytes);
        return new Result(newInternalClassName, targetMN.name, methodDescriptor, bytes, firstTime);
    }

    private void relocateGetCallSiteArray(MethodNode targetMN, String newInternalClassName) {
        InsnList units = targetMN.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s.getOpcode() != INVOKESTATIC) s = s.getNext();
        MethodInsnNode m = (MethodInsnNode)s;
        MethodInsnNode newS = new MethodInsnNode(INVOKESTATIC, newInternalClassName, m.name, m.desc);
        units.set(s, newS);
    }

    private void transformCreateCallSiteArray(MethodNode createCallSiteArray,
            String newInternalClassName) {
        System.out.println("newInternalClassName: " + newInternalClassName);
        InsnList units = createCallSiteArray.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s.getOpcode() != GETSTATIC) s = s.getNext();
        LdcInsnNode newS = new LdcInsnNode(Type.getType("L"+ newInternalClassName+ ";"));
        units.set(s, newS);
    }

    private void transformGetCallSiteArray(MethodNode getCallSiteArray,
            String newInternalClassName) {
        InsnList units = getCallSiteArray.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null)  {
            switch(s.getOpcode()) {
                case GETSTATIC:
                case PUTSTATIC: {
                    FieldInsnNode f = (FieldInsnNode)s;
                    FieldInsnNode newS = new FieldInsnNode(f.getOpcode(),
                                newInternalClassName,
                                f.name, f.desc);
                    units.set(s, newS);
                    s = newS.getNext();
                    continue;
                }
                case INVOKESTATIC: {
                    MethodInsnNode m = (MethodInsnNode)s;
                    if(m.name.equals("$createCallSiteArray")) {
                        MethodInsnNode newS = new MethodInsnNode(m.getOpcode(),
                                newInternalClassName, m.name, m.desc);
                        units.set(s, newS);
                        s = newS.getNext();
                        continue;
                    }
                }
            }
            s = s.getNext();
        }
    }

}
