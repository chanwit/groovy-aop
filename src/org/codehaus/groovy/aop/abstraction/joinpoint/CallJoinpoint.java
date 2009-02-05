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
package org.codehaus.groovy.aop.abstraction.joinpoint;

import java.util.Arrays;

import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.metaclass.InvocationContext;

public class CallJoinpoint implements Joinpoint {

    private Class<?> sender;
    private String methodName;
    private Object[] args;
    private Class<?>[] argTypes;
    private Object target;
    private Object[] callStackEntry;

    public CallJoinpoint(Class<?> sender, String methodName, Object target, Object[] args, Class<?>[] argClasses, boolean isCallToSuper) {
        super();
        this.sender = sender;
        this.methodName = methodName;
        this.target = target;
        this.args = args;
        this.argTypes = argClasses;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?> getSender() {
        return sender;
    }

    public void setSender(Class<?> sender) {
        this.sender = sender;
    }

    public void setCallStackEntry(Object[] callStack) {
        this.callStackEntry = callStack;
    }

    public Object[] getCallStackEntry() {
        return this.callStackEntry;
    }

    public InvocationContext getInvocationContext() {
        InvocationContext inv = new InvocationContext();
        inv.setMethodName(this.methodName);
        inv.setArgs(this.args);
        inv.setTarget(this.target);
        return inv;
    }

    @Override
    public int hashCode() {
        final int PRIME = 91;
        int result = 1;
        result = PRIME * result + Arrays.hashCode(argTypes);
        result = PRIME * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = PRIME * result + ((sender.getName() == null) ? 0 : sender.getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CallJoinpoint other = (CallJoinpoint) obj;
        if (!Arrays.equals(argTypes, other.argTypes))
            return false;
        if (methodName == null) {
            if (other.methodName != null)
                return false;
        } else if (!methodName.equals(other.methodName))
            return false;
        if (sender == null) {
            if (other.sender != null)
                return false;
        } else if (!sender.getName().equals(other.sender.getName()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return sender.getName() + " " + methodName;
    }



}
