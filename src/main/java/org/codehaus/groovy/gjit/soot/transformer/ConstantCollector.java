package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.groovy.gjit.soot.ConstantHolder;
import org.codehaus.groovy.gjit.soot.ConstantHolder.ConstantPack;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Transformer;
import soot.Unit;
import soot.Value;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JimpleLocal;

public class ConstantCollector extends BodyTransformer {

	private static ConstantCollector instance;

	private ConstantCollector() {}

	public static ConstantCollector v() {
		if(instance==null) {
			instance = new ConstantCollector();
		}
		return instance;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {
		if(b.getMethod().getName().equals("<clinit>")) {
			System.out.println("Collecting constants");
			ConstantPack pack = new ConstantPack();
			PatchingChain<Unit> units = b.getUnits();
			Iterator<Unit> stmts = units.snapshotIterator();
			while(stmts.hasNext()) {
				Unit s = stmts.next();
				if(s instanceof JInvokeStmt) {
					JInvokeStmt siv = (JInvokeStmt)s;
					String constName = findConstantName(units, s);
					if(constName.startsWith("$const$")) {
						pack.put(constName, siv.getInvokeExpr().getArg(0));
					}
				}
			}
			ConstantHolder.v().put(b.getMethod().getDeclaringClass().getName(),pack);
		}
	}

	private String findConstantName(PatchingChain<Unit> units, Unit s) {
		JInvokeStmt siv = (JInvokeStmt)s;
		JSpecialInvokeExpr ss = (JSpecialInvokeExpr) siv.getInvokeExpr();
		JimpleLocal lhr = (JimpleLocal) ss.getBase();
		Unit cur = s;
		while(cur != null) {
			cur = units.getSuccOf(cur);
			if(cur instanceof JAssignStmt) {
				JAssignStmt a = (JAssignStmt)cur;
				Value l = a.getLeftOp();
				Value r = a.getRightOp();
				if(r instanceof JCastExpr) {
					JCastExpr c = (JCastExpr)r;
					if(lhr.equivTo(c.getOp())) {
						lhr = (JimpleLocal) a.getLeftOp();
					}
				} else if(l instanceof StaticFieldRef){
					StaticFieldRef ref = (StaticFieldRef)l;
					if(lhr.equivTo(r)) {
						return ref.getField().getName();
					}
				}
			}
		}
		return null;
	}


}
