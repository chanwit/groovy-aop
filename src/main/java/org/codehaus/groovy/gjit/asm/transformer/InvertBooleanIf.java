package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.*;

public class InvertBooleanIf implements Transformer, Opcodes {

    //
    // Groovy uses last local var to store
    // value for the optional return.
    //

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        int last = body.maxLocals - 1;
        VarInsnNode found=null;
        while(s != null) {
        	if(s instanceof VarInsnNode) {
        		VarInsnNode v = (VarInsnNode)s;
        		if(v.var == last || v.var == last-1) {
        			found = v;
        			break;
        		}
        	}
        	s = s.getNext();
        }

        s = units.getFirst();
        AbstractInsnNode where = null;
        while(s != null) {
        	if(s.getOpcode() == ASTORE) {
        		where = s;
        		break;
        	} else if (s.getOpcode() >= ACONST_NULL && s.getOpcode() <= DCONST_1) {
        		where = s.getPrevious();
        		break;
        	}
        	s = s.getNext();
        }

        if(found.getOpcode() == ILOAD || found.getOpcode() == ISTORE) {
        	units.insert(where, new VarInsnNode(ISTORE, found.var));
        	units.insert(where, new InsnNode(ICONST_0));
        } else if(found.getOpcode() == ALOAD || found.getOpcode() == ASTORE) {
        	units.insert(where, new VarInsnNode(ASTORE, found.var));
        	units.insert(where, new InsnNode(ACONST_NULL));
        } else if(found.getOpcode() == DLOAD || found.getOpcode() == DSTORE) {
        	units.insert(where, new VarInsnNode(DSTORE, found.var));
        	units.insert(where, new InsnNode(DCONST_0));
        } else if(found.getOpcode() == LLOAD || found.getOpcode() == LSTORE) {
        	units.insert(where, new VarInsnNode(LSTORE, found.var));
        	units.insert(where, new InsnNode(LCONST_0));
        } else if(found.getOpcode() == FLOAD || found.getOpcode() == FSTORE) {
        	units.insert(where, new VarInsnNode(FSTORE, found.var));
        	units.insert(where, new InsnNode(FCONST_0));
        }
    }
}
