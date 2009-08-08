package org.codehaus.groovy.aop.abstraction.pcd;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class AndPCD implements PCD {

	private PCD left;
	private PCD right;

	public PCD getLeft() {
		return left;
	}

	public PCD getRight() {
		return right;
	}

	public AndPCD(PCD left, PCD right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean matches(Joinpoint jp) {
		if(!left.matches(jp))
			return false;
		else
			return right.matches(jp);
	}
}
