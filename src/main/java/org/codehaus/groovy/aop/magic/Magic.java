package org.codehaus.groovy.aop.magic;

import sun.reflect.GroovyAOPMagic;

public class Magic extends GroovyAOPMagic {

	void test() {
		TestMagic t = new TestMagic();
		System.out.println(t);
	}

	public static void main(String[] args) {
		new Magic().test();
	}

}
