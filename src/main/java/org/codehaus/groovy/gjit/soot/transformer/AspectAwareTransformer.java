package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;

public class AspectAwareTransformer extends BodyTransformer {

	private Class<?>[] callSiteArgTypes;
	private String withInMethodName;

	public void setCallSiteArgTypes(Class<?>[] argTypes) {
		this.callSiteArgTypes = argTypes;
	}

	public void setMethodName(String withInMethodName) {
		this.withInMethodName = withInMethodName;
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
		}
	}

}
