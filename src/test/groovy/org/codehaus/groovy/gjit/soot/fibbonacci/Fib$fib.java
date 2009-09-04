package org.codehaus.groovy.gjit.soot.fibbonacci;

import org.codehaus.groovy.runtime.callsite.AbstractCallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

class Fib$fib extends AbstractCallSite {

    private static final String[] NAMES = new String[]{
        "fib",
        "fib",
        "fib",
        "fib",
        "fib",
        "fib",
        "fib"
    };

    public Fib$fib() {
        super(new CallSiteArray(Fib.class, NAMES),6, "fib");
    }

}