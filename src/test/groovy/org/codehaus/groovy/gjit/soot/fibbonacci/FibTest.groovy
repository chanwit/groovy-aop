package org.codehaus.groovy.gjit.soot.fibbonacci

import groovy.util.GroovyTestCase
class FibTest extends GroovyTestCase {

	void testRunFib() {
		def aspect = weave(FibAspect)
		assert aspect != null
		Fib.main([] as String[])
		unweave(FibAspect)
	}

}
