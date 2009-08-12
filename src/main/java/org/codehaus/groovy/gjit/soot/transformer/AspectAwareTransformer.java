package org.codehaus.groovy.gjit.soot.transformer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.gjit.agent.Agent;
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
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.jimple.internal.JIdentityStmt;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.util.Chain;
import soot.util.JasminOutputStream;

public class AspectAwareTransformer extends BodyTransformer {

	private String withInMethodName;

	private Class<?>[] advisedTypes;
	private CallSite callSite;

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
			System.out.println(invokeStatement);

			System.out.println("argTypes: " + advisedTypes);
			for (int i = 0; i < advisedTypes.length; i++) {
				System.out.println("arg[" + i + "]: " + advisedTypes[i]);
			}

			System.out.println(b.getMethod());
			System.out.println("withInMethodName: " + withInMethodName);

			System.out.println("calling to " + callSite.getName());
			System.out.println(callSite);


			String[] targetNames = callSite.getClass().getName().split("\\$");
			SootClass   targetSc = Scene.v().loadClass(targetNames[0], SootClass.BODIES);
			SootMethod  targetSm = targetSc.getMethodByName(targetNames[1]);
			System.out.println(targetSm.getSubSignature());
			typePropagate(targetSc, targetSm, advisedTypes, callSite);
			System.out.println("========================");
		}
	}

	private void typePropagate(SootClass sc, SootMethod targetMethod, Class<?>[] advisedTypes, CallSite callSite) {
		Body body = targetMethod.retrieveActiveBody();
		ShimpleBody sBody;
		if (body instanceof ShimpleBody) {
			sBody = (ShimpleBody) body;
			if (!sBody.isSSA())
				sBody.rebuild();
		} else {
			sBody = Shimple.v().newBody(body);
		}
		JimpleBody jBody = sBody.toJimpleBody();

		Chain<Local> locals = jBody.getLocals();
		for (Iterator<Local> i = locals.iterator(); i.hasNext();) {
			Local local = i.next();
			System.out.println(local);
		}
		PatchingChain<Unit> units = jBody.getUnits();
		Iterator<Unit> stmt = units.iterator();
		while(stmt.hasNext()) {
			Unit s = stmt.next();
			if(s instanceof IdentityStmt) {
				JIdentityStmt a = (JIdentityStmt)s;
				// System.out.println(a.getRightOp());
				// System.out.println(a.getRightOp().getClass());
				Value right = a.getRightOp();
				if(right instanceof ThisRef) {
					a.setRightOp(
						Jimple.v().newParameterRef(right.getType(), 0)
					);
				} else if(right instanceof ParameterRef) {
					ParameterRef p = (ParameterRef)right;
					p.setIndex(p.getIndex() + 1);
				}
			}
		}

		SootClass newSc = new SootClass(callSite.getClass().getName() + "$x", Modifier.PUBLIC);
		newSc.setSuperclass(RefType.v("sun.reflect.GroovyAOPMagic").getSootClass());
		ArrayList<Type> typeList = new ArrayList<Type>();
		typeList.add(sc.getType());
		typeList.add(RefType.v("java.lang.Object"));
		SootMethod newMethod = new SootMethod(targetMethod.getName()+"_x", typeList, RefType.v("java.lang.Object"));
		newMethod.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		newMethod.setDeclaringClass(newSc);
		newSc.addMethod(newMethod);
		newMethod.setActiveBody(jBody);
		byte[] bytes = writeClass(newSc);
		System.out.println(bytes.length);

		FileOutputStream fos;
		try {
			fos = new FileOutputStream("Dump.class");
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

}
