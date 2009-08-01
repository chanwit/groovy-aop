package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.GroovyAOPTestCase
class AroundTests extends GroovyAOPTestCase {
	
	static targetCode ='''
class Target {

	def method(int i) {
		return i
	}
	
}
'''		
	
	static aspectCode ='''
class AroundAspect {

  static aspect = {
    def pc = pcall('Target.method')
    around(pc) { i ->
    	println args[0]
    	println args.length 
    	proceed(i)
    }
  }

}
'''

	void testAround() {
		setupAspect(aspectCode)

		def target  = gcl.parseClass(targetCode).newInstance()
		assert target.method(1) == 1		
		assert target.method(2) == 2				
	}
}