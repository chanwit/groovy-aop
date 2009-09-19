package org.codehaus.groovy.geelab.linearalgebra;

import groovy.lang.Closure;

import java.io.PrintStream;
import java.io.PrintWriter;
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


    @Override
    public String toString() {
        return "Complex Matrix " + rows + " x " + cols;
    }

    public Object asType(Class<?> c) {
        System.out.println(c);
        return this;
    }

    public Object[] find() {
        List r = new ArrayList();
        List c = new ArrayList();
        List v = new ArrayList();
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
        List r = new ArrayList();
        List c = new ArrayList();
        List v = new ArrayList();
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
}

