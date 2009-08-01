package org.codehaus.groovy.aop;

public class ProceedNotAllowedException extends Exception {

	public ProceedNotAllowedException() {
		super("proceed is not allowed to call from this context");
	}

	private static final long serialVersionUID = -1753161452936507205L;

}
