package org.codehaus.groovy.gjit.asm

import org.codehaus.groovy.gjit.asm.AsmTypeAdvisedClassGenerator
import org.codehaus.groovy.gjit.asm.CallSiteNameHolder
import org.codehaus.groovy.gjit.asm.ConstantHolder
import org.codehaus.groovy.gjit.asm.transformer.CallSiteNameCollector
import org.codehaus.groovy.gjit.asm.transformer.ConstantCollector
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib$fib
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class AsmTypeAdvisedClassGenTests extends GroovyTestCase implements Opcodes {

    static FIB_NAME = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib"

    void testPropagateOnFib() {
        // collect constant
        ConstantHolder.v().clear()
        CallSiteNameHolder.v().clear()

        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.fibbonacci.Fib");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == FIB_NAME

        def clinit = cn.@methods.find { it.name == "<clinit>" }
        new ConstantCollector().internalTransform(clinit, null);
        def ccsa   = cn.@methods.find { it.name == '$createCallSiteArray' }
        new CallSiteNameCollector().internalTransform(ccsa, null);

        def tp = new AsmTypeAdvisedClassGenerator(
            advisedTypes: [int] as Class[],
            advisedReturnType: int
        )
        def result = tp.perform(new Fib$fib())

        assert result.methodSignature ==
            'org/codehaus/groovy/gjit/soot/fibbonacci/Fib$fib$x.fib(I)I'
        assert result.body.length != 0

        //
        //  TODO write test by picking out some instructions
        //  to determine bytecode patterns
        //
        new File('debug/Fib$fib$x.class').delete()
        def os = new File('debug/Fib$fib$x.class').newOutputStream()
        os.write(result.body)
        os.flush()
        os.close()
    }

    void testOptimizeBinary() {
//  	  public static fib(I)I throws java/lang/Throwable
//  	   L0
//  	    INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
//  	    ASTORE 1
//  	   L1
//  	    LINENUMBER 6 L1
//  	    ILOAD 0
//  	    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  	    GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$0 : Ljava/lang/Integer;
//  	    INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.compareLessThan(Ljava/lang/Object;Ljava/lang/Object;)Z
//  	    ** here's checking if type information is enought **
//  	    compareLessThan x,y: Z ->
//  	    IFEQ L2
//  	   L3
//  	    LINENUMBER 7 L3
//  	    ILOAD 0
//  	    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  	    CHECKCAST java/lang/Integer
//  	    INVOKEVIRTUAL java/lang/Integer.intValue()I
//  	    IRETURN
//  	    GOTO L4
//  	   L2
//  	    LINENUMBER 9 L2
//  	    ALOAD 1
//  	    LDC 0
//  	    AALOAD
//  	    ALOAD 1
//  	    LDC 1
//  	    AALOAD
//  	    INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib()Ljava/lang/Class;
//  	    ALOAD 1
//  	    LDC 2
//  	    AALOAD
//  	    ILOAD 0
//  	    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  	    GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$1 : Ljava/lang/Integer;
//  	    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//  	    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.callStatic(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
//  	    ALOAD 1
//  	    LDC 3
//  	    AALOAD
//  	    INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib()Ljava/lang/Class;
//  	    ALOAD 1
//  	    LDC 4
//  	    AALOAD
//  	    ILOAD 0
//  	    INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  	    GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$0 : Ljava/lang/Integer;
//  	    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//  	    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.callStatic(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
//  	    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//  	   L5
//  	    CHECKCAST java/lang/Integer
//  	    INVOKEVIRTUAL java/lang/Integer.intValue()I
//  	    IRETURN
//  	   L4
//  	    GOTO L5
//  	    LOCALVARIABLE n Ljava/lang/Object; L0 L4 0
//  	    MAXSTACK = 7
//  	    MAXLOCALS = 2
    }
}