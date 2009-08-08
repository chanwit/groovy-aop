package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.IntType;
import soot.PatchingChain;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JCastExpr;

public class BoxCastUnboxEliminator extends BodyTransformer {
//  optimize this:
//		$r6 = staticinvoke <org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation: java.lang.Object box(int)>(i0)
//		$r7 = staticinvoke <Fib: java.lang.Class $get$$class$java$lang$Integer()>()
//		$r8 = staticinvoke <org.codehaus.groovy.runtime.ScriptBytecodeAdapter: java.lang.Object castToType(java.lang.Object,java.lang.Class)>($r6, $r7)
//		$r9 = (java.lang.Integer) $r8
//		$i1 = staticinvoke <org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation: int intUnbox(java.lang.Object)>($r9)
// to be:
//		$i1 = i0	

	private static final String JAVA_LANG_INTEGER = "$get$$class$java$lang$Integer";
	private static final String BOX_METHOD = "box";
	private static final String DEFAULT_TYPE_TRANSFORMATION = "org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation";

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {
		PatchingChain<Unit> units = b.getUnits();
		Iterator<Unit> stmts = units.snapshotIterator();
		while(stmts.hasNext()) {
			Unit s0 = stmts.next();
			List<ValueBox> useBoxes = s0.getUseBoxes();
			if(useBoxes.size()!=2) continue; 
			Value i0 = useBoxes.get(0).getValue();
			if(i0.getType().equals(IntType.v())) {				
				if(s0 instanceof JAssignStmt == false) continue;				
				JAssignStmt a = (JAssignStmt)s0;
				if(a.containsInvokeExpr()==false) continue;
				InvokeExpr iv = a.getInvokeExpr();
				String c = iv.getMethod().getDeclaringClass().getName();
				String m = iv.getMethod().getName();
				if(c.equals(DEFAULT_TYPE_TRANSFORMATION) && m.equals(BOX_METHOD)) {
					try {
						Unit s1 = units.getSuccOf(s0);
						JAssignStmt a1 = (JAssignStmt)s1;
						if(a1.getInvokeExpr().getMethod().getName().equals(JAVA_LANG_INTEGER)) {
							Unit s2 = units.getSuccOf(s1);
							List<ValueBox> useBoxes2 = s2.getUseBoxes();
							if(useBoxes2.size() != 3) continue;
							if(useBoxes2.get(0).getValue().equivTo(s0.getDefBoxes().get(0).getValue()) == false) continue;
							if(useBoxes2.get(1).getValue().equivTo(s1.getDefBoxes().get(0).getValue()) == false) continue;
							Unit s3 = units.getSuccOf(s2);
							JAssignStmt a3 = (JAssignStmt)s3;
							if(a3.getRightOp() instanceof JCastExpr == false) continue;
							Unit s4 = units.getSuccOf(s3);
							if(s4.getDefBoxes().get(0).getValue().getType().equals(IntType.v()) == false) continue;
							JAssignStmt a4 = (JAssignStmt)s4;
							a4.setRightOp(i0);
							units.remove(s0);
							units.remove(s1);
							units.remove(s2);
							units.remove(s3);
						} else if(a1.getInvokeExpr().getMethodRef().equals(Utils.v().intUnboxMethodRef)) {
							if(s1.getUseBoxes().get(0).getValue().equivTo(s0.getDefBoxes().get(0).getValue()) == false) continue;
							a1.setRightOp(i0);
							units.remove(s0);
						}
					} catch(Exception e) { continue; }
				}
			}
		}
	}

}
