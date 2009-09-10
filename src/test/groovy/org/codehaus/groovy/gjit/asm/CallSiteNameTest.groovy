package org.codehaus.groovy.gjit.asm

import org.codehaus.groovy.gjit.asm.AsmSingleClassOptimizer
import org.codehaus.groovy.gjit.asm.CallSiteNameHolder
import org.codehaus.groovy.gjit.asm.Subject
import org.codehaus.groovy.gjit.asm.transformer.CallSiteNameCollector
import org.objectweb.asm.Type

class CallSiteNameTest extends GroovyTestCase {

    void testCollectingCallSiteFromClass() {
        CallSiteNameHolder.v().clear()
        try {
            new AsmSingleClassOptimizer(
                transformers:[CallSiteNameCollector]
            ).optimize(Subject.class.name)
            def callsiteNames = CallSiteNameHolder.v().get(Type.getInternalName(Subject))
            assert callsiteNames != null
            assert callsiteNames[0] == "println"
        } finally {
            CallSiteNameHolder.v().clear()
        }
    }
}