package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class AutoBoxEliminatorTransformer implements Transformer, Opcodes {

//  Optimize this pattern:
//  ILOAD 0
//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  CHECKCAST java/lang/Integer
//  INVOKEVIRTUAL java/lang/Integer.intValue()I
//  IRETURN
//  To:
//  ILOAD 0
//  IRETURN

//  INVOKESTATIC java/lang/Integer.valueOf(I)Ljava/lang/Integer;
//  INVOKESTATIC org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation.intUnbox (Ljava/lang/Object;)I
	private static final String DTT = "org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation";

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() !=  INVOKESTATIC)   { s = s.getNext(); continue; }
            AbstractInsnNode s0 = s.getNext();

            if(s0 == null) break;
            while(s0.getOpcode() == -1) { s0 = s0.getNext(); if(s0 == null) break;}
            if(s0 == null) break;

            if(s0.getOpcode() == CHECKCAST) {
	            //if(s0.getOpcode() != CHECKCAST)      { s = s.getNext(); continue; }
	            AbstractInsnNode s1 = s0.getNext();

	            if(s1 == null) break;
	            while(s1.getOpcode() == -1) { s1 = s1.getNext(); if(s1 == null) break;}
	            if(s1 == null) break;

	            if(s1.getOpcode() != INVOKEVIRTUAL)  { s = s.getNext(); continue; }

	            MethodInsnNode m  = (MethodInsnNode)s;
	            TypeInsnNode   t0 = (TypeInsnNode)s0;
	            MethodInsnNode m1 = (MethodInsnNode)s1;
	            if(m.name.equals("valueOf")  &&
	               m.owner.equals(t0.desc)  &&
	               m1.owner.equals(m.owner) &&
	               m1.name.endsWith("Value")
	            ) {
	                s = s1.getNext();
	                units.remove(m);
	                units.remove(t0);
	                units.remove(m1);
	            }
            } else if(s0.getOpcode() == INVOKESTATIC) {
            	MethodInsnNode m = (MethodInsnNode) s;
            	MethodInsnNode m0 = (MethodInsnNode)s0;
            	if( m0.owner.equals(DTT) &&
            		m0.name.endsWith("Unbox") &&
            		Type.getReturnType(m0.desc) == Type.getArgumentTypes(m.desc)[0]
            	) {
            		s = s0.getNext();
            		units.remove(m);
            		units.remove(m0);
            	}
            }

           s = s.getNext();
        }
    }

}
