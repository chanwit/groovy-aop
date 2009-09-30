package org.codehaus.groovy.gjit.soot.heapsort;

import org.codehaus.groovy.runtime.callsite.AbstractCallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

class HeapSort$heapsort extends AbstractCallSite {

    private static final String[] NAMES = new String[]{
        "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort",
        "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort",
        "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort",
        "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort", "heapsort"
    };

    public HeapSort$heapsort() {
        super(new CallSiteArray(HeapSort.class, NAMES), 37, "heapsort");
    }

    public HeapSort$heapsort(int index) {
        super(new CallSiteArray(HeapSort.class, NAMES), index, "heapsort");
    }

}
