package org.codehaus.groovy.gjit.asm

import java.lang.instrument.Instrumentation
import groovy.util.GroovyTestCase
import java.lang.instrument.ClassDefinition
import org.codehaus.groovy.gjit.agent.Agent

import org.codehaus.groovy.gjit.asm.AsmSingleClassOptimizer
import org.codehaus.groovy.gjit.asm.transformer.CallSiteNameCollector;

/**
 *  This test case needs enabling JVMTI to run it
 **/

    def i

    @Override
    protected void setUp() throws Exception {
         i = Agent.getInstrumentation()
    }

    void testSelfTest() {
        assert i != null
        def c = Subject.class
        byte[] bytes = new AsmSingleClassOptimizer().optimize(c)
        assert bytes.length != 0
        i.redefineClasses(new ClassDefinition(c, bytes))
        assert new Subject().add(10, 20) == 30
    }

    void testInjectingTransformerForSingleClassOptimizer() {
        try {
            assert i != null
            def c = Subject.class
            byte[] bytes = new AsmSingleClassOptimizer(
                transformers: [CallSiteNameCollector]
            ).optimize(c)
            String[] names = CallSiteNameHolder.v().get(c.name)
            assert names[0] == "println"
            assert names[1] == "plus"

            assert bytes.length != 0
            i.redefineClasses(new ClassDefinition(c, bytes))
            assert new Subject().add(10, 20) == 30
        } finally {
            CallSiteNameHolder.v().clear()
        }
    }

}