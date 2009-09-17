package org.codehaus.groovy.geelab.linearalgebra;

import java.util.List;

public class Matrix {

    private static ThreadLocal<RowFetcher> ROW_FETCHER = new ThreadLocal<RowFetcher>() {
        @Override
        protected RowFetcher initialValue() {
            return new RowFetcher();
        }
    };

    private int rows;
    private int cols;
    private double[] data;

    Matrix(int rows, int cols, double[] data) {
        this.rows = rows;
        this.cols = cols;
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
    public Object getAt(List<?> coord) {
        int i = (Integer)coord.get(0);
        int j = (Integer)coord.get(1);
        return data[(i-1)*this.rows + (j-1)];
    }

    public double[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Matrix " + rows + " x " + cols;
    }


    public static class RowFetcher {

        private Matrix matrix;
        private int row;

        public RowFetcher(Matrix matrix, int i) {
            this.matrix = matrix;
            this.row = i;
        }

        private RowFetcher(){ }

        public Object getAt(int j) {
            return matrix.data[(row-1)*matrix.rows + (j-1)];
        }

    }
}

