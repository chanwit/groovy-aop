package org.codehaus.groovy.aop.abstraction.pcd;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class NotPCD extends ComposablePCD {

	private PCD original;

	public NotPCD(PCD original) {
		this.original = original;
	}

	@Override
	public boolean matches(Joinpoint jp) {
		return !(original.matches(jp));
	}

}
