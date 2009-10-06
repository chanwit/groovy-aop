package org.codehaus.groovy.gjit.asm.sieve

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*

class SieveTest extends GroovyTestCase {

    void testRun_Advised() {
        ExpandoMetaClass.enableGlobally()
        def aspect = weave(SieveAspect)
        assert aspect != null
        for(i in 1..5) {
            Caller.realTest(100000)
            Caller.realTest(150000)
            Caller.realTest(200000)
            Caller.realTest(250000)
            Caller.realTest(300000)
        }
        unweave(SieveAspect)
    }

    static void main(args){
        ExpandoMetaClass.enableGlobally()
        def aspect = weave(SieveAspect)
        assert aspect != null
        for(i in 1..5) {
            Caller.realTest(100000)
            Caller.realTest(150000)
            Caller.realTest(200000)
            Caller.realTest(250000)
            Caller.realTest(300000)
        }
        unweave(SieveAspect)        
    }

}