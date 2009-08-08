package org.codehaus.groovy.gjit.soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.codehaus.groovy.gjit.soot.transformer.Utils;

import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Expr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JimpleLocal;

public class GroovyJimpleBody {

	private static final RefType JAVA_LANG_CHARACTER = RefType.v("java.lang.Character");
	private static final RefType JAVA_LANG_BYTE      = RefType.v("java.lang.Byte");
	private static final RefType JAVA_LANG_SHORT     = RefType.v("java.lang.Short");
	private static final RefType JAVA_LANG_DOUBLE    = RefType.v("java.lang.Double");
	private static final RefType JAVA_LANG_FLOAT     = RefType.v("java.lang.Float");
	private static final RefType JAVA_LANG_LONG      = RefType.v("java.lang.Long");
	private static final RefType JAVA_LANG_INTEGER   = RefType.v("java.lang.Integer");

	private Body body;
	private PatchingChain<Unit> units;
	private HashMap<Value, String> callSiteNames;
	// private HashMap<Value, Type> inferredTypes = new HashMap<Value, Type>();
	private boolean optimizable;
	private ConstantPack pack;
	private List<Local> needToUnboxLHS = new ArrayList<Local>();
	private List<Local> needToUnboxRHS = new ArrayList<Local>();

	public GroovyJimpleBody(Body b) {
		this.body = b;
		this.units = body.getUnits();
		this.callSiteNames = new HashMap<Value, String>();
		this.pack = ConstantRecord.v().get(b.getMethod().getDeclaringClass().getName());
		initialise();
	}

	private void initialise() {
		Value acallsite = findCallSiteArray(units);
		if (acallsite == null) {
			this.optimizable = false;
			return;
		}
		this.optimizable = true;
		final String[] names = CallSiteArrayPack.v().get(body.getMethod().getDeclaringClass().getName());

		Iterator<Unit> stmts = units.snapshotIterator();
		while (stmts.hasNext()) {
			Unit s = stmts.next();
			// System.out.println("//" + s);
			if(extractCallSiteName(s, acallsite, names)) continue;
			if(eliminateBoxCastUnbox(s)) continue; // - included type correction
			if(unwrapConst(s)) continue; // - included type correction
			if(unwrapBoxOrUnbox(s)) continue;
			if(unwrapPrimitiveCall(s)) continue;
			if(unwrapCompare(s)) continue;
			clearCast(s);
			if(correctNormalCall(s)) continue;
			correctTypeIfPrimitive(s);
		}
//		Iterator<Unit> stmt_2s = units.snapshotIterator();
//		while (stmt_2s.hasNext()) {
//			Unit s = stmt_2s.next();
//			if(unwrapCompare(s)) continue;
//			if(clearCast(s)) continue;
//			if(correctNormalCall(s)) continue;
//			correctTypeIfPrimitive(s);
//		}
	}

	private static final String STR_JAVA_LANG_INTEGER = "$get$$class$java$lang$";
	private static final String BOX_METHOD = "box";
	private static final String DEFAULT_TYPE_TRANSFORMATION = "org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation";

	private boolean eliminateBoxCastUnbox(Unit s0) {
		List<ValueBox> useBoxes = s0.getUseBoxes();
		if(useBoxes.size()!=2) return false;
		Value i0 = useBoxes.get(0).getValue();
		if(i0.getType() instanceof PrimType) {
			if(s0 instanceof JAssignStmt == false) return false;
			JAssignStmt a = (JAssignStmt)s0;
			if(a.containsInvokeExpr()==false) return false;
			InvokeExpr iv = a.getInvokeExpr();
			String c = iv.getMethod().getDeclaringClass().getName();
			String m = iv.getMethod().getName();
			if(c.equals(DEFAULT_TYPE_TRANSFORMATION) && m.equals(BOX_METHOD)) {
				try {
					Unit s1 = units.getSuccOf(s0);
					JAssignStmt a1 = (JAssignStmt)s1;
					if(a1.getInvokeExpr().getMethod().getName().startsWith("$get$$class$java$lang$")) {
						Unit s2 = units.getSuccOf(s1);
						List<ValueBox> useBoxes2 = s2.getUseBoxes();
						if(useBoxes2.size() != 3) return false;
						if(useBoxes2.get(0).getValue().equivTo(s0.getDefBoxes().get(0).getValue()) == false) return false;
						if(useBoxes2.get(1).getValue().equivTo(s1.getDefBoxes().get(0).getValue()) == false) return false;
						Unit s3 = units.getSuccOf(s2);
						JAssignStmt a3 = (JAssignStmt)s3;
						if(a3.getRightOp() instanceof JCastExpr == false) return false;
						Unit s4 = units.getSuccOf(s3);
						if(s4.getDefBoxes().get(0).getValue().getType().equals(IntType.v()) == false) return false;
						JAssignStmt a4 = (JAssignStmt)s4;
						a4.setRightOp(i0);
						// set type here
						((Local)a4.getLeftOp()).setType(i0.getType());
						units.remove(s0);
						units.remove(s1);
						units.remove(s2);
						units.remove(s3);
						return true;
					}
				} catch(Exception e) { return false; }
			}
		}
		return false;
	}

	private void correctTypeIfPrimitive(Unit s) {
		if(s instanceof AssignStmt) {
			AssignStmt a = (AssignStmt)s;
			// System.out.println(a.getLeftOp() + ":" + a.getLeftOp().getClass());
			if(a.getRightOp() instanceof Local) {
				Local rv = (Local)a.getRightOp();
				if(rv.getType() instanceof PrimType) {
					if(a.getLeftOp() instanceof Local) {
						Local lv = (Local)a.getLeftOp();
						lv.setType(rv.getType());
						//System.out.println("]] correctTypeIfPrimitive");
					} else if (a.getLeftOp() instanceof JArrayRef) {
						Local l = box(s, a.getRightOp());
						a.setRightOp(l);
						//System.out.println("]] correctTypeIfPrimitive, fixed");
					}
				}
			}
		}
	}


	private boolean clearCast(Unit s0) {
//		$r17 = ?
//		$r18 = staticinvoke <TreeNode: java.lang.Class $get$$class$java$lang$Integer()>()
//		$r19 = staticinvoke <org.codehaus.groovy.runtime.ScriptBytecodeAdapter: java.lang.Object castToType(java.lang.Object,java.lang.Class)>($r17, $r18)
//		$r20 = (java.lang.Integer) $r19
//			and
//		$r17 = interfaceinvoke $r15.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object)>($r16)
//		$r18 = staticinvoke <TreeNode: java.lang.Class $get$$class$java$lang$Long()>()
//		$r19 = staticinvoke <org.codehaus.groovy.runtime.ScriptBytecodeAdapter: java.lang.Object castToType(java.lang.Object,java.lang.Class)>($r17, $r18)
//		r2 = (java.lang.Long) $r19
		Unit s1=null;
		Unit s2=null;
		try {
		s1 = units.getSuccOf(s0);
		if(s1==null) return false;
		s2 = units.getSuccOf(s1);
		if(s2==null) return false;
		} catch(NoSuchElementException e) {return false;}
		List<ValueBox> u0 = s0.getUseBoxes();
		if(u0.size()!=1) return false;
		List<ValueBox> u1 = s1.getUseBoxes();
		if(u1.size()!=3) return false;
		List<ValueBox> u2 = s2.getUseBoxes();
		if(u2.size()!=2) return false;
		AssignStmt a2 = (AssignStmt)s2;
		if(a2.getRightOp() instanceof CastExpr == false) return false;
		Value v = u1.get(0).getValue();
		//a2.setRightOp(box(a2,v));
		if(v.getType() instanceof PrimType) {
			((Local)a2.getLeftOp()).setType(v.getType());
			a2.setRightOp(v);
			units.remove(s0);
			units.remove(s1);
			// System.out.println("]] clear cast #1");
			return true;
		} else {
			CastExpr c = (CastExpr)a2.getRightOp();
			if(unbox(a2, v, (RefType)c.getCastType())) {
				units.remove(s0);
				units.remove(s1);
				// System.out.println("]] clear cast #2");
				return true;
			}
		}
		return false;
	}

	private PrimType getPrimitiveType(RefType refType) {
		PrimType primType=null;
		if(refType.equals(JAVA_LANG_INTEGER))   primType = IntType.v(); else
		if(refType.equals(JAVA_LANG_LONG))      primType = LongType.v(); else
		if(refType.equals(JAVA_LANG_FLOAT))     primType = FloatType.v(); else
		if(refType.equals(JAVA_LANG_DOUBLE))    primType = DoubleType.v(); else
		if(refType.equals(JAVA_LANG_SHORT))     primType = ShortType.v(); else
		if(refType.equals(JAVA_LANG_BYTE))      primType = ByteType.v(); else
		if(refType.equals(JAVA_LANG_CHARACTER)) primType = CharType.v();
		return primType;
	}

	private RefType getWrapperType(PrimType t) {
		String name=null;
		if(t == IntType.v())     name = "java.lang.Integer";   else
		if(t == ByteType.v())    name = "java.lang.Byte";      else
		if(t == CharType.v())    name = "java.lang.Character"; else
		if(t == LongType.v())    name = "java.lang.Long";      else
		if(t == ShortType.v())   name = "java.lang.Short";     else
		if(t == FloatType.v())   name = "java.lang.Float";     else
		if(t == DoubleType.v())  name = "java.lang.Double";    else
		if(t == BooleanType.v()) name = "java.lang.Boolean";
		RefType refType = RefType.v(name);
		return refType;
	}

	private boolean unbox2(AssignStmt a, Value v, RefType castType) {
//		$r78_0 = interfaceinvoke $r74.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object)>($r77)
//		$r78 =
//		r79 = r68_1 + $r78
		Type primType = getPrimitiveType(castType);
		if(primType != null) {
			//RefType refType = (RefType)castType;
			String localName = v + "_0";
			if(localName.charAt(0)!='$') localName = "$" + localName;
			Local local = Jimple.v().newLocal(localName, castType);
			// AssignStmt a0 = Jimple.v().newAssignStmt(local, Jimple.v().newCastExpr(v, castType));
			// TODO generalise class
			SootMethodRef methodRef = Scene.v().makeMethodRef(castType.getSootClass(), primType.toString()+"Value", new ArrayList<Type>(),primType, false);
			a.setLeftOp(local);
			AssignStmt a0 = Jimple.v().newAssignStmt(local, Jimple.v().newCastExpr(local, castType));
			AssignStmt a1 = Jimple.v().newAssignStmt(v, Jimple.v().newVirtualInvokeExpr(local, methodRef));
			((Local)a1.getLeftOp()).setType(primType);
			body.getLocals().add(local);
			units.insertAfter(a0, a);
			units.insertAfter(a1, a0);
			return true;
		}
		return false;
	}

	private boolean unbox(AssignStmt a, Value v, RefType castType) {
		Type primType = null;
		primType = getPrimitiveType(castType);
		if(primType != null) {
			// RefType refType = (RefType)castType;
			String localName = v + "_0";
			if(localName.charAt(0)!='$') localName = "$" + localName;
			Local local = Jimple.v().newLocal(localName, castType);
			AssignStmt a0 = Jimple.v().newAssignStmt(local, Jimple.v().newCastExpr(v, castType));
			// TODO generalise class
			SootMethodRef methodRef = Scene.v().makeMethodRef(castType.getSootClass(), primType.toString()+"Value", new ArrayList<Type>(),primType, false);
			a.setRightOp(Jimple.v().newVirtualInvokeExpr(local, methodRef));
			((Local)a.getLeftOp()).setType(primType);
			body.getLocals().add(local);
			units.insertBefore(a0, a);
			return true;
		}
		return false;
	}

//	$r18 = staticinvoke <TreeNode: java.lang.Class $get$$class$TreeNode()>()
//	$r19 = r0[4]
//	$r4 = 1
//	$r20 = r3 - $r4
//	$r21 = i2
//	$r22 = interfaceinvoke $r17.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object callStatic(java.lang.Class,java.lang.Object,java.lang.Object)>($r18, $r20, $r21)

//	$r1 = new java.lang.Integer
//	specialinvoke $r1.<java.lang.Integer: void <init>(int)>(i0)
	private boolean correctNormalCall(Unit s) {
		InvokeExpr iv = null;
		Local ret = null;
		AssignStmt a=null;
		boolean rhs = false;
		if(s instanceof InvokeStmt) {
			iv = ((InvokeStmt)s).getInvokeExpr();
		} if (s instanceof AssignStmt) {
			a = ((AssignStmt)s);
			if(a.containsInvokeExpr() == false) return false;
			iv = a.getInvokeExpr();
			ret = (Local)a.getLeftOp();
//			if(needToUnboxRHS.contains(ret)) {
//				rhs = true;
//			} else if(needToUnboxLHS.contains(ret)){
//				rhs = false;
//			} else {
//				ret = null;
//			}
		}
		if(iv==null) return false;
		if(iv.getMethod().getName().startsWith("createRange")==false) {
			if(iv.getMethod().getName().startsWith("compareEqual")==false) {
				if(iv.getMethod().getName().startsWith("call")==false) return false;
			}
		}
		List<Value> list = iv.getArgs();
		int cur = 0;
		SootMethod m = iv.getMethod();
		for (Value value : list) {
			// System.out.println("]] correctNormalCall, " + value + ":" + value.getType());
			Type t = value.getType();
			if((t instanceof PrimType) && (m.getParameterType(cur) instanceof RefType)) {
				Local l = box(s, value);
				iv.setArg(cur, l);
			}
			cur++;
		}
		if(ret != null) {
			if(ret.getType() instanceof PrimType) {
				unbox2(a, ret, getWrapperType((PrimType)ret.getType()));
			}
//			if(rhs == true) {
//				// TODO check, get type from LHR of the binary statement
//				//System.err.println("!! -- correct normal call, " + ret + ", " + ret.getType());
//
//			} else {
//				//System.err.println("!! -- correct normal call, " + ret + ", " + ret.getType());
//			}
		}
		return true;
	}

	private Local box(Unit s, Value value) {
		Type t = value.getType();
		String localName = value+"x";
		if(localName.charAt(0)!='$') localName = "$" + localName;
		Local l = Jimple.v().newLocal(localName, RefType.v("java.lang.Object"));
		// System.out.println("box, >> " + t);
		RefType refType = getWrapperType((PrimType)t);
		List<Type> pTypes = new ArrayList<Type>();
		pTypes.add(t);
		SootMethodRef mf = Scene.v().makeMethodRef(refType.getSootClass(), "valueOf", pTypes, refType, true);
		StaticInvokeExpr ie = Jimple.v().newStaticInvokeExpr(mf, value);
		AssignStmt a = Jimple.v().newAssignStmt(l, ie);
		units.insertBefore(a, s);
		body.getLocals().add(l);
		return l;
	}



	private boolean unwrapConst(Unit s) {
		//$r2 = <Simple01: java.lang.Integer $const$0>		return false;
		List<ValueBox> ub = s.getUseBoxes();
		if(ub.size() != 1) return false;
		Value value = ub.get(0).getValue();
		if(value instanceof StaticFieldRef == false) return false;
		SootField field = ((StaticFieldRef)value).getField();
		String fieldName = field.getName();
		if(fieldName.startsWith("$const$")==false) return false;
		Value c = pack.get(fieldName);
		AssignStmt a = ((AssignStmt)s);
		a.setRightOp(c);
		// set type here
		// using PRIM type of FIELD
		PrimType fieldPrimType = getPrimitiveType((RefType)field.getType());
		System.out.println(a.getLeftOp());
		System.out.println(fieldPrimType);
		((Local)a.getLeftOp()).setType(fieldPrimType);
				// a.getRightOp().getType());
//		System.out.print("unwrapConst, " + a + ": -- ");
//		System.out.print(a.getLeftOp().getType() + ",");
//		System.out.println(a.getRightOp().getType());
//		System.out.println("]] did unwrapConst");
		return true;
	}

	private boolean unwrapBoxOrUnbox(Unit s) {
		// staticinvoke <org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation: java.lang.Object box(int)>(i0)
		List<ValueBox> ub = s.getUseBoxes();
		if(ub.size() != 2) return false;
		Value value = ub.get(1).getValue();
		if(value instanceof StaticInvokeExpr == false) return false;
		StaticInvokeExpr iv = (StaticInvokeExpr)(value);
		SootMethod method = iv.getMethod();
		if(method.getDeclaringClass().equals(Utils.v().transformationClass)) {
			String name = method.getName();
			if(name.equals("box") || name.endsWith("Unbox")) {
				AssignStmt a = (AssignStmt)s;
				a.setRightOp(ub.get(0).getValue());
				if(name.equals("box")) {
					// correcting type
					((Local)a.getLeftOp()).setType(method.getParameterType(0));
				} else { // xxxUnbox, use prim type of method signature
					((Local)a.getLeftOp()).setType(method.getReturnType());
				}
				// System.out.println("]] did unwrapBoxOrUnbox");
				return true;
			}
		}
		return false;
	}

	private boolean extractCallSiteName(Unit s, Value acallsite, String[] names) {
		List<ValueBox> useBoxes = s.getUseBoxes();
		if(useBoxes.size()!=3) return false;
		if(useBoxes.get(0).getValue().equivTo(acallsite)==false) return false;
		int index = ((IntConstant)useBoxes.get(1).getValue()).value;
		callSiteNames.put(s.getDefBoxes().get(0).getValue(),names[index]);
		// System.out.println("]] did extractCallSiteName");
		return true;
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

	private enum BinOp {
		minus,
		plus,
		multiply,
		div,
		leftShift,
		rightShift
	}

	private enum ComparingMethod {
		compareLessThan,
		compareGreaterThan,
		compareLessThanEqual,
		compareGreaterThanEqual
	};

	private ComparingMethod checkCompareMethod(String m) {
		try { return ComparingMethod.valueOf(m); } catch(java.lang.IllegalArgumentException e){return null; }
	}

	private boolean unwrapCompare(Unit s) {
		if(s instanceof JAssignStmt == false) return false;
		JAssignStmt a = (JAssignStmt)s;
		if(a.leftBox.getValue().getType().equals(BooleanType.v())==false) return false;
		if(a.containsInvokeExpr() == false) return false;
		InvokeExpr iv = a.getInvokeExpr();
		SootClass c = iv.getMethod().getDeclaringClass();
		String m = iv.getMethod().getName();
		if(c.equals(Utils.v().scriptBytecodeAdapter)==false) return false;
		ComparingMethod cm = checkCompareMethod(m);
		if(cm == null) return false;
		Unit s1 = units.getSuccOf(s);
		if(s1 instanceof JIfStmt) {
			Value v = s.getUseBoxes().get(0).getValue();
			if(v instanceof StaticFieldRef) {
				StaticFieldRef fr = (StaticFieldRef)v;
				String fName = fr.getField().getName();
				v = pack.get(fName);
			}
			Value v2 = s.getUseBoxes().get(1).getValue();
			if(v2 instanceof StaticFieldRef) {
				StaticFieldRef fr = (StaticFieldRef)v2;
				String fName = fr.getField().getName();
				v2 = pack.get(fName);
			}
			Value condition=null;
			switch(cm) {
				case compareLessThan:
					condition = Jimple.v().newGeExpr(v,v2); break;
				case compareGreaterThan:
					condition = Jimple.v().newLeExpr(v,v2); break;
				case compareLessThanEqual:
					condition = Jimple.v().newGtExpr(v,v2); break;
				case compareGreaterThanEqual:
					condition = Jimple.v().newLtExpr(v,v2); break;
			}
			if(condition==null) return false;
			JIfStmt f = (JIfStmt)s1;
			f.setCondition(condition);
			units.remove(s);
			// System.out.println("]] did unwrapCompare");
			return true;
		}
		return false;
	}


	private boolean unwrapPrimitiveCall(Unit s) {
		BinOp op;
		if((op = containsBinOp(s.getUseBoxes()))!=null) {
			// String name = callSiteNames.get(s.getUseBoxes().get(0).getValue());
			JAssignStmt a1 = (JAssignStmt)s;
			Value v1 = s.getUseBoxes().get(1).getValue();
			Value v2 = s.getUseBoxes().get(2).getValue();
			if(v1.getType() instanceof RefType) {
				System.err.println("unwrapPrimitiveCall, v1 not primitive, " + v1 + ", " + v1.getType());
				//needToUnboxLHS.add((Local)v1);
			}
			if(v2.getType() instanceof RefType) {
				if(v1.getType() instanceof PrimType) {
					AssignStmt a = (AssignStmt)findStatementDefine(v2, s, false);
					unbox2(a, v2, getWrapperType((PrimType)v1.getType()));
				} else {
					System.err.println("unwrapPrimitiveCall, v2 not primitive and not boxable, " + v2 + ", " + v2.getType());
					// needToUnboxRHS.add((Local)v2);
				}
			}
			Expr expr;
			switch(op) {
				case plus:  expr = Jimple.v().newAddExpr(v1, v2);
							a1.setRightOp(expr);
							break;
				case minus: expr = Jimple.v().newSubExpr(v1, v2);
							a1.setRightOp(expr);
							break;
				case multiply: expr = Jimple.v().newMulExpr(v1, v2);
							a1.setRightOp(expr);
							break;
				case div:	expr = Jimple.v().newDivExpr(v1, v2);
							a1.setRightOp(expr);
							break;
				case leftShift: expr = Jimple.v().newShlExpr(v1, v2);
							a1.setRightOp(expr);
							break;
				case rightShift: expr = Jimple.v().newShrExpr(v1, v2);
							a1.setRightOp(expr);
							break;
			}
			// TODO fix here
			((JimpleLocal)a1.getLeftOp()).setType(inferReturnType(v1.getType(),v2.getType()));
			System.out.println("]] did unwrapPrimitiveCall");
			System.out.print(((JimpleLocal)a1.getLeftOp()));
			System.out.print(",L :" + a1.getLeftOp().getType());
			System.out.println(",R :" + a1.getRightOp().getType());
			return true;
		}
		return false;
	}

//	private UnwrappedValue findTarget(Value value, Unit s) {
//		System.out.println(s);
//		Unit n = findStatementDefine(value, s, true);
//		System.out.println(n);
//		if(n!=null) {
//			return new UnwrappedValue(ValueType.UNBOXED,n.getDefBoxes().get(0).getValue(),n);
//		}
//		return null;
//	}
//
	private Unit findStatementDefine(Value value, Unit s) {
		return findStatementDefine(value, s, false);
	}

	private Unit findStatementDefine(Value value, Unit s, final boolean forward) {
		Unit p = s;
		while(p != null){
			Value v2;
			if(forward==false) {
				p = units.getPredOf(p);
				if(p==null) break;
				List<ValueBox> boxes = p.getDefBoxes();
				if(boxes.size()==0) continue;
				v2 = boxes.get(0).getValue();
			}  else {
				p = units.getSuccOf(p);
				if(p==null) break;
				List<ValueBox> boxes = p.getUseBoxes();
				if(boxes.size()==0) continue;
				v2 = boxes.get(0).getValue();
			}
			if(v2.equivTo(value)) {
				return p;
			}
		}
		return null;
	}

//	private UnwrappedValue unwrap(Value value, Unit s) {
//		Unit p = findStatementDefine(value, s);
//		Value v;
//		if((v=getBoxedValue(p))!=null) {
//			return new UnwrappedValue(ValueType.BOXED, v, p);
//		}
//		if((v = getConst(p))!=null) {
//			return new UnwrappedValue(ValueType.CONST, v, p);
//		}
//		if((v = getVar(p)) != null) {
//			return new UnwrappedValue(ValueType.VAR, v, p);
//		}
//		return null;
//	}
//
//	private Value getVar(Unit p) {
//		List<ValueBox> ub = p.getUseBoxes();
//		return ub.get(0).getValue();
//	}
//
//	private Value getConst(Unit p) {
//		List<ValueBox> ub = p.getUseBoxes();
//		if(ub.size() != 1) return null;
//		Value value = ub.get(0).getValue();
//		if(value instanceof StaticFieldRef == false) return null;
//		String fieldName = ((StaticFieldRef)value).getField().getName();
//		if(fieldName.startsWith("$const$")==false) return null;
//		return pack.get(fieldName);
//	}
//
//	private Value getBoxedValue(Unit p) {
//		List<ValueBox> ub = p.getUseBoxes();
//		if(ub.size() != 2) return null;
//		Value value = ub.get(1).getValue();
//		if(value instanceof StaticInvokeExpr == false) return null;
//		StaticInvokeExpr iv = (StaticInvokeExpr)(value);
//		if(iv.getMethodRef().equals(Utils.v().intBoxMethodRef))
//			return ub.get(0).getValue();
//		else
//			return null;
//	}

//	private Type findType(ValueBox valueBox) {
//		return null;
//	}
//
	private Type inferReturnType(Type recv, Type arg) {
		// if it's the same type, no problem
		if(recv.equals(arg)) return recv;
		if(recv.equals(DoubleType.v()) || arg.equals(DoubleType.v())) return DoubleType.v();
		if(recv.equals(FloatType.v()) || arg.equals(FloatType.v())) return FloatType.v();
		if(recv.equals(LongType.v()) || arg.equals(LongType.v())) return LongType.v();
		if(recv.equals(IntType.v()) || arg.equals(IntType.v())) return IntType.v();
		return IntType.v();
	}

	private BinOp containsBinOp(List<ValueBox> useBoxes) {
		if (useBoxes.size() != 4) return null;
		Value ub3 = useBoxes.get(3).getValue();
		if(ub3 instanceof InterfaceInvokeExpr == false) return null;
		InterfaceInvokeExpr iv = (InterfaceInvokeExpr)ub3;
		if(iv.getMethodRef().equals(Utils.v().callSiteBinMethodRef)==false) return null;
		String name = callSiteNames.get(useBoxes.get(0).getValue());
		try {
			return BinOp.valueOf(name);
		}catch(IllegalArgumentException e) {
			return null;
		}
	}

	public boolean isOptimizable() {
		return this.optimizable;
	}

}
