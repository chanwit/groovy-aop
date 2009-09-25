package org.codehaus.groovy.geelab.linearalgebra;

public class ComplexMatrixHelper {

    public static ComplexMatrix ones(int i, int n) {
        ComplexMatrix c = new ComplexMatrix(n, m, new double[n*m*2]);
        c.fill(1, 0);
        return c;
    }

}
