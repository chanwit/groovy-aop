package org.codehaus.groovy.aop.tests.pcd

import org.codehaus.groovy.aop.GroovyAOPTestCase
import org.codehaus.groovy.aop.metaclass.AspectMetaClass
import org.codehaus.groovy.aop.metaclass.AspectMetaclassCreationHandle
import org.codehaus.groovy.aop.AspectRegistry
import org.codehaus.groovy.aop.abstraction.Aspect


class GetterTests extends GroovyAOPTestCase {

	static targetCode='''
class Target {
  
	Boolean done = false
	Integer prop

} 
'''
	
	static beforeCode='''
class BeforeAspect {

	static aspect = {
		def pc = get('Target.prop')
        assert pc != null
		before(pc) { 
		  done = true
		}
	}

}
'''

	static afterCode='''
class AfterAspect {

	static aspect = {
		def pc = get('Target.prop')
        assert pc != null
		after(pc) { i ->  
		  assert i == 2
		}
	}

}
'''

	static beforeAfterCode='''
class BeforeAfterAspect {

  static aspect = {
    def pc = get('Target.prop')
    assert pc != null
    before(pc) { done = true }
    after(pc)  { i -> assert i == 3 }
  }

}
'''

	void testBeforeGetter() {
		setupAspect(beforeCode)
		
		def target = gcl.parseClass(targetCode).newInstance()
		target.prop = 1
		assert target.prop == 1		
		assert target.done == true		
	}

	void testAfterGetter() {
		setupAspect(afterCode)

		def target = gcl.parseClass(targetCode).newInstance()
		target.prop = 2
		assert target.prop == 2
		assert target.done == false		
	}
	
	void testBeforeAfterGetter() {
		setupAspect(beforeAfterCode)

		def target = gcl.parseClass(targetCode).newInstance()
		target.prop = 3
		assert target.prop == 3
		assert target.done == true		
	}	
	
}