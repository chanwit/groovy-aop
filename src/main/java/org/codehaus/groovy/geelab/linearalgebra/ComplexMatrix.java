package org.codehaus.groovy.geelab.linearalgebra;

import groovy.lang.Closure;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ComplexMatrix {

    private static ThreadLocal<RowFetcher> ROW_FETCHER = new ThreadLocal<RowFetcher>() {
        @Override
        protected RowFetcher initialValue() {
            return new RowFetcher();
        }
    };

    private int rows;
    private int cols;
    private double[] data;

    private int rowspan;

    public ComplexMatrix(int rows, int cols, double[] data) {
        this.rows = rows;
        this.cols = cols;
        this.rowspan = cols * 2;
        this.data = data;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Groovy compatible method for []
     *
     * @param i
     * @return
     */
    public Object getAt(int i) {
        RowFetcher rf = ROW_FETCHER.get();
        rf.matrix = this;
        rf.row = i;
        return rf;
    }

    /**
     *
     * @param coord
     * @return
     */
    public Complex getAt(List<?> coord) {
        int i = (Integer)coord.get(0);
        int j = (Integer)coord.get(1);
        double re = data[(i-1)*rowspan + ((j-1)*2)    ];
        double im = data[(i-1)*rowspan + ((j-1)*2) + 1];
        return new Complex(re, im);
    }

    public void putAt(List<?> coord, double d) {
        int i = (Integer)coord.get(0);
        int j = (Integer)coord.get(1);
        data[(i-1)*rowspan + ((j-1)*2)    ] = d;
        data[(i-1)*rowspan + ((j-1)*2) + 1] = 0;
    }

    public void putAt(List<?> coord, Complex c) {
        int i = (Integer)coord.get(0);
        int j = (Integer)coord.get(1);
        data[(i-1)*rowspan + ((j-1)*2)    ] = c.getReal();
        data[(i-1)*rowspan + ((j-1)*2) + 1] = c.getImaginary();
    }

    public double[] getData() {
        return data;
    }

    public void fill(double re) {
        for(int i=0;i < data.length; i+=2) {
            data[i] = re;
        }
    }

    public void fill(double re, double im) {
        for(int i=0; i < data.length; i+=2) {
            data[i]   = re;
            data[i+1] = im;
        }
    }

    public void fill(Complex c) {
        for(int i=0; i < data.length; i+=2) {
            data[i]   = c.getReal();
            data[i+1] = c.getImaginary();
        }
    }

    public void dump() {
        dump(System.out);
    }

    public void dump(PrintStream out) {
        for(int i=0; i<rows;i++) {
            for(int j=0; j<cols;j++) {
                double re = data[(i*rowspan) + (2*j)];
                double im = data[(i*rowspan) + (2*j) + 1];
                if(im == 0) out.print(re);
                else out.print(re + " + " + im + "i");
                if(j == 9) {
                    out.print(" ...");
                    break;
                } else {
                    out.print("\t");
                }
            }
            out.println();
            if(i == 9) {
                out.println("...");
                break;
            }
        }
    }

    public ComplexMatrix dotMultiply(ComplexMatrix c) {
        double[] result = new double[rows*cols*2];
        for(int i=0; i<rows;i++) {
            for(int j=0; j<cols;j++) {
                int k = (i*rowspan) + (2*j);
                int l = k + 1;
                if(data[l] == 0 && c.data[l] == 0) {
                    result[k] = data[k] * c.data[k];
                    result[l] = 0;
                } else {
                    result[k] = (data[k] * c.data[k]) - (data[l] * c.data[l]);
                    result[l] = (data[k] * c.data[l]) + (data[l] * c.data[k]);
                }
            }
        }
        return new ComplexMatrix(rows, cols, result);
    }

    public ComplexMatrix negative() {
        double[] result = new double[rows*cols*2];
        for(int i=0; i<rows;i++) {
            for(int j=0; j<cols;j++) {
                int k = (i*rowspan) + (2*j);
                int l = k + 1;
                result[k] = -data[k];
                result[l] = -data[l];
            }
        }
        return new ComplexMatrix(rows, cols, result);
    }

    public ComplexMatrix exp() {
        double[] result = new double[rows*cols*2];
        for(int j=0; j<cols;j++) {
            for(int i=0; i<rows;i++) {
                int k = (i*rowspan) + (2*j);
                int l = k + 1;
                double re = data[k];
                double im = data[l];
                if(im != 0) {
                    result[k] = Math.exp(re) * Math.cos(im);
                    result[l] = Math.exp(re) * Math.sin(im);
                } else {
                    result[k] = Math.exp(re);
                    result[l] = 0;
                }
            }
        }
        return new ComplexMatrix(rows, cols, result);
    }

    public Object[] find() {
        List<Integer> r = new ArrayList<Integer>();
        List<Integer> c = new ArrayList<Integer>();
        List<Object>  v = new ArrayList<Object>();
        for(int j=0; j<cols;j++) {
            for(int i=0; i<rows;i++) {
                double re = data[(i*rowspan) + (2*j)];
                double im = data[(i*rowspan) + (2*j) + 1];
                if(re != 0 && im !=0) {
                    r.add(i+1);
                    c.add(j+1);
                    v.add(new Complex(re, im));
                }
            }
        }
        return new Object[]{r,c,v};
    }

    public Object[] find(Closure closure) {
        List<Integer> r = new ArrayList<Integer>();
        List<Integer> c = new ArrayList<Integer>();
        List<Object>  v = new ArrayList<Object>();
        for(int j=0; j<cols;j++) {
            for(int i=0; i<rows;i++) {
                double re = data[(i*rowspan) + (2*j)];
                double im = data[(i*rowspan) + (2*j) + 1];
                Complex value = new Complex(re, im);
                boolean found = (Boolean)closure.call(value);
                if(found) {
                    r.add(i+1);
                    c.add(j+1);
                    v.add(found);
                }
            }
        }
        return new Object[]{r,c,v};
    }

    public void each(Closure closure) {
        for(int j=0; j<cols;j++) {
            for(int i=0; i<rows;i++) {
                int k = (i*rowspan) + (2*j);
                int l = k + 1;
                double re = data[k];
                double im = data[l];
                Complex value = new Complex(re, im);
                Object result = closure.call(value);
                if(result instanceof Complex) {
                    Complex c = (Complex)result;
                    data[k] = c.getReal();
                    data[l] = c.getImaginary();
                } else if(result instanceof Number) {
                    data[k] = ((Number)result).doubleValue();
                    data[l] = 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Complex Matrix " + rows + " x " + cols;
    }

    public static class RowFetcher {

        private ComplexMatrix matrix;
        private int row;

        public RowFetcher(ComplexMatrix matrix, int i) {
            this.matrix = matrix;
            this.row = i;
        }

        private RowFetcher(){ }

        public Complex getAt(int j) {
            double re = matrix.data[(row-1)*matrix.rowspan + ((j-1)*2)    ];
            double im = matrix.data[(row-1)*matrix.rowspan + ((j-1)*2) + 1];
            return new Complex(re, im);
        }

    }

	public void putAt(int i, int j, double d) {
		data[(i-1)*rowspan + 2*(j-1)] = d;
		data[(i-1)*rowspan + 2*(j-1) + 1] = 0;
	}
}

