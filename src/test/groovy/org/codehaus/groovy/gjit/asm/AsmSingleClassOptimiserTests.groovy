package org.codehaus.groovy.gjit.asm

import java.io.PrintWriter

import org.codehaus.groovy.runtime.callsite.CallSite

import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*
import org.codehaus.groovy.gjit.soot.fibbonacci.*

import org.objectweb.asm.*
import org.objectweb.asm.util.*
import org.objectweb.asm.tree.*

import groovy.util.GroovyTestCase

public class AsmSingleClassOptimiserTests extends GroovyTestCase {

    static FIB_FIB_X = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib_fib_x"

    void testOptimisationFib() {
        InsnListHelper.install()

        def sender = Fib.class
        def sco = new AsmSingleClassOptimizer()
        def aatf = new AspectAwareTransformer(
            advisedTypes:[int] as Class[],
            advisedReturnType: int,
            callSite: new Fib$fib(),
            withInMethodName: "main"
        )
        sco.transformers = [DeConstantTransformer.class,
                            aatf,
                            AutoBoxEliminatorTransformer.class]
        byte[] bytes = sco.optimize(sender.name)
        def cr = new ClassReader(bytes)
        def cn = new ClassNode()
        cr.accept cn, 0
        def main = cn.@methods.find { it.name == "main" }
        assert main.name == "main"

        def units = main.instructions
        // skip 0, it's a label
        assertEquals asm {
            invokestatic Fib,'$getCallSiteArray',[],CallSite[]
            astore 1
        }, units[1..2]
        // skip 3,4 they're label and line no
        assertEquals asm {
            aload 1
            ldc 5
            aaload
            invokestatic Fib, '$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib',[],Class
            iconst_5
            invokestatic FIB_FIB_X,'fib',[int],int
            invokestatic Integer,"valueOf",[int],Integer
        }, units[5..11]
        //
        // re-check again with a verifier
        //
        CheckClassAdapter.verify(cr, false, new PrintWriter(System.out))

        def fib_x_body = ClassBodyCache.v().get(FIB_FIB_X)
        assert fib_x_body != null
        assertFib_fib_x()
    }

    private assertFib_fib_x() {
        def sco = new TypeAdvisedReOptimizer()
        def aatf = new AspectAwareTransformer(
                advisedTypes:[int] as Class[],
                advisedReturnType: int,
                callSite: new Fib_fib_x$fib(),
                withInMethodName: "fib"
            )
        sco.transformers = [DeConstantTransformer.class,
                            aatf,
                            AutoBoxEliminatorTransformer.class]
        byte[] bytes = sco.optimize(FIB_FIB_X)
        assert bytes != null
    }

}