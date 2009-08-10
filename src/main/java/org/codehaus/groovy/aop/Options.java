package org.codehaus.groovy.aop;

public class Options {

	private static boolean typeAdviceEnabled = false;

	public static boolean isTypeAdviceEnabled() {
		return typeAdviceEnabled;
	}

	static {
		String prop = System.getProperty("type.advice.enabled", "false");
		typeAdviceEnabled = prop.equals("true");
	}

}
