package org.codehaus.groovy.gjit.asm.transformer

import org.codehaus.groovy.gjit.asm.InsnListHelper
import org.codehaus.groovy.gjit.asm.transformer.AutoBoxEliminatorTransformer
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.VarInsnNode
import org.objectweb.asm.tree.TypeInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.Opcodes

import groovy.util.GroovyTestCase

public class AutoBoxEliminatorTxTests extends GroovyTestCase implements Opcodes {

//  ILOAD 0
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  CHECKCAST java/lang/Integer
//  INVOKEVIRTUAL java/lang/Integer.intValue()I
//  IRETURN
    void testEliminatingInteger() {
        InsnListHelper.install()
        def mn = new MethodNode()
        def units = mn.instructions
        units.append {
            iload 0
            invokestatic  Integer,"valueOf",[int],Integer
            checkcast     Integer
            invokevirtual Integer,"intValue",[],int
            ireturn
        }
        new AutoBoxEliminatorTransformer().internalTransform(mn, null);
        assert units.size() == 2
        assertEquals asm {
            iload 0
            ireturn
        }, units
    }

}