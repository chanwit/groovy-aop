package org.codehaus.groovy.gjit.asm

import org.codehaus.groovy.gjit.asm.transformer.AsmAspectAwareTransformer;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib
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
        sco.transformers = [aatf]
        byte[] bytes = sco.optimize(sender)
        def cr = new ClassReader(bytes);
        def cn = new ClassNode()
        cr.accept cn, 0
        def main = cn.@methods.find { it.name == "main"}
        assert main.name == "main"

        def units = main.instructions
        assertEquals asm { invokestatic Fib,'$getCallSiteArray',[],CallSite[] }, units[1]
        assertEquals asm { astore 1 }, units[2]
        assertEquals asm { aload 1  }, units[5]
        assertEquals asm { ldc 5    }, units[6]
        assertEquals asm { aaload   }, units[7]
        assertEquals asm { invokestatic Fib, '$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib',
                                        [],Class }, units[8]
        assertEquals asm { aload 1  }, units[9]
        assertEquals asm { ldc 6    }, units[10]
        assertEquals asm { aaload   }, units[11]
        assertEquals asm { invokestatic Fib, '$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib',
                                        [],Class }, units[12]
        assertEquals asm { getstatic Fib,'$const$2',Integer}, units[13]
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