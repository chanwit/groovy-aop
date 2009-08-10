package org.codehaus.groovy.aop.tests.typing

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase

class TypingTests extends GroovyAOPTestCase {

    static targetCode ='''
class Target {

    def method(i) {
        return i
    }

}
'''

    static aspectCode ='''
class TypingAspect {

  static aspect = {
    def pc = pcall('Target.method') & args(i)
    typing(pc) {
      i >> int
    }
  }

}
'''

    void testTyping() {
        setupAspect(aspectCode)
        def target = gcl.parseClass(targetCode).newInstance()
        assert target.method(1).class.toString() == "class int"
    }
}
