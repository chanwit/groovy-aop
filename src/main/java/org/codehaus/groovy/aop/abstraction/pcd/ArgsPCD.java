package org.codehaus.groovy.aop.abstraction.pcd;

import java.util.regex.Pattern;

import org.codehaus.groovy.aop.Symbol;
import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class ArgsPCD extends AbstractPCD {

    private final Symbol[] args;

    public ArgsPCD(Object[] args) {
        this.args = new Symbol[args.length];
        for (int i = 0; i < args.length; i++) {
            this.args[i] = (Symbol)args[i];
            this.args[i].setIndex(i);
        }
    }

    @Override
    protected boolean doMatches(Pattern pt, Joinpoint jp) {
        // System.out.println("(ArgsPCD) in do matches: " + args);
        jp.setBinding(args);
        // always return true
        // until ? a number of argument not matched?
        return true;
    }

}
