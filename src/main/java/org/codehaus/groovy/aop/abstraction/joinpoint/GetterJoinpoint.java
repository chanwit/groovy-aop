package org.codehaus.groovy.aop.abstraction.joinpoint;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class GetterJoinpoint extends JoinpointAdapter {

    private Class<?> sender;
    private String property;

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

}
