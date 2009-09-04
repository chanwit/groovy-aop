package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
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

    public static InsnList getUnboxNodes(String desc) {
        InsnList result = new InsnList();
        String primitive = null;
        String shortName = null;
        Type t = Type.getType(desc);
        if(desc.equals("Ljava/lang/Integer;"))   { primitive = "I"; shortName = "int";     } else
        if(desc.equals("Ljava/lang/Long;"))      { primitive = "L"; shortName = "long";    } else
        if(desc.equals("Ljava/lang/Byte;"))      { primitive = "B"; shortName = "byte";    } else
        if(desc.equals("Ljava/lang/Boolean;"))   { primitive = "Z"; shortName = "boolean"; } else
        if(desc.equals("Ljava/lang/Short;"))     { primitive = "S"; shortName = "short";   } else
        if(desc.equals("Ljava/lang/Double;"))    { primitive = "D"; shortName = "double";  } else
        if(desc.equals("Ljava/lang/Float;"))     { primitive = "F"; shortName = "float";   } else
        if(desc.equals("Ljava/lang/Character;")) { primitive = "C"; shortName = "char";    }
        if(primitive == null) throw new RuntimeException("No unbox for " + t);

        result.add(new TypeInsnNode(CHECKCAST, t.getInternalName()));
        result.add(new MethodInsnNode(INVOKEVIRTUAL, t.getInternalName(), shortName + "Value", "()" + primitive));
        return result;
    }

    public static InsnList getUnboxNodes(Class<?> type) {
        InsnList result = new InsnList();
        String name=null;
        String shortName = type.getName();
        String primitive=null;
        if(type == int.class)     {name = "Integer";  primitive = "I"; } else
        if(type == long.class)    {name = "Long";     primitive = "J"; } else
        if(type == byte.class)    {name = "Byte";     primitive = "B"; } else
        if(type == boolean.class) {name = "Boolean";  primitive = "Z"; } else
        if(type == short.class)   {name = "Short";    primitive = "S"; } else
        if(type == double.class)  {name = "Double";   primitive = "D"; } else
        if(type == float.class)   {name = "Float";    primitive = "F"; } else
        if(type == char.class)    {name = "Character";primitive = "C"; }
        if(name == null) throw new RuntimeException("No unbox for " + type);
        result.add(new TypeInsnNode(CHECKCAST, "java/lang/" + name));
        result.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/" + name, shortName + "Value", "()" + primitive));
        return result;
    }

    public static Type getType(AbstractInsnNode node) {
        if(node instanceof MethodInsnNode) {
            return Type.getReturnType(((MethodInsnNode)node).desc);
        }
        throw new RuntimeException("NYI");
    }

    public static Class<?> defineClass(String className, byte[] bytes) {
        Class<?> clazz = null;
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<?> cls = Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method = cls.getDeclaredMethod( "defineClass",
                new Class[] { String.class, byte[].class, int.class, int.class });

            // protected method invocaton
            method.setAccessible(true);
            try {
                Object[] args = new Object[]{ className, bytes, 0, bytes.length };
                clazz = (Class<?>) method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }


}
