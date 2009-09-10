package org.codehaus.groovy.gjit;

public interface SingleClassOptimizer {

    /**
     * The main entry of optimisation.
     *
     * @param c
     *            A class name. Actually this method uses only its name to perform
     *            optimization.
     * @return a byte array containing optimized class
     */
    public abstract byte[] optimize(String className);

}