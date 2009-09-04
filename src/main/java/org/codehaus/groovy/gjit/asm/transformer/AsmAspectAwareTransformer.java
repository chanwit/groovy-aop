package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.asm.AsmTypeAdvisedClassGenerator;
import org.codehaus.groovy.gjit.asm.PartialDefUseAnalyser;
import org.codehaus.groovy.gjit.asm.AsmTypeAdvisedClassGenerator.Result;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.AbstractVisitor;

public class AsmAspectAwareTransformer implements Transformer, Opcodes {

    private String withInMethodName;
    private MethodNode body;
    private InsnList units;
    private CallSite callSite;

    private Class<?> advisedReturnType;
    private Class<?>[] advisedTypes;

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        String fullname = body.name + body.desc;
        if( withInMethodName == null ||
            fullname.equals(withInMethodName))
        {
            this.body  = body;
            this.units = body.instructions;

            VarInsnNode acallsite = findCallSiteArray(units);
            AbstractInsnNode invokeStmt  = locateCallSiteByIndex(units, acallsite, callSite.getIndex());
            MethodInsnNode newInvokeStmt = typePropagate(callSite);
            replaceCallSite((MethodInsnNode)invokeStmt, newInvokeStmt);
        }
    }

    private void replaceCallSite(MethodInsnNode invokeStmt, MethodInsnNode newInvokeStmt) {
        //
        // match old and new arguments
        //
        System.out.println("old");
        System.out.println(AbstractVisitor.OPCODES[invokeStmt.getOpcode()]);
        System.out.println(invokeStmt.owner);
        System.out.println(invokeStmt.name);
        System.out.println(invokeStmt.desc);
        System.out.println("new");
        System.out.println(AbstractVisitor.OPCODES[newInvokeStmt.getOpcode()]);
        System.out.println(newInvokeStmt.owner);
        System.out.println(newInvokeStmt.name);
        System.out.println(newInvokeStmt.desc);
        // units.set(invokeStmt, newInvokeStmt);
    }

    private MethodInsnNode typePropagate(CallSite callSite) {
        AsmTypeAdvisedClassGenerator atacg = new AsmTypeAdvisedClassGenerator();
        atacg.setAdvisedTypes(advisedTypes);
        atacg.setAdvisedReturnType(advisedReturnType);
        Result result = atacg.perform(callSite);
        //
        // do caching the generated class for further optimisation
        //

        Utils.defineClass(result.owner, result.body);
        return new MethodInsnNode(INVOKESTATIC, result.owner, result.name, result.desc);
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

        PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(
                                            body, found,
                                            INVOKEINTERFACE);
        AbstractInsnNode invoke = pdua.analyse0();
        //
        // TODO Must find a way to use the "result"
        // for further autoboxing
        //
        Map<AbstractInsnNode, AbstractInsnNode[]> result = pdua.getUsedMap();
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

    public void setAdvisedTypes(Class<?>[] argTypeOfBinding) {
        this.advisedTypes = argTypeOfBinding;
    }

    public void setAdvisedReturnType(Class<?> returnType) {
        this.advisedReturnType = returnType;
    }

    public void setCallSite(CallSite callSite) {
        this.callSite = callSite;
    }

    public void setWithInMethodName(String withInMethodName) {
        this.withInMethodName = withInMethodName;
    }

}
