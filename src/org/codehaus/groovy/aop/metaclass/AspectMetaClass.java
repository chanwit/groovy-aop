/**
 * Copyright 2007-2009 the original author or authors.
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
import java.util.Iterator;
import java.util.Stack;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.joinpoint.GetterJoinpoint;
import org.codehaus.groovy.aop.abstraction.joinpoint.SetterJoinpoint;
import org.codehaus.groovy.aop.cache.AdviceCacheL1;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;
import org.codehaus.groovy.runtime.callsite.CallSite;

public class AspectMetaClass extends MetaClassImpl {

    @Override
    public CallSite createPogoCallSite(CallSite arg0, Object[] arg1) {
        return new AspectAwareCallSite(super.createPogoCallSite(arg0, arg1));
    }

    @Override
    public CallSite createConstructorSite(CallSite arg0, Object[] arg1) {
        return new AspectAwareCallSite(super.createConstructorSite(arg0, arg1));
    }

    @Override
    public CallSite createPogoCallCurrentSite(CallSite arg0, Class arg1, Object[] arg2) {
        return new AspectAwareCallSite(super.createPogoCallCurrentSite(arg0, arg1, arg2));
    }

    @Override
    public CallSite createPojoCallSite(CallSite arg0, Object arg1, Object[] arg2) {
        // TODO Auto-generated method stub
        return new AspectAwareCallSite(super.createPojoCallSite(arg0, arg1, arg2));
    }

    @Override
    public CallSite createStaticSite(CallSite arg0, Object[] arg1) {
        // TODO Auto-generated method stub
        return new AspectAwareCallSite(super.createStaticSite(arg0, arg1));
    }

/**
    @Override
    public Object invokeMethod(Object object, String methodName, Object arg2) {
        // Class<?>[] argClasses = MetaClassHelper.convertToTypeArray(args);
        CallJoinpoint cjp = new CallJoinpoint(object.getClass(), methodName, object, new Object[]{arg2}, new Class[]{arg2.getClass()});
        System.out.println(cjp);
        return super.invokeMethod(object, methodName, arg2);
    }

    @Override
    public Object invokeMethod(Object object, String methodName, Object[] args) {
        Class<?>[] argClasses = MetaClassHelper.convertToTypeArray(args);

        CallJoinpoint cjp = new CallJoinpoint(object.getClass(), methodName, object, args, argClasses);

        if (!callStack.get().empty()) {
            cjp.setCallStackEntry((Object[]) callStack.get().peek());
        } else {
            cjp.setCallStackEntry(new Object[] { null, "main" }); // TODO correct this
        }
        callStack.get().push(new Object[] { object, methodName });

        EffectiveAdvices effAdvices = new EffectiveAdvices();
        System.out.println(cjp);
        matcher.matchPerClass(effAdvices, cjp);
        System.out.println("effAdvices : " + effAdvices.isEmpty());
        if (effAdvices.isEmpty()) {
            // || (object instanceof Closure && "doCall".equals(methodName))
            //Object result = superInvokeMethod(sender, object, methodName, args,
            //        isCallToSuper, fromInsideClass);
            Object result = super.invokeMethod(object, methodName, args);
            callStack.get().pop();
            return result;
        }

        // Execute before closures
        InvocationContext inv = cjp.getInvocationContext();
        inv.setArgs(args);
        ArrayList<Closure> beforeClosures = effAdvices.get(Advice.BEFORE);
        execClosures(beforeClosures, inv, args);

        //
        // prepare proceed closure
        //
        Closure proceedClosure = new Closure(inv, object) {
            @SuppressWarnings("unused")
            public Object doCall(final Object params) {
                if (params instanceof Object[]) {
                    //return superInvokeMethod(sender, object, methodName,
                    //        (Object[]) params, false, false);
                    return null;
                } else {
                    //return superInvokeMethod(sender, object, methodName,
                    //        new Object[] { params }, false, false);
                    return null;
                }
            }
        };
        Object result = null;
        ArrayList<Closure> aroundClosures = effAdvices.get(Advice.AROUND);
        inv.setProceedClosure(proceedClosure);


        //
        // execute around closures
        //
        if(aroundClosures.size() > 0) {
            Closure aroundClosure = aroundClosures.get(0);
            aroundClosure.setResolveStrategy(Closure.DELEGATE_ONLY);
            aroundClosure.setDelegate(inv);
            result = aroundClosure.call(args);
            aroundClosure.setDelegate(null);
        } else {
            // TODO result = superInvokeMethod(sender, object,
                    // methodName, (Object[]) args,
                    // false, false);
        }

        //
        // execute after closures
        //
        inv.setProceedClosure(null);
        ArrayList<Closure> afterClosures = effAdvices.get(Advice.AFTER);
        execClosures(afterClosures, inv, args);

        //
        // execute after returning closures
        //
        callStack.get().pop();
        ArrayList<Closure> afterReturnClosures = effAdvices.get(Advice.AFTER_RETURNING);
        execClosures(afterReturnClosures, inv, result);

        return result;
    }
    */

    private Matcher matcher = new Matcher(AspectRegistry.v(), AdviceCacheL1.v(), AdviceCacheL2.v());

    private static final ThreadLocal<Stack<Object>> callStack = new ThreadLocal<Stack<Object>>() {
        protected Stack<Object> initialValue() {
            return new Stack<Object>();
        }
    };

    public AspectMetaClass(MetaClassRegistry registry, final Class<?> theClass) {
        super(registry, theClass);
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
        matcher.matchPerClass(effAdvices, gjp);
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
        matcher.matchPerClass(effAdvices, sjp);
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


/*
    @SuppressWarnings("unchecked")
    public Object invokeMethod(final Class sender, final Object object,
            final String methodName, Object[] args, boolean isCallToSuper,
            boolean fromInsideClass) {


    }
*/

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
