package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.gjit.soot.GroovyJimpleBody;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.PrimType;
import soot.Type;
import soot.Unit;


public class PrimitiveBinOps extends BodyTransformer {

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {
		boolean go = false;
		go = b.getMethod().getReturnType() instanceof PrimType;
		List<Type> pTypes = b.getMethod().getParameterTypes();
		for (Type t : pTypes) {
			if(t instanceof PrimType) {
				go = true;
				break;
			}
		}
		go = go || (b.getMethod().getName().equals("main") && b.getMethod().isStatic() == true);		
		if(go == false) return;

		GroovyJimpleBody gBody = new GroovyJimpleBody(b);
		if(gBody.isOptimizable()==false) return;
		
		PatchingChain<Unit> u = b.getUnits();
		Iterator<Unit> stmts = u.snapshotIterator();
		while (stmts.hasNext()) {
			Unit s = stmts.next();
			System.out.println(s);
		}		
	}



}
