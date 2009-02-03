package org.codehaus.groovy.aop.tests.pcd

import org.codehaus.groovy.aop.GroovyAOPTestCase
class PCallTests extends GroovyAOPTestCase {
	
	static targetCode = '''
class Target {
	def method_001(int i) {
		return 10
	}
	def method_002(int i) {
		return 20
	}
	def method_003(int i) {
		return i
	}
}
'''

	static aspectCode = '''
class PCallAspect {

	static aspect = {
		def pc1 = pcall('Target.method_001')
		def pc2 = pcall('Target.method_002')
		def pc3 = pcall('Target.method_003')

		before(pc1) { i ->
			assert i == 1
		}
		after(pc1)  { i ->
			assert i == 1
		}
		after(pc2) { i ->
			assert i == 2
		}
		before(pc3) { i -> assert i== 3 }
		after(pc3)  { i -> assert i== 3 }
		around(pc3) { i ->
			assert i == 3
			proceed(i+1)
		}
		after(returning:pc3) { r ->
			assert r == 4
		}
	}

}
'''
	void testPCall_All() {
		setupAspect(aspectCode)
		def target = gcl.parseClass(targetCode).newInstance()
		assert target.method_001(1) == 10
		assert target.method_002(2) == 20
		assert target.method_003(3) == 4
	}
	
}