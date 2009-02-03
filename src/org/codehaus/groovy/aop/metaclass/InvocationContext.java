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
package org.codehaus.groovy.aop.metaclass;

import org.codehaus.groovy.aop.ProceedNotAllowedException;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

public class InvocationContext extends GroovyObjectSupport {

	private Object called;
	private Object[] args;
	private String methodName;
	
	private Closure proceedClosure;
//	private Closure parProceedClosure;
//	private Stack<Closure> stack;
	
	public Object[] getArgs() {
		return args;
	}
	
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	public Object getCalled() {
		return called;
	}
	
	public void setTarget(Object called) {
		this.called = called;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public void setProceedClosure(Closure closure) {
		this.proceedClosure = closure;
	}
	
	public Object proceed(Object args) throws ProceedNotAllowedException {
		if(this.proceedClosure==null) throw new ProceedNotAllowedException();
		if(args instanceof Object[]) {
			return this.proceedClosure.call((Object[]) args);
		} else {
			return this.proceedClosure.call(args);
		}
	}
		
//    public Object invokeMethod(String name, Object args) {
//    	if("proceed".equals(name)) {
//    		return this.proceedClosure.call((Object[]) args);
//    	} else {
//    		return super.invokeMethod(name, args);
//    	}
//    }

//	public void setProceedStack(Stack<Closure> stack) {
//		this.stack = stack;
//	}
    
 }
