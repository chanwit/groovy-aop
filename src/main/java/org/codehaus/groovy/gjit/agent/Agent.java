package org.codehaus.groovy.gjit.agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    private static Instrumentation s_inst;

    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new Transformer());
        s_inst = inst;
    }

    public static Instrumentation getInstrumentation() {
        return s_inst;
    }
}
