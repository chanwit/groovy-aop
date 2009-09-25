package org.codehaus.groovy.geelab.linearalgebra;

public class ComplexMatrixHelper {

    public static ComplexMatrix zeros(int n, int m) {
        ComplexMatrix c = new ComplexMatrix(n, m, new double[n*m*2]);
        c.fill(0, 0);
        return c;
    }
    
    public static ComplexMatrix zeros(int n) {
        ComplexMatrix c = new ComplexMatrix(n, n, new double[n*n*2]);
        c.fill(0, 0);
        return c;
    }    

    public static ComplexMatrix ones(int n, int m) {
        ComplexMatrix c = new ComplexMatrix(n, m, new double[n*m*2]);
        c.fill(1, 0);
        return c;
    }
    
    public static ComplexMatrix ones(int n) {
        ComplexMatrix c = new ComplexMatrix(n, n, new double[n*n*2]);
        c.fill(1, 0);
        return c;
    }
    
    public static ComplexMatrix magic(int n) {
        double[] data = MagicSquare.magic(n).getRowPackedCopy();
        double[] complex = new double[data.length*2];
        for(int i=0,j=0;i<complex.length;i+=2,j++) {
            complex[i] = data[j];
        }
        return new ComplexMatrix(n, n, complex);
    }
    
}
