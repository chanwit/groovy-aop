package org.codehaus.groovy.aop.abstraction.pcd;

import java.util.regex.Pattern;

import org.codehaus.groovy.aop.Symbol;
import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class ArgsPCD extends AbstractPCD implements Cloneable {

    private Symbol[] args;

    public Symbol[] getArgs() {
        return args;
    }

    public void setArgs(Symbol[] args) {
        this.args = args;
    }

    public ArgsPCD(Object[] args) {
        this.args = new Symbol[args.length];
        for (int i = 0; i < args.length; i++) {
            this.args[i] = (Symbol)args[i];
            this.args[i].setIndex(i);
        }
    }

    @Override
    protected boolean doMatches(Pattern pt, Joinpoint jp) {
        jp.setBinding(args);
        return true;
    }

    public void exposeContext(Joinpoint jp) {
        jp.setBinding(args);
    }

}
