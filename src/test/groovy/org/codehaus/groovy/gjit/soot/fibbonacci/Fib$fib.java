package org.codehaus.groovy.gjit.soot.fibbonacci;

import org.codehaus.groovy.runtime.callsite.AbstractCallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

class Fib$fib extends AbstractCallSite {

    public Fib$fib() {
        super(new CallSiteArray(Fib.class, new String[]{"a"}),0, "a");
    }

}