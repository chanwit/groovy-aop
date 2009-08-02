package org.codehaus.groovy.aop.tests.pcd

import org.codehaus.groovy.aop.GroovyAOPTestCase

import org.codehaus.groovy.aop.AspectRegistry
import org.codehaus.groovy.aop.abstraction.Aspect

class SetterTests extends GroovyAOPTestCase {
	
	static targetCode='''
class Target {
    
	Integer prop
	Boolean done = false

} 
'''
			
	static beforeCode='''
class BeforeAspect {

	static aspect = {
		def pc = set('Target.prop')
	    assert pc != null
		before(pc) { value -> 
			++value
		}
	}

}
'''

	static afterCode='''
class AfterAspect {

	static aspect = {
		def pc = set('Target.prop')
		assert pc != null
		after(pc) {
			done = true
		}
	}

}
'''

	static beforeAfterCode='''
class BeforeAfterAspect {
	static aspect = {
		def pc = set('Target.prop')
		before(pc) { v -> ++v }
		after(pc)  { v -> assert v == 2 }
	}
}
'''
    void testSomething() {}
	
	// void testBeforeSetter() {
	// 	setupAspect(beforeCode)

	// 	def target = gcl.parseClass(targetCode).newInstance()
	// 	target.prop = 1
	// 	assert target.prop == 2			
	// }

	// void testAfterSetter() {
	// 	setupAspect(afterCode)
	// 	
	// 	def target = gcl.parseClass(targetCode).newInstance()
	// 	target.prop = 1
	// 	assert target.prop == 1
	// 	assert target.done == true
	// }
	
	// void testBeforeAfterSetter() {
	// 	setupAspect(beforeAfterCode)
	// 	
	// 	def target = gcl.parseClass(targetCode).newInstance()
	// 	target.prop = 1
	// 	assert target.prop == 2
	// 	assert target.done == false
	// }	
	
}