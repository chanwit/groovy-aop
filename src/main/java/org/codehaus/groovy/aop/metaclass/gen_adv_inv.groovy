#!/usr/bin/env groovy

def header = '''
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

'''

println header

//
// each normal call will gen [],0,1,2,3,4
//
def normal = ['','Constructor','Current','Safe','Static']

def callIndex = 0;
//
// cv is call convention
//
normal.each { cv ->
  for(n in -1..4) {
    def args = []
    def params = []
    for(i in 0..n)  {
        // special case, for Object[]
        if(i == -1) {
            args << 'Object[] arg1'
            params << 'arg1'
        } else {
            if(cv == 'Current' && i == 0)
                args << "GroovyObject arg${i}"
            else if (cv == 'Static' && i == 0)
                args << "Class arg${i}"
            else
                args << "Object arg${i}"

            params << "arg${i}"
        }
    }

    //
    // special case
    // when called from N arguments    
    //
    def context_SetArgs    
    if(cv == '' || cv == 'Current') {
        def p = params.clone()
        p.remove(0)
        context_SetArgs = "context.setArgs(new Object[]{${p.join(", ")}});"
    } else {
        context_SetArgs = "context.setArgs(new Object[]{${params.join(", ")}});"
    }
    if(n == -1) {
        context_SetArgs = "context.setArgs(arg1);"
    }
    
    callIndex++
    
def text = """
    public Object call${cv}(${args.join(", ")}) throws Throwable {
        // TODO what if the call is STATIC?
        context.setTarget(arg0);
        ${context_SetArgs}
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
            if(this.callIndex != ${callIndex}) {
                throw new RuntimeException("Code generation broken");
            }
            context.callIndex = ${callIndex};
            context.proceedCallSite = delegate;
            around[0].setDelegate(context);
            result = around[0].call(context);
        } else {
            result = delegate.call${cv}(${params.join(", ")});   
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
"""
    print text
  }
}

//
// another set to gen *property with arg0
//
def special_0 = ['callGetProperty',
                 'callGetPropertySafe',
                 'callGroovyObjectGetProperty',
                 'callGroovyObjectGetPropertySafe']
special_0.each { m ->
def text = """
    public Object ${m}(Object arg0) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        for(int i = 0; i < before.length; i++) {
            before[i].call(context);
        }
        Object result = delegate.${m}(arg0);
        for(int i = 0; i < after.length; i++) {
            after[i].call(context);
        }
        return result;
    }
"""
    print text
}

def footer = '''
}
'''

println footer