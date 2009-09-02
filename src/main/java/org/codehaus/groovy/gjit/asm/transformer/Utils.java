package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class Utils implements Opcodes {

    public static MethodInsnNode getBoxNode(String desc) {
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

    public static MethodInsnNode getBoxNode(Class<?> type) {
        String name=null;
        String desc=null;
        if(type == int.class)     {name = "Integer";  desc = "I"; } else
        if(type == long.class)    {name = "Long";     desc = "J"; } else
        if(type == byte.class)    {name = "Byte";     desc = "B"; } else
        if(type == boolean.class) {name = "Boolean";  desc = "Z"; } else
        if(type == short.class)   {name = "Short";    desc = "S"; } else
        if(type == double.class)  {name = "Double";   desc = "D"; } else
        if(type == float.class)   {name = "Float";    desc = "F"; } else
        if(type == char.class)    {name = "Character";desc = "C"; }
        if(name == null) throw new RuntimeException("No box for " + type);
        return new MethodInsnNode(INVOKESTATIC, "java/lang/" + name, "valueOf", "(" + desc + ")Ljava/lang/" + name + ";");
    }

    public static InsnList getUnboxNodes(Class<?> type) {
        InsnList result = new InsnList();
        String name=null;
        String shortName = type.getName();
        String desc=null;
        if(type == int.class)     {name = "Integer";  desc = "I"; } else
        if(type == long.class)    {name = "Long";     desc = "J"; } else
        if(type == byte.class)    {name = "Byte";     desc = "B"; } else
        if(type == boolean.class) {name = "Boolean";  desc = "Z"; } else
        if(type == short.class)   {name = "Short";    desc = "S"; } else
        if(type == double.class)  {name = "Double";   desc = "D"; } else
        if(type == float.class)   {name = "Float";    desc = "F"; } else
        if(type == char.class)    {name = "Character";desc = "C"; }
        if(name == null) throw new RuntimeException("No unbox for " + type);
        result.add(new TypeInsnNode(CHECKCAST, "java/lang/" + name));
        result.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/" + name, shortName + "Value", "()" + desc));
        return result;
    }

}
