package org.codehaus.groovy.gjit.asm.fannkuch

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*

class FannkuchTest extends GroovyTestCase {
    
    void testRun_Advised() {
        ExpandoMetaClass.enableGlobally()
        def aspect = weave(FannkuchAspect)
        assert aspect != null
        for(i in 1..5) {
            Caller.realTest(9)
            Caller.realTest(10)
            Caller.realTest(11)
            Caller.realTest(12)
        }
        unweave(FannkuchAspect)
    }
    
}