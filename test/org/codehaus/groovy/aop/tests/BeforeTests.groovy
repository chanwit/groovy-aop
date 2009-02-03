package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.metaclass.AspectMetaClass
import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase
class BeforeTests extends GroovyAOPTestCase {
	
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