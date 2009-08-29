package org.codehaus.groovy.gjit.asm.transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.gjit.asm.PartialDefUseAnalyser;
import org.codehaus.groovy.gjit.soot.transformer.Utils;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class AsmAspectAwareTransformer implements Transformer, Opcodes {

    private String withInMethodName;
    private MethodNode body;
    private InsnList units;
    private CallSite callSite;

    private Class<?> advisedReturnType;
    private Class<?>[] advisedTypes;

    @Override
    public void internalTransform(MethodNode body) {
        String fullname = body.name + body.desc;
        if( withInMethodName == null ||
            fullname.equals(withInMethodName))
        {
            this.body  = body;
            this.units = body.instructions;

            VarInsnNode acallsite = findCallSiteArray(units);
            AbstractInsnNode invokeStatement = locateCallSiteByIndex(units, acallsite, callSite.getIndex());
            MethodNode newTargetMethod = typePropagate(callSite);
            replaceCallSite(invokeStatement, newTargetMethod);
        }

    }

    private void replaceCallSite(AbstractInsnNode invokeStatement, MethodNode newTargetMethod) {
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

    private MethodNode typePropagate(CallSite callSite) {
        //
        // the name of call site class is in this format:
        //   Class$method
        // so that it.split('$') will be resulting into:
        //   [Class, method]
        //
        String[] targetNames = callSite.getClass().getName().split("\\$");
        ClassReader cr;
        ClassNode targetCN = new ClassNode();
        try {
            cr = new ClassReader(targetNames[0]);
            cr.accept(targetCN, 0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        typeList.add(Type.getType("L" + targetCN.name + ";"));
        for (int i = 0; i < advisedTypes.length; i++) {
            Class<?> advisedParamType = advisedTypes[i];
            if(advisedParamType == null)
                typeList.add( targetMN_types[i] );
            else
                typeList.add( Type.getType(advisedParamType) );
        }

        //
        // Advise return type, if available
        //
        Type returnType = Type.getReturnType(targetMN.desc);
        if(advisedReturnType != null) {
            returnType = Type.getType(advisedReturnType);
        }

        String newClassName = Type.getInternalName(callSite.getClass()) + "$x";

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC,
                 newClassName, null, "sun/reflect/GroovyAOPMagic", null);

        //
        //Type.getMethodDescriptor(returnType, argumentTypes)

//        desc =
//        MethodVisitor mv = cw.visitMethod(
//            Opcodes.ACC_PUBLIC, body.name,
//            desc, null, body.exceptions);
//        cw.visitEnd();
        return null;
    }

    private AbstractInsnNode locateCallSiteByIndex(InsnList units,
            VarInsnNode acallsite, int callSiteIndex) {
        //
        // finding this pattern
        //	    ALOAD 1
        //	    LDC 0
        //	    AALOAD
        //
        AbstractInsnNode found = null;
        for (int i = 0; i < units.size(); i ++) {
            AbstractInsnNode s = (AbstractInsnNode) units.get(i);
            if(s.getOpcode() != ALOAD) continue;
            VarInsnNode aload = (VarInsnNode)s;
            if(aload.var != acallsite.var) continue;
            AbstractInsnNode s0 = units.get(i + 1);
            if(s0.getOpcode() != LDC) continue;
            LdcInsnNode ldc = (LdcInsnNode)s0;
            if((Integer)ldc.cst != callSiteIndex) continue;
            AbstractInsnNode s1 = units.get(i + 2);
            if(s1.getOpcode() != AALOAD) continue;
            found = s;
            break;
        }

        if(found == null) return null;

        PartialDefUseAnalyser ana = new PartialDefUseAnalyser(
                                            body, found,
                                            INVOKEINTERFACE);
        AbstractInsnNode invoke = ana.analyse0();
        //
        // TODO Must find a way to use the "result"
        // for further autoboxing
        //
        Map<AbstractInsnNode, AbstractInsnNode[]> result = ana.getUsedMap();
        return invoke;
    }

    private static final String GET_CALL_SITE_ARRAY =
        "$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite";

    private VarInsnNode findCallSiteArray(InsnList units) {
//	    ALOAD 0
//	    INVOKESPECIAL java/lang/Object.<init>()V
//	   L0
//	    INVOKESTATIC org/codehaus/groovy/gjit/soot/Subject.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
//	    ASTORE 1
        for (int i = 0; i < units.size(); i ++) {
            AbstractInsnNode node = (AbstractInsnNode) units.get(i);
            if(node.getOpcode() != INVOKESTATIC) continue;
            MethodInsnNode invNode = (MethodInsnNode) node;
            if( GET_CALL_SITE_ARRAY.equals(invNode.name + invNode.desc)) {
                AbstractInsnNode s0 = units.get(i + i);
                if(s0.getOpcode() == ASTORE) return (VarInsnNode)s0;
            }
        }

        return null;
    }

}
