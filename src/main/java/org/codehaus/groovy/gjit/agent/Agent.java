package org.codehaus.groovy.gjit.agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    private static Instrumentation _inst = null;

    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new Transformer());
        _inst = inst;
    }

    public static Instrumentation getInstrumentation() {
        return _inst;
    }
}
