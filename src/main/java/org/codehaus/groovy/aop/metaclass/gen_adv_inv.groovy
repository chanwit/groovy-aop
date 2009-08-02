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

    public AdviceInvoker (CallSite delegate, EffectiveAdvices ea) {
        this.delegate = delegate;
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
    def context_SetArgs = "context.setArgs(new Object[]{${params.join(", ")}});"
    if(n == -1) {
        context_SetArgs = "context.setArgs(arg1);"
    }
def text = """
    public Object call${cv}(${args.join(", ")}) throws Throwable {
        InvocationContext context = new InvocationContext();
        ${context_SetArgs}
        if(before != null) {
            // System.out.println("doing before ...");
            for(int i = 0; i < before.length; i++) {
                try {
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
        Object result = delegate.call${cv}(${params.join(", ")});
        if(after != null) {
            // System.out.println("doing after ...");
            for(int i = 0; i < after.length; i++) {
                try {
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