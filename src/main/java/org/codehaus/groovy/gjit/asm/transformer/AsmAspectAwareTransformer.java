package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.groovy.gjit.asm.PartialDefUseAnalyser;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import soot.SootMethod;
import soot.Unit;
import soot.Value;

public class AsmAspectAwareTransformer implements Transformer, Opcodes {

	private String withInMethodName;
	private MethodNode body;
	private InsnList units;
	private CallSite callSite;

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

	private MethodNode typePropagate(CallSite callSite) {
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

		PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(body, found, ?);
		Map<AbstractInsnNode, AbstractInsnNode[]> result = pdua.analyse();

		return null;
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
