package org.codehaus.groovy.gjit.asm

import org.codehaus.groovy.gjit.asm.TypeAdvisedClassGenerator
import java.lang.ref.SoftReference;

import org.codehaus.groovy.gjit.asm.CallSiteNameHolder
import org.codehaus.groovy.gjit.asm.ConstantHolder
import org.codehaus.groovy.gjit.asm.transformer.CallSiteNameCollector
import org.codehaus.groovy.gjit.asm.transformer.ConstantCollector
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib$fib
import org.codehaus.groovy.runtime.callsite.CallSiteArray
import org.codehaus.groovy.runtime.callsite.CallSite
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.Type

class AsmTypeAdvisedClassGenTests extends GroovyTestCase implements Opcodes {

    static FIB_NAME     = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib"
    static FIB_NEW_NAME = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib_fib_x"

    void testTypeAdvisedClassGeneratorOnFib() {
        InsnListHelper.install()

        // collect constant
        ConstantHolder.v().clear()
        CallSiteNameHolder.v().clear()

        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.fibbonacci.Fib")
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == FIB_NAME

        def clinit = cn.@methods.find { it.name == "<clinit>" }
        new ConstantCollector().internalTransform(clinit, null);
        def ccsa   = cn.@methods.find { it.name == '$createCallSiteArray' }
        new CallSiteNameCollector().internalTransform(ccsa, null);

        def tp = new TypeAdvisedClassGenerator(
            advisedTypes: [int] as Class[],
            advisedReturnType: int
        )
        def result = tp.perform(new Fib$fib())

        assert result.owner == FIB_NEW_NAME
        assert result.name  == "fib"
        assert result.desc  == "(I)I"
        assert result.body.length != 0

        def actualCr = new ClassReader(result.body)
        def actualCn = new ClassNode()
        actualCr.accept actualCn, 0
        assert actualCn.name == FIB_NEW_NAME

        def actualCSA  = actualCn.@fields.find  { it.name == '$callSiteArray' }
        assert actualCSA  != null

        assertCCSA( actualCn.@methods.find { it.name == '$createCallSiteArray' } )
        assertGCSA( actualCn.@methods.find { it.name == '$getCallSiteArray'    } )
        assertFib ( actualCn.@methods.find { it.name == 'fib' } )

    }

    private assertCCSA(body) {
        assert body != null
        def u = body.instructions
        assert asm {
           _new CallSiteArray
            dup
            ldc Type.getType("L${FIB_NEW_NAME};")
            ldc 19
            anewarray String
        } == u[0..4]
    }

    private assertGCSA(body) {
        assert body != null
        def u = body.instructions
        assert asm {
            getstatic FIB_NEW_NAME, '$callSiteArray', SoftReference
        } == u[0]
        assert IFNULL == u[1].opcode
        assert asm {
            getstatic FIB_NEW_NAME, '$callSiteArray', SoftReference
            invokevirtual SoftReference, 'get', [], Object
            checkcast CallSiteArray
            dup
            astore 0
        } == u[2..6]
        assert IFNONNULL == u[7].opcode
        assert asm {
            invokestatic  FIB_NEW_NAME, '$createCallSiteArray', [], CallSiteArray
            astore 0
           _new SoftReference
            dup
            aload 0
            invokespecial SoftReference, '<init>', [Object], void.class
            putstatic     FIB_NEW_NAME,  '$callSiteArray', SoftReference
        } == u[9..15]
        assert asm {
            aload 0
            getfield      CallSiteArray, 'array', CallSite[]
            areturn
        } == u[17..19]
    }

    private assertFib(body) {
        assert body != null
        def u = body.instructions
        // skip 0
        assert asm {
            invokestatic FIB_NEW_NAME, '$getCallSiteArray', [], CallSite[]
            astore 1
        } == u[1..2]
        // skip 3, 4
        assert asm {
            iload 0
            iconst_2
        } == u[5..6]
        assert IF_ICMPGE == u[7].opcode
        // skip 8, 9
        assert asm {
            iload 0
            ireturn
        } == u[10..11]
//        public static fib(Ljava/lang/Object;)Ljava/lang/Object;
//        L0
//         INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
//         ASTORE 1
//        L1
//         LINENUMBER 6 L1
//         ALOAD 0
//         GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$0 : Ljava/lang/Integer;
//         INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.compareLessThan(Ljava/lang/Object;Ljava/lang/Object;)Z
//         IFEQ L2
//        L3
//         LINENUMBER 7 L3
//         ALOAD 0
//         ARETURN
//         GOTO L4
//        L2
//         LINENUMBER 9 L2
//         ALOAD 1
//         LDC 0
//         AALOAD
//         ALOAD 1
//         LDC 1
//         AALOAD
//         INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib()Ljava/lang/Class;
//         ALOAD 1
//         LDC 2
//         AALOAD
//         ALOAD 0
//         GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$1 : Ljava/lang/Integer;
//         INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//         INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.callStatic(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
//         ALOAD 1
//         LDC 3
//         AALOAD
//         INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib()Ljava/lang/Class;
//         ALOAD 1
//         LDC 4
//         AALOAD
//         ALOAD 0
//         GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$0 : Ljava/lang/Integer;
//         INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//         INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.callStatic(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
//         INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//        L5
//         ARETURN
//        L4
//         GOTO L5
//         LOCALVARIABLE n Ljava/lang/Object; L0 L4 0
//         MAXSTACK = 7
//         MAXLOCALS = 2
    }
}