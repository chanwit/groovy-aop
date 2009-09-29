package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class WhileTrueEliminatorTransformer implements Transformer, Opcodes {
    
    private static final String DTT = "org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation";
    
    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != GETSTATIC)                 { s = s.getNext(); continue; }
            FieldInsnNode f = (FieldInsnNode)s;
            if(f.name.equals("TRUE") == false)             { s = s.getNext(); continue; }
            if(f.owner.equals("java/lang/Boolean")==false) { s = s.getNext(); continue; }
            
            AbstractInsnNode s0 = s.getNext();
            if(s0.getOpcode() != INVOKESTATIC)             { s = s.getNext(); continue; }
            MethodInsnNode m = (MethodInsnNode)s0;
            if(m.name.equals("booleanUnbox") == false)     { s = s.getNext(); continue; }
            if(m.owner.equals(DTT) == false)               { s = s.getNext(); continue; }
            
            AbstractInsnNode s1 = s0.getNext();
            if(s1.getOpcode() != IFEQ) {s = s.getNext(); continue; }
            
            AbstractInsnNode oldS = s;
            s = s.getNext();
            
            units.remove(s1);
            units.remove(s0);
            units.remove(oldS);
        }
    }

}
