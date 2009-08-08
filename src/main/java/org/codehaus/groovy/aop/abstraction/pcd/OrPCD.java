package org.codehaus.groovy.aop.abstraction.pcd;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class OrPCD implements PCD {

	private PCD left;
	private PCD right;

	public PCD getLeft() {
		return left;
	}

	public PCD getRight() {
		return right;
	}

	public OrPCD(PCD left, PCD right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean matches(Joinpoint jp) {
		if(left.matches(jp) == true)
		return true;
			else
		return right.matches(jp);
	}
}
