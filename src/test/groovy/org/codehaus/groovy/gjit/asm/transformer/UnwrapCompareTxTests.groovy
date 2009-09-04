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
import org.codehaus.groovy.gjit.asm.transformer.UnwrapCompareTransformer;

import groovy.util.GroovyTestCase;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter as SBA

public class UnwrapCompareTxTests extends GroovyTestCase implements Opcodes {

//  ILOAD 0
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  LDC 0
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.compareLessThan(Ljava/lang/Object;Ljava/lang/Object;)Z
//  IFEQ L2
    void testUnwrap_CompareLessThan_ForInt() {
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
        assertEquals units[0], asm { iload 0 }
        assertEquals units[1], asm { invokestatic Integer,"valueOf",[int],Integer }
        assertEquals units[2], asm { checkcast Integer }
        assertEquals units[3], asm { invokevirtual Integer,"intValue",[],int }
        assertEquals units[4], asm { ldc 0 }
        assertEquals units[5], asm { invokestatic Integer,"valueOf",[int],Integer }
        assertEquals units[6], asm { checkcast Integer }
        assertEquals units[7], asm { invokevirtual Integer,"intValue",[],int }
        assert units[8].opcode == IF_ICMPGE
    }

}