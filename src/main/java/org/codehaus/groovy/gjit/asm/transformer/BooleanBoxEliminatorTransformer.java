package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BooleanBoxEliminatorTransformer implements Transformer, Opcodes {

//GETSTATIC java/lang/Boolean.FALSE : Ljava/lang/Boolean;
//CHECKCAST java/lang/Integer
//INVOKEVIRTUAL java/lang/Integer.intValue ()I
//ISTORE 18
//ILOAD 18
//INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
//CHECKCAST java/lang/Boolean
//INVOKEVIRTUAL java/lang/Boolean.booleanValue ()Z

	@Override
	public void internalTransform(MethodNode body, Map<String, Object> options) {
		InsnList units = body.instructions;
		AbstractInsnNode s = units.getFirst();
		while(s!=null) {
			if(s.getOpcode() != GETSTATIC) { s = s.getNext(); continue; }
			FieldInsnNode f = (FieldInsnNode)s;
			if(f.owner.equals("java/lang/Boolean") == false) { s = s.getNext(); continue; }

			AbstractInsnNode s0 = s.getNext();
			if(s0.getOpcode() != CHECKCAST) { s = s.getNext(); continue; }

			AbstractInsnNode s1 = s0.getNext();
			if(s1.getOpcode() != INVOKEVIRTUAL) { s = s.getNext(); continue; }

			AbstractInsnNode s2 = s1.getNext();
			if(s2.getOpcode() >= ISTORE && s2.getOpcode() <= DSTORE) {
				AbstractInsnNode s3 = s2.getNext();
				if(!(s3.getOpcode() >= ILOAD && s3.getOpcode() <= DLOAD)) { s = s.getNext(); continue; }
				VarInsnNode v2 = (VarInsnNode)s2;
				VarInsnNode v3 = (VarInsnNode)s3;
				if(v2.var != v3.var) { s = s.getNext(); continue; }

				AbstractInsnNode s4 = s3.getNext();
				if(s4.getOpcode() != INVOKESTATIC) { s = s.getNext(); continue; }

				AbstractInsnNode s5 = s4.getNext();
				if(s5.getOpcode() != CHECKCAST) { s = s.getNext(); continue; }
				TypeInsnNode t5 = (TypeInsnNode)s5;
				if(t5.desc.equals("java/lang/Boolean") == false) { s = s.getNext(); continue; }

				AbstractInsnNode s6 = s5.getNext();
				if(s6.getOpcode() != INVOKEVIRTUAL) {s = s.getNext(); continue; }

				// ICONST_0
				// ILOAD 18
				// ISTORE 18
				InsnNode newS = null;
				if(f.name.equals("FALSE")) {
					newS = new InsnNode(ICONST_0);
				} else if (f.name.equals("TRUE")) {
					newS = new InsnNode(ICONST_1);
				} else { s = s.getNext(); continue; }
				units.set(s, newS);
				units.remove(s0);
				units.remove(s1);
				units.remove(s4);
				units.remove(s5);
				units.remove(s6);
				s = newS.getNext();
				continue;
			} else if(s2.getOpcode() == INVOKESTATIC) {
				AbstractInsnNode s3 = s2.getNext();
				if(s3.getOpcode() != CHECKCAST) { s = s.getNext(); continue; }

				AbstractInsnNode s4 = s3.getNext();
				if(s4.getOpcode() != INVOKEVIRTUAL) {s = s.getNext(); continue; }

				InsnNode newS = null;
				if(f.name.equals("FALSE")) {
					newS = new InsnNode(ICONST_0);
				} else if (f.name.equals("TRUE")) {
					newS = new InsnNode(ICONST_1);
				} else { s = s.getNext(); continue; }
				units.set(s, newS);
				units.remove(s0);
				units.remove(s1);
				units.remove(s2);
				units.remove(s3);
				units.remove(s4);
				s = newS.getNext();
				continue;
			} else {
				s = s.getNext(); continue;
			}
			s = s.getNext();
		}
	}

}
