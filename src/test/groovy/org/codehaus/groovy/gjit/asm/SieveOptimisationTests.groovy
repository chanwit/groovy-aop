package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.*

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.soot.sieve.*
import org.codehaus.groovy.gjit.asm.transformer.*

public class SieveOptimisationTests extends GroovyTestCase implements Opcodes {

    static SIEVE_X = "org/codehaus/groovy/gjit/soot/sieve/Sieve_sieve_x"

    void testOptimiseOnSieve() {
        InsnListHelper.install()

        def sender = Sieve.class
        def sco  = new AsmSingleClassOptimizer()
        def aatf = new AspectAwareTransformer(
            advisedTypes:[int, int] as Class[],
            advisedReturnType: null,
            callSite: new Sieve$sieve(),
            withInMethodName: "main"
        )
        sco.transformers = [
          DeConstantTransformer.class,
          aatf,
          AutoBoxEliminatorTransformer.class
        ]
        byte[] bytes = sco.optimize(sender.name)
        def cr = new ClassReader(bytes)
        def cn = new ClassNode()
        cr.accept cn, 0
        def main = cn.methods.find { it.name == "main" }
        assert main.name == "main"
        // CheckClassAdapter.verify(cr, false, new PrintWriter(System.out))

        def sieve_x_body = ClassBodyCache.v().get(SIEVE_X)
        assert sieve_x_body != null
        cr = new ClassReader(sieve_x_body)
        CheckClassAdapter.verify(cr, false, new PrintWriter(System.out))
        def tcv = new TraceClassVisitor(new PrintWriter(System.out))
        cr.accept tcv, 0
    }

}