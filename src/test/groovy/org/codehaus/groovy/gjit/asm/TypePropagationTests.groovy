package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.AbstractVisitor

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.soot.fibbonacci.*;

class TypePropagationTests extends GroovyTestCase implements Opcodes {

    void _testSomething() {
        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.Subject");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == "org/codehaus/groovy/gjit/soot/Subject"

        def add = cn.@methods.find { it.name == "add" }
        def ins = add.instructions

        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;
        cw.visit(V1_3, ACC_FINAL + ACC_PUBLIC + ACC_SUPER, "org/codehaus/groovy/gjit/soot/Subject$add$x", null, "sun/reflect/GroovyAOPMagic", null);

        mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "add", "(Lorg/codehaus/groovy/gjit/soot/Subject;II)I", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitMethodInsn(INVOKESTATIC, "org/codehaus/groovy/gjit/soot/Subject", "$getCallSiteArray", "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
        mv.visitVarInsn(ASTORE, 3);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(14, l1);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitLdcInsn(new Integer(3));
        mv.visitInsn(AALOAD);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/groovy/runtime/callsite/CallSite", "call", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
        mv.visitInsn(IRETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitJumpInsn(GOTO, l2);
        //mv.visitLocalVariable("self", "Lorg/codehaus/groovy/gjit/soot/Subject;", null, l0, l3, 0);
        //mv.visitLocalVariable("i", "Ljava/lang/Object;", null, l0, l3, 1);
        //mv.visitLocalVariable("j", "Ljava/lang/Object;", null, l0, l3, 2);
        mv.visitMaxs 0,0
        mv.visitEnd();

        cw.toByteArray()

        /*
         * how to make change ?
         * def add(i, j) { i + j} with typing(i, j) { i,j >> int; return int }
         * will create int add(int i, int j) { i + j }
         * */

        /*
         * // access flags 1
        public add(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        L0
         INVOKESTATIC org/codehaus/groovy/gjit/soot/Subject.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
         ASTORE 3
        L1
         LINENUMBER 14 L1
         ALOAD 3
         LDC 3
         AALOAD
         ALOAD 1
         ALOAD 2
         INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        L2
         ARETURN
        L3
         GOTO L2
         LOCALVARIABLE this Lorg/codehaus/groovy/gjit/soot/Subject; L0 L3 0
         LOCALVARIABLE i Ljava/lang/Object; L0 L3 1
         LOCALVARIABLE j Ljava/lang/Object; L0 L3 2
         MAXSTACK = 3
         MAXLOCALS = 4

        // *** 1.Type Propagation FIRST PASS
        final public static add(LSubject;II)I // instance level
        L0
         INVOKESTATIC org/codehaus/groovy/gjit/soot/Subject.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
         ASTORE 3
        L1
         LINENUMBER 14 L1
         ALOAD 3
         LDC 3
         AALOAD
         ILOAD 1: i
         box(tos)
         ILOAD 2: j
         box(tos)
         INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        L2
         unbox(tos)
         IRETURN
        L3
         GOTO L2
         LOCALVARIABLE this Lorg/codehaus/groovy/gjit/soot/Subject; L0 L3 0
         LOCALVARIABLE i Ljava/lang/Object; L0 L3 1
         LOCALVARIABLE j Ljava/lang/Object; L0 L3 2
         MAXSTACK = 3
         MAXLOCALS = 4
         * */

        // if ALOAD 1 is found, change to ILOAD 1
        // if ALOAD 2 is found, change to ILOAD 2
        //
    }


    void testPropagate() {
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
        def os = new File('debug/Fib$fib$x.class').newOutputStream()
        os.write(result.body)
        os.flush()
        os.close()
    }
}