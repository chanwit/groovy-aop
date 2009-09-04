package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.tree.VarInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.Label

import org.codehaus.groovy.gjit.asm.AsmInsnList;
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
        AsmInsnList.install()

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
    }

}