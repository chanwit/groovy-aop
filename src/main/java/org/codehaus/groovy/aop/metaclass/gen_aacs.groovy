#!/usr/bin/env groovy

def header = '''
package org.codehaus.groovy.aop.metaclass;

import groovy.lang.GroovyObject;

import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.weaver.tools.PointcutParser;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

/**
 *   AspectAwareCallSite
 *   The real working code is generated from gen_aasc.groovy
 *   These call* methods are a place holder for weaving code.
**/
public class AspectAwareCallSite implements CallSite {

    private static PointcutParser parser =
        PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();

    private CallSite delegate;

    public AspectAwareCallSite(CallSite delegate) {
        this.delegate = delegate;
    }
'''

println header

//
// each normal call will gen [],0,1,2,3,4
//
def normal = ['','Constructor','Current','Safe','Static']
// cv is call convention
normal.each { cv ->
  for(n in -1..4) {
    def args = []
    def params = []
    for(i in 0..n)  {
        // special case, for Object[]
        if(i==-1) {
            args << "Object[] arg1"
            params << "arg1"
        } else {        
            if(cv == 'Static' && i == 0) 
                args << "Class arg${i}"
            else
                args << "Object arg${i}"
            params << "arg${i}"
        }
    }
def text = """
    @Override
    public Object call${cv}(${args.join(", ")}) throws Throwable {
        return delegate.call${cv}(${params.join(", ")});
    }
"""
    print text
  }
}

//
// another set to gen *property with arg0
//
def special_0 = ['callGetProperty', 'callGetPropertySafe', 'callGroovyObjectGetProperty', 'callGroovyObjectGetPropertySafe']
special_0.each { m ->
def text = """
    @Override
    public Object ${m}(Object arg0) throws Throwable {
        return delegate.${m}(arg0);
    }
"""
    print text
}

def footer = '''
    @Override
    public CallSiteArray getArray() {
        return delegate.getArray();
    }

    @Override
    public int getIndex() {
        return delegate.getIndex();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Object getProperty(Object arg0) throws Throwable {
        return delegate.getProperty(arg0);
    }

    @Override
    public AtomicInteger getUsage() {
        return delegate.getUsage();
    }
}
'''

println footer