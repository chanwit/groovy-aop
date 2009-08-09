package org.codehaus.groovy.aop.tests.callsite;

import junit.framework.*;

import org.aspectj.lang.JoinPoint;
import org.codehaus.groovy.aop.metaclass.*;

public class CallSiteTests extends TestCase {

    public void testSomething() throws Throwable {
        AspectAwareCallSite a = new AspectAwareCallSite(new MockCallSite());
        assertEquals("delegate.call(0);", a.call(0));
        assertEquals("delegate.call(10);", a.call(10));
    }

}