package org.codehaus.groovy.gjit.soot.transformer;

import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Type;

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

}
