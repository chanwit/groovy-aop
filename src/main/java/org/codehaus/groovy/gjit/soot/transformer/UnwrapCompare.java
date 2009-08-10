package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.groovy.gjit.soot.ConstantHolder;
import org.codehaus.groovy.gjit.soot.ConstantHolder.ConstantPack;

import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.PatchingChain;
import soot.Unit;
import soot.Value;
import soot.jimple.CmplExpr;
import soot.jimple.GeExpr;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.LtExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JIfStmt;

public class UnwrapCompare extends BodyTransformer {

//	optimizing condition "i0 is int" and "$const$0 is integer" :
//		$r5 = staticinvoke <org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation: java.lang.Object box(int)>(i0)
//		$r2 = <Fib: java.lang.Integer $const$0>
//		$z0 = staticinvoke <org.codehaus.groovy.runtime.ScriptBytecodeAdapter: boolean compareLessThan(java.lang.Object,java.lang.Object)>($r5, $r2)
//
	// TODO put more comparing methods here
	private enum ComparingMethod {
			compareLessThan,
			compareGreaterThan,
			compareLessThanEqual,
			compareGreaterThanEqual
		};

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {

		ConstantPack pack = ConstantHolder.v().get(b.getMethod().getDeclaringClass().getName());

		PatchingChain<Unit> u = b.getUnits();
		Iterator<Unit> stmts = u.snapshotIterator();
		while(stmts.hasNext()) {
			Unit s = stmts.next();
			// System.out.println(s);
			if(s instanceof JAssignStmt) {
				JAssignStmt a = (JAssignStmt)s;
				if(a.leftBox.getValue().getType().equals(BooleanType.v())==false) continue;
				if(a.containsInvokeExpr() == false) continue;
				InvokeExpr iv = a.getInvokeExpr();
				String c = iv.getMethod().getDeclaringClass().getName();
				String m = iv.getMethod().getName();
				// TODO use MethodRef instead == faster
				if(c.equals("org.codehaus.groovy.runtime.ScriptBytecodeAdapter")==false) continue;
				ComparingMethod cm = checkCompareMethod(m);
				if(cm == null) continue;
				Unit s1 = u.getSuccOf(s);
				if(s1 instanceof JIfStmt) {
//					Unit p1 = u.getPredOf(s);   // const
//					Unit p2 = u.getPredOf(p1);  // box(i0)
					// TODO if(p1 and p2 is not the same type) continue
					Value v = s.getUseBoxes().get(0).getValue();
					if(v instanceof StaticFieldRef) {
						StaticFieldRef fr = (StaticFieldRef)v;
						String fName = fr.getField().getName();
						v = pack.get(fName);
					}
					Value v2 = s.getUseBoxes().get(2).getValue();
					if(v2 instanceof StaticFieldRef) {
						StaticFieldRef fr = (StaticFieldRef)v2;
						String fName = fr.getField().getName();
						v2 = pack.get(fName);
					}

					Value condition=null;
					switch(cm) {
						case compareLessThan: condition = Jimple.v().newGeExpr(v2,v); break;
						case compareGreaterThan: condition = Jimple.v().newLeExpr(v2,v); break;
						case compareLessThanEqual: condition = Jimple.v().newGtExpr(v2,v); break;
						case compareGreaterThanEqual: condition = Jimple.v().newLtExpr(v2,v); break;
					}
					if(condition==null) continue;
					JIfStmt f = (JIfStmt)s1;
					f.setCondition(condition);
					u.remove(s);
//					u.remove(p1);
//					u.remove(p2);
				}
			}
		}
	}

	private ComparingMethod checkCompareMethod(String m) {
		try { return ComparingMethod.valueOf(m); } catch(java.lang.IllegalArgumentException e){return null; }
	}

}
