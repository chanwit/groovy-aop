package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.*;

public class InvertBooleanIf implements Transformer, Opcodes {

    //
    // removal of DUP, ASTORE var, POP pattern
    // to be ASTORE var
    //

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
        	if(s instanceof VarInsnNode) {
        		VarInsnNode v = (VarInsnNode)s;
        		if(v.var == 9 && (v.getOpcode() == DLOAD || v.getOpcode() == DSTORE)) {
        			s = s.getNext();
        			units.remove(v);
        		}
        	} else s = s.getNext();
//        	AbstractInsnNode s0;
//			if(s.getOpcode() == INVOKESTATIC) {
//        		MethodInsnNode m = (MethodInsnNode)s;
//        		if(m.name.equals("booleanUnbox")) {
//        			s0 = s.getNext();
//        			if(s0.getOpcode() == IFEQ) {
//        				JumpInsnNode j = (JumpInsnNode)s0;
//        				units.set(s0, new JumpInsnNode(IFNE, j.label));
//        			} else if(s0.getOpcode() == IFNE) {
//        				JumpInsnNode j = (JumpInsnNode)s0;
//        				units.set(s0, new JumpInsnNode(IFEQ, j.label));
//        			}
//        		}
//        	}
            //s = s.getNext();
        }
    }
}
