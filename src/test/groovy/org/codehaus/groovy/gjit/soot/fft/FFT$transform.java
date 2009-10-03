package org.codehaus.groovy.gjit.soot.fft;

import org.codehaus.groovy.runtime.callsite.AbstractCallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

class FFT$transform extends AbstractCallSite {

    private static final String[] NAMES = new String[] {
        "plus", "multiply", "minus", "multiply", "log2", "multiply", "plus", "transformInternal", "transformInternal", "length",
        "div", "div", "putAt", "multiply", "getAt", "next", "length", "arraycopy", "transform", "inverse",
        "iterator", "minus", "getAt", "getAt", "plus", "multiply", "sqrt", "div", "multiply", "<$constructor$>",
        "iterator", "putAt", "nextDouble", "length", "println", "test", "makeRandom", "length", "parseInt", "getAt",
        "println", "test", "makeRandom", "next", "next", "multiply", "leftShift", "<$constructor$>", "length", "div",
        "length", "log2", "bitreverse", "div", "multiply", "multiply", "PI", "multiply", "sin", "sin",
        "div", "multiply", "multiply", "multiply", "multiply", "plus", "getAt", "getAt", "plus", "putAt",
        "minus", "getAt", "putAt", "plus", "minus", "getAt", "plus", "putAt", "plus", "getAt",
        "putAt", "plus", "getAt", "plus", "plus", "multiply", "iterator", "minus", "minus", "multiply",
        "multiply", "minus", "plus", "multiply", "multiply", "multiply", "plus", "multiply", "plus", "plus",
        "getAt", "getAt", "plus", "minus", "multiply", "multiply", "plus", "multiply", "multiply", "putAt",
        "minus", "getAt", "putAt", "plus", "minus", "getAt", "plus", "putAt", "plus", "getAt",
        "putAt", "plus", "getAt", "plus", "plus", "multiply", "multiply", "next", "div", "length",
        "minus", "multiply", "multiply", "div", "getAt", "getAt", "plus", "putAt", "getAt", "putAt",
        "plus", "getAt", "plus", "putAt", "putAt", "plus", "minus", "div", "plus", "next"
    };

    public FFT$transform() {
        super(new CallSiteArray(FFT.class, NAMES), 18, NAMES[18]);
    }

    public FFT$transform(int index) {
        super(new CallSiteArray(FFT.class, NAMES), index, NAMES[index]);
    }

}
