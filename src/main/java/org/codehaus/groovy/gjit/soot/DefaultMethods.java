package org.codehaus.groovy.gjit.soot;

public class DefaultMethods {

	public static int plus(Object obj, int arg1) {
		if(obj instanceof Integer) return ((Integer)obj).intValue() + arg1;
		if(obj instanceof Short) return ((Short)obj).shortValue() + arg1;
		if(obj instanceof Byte) return ((Byte)obj).byteValue() + arg1;
		if(obj instanceof Integer) return ((Integer)obj).intValue() + arg1;
		if(obj instanceof Long) return (int) (((Long)obj).longValue() + arg1);		
		if(obj instanceof Float) return (int) (((Float)obj).floatValue() + arg1);		
		if(obj instanceof Double) return (int) (((Double)obj).doubleValue() + arg1);		
		throw new RuntimeException("Not fully implemented");
	}

//	public static int minus(Object obj, int arg1) {
//		if(obj instanceof Integer) return ((Integer)obj).intValue() - arg1;
//		if(obj instanceof Short) return ((Short)obj).shortValue() - arg1;
//		if(obj instanceof Byte) return ((Byte)obj).byteValue() - arg1;
//		if(obj instanceof Integer) return ((Integer)obj).intValue() - arg1;
//		if(obj instanceof Long) return (int) (((Long)obj).longValue() - arg1);		
//		if(obj instanceof Float) return (int) (((Float)obj).floatValue() - arg1);		
//		if(obj instanceof Double) return (int) (((Double)obj).doubleValue() - arg1);		
//		throw new RuntimeException("Not fully implemented");
//	}
//	
//	public static int minus(Object obj, int arg1) {
//		if(obj instanceof Integer) return ((Integer)obj).intValue() - arg1;
//		if(obj instanceof Short) return ((Short)obj).shortValue() - arg1;
//		if(obj instanceof Byte) return ((Byte)obj).byteValue() - arg1;
//		if(obj instanceof Integer) return ((Integer)obj).intValue() - arg1;
//		if(obj instanceof Long) return (int) (((Long)obj).longValue() - arg1);		
//		if(obj instanceof Float) return (int) (((Float)obj).floatValue() - arg1);		
//		if(obj instanceof Double) return (int) (((Double)obj).doubleValue() - arg1);		
//		throw new RuntimeException("Not fully implemented");
//	}	
	
	
}
