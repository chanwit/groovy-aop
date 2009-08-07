package org.codehaus.groovy.aop.builder;

public class Symbol {

	private String string;

	public Symbol(String string) {
		this.string = string;
	}

	public Object propertyMissing(String propName) {
		this.string = this.string + "." + propName;
		return this;
	}

	@Override
	public String toString() {
		return this.string;
	}

}
