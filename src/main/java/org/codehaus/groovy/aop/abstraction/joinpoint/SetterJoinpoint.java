package org.codehaus.groovy.aop.abstraction.joinpoint;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class SetterJoinpoint implements Joinpoint {

    private Class<?> sender;
    private String property;
    private Object newValue;
    private String[] binding;

    public SetterJoinpoint(Object object, String property, Object newValue) {
        this.sender = object.getClass();
        this.property = property;
        this.newValue = newValue;
    }

    public Class<?> getSender() {
        return sender;
    }

    public String getProperty() {
        return this.property;
    }

    public Object getNewValue() {
        return newValue;
    }

    @Override
    public void setBinding(String[] args) {
        this.binding = args;
    }

    @Override
    public String[] getBinding() {
        // TODO Auto-generated method stub
        return this.binding;
    }

}
