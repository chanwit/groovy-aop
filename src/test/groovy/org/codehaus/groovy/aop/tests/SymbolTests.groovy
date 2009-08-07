package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.Weaver
import org.codehaus.groovy.aop.GroovyAOPTestCase

class SymbolTests extends GroovyAOPTestCase {

    static targetCode =
'''class Target {

    def method(int i) {
        return i
    }

}
'''

    static aspectCode =
'''class BeforeAspect {

  static aspect = {
    def pc = pcall(Target.method)
    before(pc) { ctx ->
      def i = ctx.args[0]
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
            target.method(1)
            fail("cannot pass here")
        } catch(e) {
            assert e.message == "java.lang.Exception: 1"
        }
        // assert target.method(1) == 1
    }
}
