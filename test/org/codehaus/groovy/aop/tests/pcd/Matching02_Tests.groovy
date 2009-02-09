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

class Matching02_Tests extends GroovyAOPTestCase {

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

        def result = new EffectiveAdvices();
        def jp = new CallJoinpoint(
                target.class,
                "method_001",
                target,
                [new Integer(1)] as Object[],
                [int.class] as Class[],
                false
        );
        m.matchPerClass(result, jp);

        def before = result.get(Advice.BEFORE)
        def after = result.get(Advice.AFTER)
        def around = result.get(Advice.AROUND)
        def after_return = result.get(Advice.AFTER_RETURNING)

        assert result.empty == false

        assert before.size == 1
        assert after.size == 1
        assert around.size == 0
        assert after_return.size == 0
    }


    void testMatching_02() {
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

        def result = new EffectiveAdvices();
        def jp = new CallJoinpoint(
                target.class,
                "method_002",
                target,
                [new Integer(1)] as Object[],
                [int.class] as Class[],
                false
        );
        m.matchPerClass(result, jp);

        def before = result.get(Advice.BEFORE)
        def after = result.get(Advice.AFTER)
        def around = result.get(Advice.AROUND)
        def after_return = result.get(Advice.AFTER_RETURNING)

        assert result.empty == false

        assert before.size == 0
        assert after.size == 1
        assert around.size == 0
        assert after_return.size == 0
    }

    void testMatching_03() {
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

        def result = new EffectiveAdvices();
        def jp = new CallJoinpoint(
                target.class,
                "method_003",
                target,
                [new Integer(1)] as Object[],
                [int.class] as Class[],
                false
        );
        m.matchPerClass(result, jp);

        def before = result.get(Advice.BEFORE)
        def after = result.get(Advice.AFTER)
        def around = result.get(Advice.AROUND)
        def after_return = result.get(Advice.AFTER_RETURNING)

        assert result.empty == false

        assert before.size == 1
        assert after.size == 1
        assert around.size == 1
        println after_return.size
        // assert after_return.size == 1
    }
}
