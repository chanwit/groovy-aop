package org.codehaus.groovy.gjit.soot

import org.codehaus.groovy.gjit.soot.SingleClassOptimizer

import soot.Scene;
import java.lang.instrument.Instrumentation
import groovy.util.GroovyTestCase
import java.lang.instrument.ClassDefinition
import org.codehaus.groovy.gjit.agent.Agent

import org.codehaus.groovy.gjit.soot.transformer.CallsiteNameCollector;

/**
 *  This test case needs enabling JVMTI to run it
 **/class TypingTest extends GroovyTestCase {

	def i

	@Override
	protected void setUp() throws Exception {
		 i = Agent.getInstrumentation()
	}

	void testSelfTest() {
		assert i != null
		def c = Subject.class
		byte[] bytes = new SingleClassOptimizer(viaShimple: true).optimize(c)
		assert bytes.length != 0
		i.redefineClasses(new ClassDefinition(c, bytes))
		assert new Subject().add(10, 20) == 30
	}

	void testReplacingOperator() {
		def c = Subject.class
		byte[] bytes = new SingleClassOptimizer(
				viaShimple: true,
				transformers: [new CallsiteNameCollector()]
		).optimize(c)
		String[] names = CallsiteNameHolder.v().get(c.name)
		assert names[0] == "println"
		assert names[1] == "plus"

		assert bytes.length != 0
		i.redefineClasses(new ClassDefinition(c, bytes))
		assert new Subject().add(10, 20) == 30
	}

}
