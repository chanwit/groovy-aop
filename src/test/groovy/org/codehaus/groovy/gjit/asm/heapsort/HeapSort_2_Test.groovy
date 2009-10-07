package org.codehaus.groovy.gjit.asm.heapsort

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*

public class HeapSort_2_Test extends GroovyTestCase {

    void testRun_UnAdvised() {
        for(i in 1..5) {
            Caller.realTest(1000)
            Caller.realTest(5000)
            Caller.realTest(10000)
            Caller.realTest(50000)
            Caller.realTest(100000)
            Caller.realTest(500000)
            Caller.realTest(1000000)
            Caller.realTest(5000000)
        }
    }

    static void main(args) {
        for(i in 1..5) {
            Caller.realTest(1000)
            Caller.realTest(5000)
            Caller.realTest(10000)
            Caller.realTest(50000)
            Caller.realTest(100000)
            Caller.realTest(500000)
            Caller.realTest(1000000)
            Caller.realTest(5000000)
        }
    }

}
