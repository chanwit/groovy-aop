package org.codehaus.groovy.gjit.asm

import org.codehaus.groovy.gjit.asm.transformer.AsmAspectAwareTransformer;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib$fib
import groovy.util.GroovyTestCase

public class AsmSingleClassOptimiserTests extends GroovyTestCase {

    void testOptimisationFib() {
        def sender = Fib.class
        def sco = new AsmSingleClassOptimizer();
        def aatf = new AsmAspectAwareTransformer(
            advisedTypes:[int] as Class[],
            advisedReturnType: int,
            callSite: new Fib$fib(),
            withInMethodName: "main"
        )
        sco.transformers = [aatf]
        sco.optimize(sender)
    }

}