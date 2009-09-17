package org.codehaus.groovy.geelab.linearalgebra;

import java.util.Arrays;

public class MatrixFactory {

    public static Matrix create(int rows, int cols) {
        return new Matrix(rows, cols, new double[rows*cols]);
    }

    public static Matrix create(int rows, int cols, double initValue) {
        double[] data = new double[rows * cols];
        Arrays.fill(data, initValue);
        return new Matrix(rows, cols, data);
    }

}
