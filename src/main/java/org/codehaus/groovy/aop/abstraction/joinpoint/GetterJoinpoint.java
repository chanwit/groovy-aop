package org.codehaus.groovy.aop.abstraction.joinpoint;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class GetterJoinpoint implements Joinpoint {

    private Class<?> sender;
    private String property;
    private String[] binding;

    public GetterJoinpoint(Object object, String property) {
        this.sender = object.getClass();
        this.property = property;
    }

    public Class<?> getSender() {
        return sender;
    }

    public String getProperty() {
        return this.property;
    }

    @Override
    public void setBinding(String[] args) {
        this.binding = args;
    }

    @Override
    public String[] getBinding() {
        return this.binding;
    }

}
