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

import org.codehaus.groovy.aop.ProceedNotAllowedException;
import org.codehaus.groovy.aop.Symbol;

import groovy.lang.*;
import org.codehaus.groovy.runtime.callsite.CallSite;

public class InvocationContext extends GroovyObjectSupport {

    private Object target;
    private Object[] args;
    private String methodName;

    public  CallSite proceedCallSite;
    public  int callIndex;

    protected Symbol[] binding;

    public void setBinding(Symbol[] binding) {
        if(binding == null) {
            this.binding = new Symbol[]{};
        } else {
            this.binding = binding;
        }
    }

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

    public Object propertyMissing(String name) {
        int found = -1;
        for(int i=0; i < binding.length; i++) {
            if(binding[i].getName().equals(name)) {
                found = i;
                break;
            }
        }
        if(found == -1) throw new MissingPropertyException(name, InvocationContext.class);
        return args[found];
    }

    public Object proceed(Object arguments) throws Throwable {
        if(this.proceedCallSite == null) throw new ProceedNotAllowedException();

        if((arguments instanceof Object[]) == false) {
            arguments = new Object[]{arguments};
        }

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

    }

 }
