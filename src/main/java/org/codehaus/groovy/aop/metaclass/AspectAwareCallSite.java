package org.codehaus.groovy.aop.metaclass;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;

import java.io.PrintWriter;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.CallJoinpoint;
import org.codehaus.groovy.aop.cache.AdviceCacheL1;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;
import org.codehaus.groovy.gjit.SingleClassOptimizer;
import org.codehaus.groovy.gjit.agent.Agent;
import org.codehaus.groovy.gjit.asm.AsmSingleClassOptimizer;
import org.codehaus.groovy.gjit.asm.TypeAdvisedReOptimizer;
import org.codehaus.groovy.gjit.asm.transformer.AspectAwareTransformer;
import org.codehaus.groovy.gjit.asm.transformer.AutoBoxEliminatorTransformer;
import org.codehaus.groovy.gjit.asm.transformer.DeConstantTransformer;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.codehaus.groovy.gjit.asm.transformer.UnusedCSARemovalTransformer;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import sun.reflect.GroovyAOPMagic;

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
//        System.out.println("===");
//        System.out.println("delegate owner : " + delegate.getArray().owner.getName());
//        System.out.println("delegate class : " + delegate.getClass().getName());
//        System.out.println("delegate name  : " + delegate.getName());
//        System.out.println("delegate super : " + delegate.getClass().getSuperclass().getName());
//        System.out.println("===");
    }


    @Override
    public Object call(Object arg0, Object[] arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.call(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), arg0, arg1);
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 1);
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
                return adviceInvoker.call(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.call(arg0, arg1);
        }
    }

    @Override
    public Object call(Object arg0) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.call(arg0);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 2);
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
                return adviceInvoker.call(arg0);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.call(arg0);
        }
    }

    @Override
    public Object call(Object arg0, Object arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.call(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 3);
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
                return adviceInvoker.call(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.call(arg0, arg1);
        }
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.call(arg0, arg1, arg2);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 4);
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
                return adviceInvoker.call(arg0, arg1, arg2);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.call(arg0, arg1, arg2);
        }
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.call(arg0, arg1, arg2, arg3);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 5);
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
                return adviceInvoker.call(arg0, arg1, arg2, arg3);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.call(arg0, arg1, arg2, arg3);
        }
    }

    @Override
    public Object call(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.call(arg0, arg1, arg2, arg3, arg4);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3, arg4});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 6);
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
                return adviceInvoker.call(arg0, arg1, arg2, arg3, arg4);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.call(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Object callConstructor(Object arg0, Object[] arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callConstructor(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), arg0, arg1);
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 7);
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
                return adviceInvoker.callConstructor(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callConstructor(arg0, arg1);
        }
    }

    @Override
    public Object callConstructor(Object arg0) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callConstructor(arg0);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 8);
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
                return adviceInvoker.callConstructor(arg0);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callConstructor(arg0);
        }
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callConstructor(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 9);
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
                return adviceInvoker.callConstructor(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callConstructor(arg0, arg1);
        }
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callConstructor(arg0, arg1, arg2);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 10);
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
                return adviceInvoker.callConstructor(arg0, arg1, arg2);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callConstructor(arg0, arg1, arg2);
        }
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callConstructor(arg0, arg1, arg2, arg3);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 11);
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
                return adviceInvoker.callConstructor(arg0, arg1, arg2, arg3);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callConstructor(arg0, arg1, arg2, arg3);
        }
    }

    @Override
    public Object callConstructor(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callConstructor(arg0, arg1, arg2, arg3, arg4);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3, arg4});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 12);
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
                return adviceInvoker.callConstructor(arg0, arg1, arg2, arg3, arg4);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callConstructor(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object[] arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callCurrent(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), arg0, arg1);
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 13);
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
                return adviceInvoker.callCurrent(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callCurrent(arg0, arg1);
        }
    }

    @Override
    public Object callCurrent(GroovyObject arg0) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callCurrent(arg0);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 14);
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
                return adviceInvoker.callCurrent(arg0);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callCurrent(arg0);
        }
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callCurrent(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 15);
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
                return adviceInvoker.callCurrent(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callCurrent(arg0, arg1);
        }
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callCurrent(arg0, arg1, arg2);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 16);
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
                return adviceInvoker.callCurrent(arg0, arg1, arg2);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callCurrent(arg0, arg1, arg2);
        }
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callCurrent(arg0, arg1, arg2, arg3);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 17);
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
                return adviceInvoker.callCurrent(arg0, arg1, arg2, arg3);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callCurrent(arg0, arg1, arg2, arg3);
        }
    }

    @Override
    public Object callCurrent(GroovyObject arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callCurrent(arg0, arg1, arg2, arg3, arg4);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3, arg4});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 18);
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
                return adviceInvoker.callCurrent(arg0, arg1, arg2, arg3, arg4);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callCurrent(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Object callSafe(Object arg0, Object[] arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callSafe(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), arg0, arg1);
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 19);
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
                return adviceInvoker.callSafe(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callSafe(arg0, arg1);
        }
    }

    @Override
    public Object callSafe(Object arg0) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callSafe(arg0);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 20);
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
                return adviceInvoker.callSafe(arg0);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callSafe(arg0);
        }
    }

    @Override
    public Object callSafe(Object arg0, Object arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callSafe(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 21);
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
                return adviceInvoker.callSafe(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callSafe(arg0, arg1);
        }
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callSafe(arg0, arg1, arg2);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 22);
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
                return adviceInvoker.callSafe(arg0, arg1, arg2);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callSafe(arg0, arg1, arg2);
        }
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callSafe(arg0, arg1, arg2, arg3);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 23);
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
                return adviceInvoker.callSafe(arg0, arg1, arg2, arg3);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callSafe(arg0, arg1, arg2, arg3);
        }
    }

    @Override
    public Object callSafe(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callSafe(arg0, arg1, arg2, arg3, arg4);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3, arg4});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 24);
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
                return adviceInvoker.callSafe(arg0, arg1, arg2, arg3, arg4);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callSafe(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Object callStatic(Class arg0, Object[] arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callStatic(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), arg0, arg1);
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 25);
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
                return adviceInvoker.callStatic(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callStatic(arg0, arg1);
        }
    }

    @Override
    public Object callStatic(Class arg0) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callStatic(arg0);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 26);
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
                return adviceInvoker.callStatic(arg0);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callStatic(arg0);
        }
    }

    @Override
    public Object callStatic(Class arg0, Object arg1) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callStatic(arg0, arg1);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 27);
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
                return adviceInvoker.callStatic(arg0, arg1);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callStatic(arg0, arg1);
        }
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callStatic(arg0, arg1, arg2);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 28);
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
                return adviceInvoker.callStatic(arg0, arg1, arg2);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callStatic(arg0, arg1, arg2);
        }
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2, Object arg3) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callStatic(arg0, arg1, arg2, arg3);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 29);
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
                return adviceInvoker.callStatic(arg0, arg1, arg2, arg3);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callStatic(arg0, arg1, arg2, arg3);
        }
    }

    @Override
    public Object callStatic(Class arg0, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
        //
        // We do not allow to matching inside advice invokers,
        // so this flag is needed.
        //
        if(enabled.get() == false) return delegate.callStatic(arg0, arg1, arg2, arg3, arg4);

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
            Joinpoint jp = new CallJoinpoint(sender, delegate.getName(), new Object[]{arg0, arg1, arg2, arg3, arg4});
            if(delegate instanceof StaticMetaMethodSite) {
                ((CallJoinpoint)jp).setStatic(true);
            }
            matcher.matchPerClass(effectiveAdviceCodes, jp);
            if(effectiveAdviceCodes.containsTypeAdvice()) {
                performTypeAdvice(effectiveAdviceCodes, delegate, jp);
            }
            if(effectiveAdviceCodes.isEmpty() == false) { // matched and get some advice codes to perform
                InvocationContext context = new InvocationContext();
                context.setBinding(jp.getBinding());
                adviceInvoker = new AdviceInvoker(delegate, effectiveAdviceCodes, context, 30);
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
                return adviceInvoker.callStatic(arg0, arg1, arg2, arg3, arg4);
            } finally {
                enabled.set(true);
            }
        } else {
            return delegate.callStatic(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Object callGetProperty(Object arg0) throws Throwable {
        Object result = delegate.callGetProperty(arg0);
        return result;
    }

    @Override
    public Object callGetPropertySafe(Object arg0) throws Throwable {
        Object result = delegate.callGetPropertySafe(arg0);
        return result;
    }

    @Override
    public Object callGroovyObjectGetProperty(Object arg0) throws Throwable {
        Object result = delegate.callGroovyObjectGetProperty(arg0);
        return result;
    }

    @Override
    public Object callGroovyObjectGetPropertySafe(Object arg0) throws Throwable {
        Object result = delegate.callGroovyObjectGetPropertySafe(arg0);
        return result;
    }

    private void performTypeAdvice(EffectiveAdvices effectiveAdviceCodes,
            final CallSite callSite, Joinpoint jp) throws Throwable {
        // System.out.println("perform Type Advice:" + callSite);
        final Class<?> sender = callSite.getArray().owner;
        // create typing-invocation-context
        // execute type advice closure to obtain type intervention
        Closure[] typing = effectiveAdviceCodes.getTypeAdviceClosureArray();
        Class<?> returnTypeTemp=null;
        TypingInvocationContext ticTemp=null;
        // System.out.println("typing length: " + typing.length);
        for (int i = 0; i < typing.length; i++) {
            TypingInvocationContext _tic = new TypingInvocationContext();
            _tic.setBinding(jp.getBinding());
            typing[i].setDelegate(_tic);
            typing[i].setResolveStrategy(Closure.DELEGATE_ONLY);
            Object result = null;
            try {
                result = typing[i].call(_tic);
                if(result != null && result instanceof Class) { returnTypeTemp = (Class<?>)result; }
                ticTemp = _tic;
            }catch(Throwable e) {
                throw e;
                // continue;
            }
        }
        final TypingInvocationContext tic = ticTemp;
        final Class<?> returnType = returnTypeTemp;
        // System.out.println("advised return type: " + returnType);

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
                SingleClassOptimizer sco;
                if(sender.getSuperclass() == GroovyAOPMagic.class) {
                    // System.out.println(">> doing TypeAdvisedReOptimizer");
                    sco = new TypeAdvisedReOptimizer();
                    AspectAwareTransformer aatf = new AspectAwareTransformer();
                    aatf.setAdvisedTypes(tic.getArgTypeOfBinding());
                    aatf.setAdvisedReturnType(returnType);
                    aatf.setCallSite(callSite);
//                    System.out.print("re-transform by callsite: ");
//                    System.out.print(callSite.getIndex() + ", ");
//                    System.out.println(callSite.getName());
                    aatf.setWithInMethodName(withInMethodName);
                    sco.setTransformers(new Transformer[]{
                        new DeConstantTransformer(),
                        aatf,
                        new AutoBoxEliminatorTransformer(),
                        new UnusedCSARemovalTransformer()
                    });
                } else {
                    // System.out.println(">> doing AsmSingleClassOptimizer");
                    sco = new AsmSingleClassOptimizer();
                    AspectAwareTransformer aatf = new AspectAwareTransformer();
                    aatf.setAdvisedTypes(tic.getArgTypeOfBinding());
                    aatf.setAdvisedReturnType(returnType);
                    aatf.setCallSite(callSite);
                    aatf.setWithInMethodName(withInMethodName);
                    sco.setTransformers(new Transformer[]{aatf});
                }
                byte[] bytes = null;
                try {
                    bytes = sco.optimize(sender.getName());
                    Instrumentation i = Agent.getInstrumentation();
                    if(i != null) {
                        i.redefineClasses(new ClassDefinition(sender, bytes));
//                        System.out.println(">>>>>>>> class " + sender.getName() + " redefined");
//                        TraceClassVisitor tcv = new TraceClassVisitor(new PrintWriter(System.out));
//                        new ClassReader(bytes).accept(tcv, 0);
//                        CheckClassAdapter.verify(new ClassReader(bytes), true, new PrintWriter(System.out));
                    }
                } catch (Throwable e) {
                    //
                    // TODO if production, should print stack trace and continue
                    //
                    e.printStackTrace();
                    // CheckClassAdapter.verify(new ClassReader(bytes), true, new PrintWriter(System.out));
                    // throw new RuntimeException("Error while optimising class " + sender.getName(), e);
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

