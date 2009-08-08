package org.codehaus.groovy.gjit.soot;

import groovy.lang.Closure;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;

@SuppressWarnings("unused")
public class Main {

    public static void main(String[] args) {
        byte[] bytes = new SingleClassOptimizer().optimize(Closure.class);
        System.out.println(bytes.length);
    }

}
