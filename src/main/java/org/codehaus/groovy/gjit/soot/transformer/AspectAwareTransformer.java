package org.codehaus.groovy.gjit.soot.transformer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.runtime.callsite.CallSite;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.CompilationDeathException;
import soot.Local;
import soot.Modifier;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ParameterRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.ThisRef;
import soot.jimple.internal.JIdentityStmt;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.util.JasminOutputStream;

public class AspectAwareTransformer extends BodyTransformer {

	private String withInMethodName;

	private Class<?> advisedReturnType;
	private Class<?>[] advisedTypes;
	private CallSite callSite;

	public void setAdvisedReturnType(Class<?> advisedReturnType) {
		this.advisedReturnType = advisedReturnType;
	}

	public void setAdvisedTypes(Class<?>[] argTypes) {
		this.advisedTypes = argTypes;
	}

	public void setWithInMethodName(String withInMethodName) {
		this.withInMethodName = withInMethodName;
	}

	public void setCallSite(CallSite callSite) {
		this.callSite = callSite;
	}

	private Value findCallSiteArray(PatchingChain<Unit> u) {
		Iterator<Unit> stmts = u.iterator();
		while(stmts.hasNext()) {
			Unit s = stmts.next();
			List<ValueBox> defBoxes = s.getDefBoxes();
			if(defBoxes.size()!=1) continue;
			// check if it's CallSite[]
			Value v = defBoxes.get(0).getValue();
			if(v.getType() instanceof ArrayType == false) continue;
			ArrayType at = (ArrayType)v.getType();
			if(at.getArrayElementType().equals(Utils.v().getCallSiteType())==false) continue;
			return v;
		}
		return null;
	}

	private Unit locateCallSiteByIndex(PatchingChain<Unit> units,
			Value acallsite, int callSiteIndex) {
		Iterator<Unit> stmts = units.iterator();
		Unit found = null;
		while(stmts.hasNext()) {
			Unit s = stmts.next();
			List<ValueBox> useBoxes = s.getUseBoxes();
			if(useBoxes.size()!=3) continue;
			if(useBoxes.get(0).getValue().equivTo(acallsite)==false) continue;
			int index = ((IntConstant)useBoxes.get(1).getValue()).value;
			if(index != callSiteIndex) continue;
			found = s;
			break;
		}

		if(found == null) return null;
		Value callSiteBase = found.getDefBoxes().get(0).getValue();
		Unit s = found;
		while(true) {
			s = units.getSuccOf(s);
			if(s.getUseBoxes().get(0).getValue().equivTo(callSiteBase)) {
				return s;
			}
			if(s==null) break;
		}
		return null;
	}

	@Override
	protected void internalTransform(Body b, String phase, Map options) {
		// do type matching
		// if match, do tranformation for matched call
		// this transformation just occurs right before invocation
		// NOT after weaving
		// but *before* invocation
		// this this should be triggered in EMC

		// PatchingChain<Unit> u = b.getUnits();
		// Iterator<Unit> stmts = u.snapshotIterator();
		// while (stmts.hasNext()) {
		//	Unit s = stmts.next();
		//	System.out.println(s);
		// }
		if(b.getMethod().getName().equals(withInMethodName)) {
			Value acallsite = findCallSiteArray(b.getUnits());
			Unit invokeStatement = locateCallSiteByIndex(b.getUnits(), acallsite, callSite.getIndex());
			SootMethod newTargetMethod = typePropagate(callSite);
			replaceCallSite(invokeStatement, newTargetMethod);
		}
	}

	private void replaceCallSite(Unit invokeStatement, SootMethod newTargetMethod) {
		// 2 cases
		// 1. AssignStmt
		// 2. InvokeStmt
		System.out.println(invokeStatement);
		System.out.println(newTargetMethod);
		// always static

		if(invokeStatement instanceof AssignStmt) {
			AssignStmt stmt = (AssignStmt)invokeStatement;
			InvokeExpr e = stmt.getInvokeExpr();
			StaticInvokeExpr expr = Jimple.v().newStaticInvokeExpr(
				newTargetMethod.makeRef(), e.getArgs()
			);
			stmt.setRightOp(expr);
		} else if(invokeStatement instanceof InvokeStmt) {
			InvokeStmt stmt = (InvokeStmt)invokeStatement;
			InvokeExpr e = stmt.getInvokeExpr();
			StaticInvokeExpr expr = Jimple.v().newStaticInvokeExpr(
				newTargetMethod.makeRef(), e.getArgs()
			);
			stmt.setInvokeExpr(expr);
		}
		System.out.println(invokeStatement);
		//
		// What to do here,
		// 1. creating a newSC class
		// 2. replace a call site object with the direct call to newSC class
		// and "redefine" the caller class.
		//

//		try {
//			Agent.getInstrumentation().redefineClasses(
//				new ClassDefinition(Class.forName(newSc.getName()), bytes)
//			);
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException(e);
//		} catch (UnmodifiableClassException e) {
//			throw new RuntimeException(e);
//		}
	}

	private SootMethod typePropagate(CallSite callSite) {
		String[] targetNames = callSite.getClass().getName().split("\\$");
		SootClass targetSc = Scene.v().loadClass(targetNames[0], SootClass.BODIES);

		//
		// TODO: This should be
		// SootMethod  targetSm = targetSc.getMethod("sub signature");
		//
		SootMethod targetSm = targetSc.getMethodByName(targetNames[1]);

		//
		// Retrieve body from the original target method.
		// We need a jimple body.
		//
		Body body = targetSm.retrieveActiveBody();
		ShimpleBody sBody;
		if (body instanceof ShimpleBody) {
			sBody = (ShimpleBody)body;
			if (!sBody.isSSA()) sBody.rebuild();
		} else {
			sBody = Shimple.v().newBody(body);
		}
		JimpleBody jBody = sBody.toJimpleBody();

		//
		// Create a new class using the same name with "$x".
		// CallSite in groovy is created using the pattern Class$method.
		// We append $x to it.
		//
		String newClassName = callSite.getClass().getName() + "$x";
		SootClass newSc = new SootClass(newClassName, Modifier.PUBLIC);

		//
		// Need a magic super type for bypassing security check
		//
		newSc.setSuperclass(RefType.v("sun.reflect.GroovyAOPMagic").getSootClass());
		ArrayList<Type> typeList = new ArrayList<Type>();
		typeList.add(targetSc.getType());
		for (int i = 0; i < advisedTypes.length; i++) {
			Class<?> cls = advisedTypes[i];
			if(cls.isPrimitive()) {
				typeList.add(Utils.v().primitive(cls));
			} else {
				typeList.add(RefType.v(cls.getName()));
			}
		}
		Type returnType = targetSm.getReturnType();
		if(advisedReturnType != null) {
			if(advisedReturnType.isPrimitive()) {
				returnType = Utils.v().primitive(advisedReturnType);
			} else {
				returnType = RefType.v(advisedReturnType.getName());
			}
		}

		PatchingChain<Unit> units = jBody.getUnits();
		Iterator<Unit> stmt = units.iterator();
		while(stmt.hasNext()) {
			Unit s = stmt.next();
			if(s instanceof IdentityStmt) {
				JIdentityStmt a = (JIdentityStmt)s;
				Value right = a.getRightOp();
				if(right instanceof ThisRef) {
					a.setRightOp(
						Jimple.v().newParameterRef(right.getType(), 0)
					);
				} else if(right instanceof ParameterRef) {
					ParameterRef p = (ParameterRef)right;
					a.setRightOp(
						Jimple.v().newParameterRef(
							typeList.get(p.getIndex() + 1),
							p.getIndex() + 1
						)
					);
					//
					// $r_i.type = typeof(rhs)
					//
					((Local)a.getLeftOp()).setType(a.getRightOp().getType());
				}
			}
		}

		SootMethod newMethod = new SootMethod(targetSm.getName(), typeList, returnType);
		newMethod.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		newMethod.setDeclaringClass(newSc);
		newSc.addMethod(newMethod);
		newMethod.setActiveBody(jBody);
		byte[] bytes = writeClass(newSc);
		defineClass(newClassName, bytes);

		//
		// TODO: for D E B U G G I N G
		//
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("Dump_03.class");
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newMethod;
	}

	private byte[] writeClass(SootClass c) {
		List<SootMethod> m = c.getMethods();
		for (Iterator iterator = m.iterator(); iterator.hasNext();) {
			SootMethod sootMethod = (SootMethod) iterator.next();
			sootMethod.retrieveActiveBody();
		}

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		//String fileName = SourceLocator.v().getFileNameFor(c, format);
		JasminOutputStream streamOut = new JasminOutputStream(bout);
		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));

		if (c.containsBafBody())
			new soot.baf.JasminClass(c).print(writerOut);
		else
			new soot.jimple.JasminClass(c).print(writerOut);

		try {
			writerOut.flush();
			streamOut.close();
			return bout.toByteArray();
		} catch (IOException e) {
			throw new CompilationDeathException("Cannot close output file ");
		}
	}

	private Class<?> defineClass(String className, byte[] b) {
		Class<?> clazz = null;
		try {
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class<?> cls = Class.forName("java.lang.ClassLoader");
			java.lang.reflect.Method method = cls.getDeclaredMethod( "defineClass",
				new Class[] { String.class, byte[].class, int.class, int.class });

			// protected method invocaton
			method.setAccessible(true);
			try {
				Object[] args = new Object[]{ className, b, 0, b.length };
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
