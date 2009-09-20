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
            return real + " - " + -imaginary + "i";
        } else {
            return real + "";
        }
    }

    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }

    public Complex reciprocal() {
        double scale = real*real + imaginary*imaginary;
        return new Complex(real / scale, -imaginary / scale);
    }

    public Complex multiply(Complex b) {
        Complex a = this;
        double real = a.real * b.real - a.imaginary * b.imaginary;
        double imag = a.real * b.imaginary + a.imaginary * b.real;
        return new Complex(real, imag);
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

    public Complex exp() {
        return new Complex(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
    }

    public Complex sin() {
        return new Complex(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
    }

    public Complex cos() {
        return new Complex(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
    }

    public Complex divide(Complex b) {
        Complex a = this;
        return a.multiply(b.reciprocal());
    }

    public Complex tan() {
        return sin().divide(cos());
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
