package org.codehaus.groovy.geelab.linearalgebra;

import org.codehaus.groovy.geelab.osp.numerics.FFT;
import org.codehaus.groovy.geelab.osp.numerics.FFT2D;

public class FFTHelper {

    public static ComplexMatrix fft(ComplexMatrix mat) {
        if(mat.getRows() == 1) {
            FFT fft = new FFT(mat.getCols());
            double[] result = mat.getData().clone();
            fft.transform(result);
            return new ComplexMatrix(mat.getRows(), mat.getCols(), result);
        } else if(mat.getRows() == mat.getCols()) {
            return fft2(mat);
        }
        throw new RuntimeException("NYI");
    }

    public static ComplexMatrix ifft(ComplexMatrix mat) {
        if(mat.getRows() == 1) {
            FFT fft = new FFT(mat.getCols());
            double[] result = mat.getData().clone();
            fft.inverse(result);
            return new ComplexMatrix(mat.getRows(), mat.getCols(), result);
        } else if(mat.getRows() == mat.getCols()) {
            return ifft2(mat);
        }
        throw new RuntimeException("NYI");
    }

    public static ComplexMatrix fft2(ComplexMatrix mat) {
        FFT2D fft2d = new FFT2D(mat.getRows(), mat.getCols());
        double[] result = mat.getData().clone();
        fft2d.transform(result);
        return new ComplexMatrix(mat.getRows(), mat.getCols(), result);
    }

    public static ComplexMatrix ifft2(ComplexMatrix mat) {
        FFT2D fft2d = new FFT2D(mat.getRows(), mat.getCols());
        double[] result = mat.getData().clone();
        fft2d.inverse(result);
        return new ComplexMatrix(mat.getRows(), mat.getCols(), result);
    }

}
