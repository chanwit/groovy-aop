package org.codehaus.groovy.gjit.asm;

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
        MethodNode mn = new MethodNode()
        mn.instructions.add(new VarInsnNode(ILOAD, 0))
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"))
        mn.instructions.add(new TypeInsnNode(CHECKCAST, "java/lang/Integer"))
        mn.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Integer", "intValue","()I"))
        mn.instructions.add(new InsnNode(IRETURN))
        new AutoBoxEliminatorTransformer().internalTransform(mn, null);
        assert mn.instructions.size == 2
        assert mn.instructions.get(0).opcode == ILOAD
        assert mn.instructions.get(1).opcode == IRETURN
    }

}