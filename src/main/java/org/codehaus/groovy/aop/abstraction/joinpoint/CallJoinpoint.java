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

import org.codehaus.groovy.aop.metaclass.InvocationContext;
import org.codehaus.groovy.runtime.MetaClassHelper;

public class CallJoinpoint extends JoinpointAdapter {

    private Class<?> sender;
    private Class<?> receiverClass;
    private String methodName;
    private Object[] args;
    private Class<?>[] argTypes;
    private Object target;
    private Object[] callStackEntry;
    private boolean isStatic=false;

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        this.receiverClass = (Class)target;
    }

    public CallJoinpoint(Class<?> sender, String methodName, Object target, Object[] args, Class<?>[] argClasses) {
        super();
        this.sender = sender;
        this.methodName    = methodName;
        this.target        = target;
        this.receiverClass = target.getClass();
        this.args          = args;
        this.argTypes      = argClasses;
    }

    public CallJoinpoint(Class<?> sender, String methodName, Object target, Object[] args) {
        super();
        this.sender = sender;
        this.methodName = methodName;
        this.target     = target;
        this.receiverClass = target.getClass();
        this.args     	= args;
        this.argTypes 	= MetaClassHelper.convertToTypeArray(this.args);
    }

    public CallJoinpoint(Class<?> sender, String methodName, Object[] args) {
        super();
        this.sender = sender;
        this.methodName = methodName;
        this.target     = args[0];
        if(target != null) {
        	this.receiverClass = target.getClass();
        } else {
        	this.receiverClass = sender;
        }

        if(args.length == 1) {
            this.args     = new Object[]{};
            this.argTypes = new Class[]{};
        } else {
            this.args     = Arrays.copyOfRange(args, 1, args.length-1);
            this.argTypes = MetaClassHelper.convertToTypeArray(this.args);
        }
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

    public Class<?> getReceiverClass() {
        return receiverClass;
    }

    public void setReceiverClass(Class<?> receiverClass) {
        this.receiverClass = receiverClass;
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
        result = PRIME * result + ((receiverClass.getName() == null) ? 0 : receiverClass.getName().hashCode());
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
        if (receiverClass == null) {
            if (other.receiverClass != null)
                return false;
        } else if (!receiverClass.getName().equals(other.receiverClass.getName()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return receiverClass.getName() + " " + methodName;
    }

}
