/**
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Chanwit Kaewkasi
 *
 **/
package org.codehaus.groovy.aop.metaclass;

import groovy.lang.Closure;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaClassRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.CallJoinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.GetterJoinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.SetterJoinpoint;
import org.codehaus.groovy.aop.builder.AspectBuilder;
import org.codehaus.groovy.aop.builder.AspectCreationFailedException;
import org.codehaus.groovy.aop.cache.AdviceCacheL1;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;
import org.codehaus.groovy.runtime.MetaClassHelper;

public class AspectMetaClass extends MetaClassImpl {

    private static final ThreadLocal<Stack<Object>> callStack = new ThreadLocal<Stack<Object>>() {
        protected Stack<Object> initialValue() {
            return new Stack<Object>();
        }
    };

    public AspectMetaClass(MetaClassRegistry registry, final Class<?> theClass) {
        super(registry, theClass);
    }

    public Object invokeMissingMethod(Object object, String methodName, Object[] originalArguments) {
        Object result=null;
        try {
            result = AspectBuilder.buildAspect(object, methodName, originalArguments);
        } catch (AspectCreationFailedException e) {
            return super.invokeMissingMethod(object, methodName, originalArguments);
        }
        if(result==null) {
            return super.invokeMissingMethod(object, methodName, originalArguments);
        } else {
            return result;
        }
    }

    private Object superInvokeMethod(Class<?> sender, Object arg0, String name,
            Object[] args, boolean isCallToSuper, boolean fromInsideClass) {
        return super.invokeMethod(sender, arg0, name, args, false, false);
    }

    private Object execClosures(ArrayList<Closure> closures, Object delegate) {
        Object result=null;
        for (Iterator<Closure> i = closures.iterator(); i.hasNext();) {
            Closure c = i.next();
            c.setResolveStrategy(Closure.DELEGATE_ONLY);
            c.setDelegate(delegate);
            result = c.call();
            c.setDelegate(null);
        }
        return result;
    }

    private Object execClosures(ArrayList<Closure> closures, Object delegate, Object arg) {
        Object result=arg;
        for (Iterator<Closure> i = closures.iterator(); i.hasNext();) {
            Closure c = i.next();
            c.setResolveStrategy(Closure.DELEGATE_ONLY);
            c.setDelegate(delegate);
            result = c.call(arg);
            c.setDelegate(null);
        }
        return result;
    }

    private Object execClosures(ArrayList<Closure> closures, Object delegate, Object[] args) {
        Object result=args;
        for (Iterator<Closure> i = closures.iterator(); i.hasNext();) {
            Closure c = i.next();
            c.setResolveStrategy(Closure.DELEGATE_ONLY);
            c.setDelegate(delegate);
            result = c.call(args);
            c.setDelegate(null);
        }
        return result;
    }

    public Object getProperty(Object object, String property) {
        GetterJoinpoint gjp = new GetterJoinpoint(object, property);
        EffectiveAdvices effAdvices = new EffectiveAdvices();
        matchPerClass(effAdvices, gjp);
        if(effAdvices.isEmpty()==false) {
            ArrayList<Closure> beforeClosures = effAdvices.get(Advice.BEFORE);
            execClosures(beforeClosures, object);
            Object result = super.getProperty(object, property);
            ArrayList<Closure> afterClosures = effAdvices.get(Advice.AFTER);
            execClosures(afterClosures, object, result);
            return result;
        } else {
            return super.getProperty(object, property);
        }
    }

    public void setProperty(Object object, String property, Object newValue) {
        SetterJoinpoint sjp = new SetterJoinpoint(object, property, newValue);
        EffectiveAdvices effAdvices = new EffectiveAdvices();
        matchPerClass(effAdvices, sjp);
        if(effAdvices.isEmpty()==false) {
            ArrayList<Closure> beforeClosures = effAdvices.get(Advice.BEFORE);
            Object newerValue = execClosures(beforeClosures, object, newValue);
            super.setProperty(object, property, newerValue);
            ArrayList<Closure> afterClosures = effAdvices.get(Advice.AFTER);
            execClosures(afterClosures, object, newerValue);
        } else {
            super.setProperty(object, property, newValue);
        }

    }

    @SuppressWarnings("unchecked")
    public Object invokeMethod(final Class sender, final Object object,
            final String methodName, Object[] args, boolean isCallToSuper,
            boolean fromInsideClass) {
        if (object instanceof Closure) {
            return superInvokeMethod(sender, object, methodName, args,
                    isCallToSuper, fromInsideClass);
        }

        Class[] argClasses = MetaClassHelper.convertToTypeArray(args);

        EffectiveAdvices effAdvices = null;
        CallJoinpoint cjp = new CallJoinpoint(sender, methodName, object, args,
                argClasses, isCallToSuper);

        if (!callStack.get().empty()) {
            cjp.setCallStackEntry((Object[]) callStack.get().peek());
        } else {
            cjp.setCallStackEntry(new Object[] { null, "main" }); // TODO correct this
        }
        callStack.get().push(new Object[] { object, methodName });

        effAdvices = new EffectiveAdvices();
        matchPerClass(effAdvices, cjp);
        // TODO not included in 0.2 release
        // matchPerInstance(object, effAdvices, cjp);
        // if(effectiveAdvices.isEmpty()) {
        if (effAdvices.isEmpty()
            || (object instanceof Closure && "doCall".equals(methodName))) {
            Object result = superInvokeMethod(sender, object, methodName, args,
                    isCallToSuper, fromInsideClass);
            callStack.get().pop();
            return result;
        }

        InvocationContext inv = cjp.getInvocationContext();
        inv.setArgs(args);
        ArrayList<Closure> beforeClosures = effAdvices.get(Advice.BEFORE);
        execClosures(beforeClosures, inv, args);
        Closure proceedClosure = new Closure(inv, object) {
            @SuppressWarnings("unused")
            public Object doCall(final Object params) {
                if (params instanceof Object[]) {
                    return superInvokeMethod(sender, object, methodName,
                            (Object[]) params, false, false);
                } else {
                    return superInvokeMethod(sender, object, methodName,
                            new Object[] { params }, false, false);
                }
            }
        };

        Object result = null;
        ArrayList<Closure> aroundClosures = effAdvices.get(Advice.AROUND);
        inv.setProceedClosure(proceedClosure);

        if(aroundClosures.size() > 0) {
            Closure aroundClosure = aroundClosures.get(0);
            aroundClosure.setResolveStrategy(Closure.DELEGATE_ONLY);
            aroundClosure.setDelegate(inv);
            result = aroundClosure.call(args);
            aroundClosure.setDelegate(null);
        } else {
            result = superInvokeMethod(sender, object,
                    methodName, (Object[]) args,
                    false, false);
        }
        inv.setProceedClosure(null);
        ArrayList<Closure> afterClosures = effAdvices.get(Advice.AFTER);
        execClosures(afterClosures, inv, args);
        callStack.get().pop();
        ArrayList<Closure> afterReturnClosures = effAdvices.get(Advice.AFTER_RETURNING);
        execClosures(afterReturnClosures, inv, result);

        return result;
    }

    private void matchPerClass(EffectiveAdvices effAdvices, Joinpoint jp) {
        // If there is the jp as a key in L1, retrieve it
        if (AdviceCacheL1.v().contains(jp)) {
            effAdvices.addAll(AdviceCacheL1.v().get(jp));
            return;
        }
        // if there is no jp in L1:
        //  - pick aspects from the registry
        Collection<Aspect> aspects = AspectRegistry.v().getClassAspects();

        // - matchPCD
        // - store them in L2
        for (Iterator<Aspect> iter = aspects.iterator(); iter.hasNext();) {
            Aspect aspect = iter.next();
            ArrayList<Advice> advices = aspect.getAdvices();
            for (Iterator<Advice> i = advices.iterator(); i.hasNext();) {
                Advice advice = i.next();
                //if(advice.getPointcut() == null) continue;
                if (advice.getPointcut().matches(jp)) {
                    AdviceCacheL2.v().put(jp, aspect, advice);
                }
            }
        }
        // build an L1 entry from a set of L2 entries
        // store to L1 using jp as its key
        EffectiveAdvices result = AdviceCacheL2.v().getByJoinpoint(jp);
        AdviceCacheL1.v().put(jp, result);
        // then all matched advice is going to be the result
        effAdvices.addAll(result);
    }

    /**
     * Enable AOP globally
     *
     * @since 0.3
     *
     * @see AspectMetaClass#disableGlobally()
     * @deprecated since 0.5, AMCCH will be enabled by default
     **/
    public static void enableGlobally() {
        // AspectMetaclassCreationHandle.enable();
    }

    /**
     * Disable AOP globally
     *
     * @since 0.3
     *
     * @see AspectMetaClass#enableGlobally()
     * @deprecated since 0.5, AMCCH will be enabled by default
     **/
    public static void disableGlobally() {
        // AspectMetaclassCreationHandle.disable();
    }


}