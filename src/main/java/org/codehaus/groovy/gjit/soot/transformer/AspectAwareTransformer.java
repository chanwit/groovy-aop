package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;

public class AspectAwareTransformer extends BodyTransformer {

	private Class<?>[] argTypes;

	public  Class<?>[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(Class<?>[] argTypes) {
		this.argTypes = argTypes;
	}

	@Override
	protected void internalTransform(Body body, String phase, Map option) {
		// do type matching
		// if match, do tranformation for matched call
		// this transformation just occurs right before invocation
		// NOT after weaving
		// but *before* invocation
		// this this should be tricker in EMC
	}

}
