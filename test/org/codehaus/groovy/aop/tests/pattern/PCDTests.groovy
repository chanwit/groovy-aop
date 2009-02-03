package org.codehaus.groovy.aop.tests.pattern

import org.codehaus.groovy.aop.pattern.Parser

class PCDTests extends GroovyTestCase {
	
	void test_001() {
		def pc1 = callsTo(
			methods:'*',
			forTypes:[],
			inTypes:[],
			methodOptions:[]
		)
		
		def pc2 = executionOf(
			methods:'*',
			forTypes:[],
			inTypes:[],
			methodOptions:[]
		)
		
		def pc3 = pc1 & pc2
				
	}
	
}