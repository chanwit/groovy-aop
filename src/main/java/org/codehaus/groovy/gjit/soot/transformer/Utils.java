package org.codehaus.groovy.gjit.soot.transformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.CompilationDeathException;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.util.JasminOutputStream;

public class Utils {

	private static Utils instance = null;

	private final String CALL_SUB_SIGNATURE = "java.lang.Object call(java.lang.Object,java.lang.Object)";
	private final String CALL_SITE_STR = "org.codehaus.groovy.runtime.callsite.CallSite";

	private final String TYPE_TRANS_STR = "org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation";
	private final String INT_UNBOX_SIGNATURE = "int intUnbox(java.lang.Object)";
	private final String INT_BOX_SIGNATURE = "java.lang.Object box(int)";

	private final String S_B_A = "org.codehaus.groovy.runtime.ScriptBytecodeAdapter";
	private final String CAST_TO_TYPE_SIGNATURE = "java.lang.Object castToType(java.lang.Object,java.lang.Class)";

	private final SootClass callSiteClass;
	public final SootClass transformationClass;

	public final SootMethodRef callSiteBinMethodRef;
	public final SootMethodRef intUnboxMethodRef;
	public final SootMethodRef intBoxMethodRef;

	public final SootClass scriptBytecodeAdapter;
	public final SootMethodRef castToType;

	private Utils(){
		callSiteClass = Scene.v().loadClass(CALL_SITE_STR,SootClass.SIGNATURES);
		callSiteBinMethodRef = callSiteClass.getMethod(CALL_SUB_SIGNATURE).makeRef();
		transformationClass = Scene.v().loadClass(TYPE_TRANS_STR, SootClass.SIGNATURES);
		intUnboxMethodRef = transformationClass.getMethod(INT_UNBOX_SIGNATURE).makeRef();
		intBoxMethodRef = transformationClass.getMethod(INT_BOX_SIGNATURE).makeRef();
		scriptBytecodeAdapter = Scene.v().loadClass(S_B_A,SootClass.SIGNATURES);
		castToType = scriptBytecodeAdapter.getMethod(CAST_TO_TYPE_SIGNATURE).makeRef();
	}

	public static Utils v() {
		if(instance == null) instance = new Utils();
		return instance;
	}

	public Type getCallSiteType() {
		return callSiteClass.getType();
	}

	public Type primitive(Class<?> cls) {
		if(cls == int.class)     return IntType.v();
		if(cls == long.class)    return LongType.v();
		if(cls == byte.class)    return ByteType.v();
		if(cls == boolean.class) return BooleanType.v();
		if(cls == char.class)    return CharType.v();
		if(cls == double.class)  return DoubleType.v();
		if(cls == float.class)   return FloatType.v();
		if(cls == short.class)   return ShortType.v();
		return null;
	}

	public Type classToSootType(Class<?> cls) {
		if(cls.isPrimitive()) {
			return primitive(cls);
		} else {
			return RefType.v(cls.getName());
		}
	}

	public Class<?> defineClass(String className, byte[] bytes) {
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

	public byte[] writeClass(SootClass sc) {
		List<SootMethod> methods = sc.getMethods();
		for (Iterator<SootMethod> i = methods.iterator(); i.hasNext();) {
			SootMethod sm = i.next();
			sm.retrieveActiveBody();
		}

		ByteArrayOutputStream bout   = new ByteArrayOutputStream();
		JasminOutputStream streamOut = new JasminOutputStream(bout);
		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));

		if (sc.containsBafBody())
			new soot.baf.JasminClass(sc).print(writerOut);
		else
			new soot.jimple.JasminClass(sc).print(writerOut);

		try {
			writerOut.flush();
			streamOut.close();
			return bout.toByteArray();
		} catch (IOException e) {
			throw new CompilationDeathException("Cannot close output file ");
		}
	}

	public RefType getWrapperType(Type toType) {
		if(toType == IntType.v())     return RefType.v("java.lang.Integer");
		if(toType == LongType.v())    return RefType.v("java.lang.Long");
		if(toType == DoubleType.v())  return RefType.v("java.lang.Double");
		if(toType == FloatType.v())   return RefType.v("java.lang.Float");
		if(toType == ByteType.v())    return RefType.v("java.lang.Byte");
		if(toType == BooleanType.v()) return RefType.v("java.lang.Boolean");
		if(toType == CharType.v())    return RefType.v("java.lang.Character");
		if(toType == ShortType.v())   return RefType.v("java.lang.Short");
		return null;
	}

}
