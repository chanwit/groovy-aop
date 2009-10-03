package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.*

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.soot.fft.*
import org.codehaus.groovy.gjit.asm.transformer.*

public class FFTOptimisationTests extends GroovyTestCase implements Opcodes {

    static FFT_TRANS_X = "org/codehaus/groovy/gjit/soot/fft/FFT_transform_x"

    void testOptimiseOnFFT() {
        InsnListHelper.install()

        def sender = FFT.class
        def sco  = new AsmSingleClassOptimizer()
        def aatf = new AspectAwareTransformer(
            advisedTypes:[double[]] as Class[],
            advisedReturnType: null,
            callSite: new FFT$transform(),
            withInMethodName: "test"
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

        def fft_x_body = ClassBodyCache.v().get(FFT_TRANS_X)
        assert fft_x_body != null
        cr = new ClassReader(fft_x_body)
        CheckClassAdapter.verify(cr, false, new PrintWriter(System.out))
        def tcv = new TraceClassVisitor(new PrintWriter(System.out))
        cr.accept tcv, 0
    }

}
