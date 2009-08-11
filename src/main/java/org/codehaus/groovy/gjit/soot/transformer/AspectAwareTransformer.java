package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;

public class AspectAwareTransformer extends BodyTransformer {

	private Class<?>[] argTypes;
	public void setArgTypes(Class<?>[] argTypes) {
		this.argTypes = argTypes;
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
		System.out.println("========================");
		System.out.println("argTypes: " + argTypes);
		for (int i = 0; i < argTypes.length; i++) {
			System.out.println("arg[" + i + "]: " + argTypes[i]);
		}
		System.out.println(b.getMethod());
		System.out.println("========================");
	}

}
