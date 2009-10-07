package org.codehaus.groovy.gjit.asm.sieve

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*

class Sieve_2_Test extends GroovyTestCase {

    void testRun_UnAdvised() {
        for(i in 1..5) {
            Caller.realTest(100000)
            Caller.realTest(150000)
            Caller.realTest(200000)
            Caller.realTest(250000)
            Caller.realTest(300000)
        }
    }

    static void main(args){
        for(i in 1..5) {
            Caller.realTest(100000)
            Caller.realTest(150000)
            Caller.realTest(200000)
            Caller.realTest(250000)
            Caller.realTest(300000)
        }
    }

}