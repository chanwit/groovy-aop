package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TypePropagateTransformer implements Transformer, Opcodes {

    public TypePropagateTransformer() {
    }

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {

        final Class<?>[] advisedTypes = (Class<?>[]) options.get("advisedTypes");
        final Class<?>   advisedReturnType = (Class<?>) options.get("advisedReturnType");

        final boolean staticMethod = (body.access & ACC_STATIC) != 0;

        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        while(s != null) {
            if(s.getOpcode() == ALOAD || s.getOpcode() == ASTORE) {
                VarInsnNode v = (VarInsnNode)s;
                Class<?> type;
                if(staticMethod) {
                    if(v.var >= advisedTypes.length)   { s = s.getNext(); continue; }
                    type = advisedTypes[v.var];
                }
                else {
                    if(v.var-1 >= advisedTypes.length) { s = s.getNext(); continue; }
                    type = advisedTypes[v.var - 1];
                }
                if(type != null && type.isPrimitive()) {
                    int offset = 4;
                    if(type == int.class)    offset = 4; else
                    if(type == long.class)   offset = 3; else
                    if(type == float.class)  offset = 2; else
                    if(type == double.class) offset = 1;
                    VarInsnNode newS = new VarInsnNode(v.getOpcode()-offset, v.var);
                    units.set(s, newS);
                    units.insert(newS, Utils.getBoxNode(type));
                    s = newS.getNext().getNext();
                    continue;
                } else if(type != null) {
                    throw new RuntimeException("NYI");
                }
            } else if (s.getOpcode() == ARETURN) {
                if(advisedReturnType != null && advisedReturnType.isPrimitive()) {
                    int offset = 4;
                    if(advisedReturnType == int.class   ) offset = 4; else
                    if(advisedReturnType == long.class  ) offset = 3; else
                    if(advisedReturnType == float.class ) offset = 2; else
                    if(advisedReturnType == double.class) offset = 1;
                    InsnNode newS = new InsnNode(ARETURN - offset);
                    units.set(s, newS);
                    units.insertBefore(newS, Utils.getUnboxNodes(advisedReturnType));
                    s = newS.getNext();
                    continue;
                } else if (advisedReturnType != null) {
                    throw new RuntimeException("NYI");
                }
            }
            s = s.getNext();
        }
    }
}
