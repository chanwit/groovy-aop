package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

// This class transforms
//  INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
//  ASTORE 5
// to
//  INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
//  CHECKCAST Integer
//  unbox
//  ISTORE 5
//
public class InferLocalsTransformer implements Transformer, Opcodes {

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        int[] localMarker = new int[body.maxLocals];
        for(int i=0; i<localMarker.length; i++) localMarker[i] = 1;

        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode()  != ASTORE)         { s = s.getNext(); continue; }
            AbstractInsnNode p0 = s.getPrevious();
            if(p0.getOpcode() != INVOKESTATIC)   { s = s.getNext(); continue; }
            MethodInsnNode m0 = (MethodInsnNode)p0;
            if(m0.name.equals("valueOf")==false) { s = s.getNext(); continue; }

            // (I)Ljava/lang/Integer; -> I
            char type = m.desc.charAt(1);

            VarInsnNode v = (VarInsnNode)s;
            AbstractInsnNode newS = null;
            switch(type) {
                case 'I': newS = new VarInsnNode(ISTORE, v.var); break;
                case 'L': newS = new VarInsnNode(LSTORE, v.var);
                          localMarker[v.var] = 2;
                          break;
                case 'F': newS = new VarInsnNode(FSTORE, v.var); break;
                case 'D': newS = new VarInsnNode(DSTORE, v.var);
                          localMarker[v.var] = 2;
                          break;
            }
            if(newS == null) {
                throw new RuntimeException("NYI");
            }
            units.set(s, newS);
            units.insertBefore(newS, Utils.getUnboxNodes("L"+m0.owner+";"));
            s = newS.getNext();
        }

        // Index Relocation Algorithm
        // relocate L and D
        // index:      0 1 2 3 4 5 6
        // old values: 1 1 1 1 2 1 1
        // new values: 0 1 2 3 4 6 7
        for(int i=1; i<localMarker.length; i++) {
            localMarker[i] = localMarker[i-1] + localMarker[i];
        }

        s = units.getFirst();
        while(s != null) {
            if(s instanceof VarInsnNode) {
                VarInsnNode v = (VarInsnNode)s;
                AbstractInsnNode newS = new VarInsnNode(v.getOpcode(), localMarker[v.var]);
                units.set(s, newS);
                s = newS.getNext();
            } else {
                s = s.getNext();
            }
        }
    }
}
