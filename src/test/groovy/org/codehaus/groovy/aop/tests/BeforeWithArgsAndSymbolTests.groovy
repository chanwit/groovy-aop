package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase

class BeforeWithArgsAndSymbolTests extends GroovyAOPTestCase {

    static targetCode =
'''class SymbolTarget {

    def new_method(int i) {
        return i
    }

}
'''

    static aspectCode =
'''class SymbolAspect {

  static aspect = {
    def pc = pcall(SymbolTarget.new_method) & args(i)
    before(pc) {
      println i
      println i.class
      assert i == 1
      throw new Exception(i.toString())
    }
  }

}
'''

    void testBefore() {
        setupAspect(aspectCode)
        def target  = gcl.parseClass(targetCode).newInstance()
        try {
            target.new_method(1)
            fail("cannot pass here")
        } catch(e) {
            println e.message
            assert e.message == "java.lang.Exception: 1"
        }
    }
}
