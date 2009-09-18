package org.codehaus.groovy.geelab.linearalgebra;

public class Complex {

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
}
