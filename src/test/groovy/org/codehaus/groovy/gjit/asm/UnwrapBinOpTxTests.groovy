package org.codehaus.groovy.gjit.asm;

import org.objectweb.asm.tree.*
import org.objectweb.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*;
import groovy.util.GroovyTestCase;

public class UnwrapBinOpTxTests extends GroovyTestCase implements Opcodes {

    static FIB_NAME = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib"

    private loadConstantsFromFib() {
        CallSiteNameHolder.v().clear()
        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.fibbonacci.Fib");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == FIB_NAME
        def ccsa = cn.@methods.find { it.name == '$createCallSiteArray' }
        new CallSiteNameCollector().internalTransform(ccsa, null);
        def names = CallSiteNameHolder.v().get(FIB_NAME)
        assert names.size() == 7
        assert names[0] == "plus"
        assert names[1] == "fib"
        assert names[2] == "minus"
        assert names[3] == "fib"
        assert names[4] == "minus"
        assert names[5] == "println"
        assert names[6] == "fib"
    }

//  INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
//  ASTORE 1
//
//  ALOAD 1
//  LDC 2
//  AALOAD
//  ILOAD 0
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  ICONST_1
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    void testUnwrap_Int_Int_BinOp_of_Fib() {
        loadConstantsFromFib()
        MethodNode mn = new MethodNode()
        def u = mn.instructions
        u.add(new MethodInsnNode(INVOKESTATIC,
                  "org/codehaus/groovy/gjit/soot/fibbonacci/Fib",
                  '$getCallSiteArray',
                  "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;"))
        u.add new VarInsnNode(ASTORE, 1)
        u.add new VarInsnNode(ALOAD, 1)
        u.add new LdcInsnNode(2)
        u.add new InsnNode(AALOAD)
        u.add new VarInsnNode(ILOAD, 0)
        u.add new MethodInsnNode(INVOKESTATIC,
                "java/lang/Integer","valueOf","(I)Ljava/lang/Integer;")
        u.add new InsnNode(ICONST_1)
        u.add new MethodInsnNode(INVOKESTATIC,
                "java/lang/Integer","valueOf","(I)Ljava/lang/Integer;")
        u.add new MethodInsnNode(INVOKEINTERFACE,
                "org/codehaus/groovy/runtime/callsite/CallSite",
                "call","(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
        new UnwrapBinOpTransformer().internalTransform(mn, null)
    }

}