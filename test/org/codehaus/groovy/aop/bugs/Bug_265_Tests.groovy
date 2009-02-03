package org.codehaus.groovy.aop.bugs

import org.codehaus.groovy.aop.metaclass.AspectMetaClass
import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase
/**
 * This bug should generate stack overflow, but cannot reproduce atm 
 * **/
class Bug_265_Tests extends GroovyAOPTestCase {
		
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
    def pc = pcall('*')
    before(pc) { i ->
      assert i == 1
    }
  }

}
'''

	void testIt() {
	    setupAspect(aspectCode)
	    
		def target  = gcl.parseClass(targetCode).newInstance()
		assert target.method(1) == 1				
	}
}