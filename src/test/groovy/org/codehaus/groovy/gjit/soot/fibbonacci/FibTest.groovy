package org.codehaus.groovy.gjit.soot.fibbonacci

import groovy.util.GroovyTestCase
class FibTest extends GroovyTestCase {

	void testRunFib() {
		weave FibAspect.class
		Fib.main([] as String[])
	}

}
