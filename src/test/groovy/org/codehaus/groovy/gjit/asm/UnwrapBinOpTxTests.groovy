package org.codehaus.groovy.gjit.asm;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.tree.*
import org.objectweb.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*;
import groovy.util.GroovyTestCase;
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib

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
        AsmInsnList.install()
        loadConstantsFromFib()
        MethodNode mn = new MethodNode()
        def u = mn.instructions
        u.append {
            invokestatic    Fib, '$getCallSiteArray',[],CallSite[]
            astore 1
            aload  1
            ldc    2
            aaload
            iload  0
            invokestatic    Integer,"valueOf",[int],Integer
            iconst_1
            invokestatic    Integer,"valueOf",[int],Integer
            invokeinterface CallSite,"call",[Object,Object],Object
        }
        assert u.size() == 10
        new UnwrapBinOpTransformer().internalTransform(mn, null)
    }

}