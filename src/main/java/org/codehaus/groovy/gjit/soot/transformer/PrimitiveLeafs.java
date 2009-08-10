package org.codehaus.groovy.gjit.soot.transformer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.gjit.soot.ConstantHolder;
import org.codehaus.groovy.gjit.soot.ConstantHolder.ConstantPack;

import soot.Body;
import soot.BodyTransformer;
import soot.IntType;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JimpleLocal;

public class PrimitiveLeafs extends BodyTransformer {

	private static PrimitiveLeafs instance = new PrimitiveLeafs();

	private PrimitiveLeafs() {
	}

	public static PrimitiveLeafs v() {
		return instance;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {

		//if(b.getMethod().getName().equals("expr_001")==false) return;

		Set<JimpleLocal> leafs = new HashSet<JimpleLocal>();
		HashMap<JimpleLocal, JimpleLocal> boxedFromInts = new HashMap<JimpleLocal, JimpleLocal>();
		HashMap<JimpleLocal, Value> constants = new HashMap<JimpleLocal, Value>();
		ConstantPack constPack = ConstantHolder.v().get(b.getMethod().getDeclaringClass().getName());

		PatchingChain<Unit> units = b.getUnits();
		Iterator<Unit> stmts = units.snapshotIterator();
		while(stmts.hasNext()) {
			Unit stmt = stmts.next();

			recordLeafs(stmt, leafs);
			recordBoxedFromInts(stmt, boxedFromInts);
			recordConstants(stmt, constants, constPack);

			if(isBinaryOperation(stmt)) {
				JAssignStmt a = (JAssignStmt)stmt;
				JInterfaceInvokeExpr iv = (JInterfaceInvokeExpr)a.getInvokeExpr();
				if(isLeafCall(iv, leafs)) {
					Value[] olds = new Value[]{iv.getArg(0), iv.getArg(1)};
					Value[] args = isConvertableToPrimitive(iv, boxedFromInts, constants);
					if(args != null) {
						SootClass c = Scene.v().loadClass("org.codehaus.groovy.runtime.callsite.CallSite", SootClass.SIGNATURES);
						/// TODO generalize types
						SootMethod m = c.getMethod("java.lang.Object call(int,int)");
						iv.setMethodRef(m.makeRef());
						iv.setArg(0, args[0]);
						iv.setArg(1, args[1]);
						cleanIntBoxes(units, stmt, olds);
					}
				}
			}
		}
	}

	private void cleanIntBoxes(PatchingChain<Unit> units, Unit stmt,
			Value[] olds) {
		Unit p = stmt;
		Unit done1=null,done2 = null;
		while(true) {
			p = units.getPredOf(p); if(p==null) break;
			try {
				if(p.getDefBoxes().get(0).getValue().equivTo(olds[0])) {
					done1 = p;
					continue;
				} else if(p.getDefBoxes().get(0).getValue().equivTo(olds[1])) {
					done2 = p;
					continue;
				}
				if(done1!=null && done2!=null) {
					units.remove(done1);
					units.remove(done2);
					break;
				}
			} catch(IndexOutOfBoundsException e) {
				continue;
			}
		}
	}

	private Value[] isConvertableToPrimitive(JInterfaceInvokeExpr iv,
			HashMap<JimpleLocal, JimpleLocal> boxedFromInts,
			HashMap<JimpleLocal,Value> constants) {
		// TODO more criteria
		// 1. use primitive argument
		// 2. use wrapped constant
		// 3. ?
		JimpleLocal arg0 = (JimpleLocal)iv.getArg(0);
		JimpleLocal arg1 = (JimpleLocal)iv.getArg(1);
		Value[] v = new Value[2];
		if(!boxedFromInts.containsKey(arg0) && !constants.containsKey(arg0) ) return null;
		if(boxedFromInts.containsKey(arg0)) {
			v[0] = boxedFromInts.get(arg0);
		} else {
			v[0] = constants.get(arg0);
		}
		if(!boxedFromInts.containsKey(arg1) && !constants.containsKey(arg1) ) return null;
		if(boxedFromInts.containsKey(arg1)) {
			v[1] = boxedFromInts.get(arg1);
		} else {
			v[1] = constants.get(arg1);
		}
		return v;
	}

	private void recordConstants(Unit stmt,HashMap<JimpleLocal, Value> constants,ConstantPack pack) {
		if(stmt instanceof JAssignStmt) {
			JAssignStmt a = (JAssignStmt)stmt;
			if(a.getRightOp() instanceof StaticFieldRef) {
				StaticFieldRef r = (StaticFieldRef)a.getRightOp();
				String name = r.getField().getName();
				if(name.startsWith("$const$")) {
					Value v = pack.get(name);
					// System.out.println(name + ": " + v);
					constants.put((JimpleLocal)a.getLeftOp(), v);
				}
			}
		}
	}

	private static final String BOX_METHOD = "box";
	private static final String TRANSFORMATION_CLASS = "org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation";

	private void recordBoxedFromInts(Unit stmt, HashMap<JimpleLocal, JimpleLocal> boxedFromInts) {
		if(stmt instanceof JAssignStmt) {
			JAssignStmt a = (JAssignStmt)stmt;
			if(a.containsInvokeExpr()) {
				InvokeExpr iv = a.getInvokeExpr();
				if(iv instanceof JStaticInvokeExpr) {
					String c = iv.getMethodRef().declaringClass().getName();
					String m = iv.getMethod().getName();
					if(!c.equals(TRANSFORMATION_CLASS)) return;
					if(!m.equals(BOX_METHOD)) return;
					Type t = iv.getMethod().getParameterType(0);
					// generalise this
					if(!t.equals(IntType.v())) return;
					boxedFromInts.put((JimpleLocal)a.getLeftOp(), (JimpleLocal)iv.getArg(0));
				}
			}
		}
	}

	private static final String CALLSITE_CLASS = "org.codehaus.groovy.runtime.callsite.CallSite";
	private static final String CALLSITE_CALL_METHOD = "call";

	private boolean isBinaryOperation(Unit stmt) {
		if(stmt instanceof JIdentityStmt) return false;
		if(stmt instanceof JAssignStmt) return isBinOp((JAssignStmt)stmt);
		return false;
	}

	private boolean isBinOp(JAssignStmt stmt) {
		if(stmt.containsInvokeExpr()) {
			InvokeExpr iv = stmt.getInvokeExpr();
			if(iv instanceof JInterfaceInvokeExpr) {
				SootMethod m = iv.getMethod();
				String cname = iv.getMethodRef().declaringClass().getName();
				if(cname.equals(CALLSITE_CLASS) &&
					m.getName().equals(CALLSITE_CALL_METHOD) && (m.getParameterCount() == 2)) {
					return true;
				}
			}
		}
		return false;
	}

	private void recordLeafs(Unit stmt, Set<JimpleLocal> leafs) {
		if(stmt instanceof JIdentityStmt) {
			if(((JIdentityStmt)stmt).getLeftOp() instanceof JimpleLocal) {
				leafs.add((JimpleLocal)((JIdentityStmt)stmt).getLeftOp());
			}
		} else if(stmt instanceof JAssignStmt) {
			JAssignStmt a = (JAssignStmt)stmt;
			if(a.getLeftOp() instanceof JimpleLocal) {
				if(a.containsInvokeExpr()) {
					String c = a.getInvokeExpr().getMethodRef().declaringClass().getName();
					String m = a.getInvokeExpr().getMethod().getName();
					if(!(c.equals(CALLSITE_CLASS) && m.equals(CALLSITE_CALL_METHOD))) {
						leafs.add((JimpleLocal) a.getLeftOp());
					}
				} else { // TODO more conditions here
					leafs.add((JimpleLocal) a.getLeftOp());
				}
			}
		}
	}

	private boolean isLeafCall(JInterfaceInvokeExpr iv, Set<JimpleLocal> leafs) {
		if(leafs.contains(iv.getArg(0)) && leafs.contains(iv.getArg(1))) return true;
		return false;

	}

}
