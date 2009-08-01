package org.codehaus.groovy.aop.abstraction.advice;

import groovy.lang.Closure;

import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.Pointcut;

public class TypeAdvice extends Advice {

	public TypeAdvice(Class<?> klass, Object[] args) {
		super(klass, args);
	}

	public TypeAdvice(Object[] args) {
		super(null, args);
	}

	public TypeAdvice(Pointcut pointcut, Closure code) {
		super(pointcut, code);
	}

}
