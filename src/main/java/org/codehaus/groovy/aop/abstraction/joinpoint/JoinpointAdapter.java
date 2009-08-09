package org.codehaus.groovy.aop.abstraction.joinpoint;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class JoinpointAdapter implements Joinpoint {

    // arg binding for context exposure
    private String[] binding;
	private String _this;

    @Override
    public void setBinding(String[] args) {
        this.binding = args;
    }

    @Override
    public String[] getBinding() {
        return this.binding;
    }

    @Override
    public void setThisBinding(String _this) {
    	this._this = _this;
    }

    @Override
    public String getThisBinding() {
    	return this._this;
    }

}
