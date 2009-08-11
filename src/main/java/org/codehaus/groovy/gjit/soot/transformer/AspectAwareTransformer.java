package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.IntConstant;

public class AspectAwareTransformer extends BodyTransformer {

	private String withInMethodName;

	private Class<?>[] callSiteArgTypes;
	private int callSiteIndex;
	private String callSiteName;

	public void setCallSiteIndex(int callSiteIndex) {
		this.callSiteIndex = callSiteIndex;
	}

	public void setCallSiteArgTypes(Class<?>[] argTypes) {
		this.callSiteArgTypes = argTypes;
	}

	public void setWithInMethodName(String withInMethodName) {
		this.withInMethodName = withInMethodName;
	}

	public void setCallSiteName(String callSiteName) {
	    this.callSiteName = callSiteName;
	}

	private Value findCallSiteArray(PatchingChain<Unit> u) {
		Iterator<Unit> stmts = u.iterator();
		while(stmts.hasNext()) {
			Unit s = stmts.next();
			List<ValueBox> defBoxes = s.getDefBoxes();
			if(defBoxes.size()!=1) continue;
			// check if it's CallSite[]
			Value v = defBoxes.get(0).getValue();
			if(v.getType() instanceof ArrayType == false) continue;
			ArrayType at = (ArrayType)v.getType();
			if(at.getArrayElementType().equals(Utils.v().getCallSiteType())==false) continue;
			return v;
		}
		return null;
	}

	private Unit locateCallSiteByIndex(PatchingChain<Unit> units,
			Value acallsite, int callSiteIndex) {
		Iterator<Unit> stmts = units.iterator();
		Unit found = null;
		while(stmts.hasNext()) {
			Unit s = stmts.next();
			List<ValueBox> useBoxes = s.getUseBoxes();
			if(useBoxes.size()!=3) continue;
			if(useBoxes.get(0).getValue().equivTo(acallsite)==false) continue;
			int index = ((IntConstant)useBoxes.get(1).getValue()).value;
			if(index != callSiteIndex) continue;
			found = s;
			break;
		}

		if(found == null) return null;
		Value callSiteBase = found.getDefBoxes().get(0).getValue();
		Unit s = found;
		while(true) {
			s = units.getSuccOf(s);
			if(s.getUseBoxes().get(0).getValue().equivTo(callSiteBase)) {
				return s;
			}
			if(s==null) break;
		}
		return null;
	}


	@Override
	protected void internalTransform(Body b, String phase, Map options) {
		// do type matching
		// if match, do tranformation for matched call
		// this transformation just occurs right before invocation
		// NOT after weaving
		// but *before* invocation
		// this this should be triggered in EMC

		// PatchingChain<Unit> u = b.getUnits();
		// Iterator<Unit> stmts = u.snapshotIterator();
		// while (stmts.hasNext()) {
		//	Unit s = stmts.next();
		//	System.out.println(s);
		// }
		if(b.getMethod().getName().equals(withInMethodName)) {
			System.out.println("========================");
			System.out.println("argTypes: " + callSiteArgTypes);
			for (int i = 0; i < callSiteArgTypes.length; i++) {
				System.out.println("arg[" + i + "]: " + callSiteArgTypes[i]);
			}

			System.out.println(b.getMethod());
			System.out.println("withInMethodName: " + withInMethodName);
			System.out.println("========================");

			Value acallsite = findCallSiteArray(b.getUnits());
			Unit invokeStatement = locateCallSiteByIndex(b.getUnits(), acallsite, callSiteIndex);
			System.out.println(invokeStatement);
			
		}
	}


}
