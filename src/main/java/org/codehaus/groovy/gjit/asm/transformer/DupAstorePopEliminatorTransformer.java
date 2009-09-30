package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class DupAstorePopEliminatorTransformer implements Transformer, Opcodes {
    
    //
    // removal of DUP, ASTORE var, POP pattern
    // to be ASTORE var
    //

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != DUP)     { s = s.getNext(); continue; }
            AbstractInsnNode s0 = s.getNext();
            if(s0.getOpcode() != ASTORE) { s = s.getNext(); continue; }
            AbstractInsnNode s1 = s0.getNext();
            if(s1.getOpcode() != POP)    { s = s.getNext(); continue; }
            
            units.remove(s);
            units.remove(s1);
            
            s = s0.getNext();
        }
    }
}
