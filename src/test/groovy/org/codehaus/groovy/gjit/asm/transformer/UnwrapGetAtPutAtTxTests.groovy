package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.tree.VarInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.Label

import org.codehaus.groovy.gjit.asm.InsnListHelper;
import org.codehaus.groovy.gjit.asm.transformer.*;

import groovy.util.GroovyTestCase;

public class UnwrapGetAtPutAtTxTests extends GroovyTestCase implements Opcodes {
    
    void testSomething(){}

/*
    static FIB_NAME = "org/codehaus/groovy/gjit/soot/heapsort/HeapSort"

    private loadConstantsFromHeapSort() {
        InsnListHelper.install()
        CallSiteNameHolder.v().clear()
        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.heapsort.HeapSort");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == FIB_NAME
        def ccsa = cn.@methods.find { it.name == '$createCallSiteArray' }
        new CallSiteNameCollector().internalTransform(ccsa, null);
        def names = CallSiteNameHolder.v().get(FIB_NAME)
    }

    void testUnwrap_GetAt_ForDouble() {
        InsnListHelper.install()

        def mn = new MethodNode();
        def units = mn.instructions
        units.append {
            iload 0
            invokestatic Integer,"valueOf",[int],Integer
            ldc 0
            invokestatic Integer,"valueOf",[int],Integer
            invokestatic SBA,"compareLessThan",[Object,Object],boolean
            def label_001 = new LabelNode()
            ifeq label_001
        }
        def oldSize = units.size()
        new UnwrapCompareTransformer().internalTransform(mn, null)
        assert units.size() == oldSize + 2 + 2 - 1
        assert asm {
            iload 0
            invokestatic  Integer,"valueOf", [int],Integer
            checkcast Integer
            invokevirtual Integer,"intValue",[],int
            ldc 0
            invokestatic  Integer,"valueOf", [int],Integer
            checkcast Integer
            invokevirtual Integer,"intValue",[],int
        } == units[0..7]
        assert units[8].opcode == IF_ICMPGE
    }

    void testUnwrap_CompareEqual_ForInt() {
        InsnListHelper.install()

        def mn = new MethodNode();
        def units = mn.instructions
        units.append {
            iload 0
            invokestatic Integer,"valueOf",[int],Integer
            ldc 0
            invokestatic Integer,"valueOf",[int],Integer
            invokestatic SBA,"compareEqual",[Object,Object],boolean
            def label_001 = new LabelNode()
            ifeq label_001
        }
        def oldSize = units.size()
        new UnwrapCompareTransformer().internalTransform(mn, null)
        assert units.size() == oldSize + 2 + 2 - 1
        assert asm {
            iload 0
            invokestatic  Integer,"valueOf", [int],Integer
            checkcast Integer
            invokevirtual Integer,"intValue",[],int
            ldc 0
            invokestatic  Integer,"valueOf", [int],Integer
            checkcast Integer
            invokevirtual Integer,"intValue",[],int
        } == units[0..7]
        assert units[8].opcode == IF_ICMPNE
    }
*/    
}