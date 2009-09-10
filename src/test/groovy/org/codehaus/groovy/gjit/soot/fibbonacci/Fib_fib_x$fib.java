package org.codehaus.groovy.gjit.soot.fibbonacci;

import org.codehaus.groovy.runtime.callsite.AbstractCallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

/**
 *
 * A mock call site for the generated Fib_fib_x type advised class
 *
 * @author chanwit
 *
 */
class Fib_fib_x$fib extends AbstractCallSite {

    private static final String[] NAMES = new String[]{
        "fib",
        "fib",
        "fib",
        "fib",
        "fib",
        "fib",
        "fib"
    };

    public Fib_fib_x$fib() {
        //
        // this should be (generated) Fib_fib_x.class
        //
        super(new CallSiteArray(Fib.class, NAMES),6, "fib");
    }

}