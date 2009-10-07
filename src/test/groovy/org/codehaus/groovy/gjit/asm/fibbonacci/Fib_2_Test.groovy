package org.codehaus.groovy.gjit.asm.fibbonacci

import groovy.lang.ExpandoMetaClass;
import groovy.util.GroovyTestCase

class Fib_2_Test extends GroovyTestCase {

    void testRunFib_02_UnAdvised() {
        for(i in 1..10) {
            Caller.realTest(5)
            Caller.realTest(10)
            Caller.realTest(15)
            Caller.realTest(20)
            Caller.realTest(25)
            Caller.realTest(30)
            Caller.realTest(35)
        }
    }

    static void main(args) {
        for(i in 1..10) {
            Caller.realTest(5)
            Caller.realTest(10)
            Caller.realTest(15)
            Caller.realTest(20)
            Caller.realTest(25)
            Caller.realTest(30)
            Caller.realTest(35)
        }
    }

}
