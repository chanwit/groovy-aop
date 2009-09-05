package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassPopEliminatorTransformer implements Transformer, Opcodes {

//    Remove this pattern:
//      INVOKESTATIC class.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib ()Ljava/lang/Class;
//      POP

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != INVOKESTATIC)               { s = s.getNext(); continue; }
            MethodInsnNode m = (MethodInsnNode)s;
            if(m.name.startsWith("$get$$class$")==false)    { s = s.getNext(); continue; }
            if(m.desc.equals("()Ljava/lang/Class;")==false) { s = s.getNext(); continue; }
            AbstractInsnNode s0 = s.getNext();
            if(s0.getOpcode() == POP) {
                s = s0.getNext();
                units.remove(m);
                units.remove(s0);
            }
            s = s.getNext();
        }
    }

}
