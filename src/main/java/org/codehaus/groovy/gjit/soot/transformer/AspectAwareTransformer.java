package org.codehaus.groovy.gjit.soot.transformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.runtime.callsite.CallSite;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Modifier;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
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

public class AspectAwareTransformer extends BodyTransformer {

	private String withInMethodName;

	private Class<?> advisedReturnType;
	private Class<?>[] advisedTypes;
	private CallSite callSite;

	private PatchingChain<Unit> units;

	private Body body;

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
			if(defBoxes.size() != 1) continue;

			//
			// check if it's CallSite[]
			//
			Value v = defBoxes.get(0).getValue();
			if(v.getType() instanceof ArrayType == false) continue;
			ArrayType at = (ArrayType)v.getType();
			if( at.getArrayElementType().equals(Utils.v().getCallSiteType()) )
				return v;
		}
		return null;
	}

	private Unit locateCallSiteByIndex(PatchingChain<Unit> units,
									   Value callSiteArray,
									   int   callSiteIndex) {
		Iterator<Unit> stmts = units.iterator();
		Unit found = null;

		//
		// find a Unit containing the call site base
		// by checking the index in use boxes
		//
		while(stmts.hasNext()) {
			Unit s = stmts.next();
			List<ValueBox> useBoxes = s.getUseBoxes();
			if(useBoxes.size() != 3) continue;
			if(useBoxes.get(0).getValue().equivTo(callSiteArray)==false) continue;
			int index = ((IntConstant)useBoxes.get(1).getValue()).value;
			if(index != callSiteIndex) continue;
			found = s;
			break;
		}
		if(found == null) return null;

        //
        // the next Unit to the call site base is
        // an invoke instruction we've been finding
        //
		Value callSiteBase = found.getDefBoxes().get(0).getValue();
		Unit s = found;
		while(true) {
			s = units.getSuccOf(s);
			if(s==null) break;
			if(s.getUseBoxes().get(0).getValue().equivTo(callSiteBase)) {
				return s;
			}
		}
		return null;
	}

	@Override
	protected void internalTransform(Body b, String phase, Map options) {

		//
		// do type matching
		// if match, do tranformation for matched call
		// this transformation just occurs right before invocation
		// NOT after weaving
		// but *before* invocation
		// this this should be triggered in EMC
		//
		if(b.getMethod().getName().equals(withInMethodName)) {
			body  = b;
			units = b.getUnits();
			Value acallsite = findCallSiteArray(units);
			Unit invokeStatement = locateCallSiteByIndex(b.getUnits(), acallsite, callSite.getIndex());
			SootMethod newTargetMethod = typePropagate(callSite);
			replaceCallSite(invokeStatement, newTargetMethod);
		}
	}

	private void replaceCallSite(Unit invokeStatement, SootMethod newTargetMethod) {

	    //
		// 2 cases
		//  1. AssignStmt
		//  2. InvokeStmt
		//

		// always static

        //
		// case #1: AssignStmt
		//
		if(invokeStatement instanceof AssignStmt) {
			AssignStmt stmt = (AssignStmt)invokeStatement;
			InvokeExpr e = stmt.getInvokeExpr();
			StaticInvokeExpr expr = Jimple.v().newStaticInvokeExpr(
				newTargetMethod.makeRef(), e.getArgs()
			);
			stmt.setRightOp(expr);
		}

		//
        // case #2: InvokeStmt
        //
		else if(invokeStatement instanceof InvokeStmt) {
			InvokeStmt stmt = (InvokeStmt)invokeStatement;
			InvokeExpr e = stmt.getInvokeExpr();
			StaticInvokeExpr expr = Jimple.v().newStaticInvokeExpr(
				newTargetMethod.makeRef(), e.getArgs()
			);
			stmt.setInvokeExpr(expr);
		}

		//
		// do autoboxing around the call
		//
		autoboxArguments(invokeStatement);
		autoboxReturn(invokeStatement);

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

	private void autoboxArguments(Unit invokeStatement) {
		final Jimple j = Jimple.v();
		InvokeExpr expr=null;
		if(invokeStatement instanceof AssignStmt) {
			AssignStmt stmt = (AssignStmt)invokeStatement;
			expr = stmt.getInvokeExpr();
		} else if(invokeStatement instanceof InvokeStmt) {
			InvokeStmt stmt = (InvokeStmt)invokeStatement;
			expr = stmt.getInvokeExpr();
		}

		ArrayList<Unit> list = new ArrayList<Unit>();

		//
		// cast object to simulate "this"
		//
		Local sim_this = (Local)expr.getArg(0);
		CastExpr cast0 = j.newCastExpr(sim_this, expr.getMethod().getParameterType(0));
		list.add(j.newAssignStmt(sim_this, cast0)); // not SSA

		//
		// i starts from 1
		// Skip the first one, which simulates "this"
		//
		for(int i = 1; i < expr.getArgCount(); i++) {
			if(advisedTypes[i-1] == null) continue;
			Value arg = expr.getArg(i);
			String argName = ((Local)arg).getName();

			Type fromType = arg.getType();
			Type toType = Utils.v().classToSootType(advisedTypes[i-1]);
			if(toType.equals(fromType)) continue;

			System.out.println("from : " + fromType);
			System.out.println("to   : " + toType);
			if(toType instanceof PrimType) {
				String name = toType.toString();
				RefType wrapperType = Utils.v().getWrapperType(toType);

				Local castLocal = null;
				if(fromType != wrapperType) { // need cast
					castLocal     = j.newLocal(argName + "_x0", wrapperType);
					CastExpr cast = j.newCastExpr(arg, wrapperType);
					AssignStmt s0 = j.newAssignStmt(castLocal, cast);
					body.getLocals().add(castLocal);
					list.add(s0);
				}

				SootClass wrapperSc = wrapperType.getSootClass();
				SootMethod method   = wrapperSc.getMethod(name + " " + name + "Value()");
				Local newArg        = j.newLocal(argName + "_x1", toType);
				body.getLocals().add(newArg);

				InvokeExpr invoke   = null;
				if(castLocal == null) {
					invoke = j.newVirtualInvokeExpr((Local)arg, method.makeRef());
				} else {
					invoke = j.newVirtualInvokeExpr(castLocal, method.makeRef());
				}
				AssignStmt s1 = j.newAssignStmt(newArg, invoke);
				list.add(s1);

				expr.setArg(i, newArg);
				units.insertBefore(list, invokeStatement);

				// TODO: for D E B U G G I N G
				System.out.println("done");
				for (Iterator<Unit> iterator = list.iterator(); iterator.hasNext();) {
					System.out.println(iterator.next());
				}
				System.out.println(invokeStatement);
				// for D E B U G G I N G
			}
		}
	}

	private void autoboxReturn(Unit invokeStatement) {
		if(invokeStatement instanceof AssignStmt) {
			AssignStmt a = (AssignStmt)invokeStatement;
			Local left = (Local)a.getLeftOp();
			Type fromType = Utils.v().classToSootType(advisedReturnType);
			Type toType = left.getType();
			if(toType.equals(fromType)) return;

			// TODO: for D E B U G G I N G
			// System.out.println("return fromType :" + fromType);
			// System.out.println("return toType   :" + toType);
			// for D E B U G G I N G
			if(fromType instanceof PrimType) {
				Local newReturn = Jimple.v().newLocal(left.getName() + "_x0",	fromType);
				a.setLeftOp(newReturn);
				body.getLocals().add(newReturn);

				final RefType wrapper     = Utils.v().getWrapperType(fromType);
				final SootClass wrapperSc = wrapper.getSootClass();
				final String name = fromType.toString();

				//
				// create a call of valueOf:
				//   for example, java.lang.Integer valueOf(int)
				//                java.lang.Long    valueOf(long)
				//
				SootMethod method = wrapperSc.getMethod(wrapper.getClassName() + " valueOf(" + name + ")");
				StaticInvokeExpr unbox = Jimple.v().newStaticInvokeExpr(method.makeRef(), newReturn);
				AssignStmt s0 = Jimple.v().newAssignStmt(left, unbox);
				units.insertAfter(s0, invokeStatement);
			}
		}
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
			typeList.add(Utils.v().classToSootType(cls));
		}
		Type returnType = targetSm.getReturnType();
		if(advisedReturnType != null) {
			returnType = Utils.v().classToSootType(advisedReturnType);
		}

		PatchingChain<Unit> units = jBody.getUnits();
		Iterator<Unit> stmts = units.iterator();
		while(stmts.hasNext()) {
			Unit s = stmts.next();
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
		byte[] bytes = Utils.v().writeClass(newSc);
		Utils.v().defineClass(newClassName, bytes);

		//
		// TODO: for D E B U G G I N G
		//
		FileOutputStream fos;
		try {
			new File("Dump_03.class").delete();
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

}
