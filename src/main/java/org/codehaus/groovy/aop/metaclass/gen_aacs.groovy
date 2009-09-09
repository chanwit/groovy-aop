#!/usr/bin/env groovy

def header = '''package org.codehaus.groovy.aop.metaclass;

import groovy.lang.*;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.groovy.aop.*;
import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.CallJoinpoint;
import org.codehaus.groovy.aop.cache.*;
import org.codehaus.groovy.gjit.agent.Agent;
import org.codehaus.groovy.gjit.asm.AsmSingleClassOptimizer;
import org.codehaus.groovy.gjit.asm.transformer.AspectAwareTransformer;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

/**
 *   AspectAwareCallSite
 *   The real working code is generated from gen_aasc.groovy
**/
public class AspectAwareCallSite implements CallSite {

    // private static PointcutParser parser =
    //    PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();

    private final CallSite delegate;
    public static ThreadLocal<Boolean> enabled = new ThreadLocal<Boolean>(){
        @Override protected Boolean initialValue(){
            return true;
        }
    };

    //
    // These property can be reset for re-weaving
    //
    private boolean matched = false;
    private AdviceInvoker adviceInvoker = null;

    public AspectAwareCallSite(CallSite delegate) {
        this.delegate = delegate;
    }
'''

println header

//
// each normal call will gen [],0,1,2,3,4
//
def normal = ['','Constructor','Current','Safe','Static']

//
// for making call in advice invoker
// must keep in-sync with index generated for AdviceInvoker
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
    // Special case for arguments as Object[]
    // Normally, it is used for invocation the first time.
    //
    def create_jp = "Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{${params.join(", ")}});"
    if(n == -1) {
        create_jp = "Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), arg0, arg1);"
    }

    callIndex++

def text = """
    @Override
    public Object call${cv}(${args.join(", ")}) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.call${cv}(${params.join(", ")});

        //
        // if this call matching never performed
        //
        if(matched==false) {

            //
            // create an aspect matcher from global AspectRegistry, L1 and L2 caches.
            //
            Matcher matcher  = new Matcher(AspectRegistry.instance(),
                                           AdviceCacheL1.instance(),
                                           AdviceCacheL2.instance());
            //
            // do matching
            //
            EffectiveAdvices effectiveAdviceCodes = new EffectiveAdvices();
            Class<?> sender = delegate.getArray().owner;
            $create_jp
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, ${callIndex});
            } else {
                adviceInvoker = null; // just to make sure, it will be null.
            }

            //
            // matching process is performed,
            // flag set to not doing this until
            // - a new aspect is installed,
            // - or some are modified.
            // - or some are revoked.
            //
            matched = true;
        }

        if(adviceInvoker != null) {
            try {
                enabled.set(false);
                return adviceInvoker.call${cv}(${params.join(", ")});
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.call${cv}(${params.join(", ")});
        }
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
        Object result = delegate.${m}(arg0);
        return result;
    }
"""
    print text
}

def footer = '''
    private void performTypeAdvice(EffectiveAdvices effectiveAdviceCodes,
            final CallSite callSite, Joinpoint jp) throws Throwable {
        final Class<?> sender = callSite.getArray().owner;
        // create typing-invocation-context
        final TypingInvocationContext tic = new TypingInvocationContext();
        tic.setBinding(jp.getBinding());
        // execute type advice closure to obtain type intervention
        Closure[] typing = effectiveAdviceCodes.getTypeAdviceClosureArray();
        Class<?> returnTypeTemp=null;
        for (int i = 0; i < typing.length; i++) {
            typing[i].setDelegate(tic);
            typing[i].setResolveStrategy(Closure.DELEGATE_ONLY);
            Object result = typing[i].call(tic);
            if(result instanceof Class) { returnTypeTemp = (Class<?>)result; }
        }
        final Class<?> returnType = returnTypeTemp;

        String withInMethodNameTemp=null;
        StackTraceElement[] sea = Thread.currentThread().getStackTrace();
        for (int i = 2; i < sea.length; i++) {
            if(sea[i].getClassName().endsWith("CallSiteArray")) continue;
            if(sea[i].getClassName().endsWith("CallSite")) continue;
            withInMethodNameTemp = sea[i].getMethodName();
            break;
        }
        final String withInMethodName=withInMethodNameTemp;

        // do transformation
        new Thread() {
            @Override
            public void run() {
                AsmSingleClassOptimizer sco = new AsmSingleClassOptimizer();
                AspectAwareTransformer aatf = new AspectAwareTransformer();
                aatf.setAdvisedTypes(tic.getArgTypeOfBinding());
                aatf.setAdvisedReturnType(returnType);
                aatf.setCallSite(callSite);
                aatf.setWithInMethodName(withInMethodName);
                try {
                    sco.setTransformers(new Transformer[]{aatf});
                    byte[] bytes = sco.optimize(sender);
                    Instrumentation i = Agent.getInstrumentation();
                    if(i != null) {
                        i.redefineClasses(new ClassDefinition(sender, bytes));
                    }
                } catch (Throwable e) {
                    //
                    // TODO if production, should print stack trace and continue
                    //
                    // e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

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