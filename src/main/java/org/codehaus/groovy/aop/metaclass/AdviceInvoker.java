
package org.codehaus.groovy.aop.metaclass;

import groovy.lang.GroovyObject;
import groovy.lang.Closure;
import groovy.lang.MissingMethodException;

import org.codehaus.groovy.aop.ProceedNotAllowedException;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.codehaus.groovy.runtime.callsite.CallSite;

public class AdviceInvoker {

    private CallSite delegate;
    private Closure[] before;
    private Closure[] after;
    private Closure[] around;
    private int callIndex;
    private InvocationContext context;
    
    public AdviceInvoker (CallSite delegate, EffectiveAdvices ea, InvocationContext context, int callIndex) {
        this.delegate = delegate;
        this.callIndex = callIndex;
        this.context =  context;
        before = ea.getBeforeClosureArray();
        after  = ea.getAfterClosureArray();
        around = ea.getAroundClosureArray();        
    }



    public Object call(Object arg0, Object[] arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(arg1);
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 1) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 1;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.call(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object call(Object arg0) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 2) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 2;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.call(arg0);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object call(Object arg0, Object arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 3) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 3;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.call(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object call(Object arg0, Object arg1, Object arg2) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1, arg2});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 4) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 4;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.call(arg0, arg1, arg2);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object call(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1, arg2, arg3});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 5) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 5;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.call(arg0, arg1, arg2, arg3);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object call(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1, arg2, arg3, arg4});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 6) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 6;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.call(arg0, arg1, arg2, arg3, arg4);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callConstructor(Object arg0, Object[] arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(arg1);
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 7) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 7;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callConstructor(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callConstructor(Object arg0) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 8) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 8;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callConstructor(arg0);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callConstructor(Object arg0, Object arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 9) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 9;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callConstructor(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callConstructor(Object arg0, Object arg1, Object arg2) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 10) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 10;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callConstructor(arg0, arg1, arg2);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callConstructor(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2, arg3});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 11) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 11;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callConstructor(arg0, arg1, arg2, arg3);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callConstructor(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2, arg3, arg4});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 12) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 12;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callConstructor(arg0, arg1, arg2, arg3, arg4);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callCurrent(GroovyObject arg0, Object[] arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(arg1);
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 13) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 13;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callCurrent(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callCurrent(GroovyObject arg0) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 14) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 14;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callCurrent(arg0);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callCurrent(GroovyObject arg0, Object arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 15) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 15;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callCurrent(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1, arg2});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 16) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 16;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callCurrent(arg0, arg1, arg2);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1, arg2, arg3});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 17) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 17;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callCurrent(arg0, arg1, arg2, arg3);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg1, arg2, arg3, arg4});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 18) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 18;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callCurrent(arg0, arg1, arg2, arg3, arg4);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callSafe(Object arg0, Object[] arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(arg1);
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 19) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 19;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callSafe(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callSafe(Object arg0) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 20) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 20;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callSafe(arg0);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callSafe(Object arg0, Object arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 21) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 21;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callSafe(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callSafe(Object arg0, Object arg1, Object arg2) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 22) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 22;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callSafe(arg0, arg1, arg2);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callSafe(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2, arg3});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 23) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 23;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callSafe(arg0, arg1, arg2, arg3);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callSafe(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2, arg3, arg4});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 24) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 24;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callSafe(arg0, arg1, arg2, arg3, arg4);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callStatic(Class arg0, Object[] arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(arg1);
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 25) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 25;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callStatic(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callStatic(Class arg0) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 26) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 26;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callStatic(arg0);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callStatic(Class arg0, Object arg1) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 27) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 27;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callStatic(arg0, arg1);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callStatic(Class arg0, Object arg1, Object arg2) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 28) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 28;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callStatic(arg0, arg1, arg2);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callStatic(Class arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2, arg3});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 29) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 29;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callStatic(arg0, arg1, arg2, arg3);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callStatic(Class arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        context.setArgs(new Object[]{arg0, arg1, arg2, arg3, arg4});
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
                    before[i].setDelegate(context);
                    before[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    before[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        
        Object result = null;
        if(around != null) {
            if(this.callIndex != 30) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = 30;
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.callStatic(arg0, arg1, arg2, arg3, arg4);   
        }
                 
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
                    after[i].setDelegate(context);
                    after[i].setResolveStrategy(Closure.DELEGATE_FIRST);
                    after[i].call(context);
                } catch(InvokerInvocationException e) {
                    if (e.getCause() instanceof MissingMethodException) {
                        if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
                            throw new ProceedNotAllowedException();
                        }
                    }
                    throw e;
                }
            }
        }
        return result;
    }

    public Object callGetProperty(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        for(int i = 0; i < before.length; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGetProperty(arg0);
        for(int i = 0; i < after.length; i++) {
            after[i].call(context);
        }
        return result;
    }

    public Object callGetPropertySafe(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        for(int i = 0; i < before.length; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGetPropertySafe(arg0);
        for(int i = 0; i < after.length; i++) {
            after[i].call(context);
        }
        return result;
    }

    public Object callGroovyObjectGetProperty(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        for(int i = 0; i < before.length; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGroovyObjectGetProperty(arg0);
        for(int i = 0; i < after.length; i++) {
            after[i].call(context);
        }
        return result;
    }

    public Object callGroovyObjectGetPropertySafe(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        for(int i = 0; i < before.length; i++) {
            before[i].call(context);
        }
        Object result = delegate.callGroovyObjectGetPropertySafe(arg0);
        for(int i = 0; i < after.length; i++) {
            after[i].call(context);
        }
        return result;
    }

}

