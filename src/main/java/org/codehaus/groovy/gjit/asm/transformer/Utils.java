package org.codehaus.groovy.gjit.asm.transformer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.AbstractVisitor;

public class Utils implements Opcodes {

    public static MethodInsnNode getBoxNode(String desc) {
        String primitive = null;
        Type t = Type.getType(desc);
        t.getInternalName();
        if(desc.equals("Ljava/lang/Integer;"))   { primitive = "I"; } else
        if(desc.equals("Ljava/lang/Long;"))      { primitive = "J"; } else
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
        String primitive=null;
        if(type == int.class)     {name = "Integer";  primitive = "I"; } else
        if(type == long.class)    {name = "Long";     primitive = "J"; } else
        if(type == byte.class)    {name = "Byte";     primitive = "B"; } else
        if(type == boolean.class) {name = "Boolean";  primitive = "Z"; } else
        if(type == short.class)   {name = "Short";    primitive = "S"; } else
        if(type == double.class)  {name = "Double";   primitive = "D"; } else
        if(type == float.class)   {name = "Float";    primitive = "F"; } else
        if(type == char.class)    {name = "Character";primitive = "C"; }
        if(name == null) throw new RuntimeException("No box for " + type);
        return new MethodInsnNode(INVOKESTATIC, "java/lang/" + name, "valueOf", "(" + primitive + ")Ljava/lang/" + name + ";");
    }

    public static MethodInsnNode getBoxNode(Type type) {
        String name=null;
        String primitive=null;
        if(type == Type.INT_TYPE)     {name = "Integer";  primitive = "I"; } else
        if(type == Type.LONG_TYPE)    {name = "Long";     primitive = "J"; } else
        if(type == Type.BYTE_TYPE)    {name = "Byte";     primitive = "B"; } else
        if(type == Type.BOOLEAN_TYPE) {name = "Boolean";  primitive = "Z"; } else
        if(type == Type.SHORT_TYPE)   {name = "Short";    primitive = "S"; } else
        if(type == Type.DOUBLE_TYPE)  {name = "Double";   primitive = "D"; } else
        if(type == Type.FLOAT_TYPE)   {name = "Float";    primitive = "F"; } else
        if(type == Type.CHAR_TYPE)    {name = "Character";primitive = "C"; }
        if(name == null) throw new RuntimeException("No box for " + type);
        return new MethodInsnNode(INVOKESTATIC, "java/lang/" + name, "valueOf", "(" + primitive + ")Ljava/lang/" + name + ";");
    }

    public static InsnList getUnboxNodes(Type type) {
        InsnList result = new InsnList();
        char primitive=0;
        String name = null;
        if(type == Type.INT_TYPE)   	{ primitive = 'I'; name = "Integer";   } else
        if(type == Type.LONG_TYPE)      { primitive = 'J'; name = "Long";      } else
        if(type == Type.BYTE_TYPE)      { primitive = 'B'; name = "Byte";      } else
        if(type == Type.BOOLEAN_TYPE)   { primitive = 'Z'; name = "Boolean";   } else
        if(type == Type.SHORT_TYPE)     { primitive = 'S'; name = "Short";     } else
        if(type == Type.DOUBLE_TYPE)    { primitive = 'D'; name = "Double";    } else
        if(type == Type.FLOAT_TYPE)     { primitive = 'F'; name = "Float";     } else
        if(type == Type.CHAR_TYPE) 		{ primitive = 'C'; name = "Character"; }
        if(primitive == 0) throw new RuntimeException("No unbox for " + type);

        result.add(new TypeInsnNode(CHECKCAST, "java/lang/" + name));
        result.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/" + name, type.getClassName() + "Value", "()" + primitive));
        return result;
    }

    public static InsnList getUnboxNodes(String desc) {
        InsnList result = new InsnList();
        String primitive = null;
        String shortName = null;
        Type t = Type.getType(desc);
        if(desc.equals("Ljava/lang/Integer;"))   { primitive = "I"; shortName = "int";     } else
        if(desc.equals("Ljava/lang/Long;"))      { primitive = "J"; shortName = "long";    } else
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

    public static Type getType(AbstractInsnNode node, Type[] info) {
        if(node.getOpcode() == ALOAD) {
            VarInsnNode aload = (VarInsnNode)node;
            if(aload.var < info.length)
                return info[aload.var];
            else
                return getType(node);
        } else
            return getType(node);
//        AbstractInsnNode p = node.getPrevious();
//        while(p instanceof LineNumberNode == false) p = p.getPrevious();
//        throw new RuntimeException("NYI: " + AbstractVisitor.OPCODES[node.getOpcode()] + ", line: " + ((LineNumberNode)p).line);
    }

    public static Type getType(AbstractInsnNode node) {
        if(node instanceof MethodInsnNode) {
            return Type.getReturnType(((MethodInsnNode)node).desc);
        } else if(node.getOpcode() == ALOAD) {
            return Type.getType("Ljava/lang/Object;");
        } else if(node.getOpcode() >= ICONST_M1 && node.getOpcode() <= ICONST_5) {
            return Type.getType("I");
        } else if(node.getOpcode() == BIPUSH || node.getOpcode() == SIPUSH) {
            return Type.getType("I");
        } else if(node.getOpcode() == IALOAD) {
        	return Type.getType("Ljava/lang/Integer;");
        } else if(node.getOpcode() == ILOAD) {
        	return Type.getType("Ljava/lang/Integer;");
        } else if(node.getOpcode() == ACONST_NULL) {
        	return Type.getType("Ljava/lang/Object;");
        } else if(node instanceof FieldInsnNode) {
        	return Type.getType(((FieldInsnNode)node).desc);
        }
        AbstractInsnNode p = node.getPrevious();
        while(p instanceof LineNumberNode == false) p = p.getPrevious();
        throw new RuntimeException("NYI: " + AbstractVisitor.OPCODES[node.getOpcode()] + ", line: " + ((LineNumberNode)p).line);
    }

    public static Class<?> defineClass(String className, byte[] bytes) {
        // System.out.println("defining new class: " + className);
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
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        return clazz;
    }

    @SuppressWarnings("unchecked")
    public static void prepareClassInfo(ClassNode classNode) {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", classNode.name);

        List<MethodNode> methods = classNode.methods;
        for(MethodNode method: methods) {
            if(method.name.equals("<clinit>")) {
                new ConstantCollector().internalTransform(method, null);
            } else if(method.name.equals("$createCallSiteArray")) {
                new CallSiteNameCollector().internalTransform(method, options);
            }
        }
    }

	public static void log(byte[] bytes) {
		FileOutputStream f;
		try {
			f = new FileOutputStream("Log" + System.currentTimeMillis() + ".class");
			f.write(bytes);
			f.flush();
			f.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


}
