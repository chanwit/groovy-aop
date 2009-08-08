package org.codehaus.groovy.aop.abstraction.pcd;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class NotPCD implements PCD {

	PCD original;

	public NotPCD(AbstractPCD original) {
		this.original = original;
	}

	@Override
	public boolean matches(Joinpoint jp) {
		return !(original.matches(jp));
	}

}
