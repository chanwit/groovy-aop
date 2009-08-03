package org.codehaus.groovy.aop.tests.typing

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase

class TypingTests extends GroovyAOPTestCase {

    static targetCode ='''
class Target {

    int method(i) {
        return i
    }

}
'''

    static aspectCode ='''
class TypingAspect {

  static aspect = {
    def pc = pcall('Target.method') & args(i)
    typing(pc) {
      i >> int.class
    }
  }

}
'''

    void testTyping() {
        setupAspect(aspectCode)
        def target = gcl.parseClass(targetCode).newInstance()
        println target.method(1).class.toString()
        // assert target.method(1).class.toString() == "class int"
        int i = 10
        println i.class.toString()
    }
}
