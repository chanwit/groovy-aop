package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.soot.CallsiteNameHolder;

import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.StringConstant;

public class CallsiteNameCollector extends BodyTransformer {

    @Override
    protected void internalTransform(Body b, String phaseName, Map options) {
        if (b.getMethod().getName().equals("$createCallSiteArray") == false) return;

        String[] callsitenames = null;
        for (Unit u : b.getUnits()) {
            if (u instanceof AssignStmt == false) continue;

            AssignStmt a = (AssignStmt) u;
            if (a.getRightOp() instanceof NewArrayExpr) {
                NewArrayExpr right = (NewArrayExpr) a.getRightOp();
                callsitenames = new String[((IntConstant) right.getSize()).value];
            } else if ((a.getLeftOp() instanceof ArrayRef) &&
                    (a.getRightOp() instanceof StringConstant)) {
                ArrayRef left = (ArrayRef) a.getLeftOp();
                StringConstant right = (StringConstant) a.getRightOp();
                int index = ((IntConstant) left.getIndex()).value;
                callsitenames[index] = right.value;
            }

        }

        if (callsitenames != null) {
            CallsiteNameHolder.v().put(b.getMethod().getDeclaringClass(), callsitenames);
        }
    }
}
