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

import java.util.Arrays;

import org.codehaus.groovy.aop.ProceedNotAllowedException;
import groovy.lang.*;
import org.codehaus.groovy.runtime.callsite.CallSite;

public class InvocationContext extends GroovyObjectSupport {

    private Object target;
    private Object[] args;
    private String methodName;

    private Closure proceedClosure;

    public  CallSite proceedCallSite;
    public  int callIndex;

//	private Closure parProceedClosure;
//	private Stack<Closure> stack;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setProceedClosure(Closure closure) {
        this.proceedClosure = closure;
    }

    public Object proceed(Object arguments) throws Throwable {
        if(this.proceedCallSite == null) throw new ProceedNotAllowedException();

        Object[] args = (Object[])arguments;
        switch(callIndex) {
            case 1: return proceedCallSite.call(target, (Object[])arguments);
            case 2: return proceedCallSite.call(target);
            case 3: return proceedCallSite.call(target, args[0]);
            case 4: return proceedCallSite.call(target, args[0], args[1]);
            case 5: return proceedCallSite.call(target, args[0], args[1], args[2]);
            case 6: return proceedCallSite.call(target, args[0], args[1], args[2], args[3]);
            case 7: return proceedCallSite.callConstructor(target, (Object[])arguments);
            case 8: return proceedCallSite.callConstructor(target);
            case 9: return proceedCallSite.callConstructor(target, args[0]);
            case 10: return proceedCallSite.callConstructor(target, args[0], args[1]);
            case 11: return proceedCallSite.callConstructor(target, args[0], args[1], args[2]);
            case 12: return proceedCallSite.callConstructor(target, args[0], args[1], args[2], args[3]);
            case 13: return proceedCallSite.callCurrent((GroovyObject)target, (Object[])arguments);
            case 14: return proceedCallSite.callCurrent((GroovyObject)target);
            case 15: return proceedCallSite.callCurrent((GroovyObject)target, args[0]);
            case 16: return proceedCallSite.callCurrent((GroovyObject)target, args[0], args[1]);
            case 17: return proceedCallSite.callCurrent((GroovyObject)target, args[0], args[1], args[2]);
            case 18: return proceedCallSite.callCurrent((GroovyObject)target, args[0], args[1], args[2], args[3]);
            case 19: return proceedCallSite.callSafe(target, (Object[])arguments);
            case 20: return proceedCallSite.callSafe(target);
            case 21: return proceedCallSite.callSafe(target, args[0]);
            case 22: return proceedCallSite.callSafe(target, args[0], args[1]);
            case 23: return proceedCallSite.callSafe(target, args[0], args[1], args[2]);
            case 24: return proceedCallSite.callSafe(target, args[0], args[1], args[2], args[3]);
            case 25: return proceedCallSite.callStatic((Class)target, (Object[])arguments);
            case 26: return proceedCallSite.callStatic((Class)target);
            case 27: return proceedCallSite.callStatic((Class)target, args[0]);
            case 28: return proceedCallSite.callStatic((Class)target, args[0], args[1]);
            case 29: return proceedCallSite.callStatic((Class)target, args[0], args[1], args[2]);
            case 30: return proceedCallSite.callStatic((Class)target, args[0], args[1], args[2], args[3]);
        }

        return null;

        /*
        if(this.proceedClosure==null) throw new ProceedNotAllowedException();
        if(args instanceof Object[]) {
            return this.proceedClosure.call((Object[]) args);
        } else {
            return this.proceedClosure.call(args);
        }
        */
    }


    // static {
    //     //
    //     // Fixing metaClass to prevent stack overflow
    //     //
    //     MetaClass mc = new MetaClassImpl(InvocationContext.class);
    //     GroovySystem.getMetaClassRegistry().setMetaClass(
    //         InvocationContext.class, mc
    //     );
    //     mc.initialize();
    // }

//    public Object invokeMethod(String name, Object args) {
//    	if("proceed".equals(name)) {
//    		return this.proceedClosure.call((Object[]) args);
//    	} else {
//    		return super.invokeMethod(name, args);
//    	}
//    }

//	public void setProceedStack(Stack<Closure> stack) {
//		this.stack = stack;
//	}
 }
