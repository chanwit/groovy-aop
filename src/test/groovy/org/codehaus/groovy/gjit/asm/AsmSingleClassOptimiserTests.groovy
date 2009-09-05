package org.codehaus.groovy.gjit.asm

import java.io.PrintWriter;

import org.codehaus.groovy.gjit.asm.transformer.*
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.CheckMethodAdapter;
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib$fib
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.ClassReader

import groovy.util.GroovyTestCase

public class AsmSingleClassOptimiserTests extends GroovyTestCase {

    void testOptimisationFib() {
        InsnListHelper.install()

        def sender = Fib.class
        def sco = new AsmSingleClassOptimizer();
        def aatf = new AsmAspectAwareTransformer(
            advisedTypes:[int] as Class[],
            advisedReturnType: int,
            callSite: new Fib$fib(),
            withInMethodName: "main"
        )
        sco.transformers = [DeConstantTransformer.class,
                            aatf,
                            AutoBoxEliminatorTransformer.class,
                            ClassPopEliminatorTransformer.class]
        byte[] bytes = sco.optimize(sender)
        def cr = new ClassReader(bytes);
        def cn = new ClassNode()
        cr.accept cn, 0
        def main = cn.@methods.find { it.name == "main"}
        assert main.name == "main"

        def units = main.instructions
        // skip 0, it's a label
        assertEquals asm {
            invokestatic Fib,'$getCallSiteArray',[],CallSite[]
            astore 1
        }, units[1..2]
        // skip 3,4 it's label and line no
        assertEquals asm {
            aload 1
            ldc 5
            aaload
            invokestatic Fib, '$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib',[],Class
            // invokestatic Fib, '$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib',[],Class
            // pop
            bipush 40
            invokestatic 'org/codehaus/groovy/gjit/soot/fibbonacci/Fib$fib$x','fib',[int],int
            invokestatic Integer,"valueOf",[int],Integer
        }, units[5..11]
        //
        // re-check again with a verifier
        //
        CheckClassAdapter.verify(cr, true, new PrintWriter(System.out))
    }

//    public static transient varargs main([Ljava/lang/String;)V
//    L0
//     INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
//     ASTORE 1
//    L1
//     LINENUMBER 13 L1
//     ALOAD 1
//     LDC 5
//     AALOAD
//     INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib()Ljava/lang/Class;
//     ALOAD 1
//     LDC 6
//     AALOAD
//     INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib()Ljava/lang/Class;
//     GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$2 : Ljava/lang/Integer;
//     INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//     INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.callStatic(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
//     POP
//     RETURN
//    L2
//     RETURN
//     GOTO L2
//     LOCALVARIABLE args [Ljava/lang/String; L0 L2 0
//     MAXSTACK = 5
//     MAXLOCALS = 2

}