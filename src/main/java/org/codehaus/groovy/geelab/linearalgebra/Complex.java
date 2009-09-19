package org.codehaus.groovy.geelab.linearalgebra;

public class Complex implements Comparable {

    private double real;
    private double imaginary;

    public Complex(double re, double im) {
        this.real = re;
        this.imaginary = im;
    }

    public double getReal() {
        return real;
    }
    public void setReal(double real) {
        this.real = real;
    }
    public double getImaginary() {
        return imaginary;
    }
    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    @Override
    public String toString() {
        if(imaginary > 0) {
            return real + " + " + imaginary + "i";
        } else if(imaginary < 0) {
            return real + " - " + Math.abs(imaginary) + "i";
        } else {
            return real + "";
        }
    }

    public Complex multiply(double t) {
        return new Complex(this.real * t, this.imaginary * t);
    }

    public Complex multiply(int t) {
        return new Complex(this.real * t, this.imaginary * t);
    }

    public Complex plus(double t) {
        return new Complex(this.real * t, this.imaginary);
    }

    public Complex plus(int t) {
        return new Complex(this.real + t, this.imaginary);
    }

    public static class Imaginary {
        public final double v;
        public Imaginary(double v) {
            this.v = v;
        }
        public Object plus(double n) {
            return new Complex(n, this.v);
        }
        public Object plus(Complex n) {
            return new Complex(n.real, n.imaginary + this.v);
        }
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Integer) {
            Integer i = (Integer)o;
            if(real == i && imaginary == 0)return 0;
            else if(real > i) return  1;
            else if(real < i) return -1;
        }
        throw new RuntimeException("NIY");
    }
}
