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

import groovy.lang.GroovyObjectSupport;

import java.util.regex.Pattern;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public abstract class AbstractPCD extends GroovyObjectSupport implements PCD {
	
	private String expression;
	private Pattern pattern;
	private int operation;
	private PCD nextNode;
	
	private String wildcardToPattern(String wildcard) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<wildcard.length();i++) {
			char ch = wildcard.charAt(i);
			if(ch=='*' || ch=='+') sb.append('.').append(ch);
			else if(ch=='.')       sb.append('\\').append(ch);
			else sb.append(ch);
		}
		return sb.toString();
	}

//	private Closure matchWithRegExpr(Map map, String methodName) {
//		Closure result=null;
//		if(!map.containsKey(methodName)) {
//			for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
//				String element = (String) iter.next();
//				if(Pattern.compile(wildcardToPattern(element)).matcher(methodName).matches()) {
//					result = (Closure)map.get(element); 
//					break;
//				}
//			}	
//		} else {
//			result = (Closure)map.get(methodName);
//		}
//		return result;
//	}
		
	public void setExpression(String expression) {
		this.expression = wildcardToPattern(expression);
		this.pattern = Pattern.compile(this.expression);
	}

	public Object and(Object target) {
		this.operation = AND;
		this.nextNode = (PCD)target;
		return this;
	}
	
	public Object negate() {
		this.operation = NOT;
		this.nextNode = null;
		return this;
	}

	public Object or(Object target) {
		this.operation = OR;
		this.nextNode = (PCD)target;		
		return this;
	}	

	public boolean matches(Joinpoint jp) {		
		switch(this.operation) {
			case AND: return (nextNode==null?true:nextNode.matches(jp)) && doMatches(this.pattern, jp);
			case OR: return (nextNode==null?false:nextNode.matches(jp)) || doMatches(this.pattern, jp);
			case NOT: return !doMatches(this.pattern, jp);
		}
		return doMatches(this.pattern, jp);
	}

	protected abstract boolean doMatches(Pattern pt, Joinpoint jp);

	public String getExpression() {
		return expression;
	}
		
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((expression == null) ? 0 : expression.hashCode());
		result = PRIME * result + ((nextNode == null) ? 0 : nextNode.hashCode());
		result = PRIME * result + operation;
		result = PRIME * result + ((pattern == null) ? 0 : pattern.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractPCD other = (AbstractPCD) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (nextNode == null) {
			if (other.nextNode != null)
				return false;
		} else if (!nextNode.equals(other.nextNode))
			return false;
		if (operation != other.operation)
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

	public PCD getNextNode() {
		return nextNode;
	}
	
}
