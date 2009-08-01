package org.codehaus.groovy.aop.abstraction.pcd;

import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.SetterJoinpoint;

public class SetPCD extends AbstractPCD {

	public SetPCD(Object[] args) {
		if(args.length==1) {
			if(args[0] instanceof String)
				setExpression((String)args[0]);
			else if(args[0] instanceof Map)
				// System.out.println("not implemented");
				// TODO convert this to wild card
				// ((Map)args[0]).entrySet().
				setExpression(((Map)args[0]).toString());
		} else {
			System.out.println("not implemented");
		}		
	}

	@Override
	protected boolean doMatches(Pattern pt, Joinpoint jp) {
		if(jp instanceof SetterJoinpoint == false) return false;
		SetterJoinpoint gjp=(SetterJoinpoint)jp;
		Class<?> sender = gjp.getSender();
		String property = gjp.getProperty();
		return pt.matcher(sender.getCanonicalName()+"."+property).matches();
	}

}
