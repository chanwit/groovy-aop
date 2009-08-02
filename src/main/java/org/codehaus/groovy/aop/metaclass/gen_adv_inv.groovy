#!/usr/bin/env groovy

def header = '''
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
def text = """
    @Override
    public Object call${cv}(${args.join(", ")}) throws Throwable {
        //
        //  TODO replace this with a real invocation context object
        //
        Object context = null;
        if(before!=null) {
            for(int i = 0; i < before.count; i++) {
                before[i].call(context);
            }
        }
        Object result = delegate.call${cv}(${params.join(", ")});
        if(after != null) {
            for(int i = 0; i < after.count; i++) {
                after[i].call(context);
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
    @Override
    public Object ${m}(Object arg0) throws Throwable {
        for(int i = 0; i < before.count; i++) {
            before[i].call(context);
        }
        Object result = delegate.${m}(arg0);
        for(int i = 0; i < after.count; i++) {
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