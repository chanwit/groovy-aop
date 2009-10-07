package org.codehaus.groovy.gjit.asm.fannkuch

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*

class Fannkuch_2_Test extends GroovyTestCase {

    void testRun_UnAdvised() {
        for(i in 1..5) {
            Caller.realTest(8)
            Caller.realTest(9)
            Caller.realTest(10)
            Caller.realTest(11)
        }
    }

    static void main(args) {
        for(i in 1..5) {
            Caller.realTest(8)
            Caller.realTest(9)
            Caller.realTest(10)
            Caller.realTest(11)
        }
    }

}