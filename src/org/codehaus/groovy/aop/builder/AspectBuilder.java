package org.codehaus.groovy.aop.builder;

import groovy.lang.Closure;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.abstraction.Pointcut;
import org.codehaus.groovy.aop.abstraction.advice.AfterAdvice;
import org.codehaus.groovy.aop.abstraction.advice.AroundAdvice;
import org.codehaus.groovy.aop.abstraction.advice.BeforeAdvice;
import org.codehaus.groovy.aop.abstraction.pcd.GetPCD;
import org.codehaus.groovy.aop.abstraction.pcd.PCD;
import org.codehaus.groovy.aop.abstraction.pcd.PCallPCD;
import org.codehaus.groovy.aop.abstraction.pcd.SetPCD;
import org.codehaus.groovy.aop.abstraction.pcd.WithInPCD;

public class AspectBuilder {

	public static Object buildAspect(Object object, String methodName, Object[] originalArguments) throws AspectCreationFailedException {
		Aspect aspect = null;
		Closure c = null;
		try {
			if (object instanceof Closure) {
				c = (Closure) object;
				aspect = AspectRegistry.v().get((Class<?>)c.getOwner());
			} else {
				aspect = AspectRegistry.v().get(object.getClass());
			}
		} catch (Exception e) {
			throw new AspectCreationFailedException();
		}
		if ("around".equals(methodName)) {
			aspect.add(new AroundAdvice((Class<?>) c.getOwner(), originalArguments));
			return aspect;
		} else if ("before".equals(methodName) || "pre".equals(methodName)) {
			aspect.add(new BeforeAdvice((Class<?>) c.getOwner(), originalArguments));
			return aspect;
		} else if ("after".equals(methodName) || "post".equals(methodName)) {
			aspect.add(new AfterAdvice((Class<?>) c.getOwner(), originalArguments));
			return aspect;
		} else if ("pointcut".equals(methodName)) {
			Closure closure = (Closure) originalArguments[0];
			Pointcut p = new Pointcut((PCD) closure.call());
			return p;
		} else if ("pcall".equals(methodName)) {
			return new PCallPCD(originalArguments);
		} else if ("get".equals(methodName)) {
			return new GetPCD(originalArguments);
		} else if ("set".equals(methodName)) {
			return new SetPCD(originalArguments);
		} else if ("within".equals(methodName)) {
			return new WithInPCD(originalArguments);
		}
		return null;
	}	
}
