package org.codehaus.groovy.aop.metaclass;

import java.util.List;

import groovy.lang.Closure;
import groovy.lang.MetaMethod;

import org.codehaus.groovy.reflection.CachedClass;

public class AspectAwareMetaMethod extends MetaMethod {
	
	private MetaMethod delegate;
	private List<Closure> beforeClosures;
	private List<Closure> afterClosures;
	
	public AspectAwareMetaMethod(MetaMethod delegate) {
		this.delegate = delegate;
	}

	@Override
	public CachedClass getDeclaringClass() {
		return delegate.getDeclaringClass();
	}

	@Override
	public int getModifiers() {
		return delegate.getModifiers();
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void checkParameters(Class[] arguments) {
		delegate.checkParameters(arguments);
	}

	@Override
	public Object clone() {
		return delegate.clone();
	}

	@Override
	public Object doMethodInvoke(Object object, Object[] argumentArray) {
		return delegate.doMethodInvoke(object, argumentArray);
	}

	@Override
	public String getDescriptor() {
		return delegate.getDescriptor();
	}

	@Override
	public String getMopName() {
		return delegate.getMopName();
	}

	@Override
	public synchronized String getSignature() {
		return delegate.getSignature();
	}

	@Override
	public boolean isCacheable() {
		return delegate.isCacheable();
	}

	@Override
	public boolean isMethod(MetaMethod method) {
		return delegate.isMethod(method);
	}

	@Override
	public boolean isStatic() {
		return delegate.isStatic();
	}

	@Override
	public String toString() {
		return "[AOPAware: " + delegate.toString() + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getReturnType() {
		return delegate.getReturnType();
	}

	@Override
	public Object invoke(Object object, Object[] arguments) {
		for(Closure c: beforeClosures) {
			c.call();
		}
		Object result =  delegate.invoke(object, arguments);
		for(Closure c: afterClosures) {
			c.call();
		}		
		return result;
	}

}
