package org.codehaus.groovy.aop.tests.callsite;

import junit.framework.*;
import org.codehaus.groovy.aop.metaclass.*;

public class CallSiteTests extends TestCase {
    
    public void testSomething() throws Throwable {
        AspectAwareCallSite a = new AspectAwareCallSite(new MockCallSite());
        assertEquals("delegate.call(arg0);", a.call(0));
    }

}