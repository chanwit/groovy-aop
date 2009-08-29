package org.codehaus.groovy.gjit.soot.fibbonacci;

import org.objectweb.asm.*;

public class FibDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        cw.visit(V1_3, ACC_PUBLIC + ACC_SUPER + ACC_FINAL,
                        "org/codehaus/groovy/gjit/soot/fibbonacci/Fib$fib$x", null,
                        "sub/reflect/GroovyAOPMagic",
                        new String[] { "groovy/lang/GroovyObject" });
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "fib", "(I)I", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitMethodInsn(INVOKESTATIC,
                    "org/codehaus/groovy/gjit/soot/fibbonacci/Fib",
                    "$getCallSiteArray",
                    "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
            mv.visitVarInsn(ASTORE, 1);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(6, l1);
            mv.visitVarInsn(ILOAD, 0); //#1 change ALOAD to ILOAD
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            // mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETSTATIC,
                    "org/codehaus/groovy/gjit/soot/fibbonacci/Fib", "$const$0",
                    "Ljava/lang/Integer;");
            mv.visitMethodInsn(INVOKESTATIC,
                    "org/codehaus/groovy/runtime/ScriptBytecodeAdapter",
                    "compareLessThan",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Z");
            Label l2 = new Label();
            mv.visitJumpInsn(IFEQ, l2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(7, l3);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ARETURN);
            Label l4 = new Label();
            mv.visitJumpInsn(GOTO, l4);
            mv.visitLabel(l2);
            mv.visitLineNumber(9, l2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(new Integer(0));
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(new Integer(1));
            mv.visitInsn(AALOAD);
            mv.visitMethodInsn(INVOKESTATIC,
                    "org/codehaus/groovy/gjit/soot/fibbonacci/Fib",
                    "$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib",
                    "()Ljava/lang/Class;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(new Integer(2));
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETSTATIC,
                    "org/codehaus/groovy/gjit/soot/fibbonacci/Fib", "$const$1",
                    "Ljava/lang/Integer;");
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "org/codehaus/groovy/runtime/callsite/CallSite", "call",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "org/codehaus/groovy/runtime/callsite/CallSite",
                    "callStatic",
                    "(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(new Integer(3));
            mv.visitInsn(AALOAD);
            mv.visitMethodInsn(INVOKESTATIC,
                    "org/codehaus/groovy/gjit/soot/fibbonacci/Fib",
                    "$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib",
                    "()Ljava/lang/Class;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(new Integer(4));
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETSTATIC,
                    "org/codehaus/groovy/gjit/soot/fibbonacci/Fib", "$const$0",
                    "Ljava/lang/Integer;");
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "org/codehaus/groovy/runtime/callsite/CallSite", "call",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "org/codehaus/groovy/runtime/callsite/CallSite",
                    "callStatic",
                    "(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;");
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "org/codehaus/groovy/runtime/callsite/CallSite", "call",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l4);
            mv.visitJumpInsn(GOTO, l5);
            mv.visitLocalVariable("n", "Ljava/lang/Object;", null, l0, l4, 0);
            mv.visitMaxs(7, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
