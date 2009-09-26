package org.codehaus.groovy.gjit.asm.fibbonacci

import groovy.lang.ExpandoMetaClass;
import groovy.util.GroovyTestCase
class FibTest extends GroovyTestCase {

//    void testRunFib_01_Unadvised() {
//        ExpandoMetaClass.enableGlobally()
//        Caller.realTest(5)
//        Caller.realTest(10)
//        Caller.realTest(15)
//        Caller.realTest(20)
//        Caller.realTest(25)
//        Caller.realTest(30)
//        Caller.realTest(35)
//    }

    void testRunFib_02_Advised() {
        ExpandoMetaClass.enableGlobally()
        def aspect = weave(FibAspect)
        assert aspect != null
        for(i in 1..10) {
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
