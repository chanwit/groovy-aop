package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.groovy.gjit.soot.CallSiteArrayPack;

import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.StringConstant;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JNewArrayExpr;

public class CallSiteRecorder extends BodyTransformer {
	
	private static CallSiteRecorder instance=null;

	public static CallSiteRecorder v() {
		if(instance==null) {
			instance = new CallSiteRecorder();
		}
		return instance;
	}	

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {
		if("$createCallSiteArray".equals(b.getMethod().getName())==false) return;
		Iterator<Unit> stmts = b.getUnits().iterator();
		String[] siteName=null;
		while(stmts.hasNext()) {
			Unit s = stmts.next();
			if(s instanceof JAssignStmt) {
				JAssignStmt a = (JAssignStmt)s;
				Value l = a.getLeftOp();
				Value r = a.getRightOp();
				if(r instanceof JNewArrayExpr){
					JNewArrayExpr nae = (JNewArrayExpr)r;
					IntConstant size = (IntConstant)nae.getSize();
					siteName = new String[size.value];
				} else if(r instanceof StringConstant) {
					JArrayRef ar = (JArrayRef)l;
					StringConstant sc = (StringConstant)r;
					siteName[((IntConstant)ar.getIndex()).value] = sc.value;
				}				
			}
		}
		
		String className = b.getMethod().getDeclaringClass().getName();
		CallSiteArrayPack.v().put(className, siteName);		
	}
	
}
