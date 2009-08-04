package org.codehaus.groovy.aop.builder;

import groovy.lang.Closure;

import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.abstraction.Pointcut;
import org.codehaus.groovy.aop.abstraction.advice.AfterAdvice;
import org.codehaus.groovy.aop.abstraction.advice.AroundAdvice;
import org.codehaus.groovy.aop.abstraction.advice.BeforeAdvice;
import org.codehaus.groovy.aop.abstraction.advice.TypeAdvice;
import org.codehaus.groovy.aop.abstraction.pcd.ArgsPCD;
import org.codehaus.groovy.aop.abstraction.pcd.GetPCD;
import org.codehaus.groovy.aop.abstraction.pcd.PCD;
import org.codehaus.groovy.aop.abstraction.pcd.PCallPCD;
import org.codehaus.groovy.aop.abstraction.pcd.SetPCD;
import org.codehaus.groovy.aop.abstraction.pcd.WithInPCD;

public class AspectBuilder {

    public Aspect aspect;

    public AspectBuilder(Aspect aspect) {
        this.aspect = aspect;
    }

    //
    // Advice declaration
    //
    public Aspect around(Object[] args) {
        aspect.add(new AroundAdvice((Class<?>)aspect.getOwner(), args));
        return aspect;
    }

    public Aspect before(Object[] args) {
        aspect.add(new BeforeAdvice((Class<?>)aspect.getOwner(), args));
        return aspect;
    }

    public Aspect after(Object[] args) {
        aspect.add(new AfterAdvice((Class<?>)aspect.getOwner(), args));
        return aspect;
    }

    public Aspect typing(Object[] args) {
        aspect.add(new TypeAdvice((Class<?>)aspect.getOwner(), args));
        return aspect;
    }

    //
    // PCD declaration
    //
    public Object pointcut(Closure closure) {
        Pointcut p = new Pointcut((PCD)closure.call());
        return p;
    }

    public Object get(Object[] args) {
        return new GetPCD(args);
    }

    public Object set(Object[] args) {
        return new SetPCD(args);
    }

    public Object pcall(Object[] args) {
        return new PCallPCD(args);
    }

    public Object call(Object[] args) {
        return new PCallPCD(args);
    }

    public Object within(Object[] args) {
        return new WithInPCD(args);
    }

    public Object args(Object[] args) {
        return new ArgsPCD(args);
    }

    //
    // Support for named variable binding (via the args PCD)
    //
    public Object propertyMissing(String name) {
        // TODO return a proper type
        return name;
    }

}
