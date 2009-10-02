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

//  INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
//  DUP
//  ASTORE 5
// to
//  INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
//  DUP
//  CHECKCAST Integer
//  unbox
//  ISTORE 5
//
public class InferLocalsTransformer implements Transformer, Opcodes {

    private static final int OPTIMISE_TIMES = 2;

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        //
        // This algorithm works only for unoptimised codes
        // where every local is object, i.e., ASTORE/ALOAD are used.
        // Because it assumes that size of each local is 1 from the beginning.
        //
        // This algorithm will not work for partial optimised codes
        // i.e. some long and double exists (LSTORE/LLOAD, DSTORE/DLOAD).
        //
        int[] localMarker = new int[body.maxLocals];
        for(int i = 0; i < localMarker.length; i++) localMarker[i] =  1;
        int[] localTypes = new int[body.maxLocals];
        for(int i = 0; i < localTypes.length ; i++) localTypes[i]  = -1;

        InsnList units = body.instructions;
        AbstractInsnNode s = null;

        for(int i=0; i < OPTIMISE_TIMES; i++) {
            s = units.getFirst();
            while(s != null) {
                if(s.getOpcode()  != ASTORE)           { s = s.getNext(); continue; }
                AbstractInsnNode p0 = s.getPrevious();
                if(p0.getOpcode() == DUP) p0 = p0.getPrevious();
                if(p0.getOpcode() != INVOKESTATIC)     { s = s.getNext(); continue; }
                MethodInsnNode m0 = (MethodInsnNode)p0;
                if(m0.name.equals("valueOf") == false) { s = s.getNext(); continue; }

                // (I)Ljava/lang/Integer; -> I
                char type = m0.desc.charAt(1);

                VarInsnNode v = (VarInsnNode)s;
                AbstractInsnNode newS = null;
                switch(type) {
                    case 'I': newS = new VarInsnNode(ISTORE, v.var);
                              localTypes[v.var]  = Type.INT;
                              break;
                    case 'L': newS = new VarInsnNode(LSTORE, v.var);
                              localMarker[v.var] = 2;
                              localTypes[v.var]  = Type.LONG;
                              break;
                    case 'F': newS = new VarInsnNode(FSTORE, v.var);
                              localTypes[v.var]  = Type.FLOAT;
                              break;
                    case 'D': newS = new VarInsnNode(DSTORE, v.var);
                              localMarker[v.var] = 2;
                              localTypes[v.var]  = Type.DOUBLE;
                              break;
                }
                if(newS == null) {
                    // throw new RuntimeException("NYI");
                    s = s.getNext();
                    continue;
                }
                units.set(s, newS);
                units.insertBefore(newS, Utils.getUnboxNodes("L" + m0.owner + ";"));
                s = newS.getNext();
            }

            s = units.getFirst();
            while(s != null) {
                if(s instanceof VarInsnNode == false) { s = s.getNext(); continue; }

                VarInsnNode v = (VarInsnNode)s;
                int vOpcode = v.getOpcode();
                AbstractInsnNode newS = null;
                int offset = -1;
                Class<?> clazz = null;
                switch(localTypes[v.var]) {
                    case Type.INT:
                            offset = 0; clazz  = int.class;
                            break;
                    case Type.LONG:
                            offset = 1; clazz  = long.class;
                            break;
                    case Type.FLOAT:
                            offset = 2; clazz  = float.class;
                            break;
                    case Type.DOUBLE:
                            offset = 3; clazz  = double.class;
                            break;
                }
                if(offset == -1 || clazz == null) {
                    s = s .getNext(); continue;
                }
                if(vOpcode == ASTORE) {
                    newS = new VarInsnNode(ISTORE + offset, v.var);
                    units.set(s, newS);
                    units.insertBefore(newS, Utils.getUnboxNodes(clazz));
                } else if(vOpcode == ALOAD) {
                    newS = new VarInsnNode(ILOAD + offset, v.var);
                    units.set(s, newS);
                    units.insert(newS, Utils.getBoxNode(clazz));
                } else if((vOpcode >= ISTORE && vOpcode <= DSTORE) || (vOpcode >= ILOAD && vOpcode <= DLOAD)) {
                    newS = new VarInsnNode(vOpcode, v.var);
                    units.set(s, newS);
                }
                s = newS == null? s.getNext(): newS.getNext();
            }

            new UnwrapCompareTransformer().internalTransform(body, options);
            new UnwrapBinOpTransformer().internalTransform(body, options);
            new UnwrapUnaryTransformer().internalTransform(body, options);
            new NullInitToZeroTransformer().internalTransform(body, options);
            new AutoBoxEliminatorTransformer().internalTransform(body, options);

        }

        System.out.print("before = ");
        for(int i=0; i<localMarker.length; i++) {
            System.out.print(localMarker[i] + " ");
        }
        System.out.println();

        // Index Relocation Algorithm
        // relocate L and D
        // index:      0 1 2 3 4 5 6
        // old values: 1 1 1 1 2 1 1
        // new values: 0 1 2 3 4 6 7
        int old = localMarker[0];
        localMarker[0] = 0;
        for(int i = 1; i < localMarker.length; i++) {
            int temp = localMarker[i];
            localMarker[i] = localMarker[i-1] + old;
            old = temp;
        }

        System.out.print("after  = ");
        for(int i=0; i<localMarker.length; i++) {
            System.out.print(localMarker[i] + " ");
        }
        System.out.println();

        s = units.getFirst();
        while(s != null) {
            if(s instanceof VarInsnNode) {
                VarInsnNode v = (VarInsnNode)s;
                AbstractInsnNode newS = new VarInsnNode(v.getOpcode(), localMarker[v.var]);
                units.set(s, newS);
                s = newS.getNext();
                continue;
            }
            s = s.getNext();
        }
    }
}
