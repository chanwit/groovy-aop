package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.soot.CallSiteNameHolder;

import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.StringConstant;

public class CallSiteNameCollector extends BodyTransformer {

    @Override
    protected void internalTransform(Body b, String phaseName, Map options) {

        if (b.getMethod().getName().equals("$createCallSiteArray") == false) return;

        String[] callSiteNames = null;
        for (Unit u : b.getUnits()) {
            if (u instanceof AssignStmt == false) continue;

            AssignStmt a = (AssignStmt) u;
            if (a.getRightOp() instanceof NewArrayExpr) {
                NewArrayExpr right = (NewArrayExpr) a.getRightOp();
                callSiteNames = new String[((IntConstant) right.getSize()).value];
            } else if ((a.getLeftOp()  instanceof ArrayRef) &&
                       (a.getRightOp() instanceof StringConstant)
            ) {
                ArrayRef left = (ArrayRef) a.getLeftOp();
                StringConstant right = (StringConstant) a.getRightOp();
                int index = ((IntConstant) left.getIndex()).value;
                callSiteNames[index] = right.value;
            }

        }

        if (callSiteNames != null)
            CallSiteNameHolder.v().put(b.getMethod().getDeclaringClass().getName(), callSiteNames);

    }
}
