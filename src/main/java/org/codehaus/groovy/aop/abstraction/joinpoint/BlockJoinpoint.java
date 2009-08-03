package org.codehaus.groovy.aop.abstraction.joinpoint;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class BlockJoinpoint implements Joinpoint {

    private String[] binding;

    @Override
    public void setBinding(String[] args) {
        this.binding = args;
    }

    @Override
    public String[] getBinding() {
        return this.binding;
    }

}
