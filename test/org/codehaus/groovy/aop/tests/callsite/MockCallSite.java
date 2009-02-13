package org.codehaus.groovy.aop.tests.callsite;

import groovy.lang.GroovyObject;

import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

public class MockCallSite implements CallSite {
    
    @Override
    public Object call(Object arg0) throws Throwable {
        return "delegate.call(arg0);";
    }

    @Override
    public Object call(Object arg0, Object[] arg1) throws Throwable {
        return "delegate.call(arg0, arg1);";
    }

    @Override
    public Object call(Object arg0, Object arg1) throws Throwable {
        return "delegate.call(arg0, arg1);";
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2) throws Throwable {
        return "delegate.call(arg0, arg1, arg2);";
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2, Object arg3)
            throws Throwable {
        return "delegate.call(arg0, arg1, arg2, arg3);";
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2, Object arg3,
            Object arg4) throws Throwable {
        return "delegate.call(arg0, arg1, arg2, arg3, arg4);";
    }

    @Override
    public Object callConstructor(Object arg0) throws Throwable {
        return "delegate.callConstructor(arg0);";
    }

    @Override
    public Object callConstructor(Object arg0, Object[] arg1)
            throws Throwable {
        return "delegate.callConstructor(arg0, arg1);";
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1)
            throws Throwable {
        return "delegate.callConstructor(arg0, arg1);";
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2)
            throws Throwable {
        return "delegate.callConstructor(arg0, arg1, arg2);";
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2,
            Object arg3) throws Throwable {
        return "delegate.callConstructor(arg0, arg1, arg2);";
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2,
            Object arg3, Object arg4) throws Throwable {
        return "delegate.callConstructor(arg0, arg1, arg2, arg3, arg4);";
    }

    @Override
    public Object callCurrent(GroovyObject arg0) throws Throwable {
        return "delegate.callCurrent(arg0);";
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object[] arg1)
            throws Throwable {
        return "delegate.callCurrent(arg0, arg1);";
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1)
            throws Throwable {
        return "delegate.callCurrent(arg0, arg1);";
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2)
            throws Throwable {
        return "delegate.callCurrent(arg0, arg1, arg2);";
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2,
            Object arg3) throws Throwable {
        return "delegate.callCurrent(arg0, arg1, arg2, arg3);";
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2,
            Object arg3, Object arg4) throws Throwable {
        return "delegate.callCurrent(arg0, arg1, arg2, arg3, arg4);";
    }

    @Override
    public Object callGetProperty(Object arg0) throws Throwable {
        return "delegate.callGetProperty(arg0);";
    }

    @Override
    public Object callGetPropertySafe(Object arg0) throws Throwable {
         return "delegate.callGetPropertySafe(arg0);";
    }

    @Override
    public Object callGroovyObjectGetProperty(Object arg0) throws Throwable {
        return "delegate.callGroovyObjectGetProperty(arg0);";
    }

    @Override
    public Object callGroovyObjectGetPropertySafe(Object arg0)
            throws Throwable {
        return "delegate.callGroovyObjectGetPropertySafe(arg0);";
    }

    @Override
    public Object callSafe(Object arg0) throws Throwable {
        return "delegate.callSafe(arg0);";
    }

    @Override
    public Object callSafe(Object arg0, Object[] arg1) throws Throwable {
        return "delegate.callSafe(arg0, arg1);";
    }

    @Override
    public Object callSafe(Object arg0, Object arg1) throws Throwable {
        return "delegate.callSafe(arg0, arg1);";
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2)
            throws Throwable {
        return "delegate.callSafe(arg0, arg1, arg2);";
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2,
            Object arg3) throws Throwable {
        return "delegate.callSafe(arg0, arg1, arg2, arg3);";
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2,
            Object arg3, Object arg4) throws Throwable {
        return "delegate.callSafe(arg0, arg1, arg2, arg3, arg4);";
    }

    @Override
    public Object callStatic(Class arg0) throws Throwable {
        return "delegate.callStatic(arg0);";
    }

    @Override
    public Object callStatic(Class arg0, Object[] arg1) throws Throwable {
        return "delegate.callStatic(arg0, arg1);";
    }

    @Override
    public Object callStatic(Class arg0, Object arg1) throws Throwable {
        return "delegate.callStatic(arg0, arg1);";
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2)
            throws Throwable {
        return "delegate.callStatic(arg0, arg1, arg2);";
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2,
            Object arg3) throws Throwable {
        return "delegate.callStatic(arg0, arg1, arg2, arg3);";
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2,
            Object arg3, Object arg4) throws Throwable {
        return "delegate.callStatic(arg0, arg1, arg2, arg3, arg4);";
    }

    //
    // properties
    //
    @Override
    public CallSiteArray getArray() {
        return null;
    }

    @Override
    public int getIndex() {
        return 1;
    }

    @Override
    public String getName() {
        return "mock";
    }

    @Override
    public Object getProperty(Object arg0) throws Throwable {
        return null;
    }

    @Override
    public AtomicInteger getUsage() {
        return null;
    }

}
