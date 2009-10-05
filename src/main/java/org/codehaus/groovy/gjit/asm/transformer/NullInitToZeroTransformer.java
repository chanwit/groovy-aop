package org.codehaus.groovy.gjit.asm.transformer;

//    ACONST_NULL
//    CHECKCAST java/lang/Double
//    INVOKEVIRTUAL java/lang/Double.doubleValue ()D
//    DSTORE 7

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class NullInitToZeroTransformer implements Transformer, Opcodes {

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode()  != ACONST_NULL)    { s = s.getNext(); continue; }

            AbstractInsnNode s0 = s.getNext();
            if(s0.getOpcode() != CHECKCAST)      { s = s.getNext(); continue; }
            TypeInsnNode t0 = (TypeInsnNode)s0;

            AbstractInsnNode s1 = s0.getNext();
            if(s1.getOpcode() != INVOKEVIRTUAL)  { s = s.getNext(); continue; }

            MethodInsnNode m1 = (MethodInsnNode)s1;
            if(m1.name.endsWith("Value") == false) { s = s.getNext(); continue; }
            if(m1.desc.length() != 3)              { s = s.getNext(); continue; }
            if(t0.desc.equals(m1.owner)  == false) { s = s.getNext(); continue; }

            char type = m1.desc.charAt(2);
            switch(type) {
                case 'I': units.set(s, new InsnNode(ICONST_0)); break;
                case 'J': units.set(s, new InsnNode(LCONST_0)); break;
                case 'F': units.set(s, new InsnNode(FCONST_0)); break;
                case 'D': units.set(s, new InsnNode(DCONST_0)); break;
            }
            s = s1.getNext();

            units.remove(s0);
            units.remove(s1);
        }
    }
}
