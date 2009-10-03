package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.asm.ConstantHolder;
import org.codehaus.groovy.gjit.asm.ConstantHolder.ConstantPack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
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
                String fieldDesc = f.desc;
                //
                // We convert BigDecimal to Double
                //
                if(f.desc.equals("Ljava/math/BigDecimal;")) {
                	cst = Double.valueOf((String)cst);
                	fieldDesc = "Ljava/lang/Double;";
                }
                AbstractInsnNode newS = new LdcInsnNode(cst);
                if (cst instanceof Integer) {
                    int c = (Integer)cst;
                    if (c >= -1 && c <= 5) {
                        newS = new InsnNode(ICONST_0 + c);
                    } else if (c >= -128 && c <= 127) {
                        newS = new IntInsnNode(BIPUSH, c);
                    } else if (c >= -32768 && c <= 32767) {
                        newS = new IntInsnNode(SIPUSH, c);
                    }
                }
                MethodInsnNode box = Utils.getBoxNode(fieldDesc);
                units.insert(s,   newS);
                units.insert(newS, box);
                units.remove(s);

                s = box.getNext();
                continue;
            }
            s = s.getNext();
        }
    }

}
