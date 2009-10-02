package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class IincTransformer implements Transformer, Opcodes {

//    ILOAD 4
//    ICONST_1
//    IADD
//    ISTORE 4
//      to 
//    IINC 4, 1

//    ILOAD 3
//    ICONST_M1
//    IADD
//    DUP
//    ISTORE 3
//     to
//    IINC 3, -1
//    ILOAD 3


    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != ILOAD) { s = s.getNext(); continue; }
            AbstractInsnNode s0 = s.getNext();
            if(!(s0.getOpcode() >= ICONST_M1 && s0.getOpcode() <= ICONST_5)) { s = s.getNext(); continue; }
            AbstractInsnNode s1 = s0.getNext();
            if(s1.getOpcode() != IADD) { s = s.getNext(); continue; }
            AbstractInsnNode s2 = s1.getNext();
            boolean dup = false;
            if(s2.getOpcode() == DUP) {
                dup = true;
                s2 = s2.getNext();
            }
            if(s2.getOpcode() != ISTORE) { s = s.getNext(); continue; }
            
            VarInsnNode v =  (VarInsnNode)s;
            VarInsnNode v2 = (VarInsnNode)s2;
            if(v1.var != v2.var) { s = s.getNext(); continue; }
            
            AbstractInsnNode newS = new IincInsnNode(v1.var, ICONST_0-s0.getOpcode());
            units.insert(s, newS);
            if(!dup) {
                units.remove(s);
            }
            units.remove(s0);
            units.remove(s1);
            units.remove(s2);

            s = newS.getNext();
        }
    }
}
