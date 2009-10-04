package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.*

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.soot.fft.*
import org.codehaus.groovy.gjit.asm.transformer.*

public class FFTOptimisationTests extends GroovyTestCase implements Opcodes {

    static FFT_TRANS_X      = "org/codehaus/groovy/gjit/soot/fft/FFT_transform_x"
    static FFT_TRANS_INTN_X = "org/codehaus/groovy/gjit/soot/fft/FFT_transformInternal_x"

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

        def fft_x_body = ClassBodyCache.v().get(FFT_TRANS_X)
        assert fft_x_body != null

        def main = cn.methods.find { it.name == "test" }
        assert main.name == "test"

        def reop = new TypeAdvisedReOptimizer()
        aatf = new AspectAwareTransformer(
            advisedTypes:[double[], int] as Class[],
            advisedReturnType: null,
            callSite: new FFT_transform_x$transformInternal(),
            withInMethodName: "transform"
        )
        reop.transformers = [
            DeConstantTransformer.class,
            aatf,
            AutoBoxEliminatorTransformer.class,
            UnusedCSARemovalTransformer.class
        ]
        bytes = reop.optimize(FFT_TRANS_X)
        cr = new ClassReader(fft_x_body)
        def tcv = new TraceClassVisitor(new PrintWriter(System.out))
        cr.accept tcv, 0
//        CheckClassAdapter.verify(cr, false, new PrintWriter(System.out))
//        CheckClassAdapter.verify(cr, false, new PrintWriter(System.out))
//        cr.accept tcv, 0
    }

}
