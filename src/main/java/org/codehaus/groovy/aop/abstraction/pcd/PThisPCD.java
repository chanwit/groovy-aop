package org.codehaus.groovy.aop.abstraction.pcd;

import java.util.regex.Pattern;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class PThisPCD extends AbstractPCD {

    private String _this;

    public PThisPCD(Object _this) {
    	this._this = _this.toString();
    }

    @Override
    protected boolean doMatches(Pattern pt, Joinpoint jp) {
        jp.setThisBinding(_this);
        // TODO now always return true
        // how to match "this" ?
        // until ? a number of argument not matched?
        return true;
    }

}
