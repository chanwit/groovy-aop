package org.codehaus.groovy.aop.tests.pcd

import junit.framework.TestCase
import org.codehaus.groovy.aop.GroovyAOPTestCase

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.abstraction.joinpoint.CallJoinpoint;
import org.codehaus.groovy.aop.cache.AdviceCacheL1;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;
import org.codehaus.groovy.aop.metaclass.EffectiveAdvices;
import org.codehaus.groovy.aop.metaclass.Matcher;

import org.codehaus.groovy.aop.abstraction.Advice

class Matching03_Tests extends GroovyAOPTestCase {

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

        // method_003
        around(pc3) { i ->
            assert i == 3
            proceed(i+1)
        }

        // method_003
        after(returning:pc3) { r ->
            assert r == 4
        }
    }
}
'''

    void testMatching() {
        AspectRegistry.reset()
        AdviceCacheL1.reset()
        AdviceCacheL2.reset()

        setupAspect(aspectCode)
        def target = gcl.parseClass(targetCode).newInstance()
        def m = new Matcher(
                AspectRegistry.v(),
                AdviceCacheL1.v(),
                AdviceCacheL2.v()
        )

        def r1 = new EffectiveAdvices();
        def jp1 = new CallJoinpoint(
                target.class,
                "method_001",
                target,
                [new Integer(1)] as Object[],
                [int.class] as Class[],
                false
        );
        m.matchPerClass(r1, jp1);
        assert r1.get(Advice.BEFORE).size == 1
        assert r1.get(Advice.AFTER).size == 1
        assert r1.get(Advice.AROUND).size == 0
        assert r1.get(Advice.AFTER_RETURNING).size == 0

        def r2 = new EffectiveAdvices();
        def jp2 = new CallJoinpoint(
                target.class,
                "method_002",
                target,
                [new Integer(1)] as Object[],
                [int.class] as Class[],
                false
        );
        m.matchPerClass(r2, jp2);
        assert r2.get(Advice.BEFORE).size == 0
        assert r2.get(Advice.AFTER).size == 1
        assert r2.get(Advice.AROUND).size == 0
        assert r2.get(Advice.AFTER_RETURNING).size == 0

        def r3 = new EffectiveAdvices();
        def jp3 = new CallJoinpoint(
                target.class,
                "method_003",
                target,
                [new Integer(1)] as Object[],
                [int.class] as Class[],
                false
        );
        m.matchPerClass(r3, jp3);
        assert r3.get(Advice.BEFORE).size == 1
        assert r3.get(Advice.AFTER).size == 1
        assert r3.get(Advice.AROUND).size == 1
        assert r3.get(Advice.AFTER_RETURNING).size == 1
    }

}
