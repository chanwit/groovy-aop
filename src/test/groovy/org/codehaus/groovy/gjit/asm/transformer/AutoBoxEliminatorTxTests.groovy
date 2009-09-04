package org.codehaus.groovy.gjit.asm.transformer;

import org.codehaus.groovy.gjit.asm.InsnListHelper;
import org.codehaus.groovy.gjit.asm.transformer.AutoBoxEliminatorTransformer;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;
import groovy.util.GroovyTestCase;

public class AutoBoxEliminatorTxTests extends GroovyTestCase implements Opcodes {

//  ILOAD 0
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  CHECKCAST java/lang/Integer
//  INVOKEVIRTUAL java/lang/Integer.intValue()I
//  IRETURN
    void testEliminatingInteger() {
        InsnListHelper.install()
        def mn = new MethodNode()
        def u = mn.instructions
        u.append {
            iload 0
            invokestatic  Integer,"valueOf",[int],Integer
            checkcast     Integer
            invokevirtual Integer,"intValue",[],int
            ireturn
        }
        new AutoBoxEliminatorTransformer().internalTransform(mn, null);
        assert u.size() == 2
        assert u[0].opcode == ILOAD
        assert u[1].opcode == IRETURN
    }

}