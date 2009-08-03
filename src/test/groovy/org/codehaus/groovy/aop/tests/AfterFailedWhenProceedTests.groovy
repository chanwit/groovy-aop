package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.ProceedNotAllowedException
import org.codehaus.groovy.aop.GroovyAOPTestCase

class AfterFailedWhenProceedTests extends GroovyAOPTestCase {
	
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
    after(pc) { ctx ->
      proceed(ctx.args)
    }
  }
}
'''

	void testAfter() {
		setupAspect(aspectCode)

		def target  = gcl.parseClass(targetCode).newInstance()
		try {
		  target.method(1)
		} catch(e) {
			assert e.cause instanceof ProceedNotAllowedException
		}		
	}
}