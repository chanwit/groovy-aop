package org.codehaus.groovy.aop.metaclass;

import org.codehaus.groovy.aop.metaclass.AspectAwareCallSite;

public aspect CallSiteAspect {
    
    pointcut pc(): execution(Object AspectAwareCallSite.call*(..));
    Object around(): pc() {
        return proceed();
    }
}