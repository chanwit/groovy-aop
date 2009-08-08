package org.codehaus.groovy.gjit.soot

import soot.Scene
import groovy.lang.GroovyClassLoader
import groovy.util.GroovyTestCase

class CallsiteNameTest extends GroovyTestCase {

/*
	void testCollectingCallSiteFromNewlyLoadedClass() {
		def code = '''
class Test {
  def hello() {
    println "hello"
  }
}
'''
		def gcl = new GroovyClassLoader()
		def c = gcl.parseClass(code)
		gcl.parseClass()
		new SingleClassOptimizer().optimize(c)
		def sc = Scene.v().getSootClass("Test")
		assert sc != null
		def callsiteNames = CallsiteNameHolder.v().get(sc)
		assert callsiteNames != null
		callsiteNames.each {
			println it
		}
	}
*/

	void testCollectingCallSiteFromClass() {
		def sc = Scene.v().loadClassAndSupport(Subject.class.name)
		assert sc != null
		new SingleClassOptimizer().optimize(Subject.class)
		def callsiteNames = CallsiteNameHolder.v().get(sc)
		assert callsiteNames != null
		assert callsiteNames[0] == "println"
	}
}