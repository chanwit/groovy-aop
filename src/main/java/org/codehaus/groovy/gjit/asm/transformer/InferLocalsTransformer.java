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
        int[] localTypes = new int[body.maxLocals];
        for(int i=0; i<localTypes.length; i++) localTypes[i] = -1;

        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode()  != ASTORE)         { s = s.getNext(); continue; }
            AbstractInsnNode p0 = s.getPrevious();
            if(p0.getOpcode() != INVOKESTATIC)   { s = s.getNext(); continue; }
            MethodInsnNode m0 = (MethodInsnNode)p0;
            if(m0.name.equals("valueOf")==false) { s = s.getNext(); continue; }

            // (I)Ljava/lang/Integer; -> I
            char type = m0.desc.charAt(1);

            VarInsnNode v = (VarInsnNode)s;
            AbstractInsnNode newS = null;
            switch(type) {
                case 'I': newS = new VarInsnNode(ISTORE, v.var);
                          localTypes[v.var] = Type.INT;
                          break;
                case 'L': newS = new VarInsnNode(LSTORE, v.var);
                          localMarker[v.var] = 2;
                          localTypes[v.var] = Type.LONG;
                          break;
                case 'F': newS = new VarInsnNode(FSTORE, v.var); 
                          localTypes[v.var] = Type.FLOAT;
                          break;
                case 'D': newS = new VarInsnNode(DSTORE, v.var);
                          localMarker[v.var] = 2;
                          localTypes[v.var] = Type.DOUBLE;
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
        int old = localMarker[0];
        localMarker[0] = 0;
        for(int i=1; i<localMarker.length; i++) {
            int temp = localMarker[i];
            localMarker[i] = localMarker[i-1] + old;
            old = temp;
        }

        s = units.getFirst();
        while(s != null) {
            if(s instanceof VarInsnNode) {
                VarInsnNode v = (VarInsnNode)s;
                int vOpcode = v.getOpcode();
                AbstractInsnNode newS = null;
                switch(localTypes[v.var]) {
                    case Type.INT: if(vOpcode == ASTORE) {
                        newS = new VarInsnNode(ISTORE, localMarker[v.var]);
                        units.set(s, newS);
                        units.insertBefore(newS, Utils.getUnboxNodes(int.class));
                    } else if (vOpcode == ALOAD) {
                        newS = new VarInsnNode(ILOAD, localMarker[v.var]);
                        units.set(s, newS);
                        units.insert(newS, Utils.getBoxNode(int.class));
                    } break;
                    case Type.LONG: if(vOpcode == ASTORE) {
                        newS = new VarInsnNode(LSTORE, localMarker[v.var]);
                        units.set(s, newS);
                        units.insertBefore(newS, Utils.getUnboxNodes(long.class));
                    } else if (vOpcode == ALOAD) {
                        newS = new VarInsnNode(LLOAD, localMarker[v.var]);
                        units.set(s, newS);
                        units.insert(newS, Utils.getBoxNode(long.class));
                    } break;
                    case Type.FLOAT: if(vOpcode == ASTORE) {
                        newS = new VarInsnNode(FSTORE, localMarker[v.var]);
                        units.set(s, newS);
                        units.insertBefore(newS, Utils.getUnboxNodes(float.class));
                    } else if (vOpcode == ALOAD) {
                        newS = new VarInsnNode(FLOAD, localMarker[v.var]);
                        units.set(s, newS);
                        units.insert(newS, Utils.getBoxNode(float.class));
                    } break;
                    case Type.DOUBLE: if(vOpcode == ASTORE) {
                        newS = new VarInsnNode(DSTORE, localMarker[v.var]);
                        units.set(s, newS);
                        units.insertBefore(newS, Utils.getUnboxNodes(double.class));
                    } else if (vOpcode == ALOAD) {
                        newS = new VarInsnNode(DLOAD, localMarker[v.var]);
                        units.set(s, newS);
                        units.insert(newS, Utils.getBoxNode(double.class));
                    } break;
                }
                if(newS == null) 
                    s = s.getNext();
                else
                    s = newS.getNext();
            } else {
                s = s.getNext();
            }
        }
    }
}
