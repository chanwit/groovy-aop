package org.codehaus.groovy.gjit.soot

import org.codehaus.groovy.gjit.soot.SingleClassOptimizer
import java.lang.instrument.Instrumentation
import groovy.util.GroovyTestCase
import java.lang.instrument.ClassDefinition
import org.codehaus.groovy.gjit.agent.Agent

/**
 *  This test case needs enabling JVMTI to run it
 **/class TypingTest extends GroovyTestCase {

	void testSomething() {
		def i = Agent.getInstrumentation()
		assert i != null
		byte[] bytes = new SingleClassOptimizer(viaShimple: true).optimize(Subject.class)
		assert bytes.length != 0
		i.redefineClasses(new ClassDefinition(Subject.class, bytes))
		assert new Subject().add(10, 20) == 30
	}

}
