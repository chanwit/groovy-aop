package org.codehaus.groovy.aop.abstraction.joinpoint;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class SetterJoinpoint implements Joinpoint {

	private Class<?> sender;
	private String property;
	private Object newValue;

	public SetterJoinpoint(Object object, String property, Object newValue) {
		this.sender = object.getClass();
		this.property = property;
		this.newValue = newValue;
	}

	public Class<?> getSender() {
		return sender;
	}

	public String getProperty() {
		return this.property;
	}

	public Object getNewValue() {
		return newValue;
	}

}
