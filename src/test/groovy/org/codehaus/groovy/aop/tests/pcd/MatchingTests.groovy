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

class MatchingTests extends GroovyAOPTestCase {

static targetCode = '''
    class Target {
        def method_001(int i) {
            return 10
        }
    }
    '''

static aspectCode = '''
    class PCallAspect {

        static aspect = {
            def pc1 = pcall('Target.method_001')
            before(pc1) { i ->
                assert i == 1
            }
            after(pc1)  { i ->
                assert i == 1
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
                AspectRegistry.instance(),
                AdviceCacheL1.instance(),
                AdviceCacheL2.instance()
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
        assert result.empty == false
        def before = result.get(Advice.BEFORE)
        def after = result.get(Advice.AFTER)
        def around = result.get(Advice.AROUND)
        def after_return = result.get(Advice.AFTER_RETURNING)

        assert before.size == 1
        assert after.size == 1
        assert around.size == 0
        assert after_return.size == 0
    }

}
