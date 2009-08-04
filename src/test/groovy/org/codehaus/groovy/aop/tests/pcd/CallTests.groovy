package org.codehaus.groovy.aop.tests.pcd

import org.codehaus.groovy.aop.GroovyAOPTestCase

class CallTests extends GroovyAOPTestCase {

    static targetCode = '''
class CallTarget {
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
class CallAspect {

    static aspect = {
        def pc1 = call('CallTarget.method_001')
        def pc2 = call('CallTarget.method_002')
        def pc3 = call('CallTarget.method_003')

        before(pc1) { ctx ->
            assert ctx.args[0] == 1
        }
        after(pc1)  { ctx ->
            assert ctx.args[0] == 1
        }
        after(pc2) { ctx ->
            assert ctx.args[0] == 2
        }
        before(pc3) { ctx -> assert ctx.args[0] == 3 }
        after(pc3)  { ctx -> assert ctx.args[0] == 3 }

        // method_003
        around(pc3) { ctx ->
            assert ctx.args[0] == 3
            return proceed(ctx.args[0]+1)
        }

        // method_003
        after(returning:pc3) { r ->
            assert r == 4
        }
    }

}
'''
    void testCall_All() {
        setupAspect(aspectCode)
        def target = gcl.parseClass(targetCode).newInstance()
        // println target.metaClass
        assert target.method_001(1) == 10
        assert target.method_002(2) == 20
        assert target.method_003(3) == 4
    }

}
