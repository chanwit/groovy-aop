/**
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Chanwit Kaewkasi
 *
 **/
package org.codehaus.groovy.aop.abstraction.pcd;

import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.CallJoinpoint;

public class PCallPCD extends AbstractPCD {

	public PCallPCD(Object[] args) {
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

	protected boolean doMatches(Pattern pattern, Joinpoint jp) {
		if(jp instanceof CallJoinpoint == false) return false;
		CallJoinpoint cjp=(CallJoinpoint)jp;
		Class receiverClass = cjp.getReceiverClass();
		String methodName = cjp.getMethodName();
		return pattern.matcher(receiverClass.getCanonicalName()+"."+methodName).matches();
	}	

}
