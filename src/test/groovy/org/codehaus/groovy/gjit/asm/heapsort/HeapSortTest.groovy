package org.codehaus.groovy.gjit.asm.heapsort;

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*

public class HeapSortTest extends GroovyTestCase {

	void testRun_Advised() {
		InsnListHelper.install()

		def sender = HeapSort.class
		def sco  = new AsmSingleClassOptimizer()
		def aatf = new AspectAwareTransformer()
	}

}
