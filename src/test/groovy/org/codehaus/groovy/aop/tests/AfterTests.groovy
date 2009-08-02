package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase

class AfterTests extends GroovyAOPTestCase {
	
	static targetCode =
'''class Target {

	def method(int i) {
		return i
	}
	
}
'''		
	
	static aspectCode =
'''class AfterAspect {

  static aspect = {
    def pc = pcall('Target.method')
    after(pc) { i ->
      assert i == 1
    }
  }

}
'''

	void testBefore() {
		setupAspect(aspectCode)

		def target  = gcl.parseClass(targetCode).newInstance()
		assert target.method(1) == 1		
	}
}