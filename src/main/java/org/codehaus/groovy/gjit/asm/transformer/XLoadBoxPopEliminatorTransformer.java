package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class XLoadBoxPopEliminatorTransformer implements Transformer, Opcodes {

//  Remove this pattern:
//    DLOAD 9
//    INVOKESTATIC java/lang/Double.valueOf (D)Ljava/lang/Double;
//    POP
//  To:

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() !=  POP)          { s = s.getNext(); continue; }
            AbstractInsnNode p0 = s.getPrevious();
            if(p0.getOpcode() != INVOKESTATIC) { s = s.getNext(); continue; }
            AbstractInsnNode p1 = p0.getPrevious();
            if(!(p1.getOpcode() >= ILOAD && p1.getOpcode() <= DLOAD))
                 { s = s.getNext(); continue; }

            MethodInsnNode m0 = (MethodInsnNode)p0;
            if(m0.name.equals("valueOf") == false) { s = s.getNext(); continue; }

            char type  = m0.desc.charAt(1);
            int opcode = p1.getOpcode();

            if(type == 'I' && opcode == ILOAD ||
               type == 'L' && opcode == LLOAD ||
               type == 'F' && opcode == FLOAD ||
               type == 'D' && opcode == DLOAD) {
                s = s.getNext();
                units.remove(s.getPrevious());
                units.remove(p0);
                units.remove(p1);
                continue;
            }

            s = s.getNext();
        }
    }
}
