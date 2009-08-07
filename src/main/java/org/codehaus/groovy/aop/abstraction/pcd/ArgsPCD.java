package org.codehaus.groovy.aop.abstraction.pcd;

import java.util.regex.Pattern;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class ArgsPCD extends AbstractPCD {

    private String[] args;

    public ArgsPCD(Object[] args) {
        this.args = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            this.args[i] = args[i].toString();
        }
    }

    @Override
    protected boolean doMatches(Pattern pt, Joinpoint jp) {
        jp.setBinding(args);
        // always return true
        // until ? a number of argument not matched?
        return true;
    }

}
