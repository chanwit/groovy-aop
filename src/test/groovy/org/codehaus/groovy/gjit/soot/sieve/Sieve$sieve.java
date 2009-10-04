package org.codehaus.groovy.gjit.soot.sieve;

import org.codehaus.groovy.runtime.callsite.AbstractCallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

public class Sieve$sieve extends AbstractCallSite {

    private static final String[] NAMES = new String[]{
        "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve",
        "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve",
        "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve",
        "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve", "sieve"
    };

    public Sieve$sieve() {
        super(new CallSiteArray(Sieve.class, NAMES), 18, NAMES[18]);
    }

    public Sieve$sieve(int index) {
        super(new CallSiteArray(Sieve.class, NAMES), index, NAMES[index]);
    }

}
