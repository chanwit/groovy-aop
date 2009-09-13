package org.codehaus.groovy.gjit.asm.fibbonacci

import groovy.lang.ExpandoMetaClass;
import groovy.util.GroovyTestCase
class FibTest extends GroovyTestCase {

    void testRunFib() {
        ExpandoMetaClass.enableGlobally()
        def aspect = weave(FibAspect)
        assert aspect != null
        for(i in 1..4) {
            Caller.realTest(5)
            Caller.realTest(10)
            Caller.realTest(15)
            Caller.realTest(20)
            Caller.realTest(25)
            Caller.realTest(30)
            Caller.realTest(35)
        }
        unweave(FibAspect)
    }

}
