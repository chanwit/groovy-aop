package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.asm.ConstantHolder;
import org.codehaus.groovy.gjit.asm.ConstantHolder.ConstantPack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class DeConstantTransformer implements Transformer, Opcodes {

//	  Transform:
//      GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$0 : Ljava/lang/Integer;
//    Into:
//      ICONST, SIPUSH, BIPUSH, LDC
//      INVOKESTATIC valueOf()
//    Depending on Type

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() != GETSTATIC) { s = s.getNext(); continue; }
            FieldInsnNode f = (FieldInsnNode)s;
            if(f.name.startsWith("$const$")) {
                ConstantPack pack = ConstantHolder.v().get(f.owner);
                Object cst = pack.get(f.name);
                LdcInsnNode ldc    = new LdcInsnNode(cst);
                MethodInsnNode box = getBoxNode(f.desc);
                units.insert(s,   ldc);
                units.insert(ldc, box);
                units.remove(s);
                s = box.getNext();
                continue;
            }
            s = s.getNext();
        }
    }

    private MethodInsnNode getBoxNode(String desc) {
        String primitive = null;
        Type t = Type.getType(desc);
        t.getInternalName();
        if(desc.equals("Ljava/lang/Integer;"))   { primitive = "I"; } else
        if(desc.equals("Ljava/lang/Long;"))      { primitive = "L"; } else
        if(desc.equals("Ljava/lang/Byte;"))      { primitive = "B"; } else
        if(desc.equals("Ljava/lang/Boolean;"))   { primitive = "Z"; } else
        if(desc.equals("Ljava/lang/Short;"))     { primitive = "S"; } else
        if(desc.equals("Ljava/lang/Double;"))    { primitive = "D"; } else
        if(desc.equals("Ljava/lang/Float;"))     { primitive = "F"; } else
        if(desc.equals("Ljava/lang/Character;")) { primitive = "C"; }
        if(primitive == null) throw new RuntimeException("No box for " + t);
        return new MethodInsnNode(INVOKESTATIC, t.getInternalName(), "valueOf", "(" + primitive + ")" + desc);
    }

}
