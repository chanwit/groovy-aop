package org.codehaus.groovy.gjit.asm.breakdown

import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.*

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.soot.heapsort.b2.*
import org.codehaus.groovy.gjit.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*

public class HeapSort_b2_OptimisationTests extends GroovyTestCase implements Opcodes {

    static HEAPSORT_X = "org/codehaus/groovy/gjit/soot/heapsort/b2/HeapSort_heapsort_x"

    void testOptimiseOnHeapSort_b1() {
        InsnListHelper.install()

        def sender = HeapSort.class
        def sco  = new AsmSingleClassOptimizer()
        def aatf = new AspectAwareTransformer(
            advisedTypes:[int, double[]] as Class[],
            advisedReturnType: void,
            callSite: new HeapSort$heapsort(21),
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

        def heapsort_x_body = ClassBodyCache.v().get(HEAPSORT_X)
        assert heapsort_x_body != null
        
        cr = new ClassReader(heapsort_x_body)
        CheckClassAdapter.verify(cr, false, new PrintWriter(System.out))
        def tcv = new TraceClassVisitor(new PrintWriter(System.out))
        cr.accept tcv, 0
    }

}
