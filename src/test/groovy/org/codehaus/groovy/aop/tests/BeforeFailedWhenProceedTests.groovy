package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.ProceedNotAllowedException
import org.codehaus.groovy.aop.GroovyAOPTestCase

class BeforeFailedWhenProceedTests extends GroovyAOPTestCase {
	
	static targetCode =
'''class Target {

	def method(int i) {
		return i
	}
	
}
'''		
	
	static aspectCode =
'''class BeforeAspect {

  static aspect = {
    def pc = pcall('Target.method')
    before(pc) { i ->
    	proceed(i)
    }
  }

}
'''

	void testBefore() {
		setupAspect(aspectCode)

		def target  = gcl.parseClass(targetCode).newInstance()
		try {
		  target.method(1)
		} catch(e) {
			assert e.cause instanceof ProceedNotAllowedException
		}
	}
}