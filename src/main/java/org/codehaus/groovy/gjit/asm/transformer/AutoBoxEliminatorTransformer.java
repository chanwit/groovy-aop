package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class AutoBoxEliminatorTransformer implements Transformer, Opcodes {

//  Optimize this pattern:
//  ILOAD 0
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  CHECKCAST java/lang/Integer
//  INVOKEVIRTUAL java/lang/Integer.intValue()I
//  IRETURN
//  To:
//  ILOAD 0
//  IRETURN

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() !=  INVOKESTATIC)   { s = s.getNext(); continue; }
            AbstractInsnNode s0 = s.getNext();
            if(s0 == null) break;
            if(s0.getOpcode() != CHECKCAST)      { s = s.getNext(); continue; }
            AbstractInsnNode s1 = s0.getNext();
            if(s1 == null) break;
            if(s1.getOpcode() != INVOKEVIRTUAL)  { s = s.getNext(); continue; }

            MethodInsnNode mi  = (MethodInsnNode)s;
            TypeInsnNode   ti0 = (TypeInsnNode)s0;
            MethodInsnNode mi1 = (MethodInsnNode)s1;
            if(mi.name.equals("valueOf")  &&
               mi.owner.equals(ti0.desc)  &&
               mi1.owner.equals(mi.owner) &&
               mi1.name.endsWith("Value")
            ) {
                s = s1.getNext();
                units.remove(mi);
                units.remove(ti0);
                units.remove(mi1);
            }
        }
    }

}
