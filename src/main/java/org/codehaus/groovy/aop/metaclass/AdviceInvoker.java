
package org.codehaus.groovy.aop.metaclass;

import groovy.lang.GroovyObject;

import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.weaver.tools.PointcutParser;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

public class AdviceInvoker {
    
    private CallSite delegate;
    private Closure[] before;
    private Closure[] after;
    
    public AdviceInvoker (CallSite delegate, EffectiveAdvices ea) {
        this.delegate = delegate;
        before = ea.getBeforeClosureArray();
        after =  ea.getAfterClosureArray();
    }


    @Override
    public Object call(Object arg0, Object[] arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.call(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object call(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.call(arg0);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object call(Object arg0, Object arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.call(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.call(arg0, arg1, arg2);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.call(arg0, arg1, arg2, arg3);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.call(arg0, arg1, arg2, arg3, arg4);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callConstructor(Object arg0, Object[] arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callConstructor(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callConstructor(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callConstructor(arg0);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callConstructor(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callConstructor(arg0, arg1, arg2);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callConstructor(arg0, arg1, arg2, arg3);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callConstructor(arg0, arg1, arg2, arg3, arg4);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object[] arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callCurrent(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callCurrent(GroovyObject arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callCurrent(arg0);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callCurrent(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callCurrent(arg0, arg1, arg2);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callCurrent(arg0, arg1, arg2, arg3);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callCurrent(arg0, arg1, arg2, arg3, arg4);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callSafe(Object arg0, Object[] arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callSafe(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callSafe(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callSafe(arg0);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callSafe(Object arg0, Object arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callSafe(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callSafe(arg0, arg1, arg2);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callSafe(arg0, arg1, arg2, arg3);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callSafe(arg0, arg1, arg2, arg3, arg4);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callStatic(Class arg0, Object[] arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callStatic(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callStatic(Class arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callStatic(arg0);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callStatic(Class arg0, Object arg1) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callStatic(arg0, arg1);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callStatic(arg0, arg1, arg2);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callStatic(arg0, arg1, arg2, arg3);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.callStatic(arg0, arg1, arg2, arg3, arg4);
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
            }
        }
        return result;  
    }

    @Override
    public Object callGetProperty(Object arg0) throws Throwable {
        for(int i = 0; i < before.count; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGetProperty(arg0);
        for(int i = 0; i < after.count; i++) {
            after[i].call(context);
        }      
        return result;
    }

    @Override
    public Object callGetPropertySafe(Object arg0) throws Throwable {
        for(int i = 0; i < before.count; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGetPropertySafe(arg0);
        for(int i = 0; i < after.count; i++) {
            after[i].call(context);
        }      
        return result;
    }

    @Override
    public Object callGroovyObjectGetProperty(Object arg0) throws Throwable {
        for(int i = 0; i < before.count; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGroovyObjectGetProperty(arg0);
        for(int i = 0; i < after.count; i++) {
            after[i].call(context);
        }      
        return result;
    }

    @Override
    public Object callGroovyObjectGetPropertySafe(Object arg0) throws Throwable {
        for(int i = 0; i < before.count; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGroovyObjectGetPropertySafe(arg0);
        for(int i = 0; i < after.count; i++) {
            after[i].call(context);
        }      
        return result;
    }

}

