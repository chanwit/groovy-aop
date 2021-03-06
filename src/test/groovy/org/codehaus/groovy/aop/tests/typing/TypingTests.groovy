package org.codehaus.groovy.aop.tests.typing

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase

class TypingTests extends GroovyAOPTestCase {

    static aspectCode ='''
class TypingAspect {

  static aspect = {
    def pc = pcall('org.codehaus.groovy.aop.tests.typing.Target.method') & args(i)
    typing(pc) {
      i >> int
  	  return int
    }
  }

}
'''

    void testTyping() {
        setupAspect(aspectCode)
        def target = new Target()
        assert target.method(1).class.toString() == "class int"
    }
}
