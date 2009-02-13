package org.codehaus.groovy.aop.metaclass;

import org.codehaus.groovy.aop.metaclass.AspectAwareCallSite;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.cache.AdviceCacheL1;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;

import org.codehaus.groovy.aop.abstraction.*;
import org.codehaus.groovy.aop.abstraction.joinpoint.*;

public aspect CallSiteAspect {
    
    // Join point matcher
    private Matcher matcher = new Matcher(AspectRegistry.v(), AdviceCacheL1.v(), AdviceCacheL2.v());
    
    private pointcut pc(): execution(Object AspectAwareCallSite.call*(..));
    
    before(): pc() {        
        String name = thisJoinPoint.getSignature().getName();
        // classify name
        // - call
        // - callConstructor
        // - etc.
        
        // do matching
        AspectAwareCallSite aacs = (AspectAwareCallSite)thisJoinPoint.getThis();
        EffectiveAdvices effAdvices = new EffectiveAdvices();
        matcher.matchPerClass(effAdvices, cjp);
        // execute and expose jp info to closure
        // effAdvices
    }
    Object around(): pc() {        
        AspectAwareCallSite aacs = (AspectAwareCallSite)thisJoinPoint.getThis();
        System.out.println(thisJoinPoint.getArgs());
        return proceed();
    }
    after(): pc() {
        AspectAwareCallSite aacs = (AspectAwareCallSite)thisJoinPoint.getThis();
    }
    after() returning(Object r): pc() {
        AspectAwareCallSite aacs = (AspectAwareCallSite)thisJoinPoint.getThis();
    }
}