package benchmarks;

import org.codehaus.groovy.geelab.linearalgebra.ComplexMatrix;
import org.codehaus.groovy.geelab.linearalgebra.ComplexMatrixHelper;
import org.codehaus.groovy.geelab.linearalgebra.FFTHelper;

public class FFTConv {

    public static void main(String[] args) {
        for(int m=1; m<=16; m++) {
            int N = (int) Math.pow(2, m);
            // TODO
            ComplexMatrix t = new ComplexMatrix(1, N, null);
            for(int i=0; i<N; i++) t.putAt(1, i+1,(double)i);
            ComplexMatrix h = t.negative().exp();
            ComplexMatrix H = FFTHelper.fft(h);
            ComplexMatrix x = ComplexMatrixHelper.ones(1, N);
            int Nrep = 500;
            for(int i=1; i<=Nrep; i++) {
                ComplexMatrix y = FFTHelper.ifft(FFTHelper.fft(x).dotMultiply(H));
            }
        }
    }

}


/*
for(m in 1..16) {
  N = 2 ** m
  t = 0..N-1
  h = exp(-t)
  H = fft(h)
  x = ones(1, N)
  Nrep = 500
  tic
  for(i in 1..Nrep) y = ifft(fft(x) .* H)
  t = toc
  println "Average FFT-convolution $N: ${t/Nrep}"
}
*/