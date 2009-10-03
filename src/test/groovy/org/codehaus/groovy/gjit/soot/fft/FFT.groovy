package org.codehaus.groovy.gjit.soot.fft

import org.codehaus.groovy.gjit.benchmarks.Random

/**
 * Computes FFT's of complex, double precision data where n is an integer power
 * of 2. This appears to be slower than the Radix2 method, but the code is
 * smaller and simpler, and it requires no extra storage.
 * <P>
 *
 * @author Bruce R. Miller bruce.miller@nist.gov,
 * @author Derived from GSL (Gnu Scientific Library),
 * @author GSL's FFT Code by Brian Gough bjg@vvv.lanl.gov
 */

/*
 * See {@link ComplexDoubleFFT ComplexDoubleFFT} for details of data layout.
 */

class FFT {

    static num_flops(N) {
        return (5.0 * N - 2) * log2(N) + 2 * (N + 1)
    }

    /** Compute Fast Fourier Transform of (complex) data, in place. */
    static transform(data) {
        transformInternal(data, -1)
    }

    /** Compute Inverse Fast Fourier Transform of (complex) data, in place. */
    static inverse(data) {
        transformInternal(data, +1)
        // Normalize
        def nd = data.length
        def n = nd / 2
        def norm = 1 / n
        for(i in 0..<nd)
            data[i] *= norm
    }

    /**
     * Accuracy check on FFT of data. Make a copy of data, Compute the FFT, then
     * the inverse and compare to the original. Returns the rms difference.
     */
    static test(data) {
        def nd = data.length
        // Make duplicate for comparison
        def copy = new double[nd]
        System.arraycopy(data, 0, copy, 0, nd)
        // Transform & invert
        transform(data)
        inverse(data)
        // Compute RMS difference.
        def diff = 0.0
        for (i in 0..<nd) {
            def d = data[i] - copy[i]
            diff += d * d
        }
        return Math.sqrt(diff / nd)
    }

    /** Make a random array of n (complex) elements. */
    static makeRandom(n) {
        def nd = 2 * n
        def data = new double[nd]
        def random = new Random(1729)
        for(i in 0..<nd) {
            data[i] = random.nextDouble()            
        }
        return data
    }

    /** Simple Test routine. */
    static void main(args) {
        if (args.length == 0) {
            int n = 1024
            println "n=${n} => RMS Error=${test(makeRandom(n))}"
        }
        for (int i = 0; i < args.length; i++) {
            int n = args[i].toInteger()
            println "n=${n} => RMS Error=${test(makeRandom(n))}"
        }
    }

    /* ______________________________________________________________________ */

    static log2(n) {
        def log = 0
        for (def k = 1; k < n; k *= 2) log++
        if (n != (1 << log))
            throw new Error("FFT: Data length is not a power of 2!: $n")
        return log
    }

    static transformInternal(data, direction) {
        if (data.length == 0)
            return
        def n = data.length / 2
        if (n == 1)
            return // Identity operation!
        def logn = log2(n)

        /* bit reverse the input data for decimation in time algorithm */
        bitreverse(data)

        /* apply fft recursion */
        /* this loop executed log2(N) times */
        def dual = 1
        for (def bit = 0; bit < logn; bit++) {
            def w_real = 1.0
            def w_imag = 0.0

            def theta = 2.0 * direction * Math.PI / (2.0 * dual)
            def s = Math.sin(theta)
            def t = Math.sin(theta / 2.0)
            def s2 = 2.0 * t * t

            /* a = 0 */
            for (def b = 0; b < n; b += 2 * dual) {
                def i = 2 * b
                def j = 2 * (b + dual)

                def wd_real = data[j    ]
                def wd_imag = data[j + 1]

                data[j    ] =  data[i    ] - wd_real
                data[j + 1] =  data[i + 1] - wd_imag
                data[i    ] += wd_real
                data[i + 1] += wd_imag
            }

            /* a = 1 .. (dual-1) */
            for (a in 1..<dual) {
                /* trignometric recurrence for w-> exp(i theta) w */
                def tmp_real = w_real - s * w_imag - s2 * w_real
                def tmp_imag = w_imag + s * w_real - s2 * w_imag
                w_real = tmp_real
                w_imag = tmp_imag

                for (def b = 0; b < n; b += 2 * dual) {
                    def i = 2 * (b + a)
                    def j = 2 * (b + a + dual)

                    def z1_real = data[j    ]
                    def z1_imag = data[j + 1]

                    def wd_real = w_real * z1_real - w_imag * z1_imag
                    def wd_imag = w_real * z1_imag + w_imag * z1_real

                    data[j    ] = data[i    ] - wd_real
                    data[j + 1] = data[i + 1] - wd_imag
                    data[i    ] += wd_real
                    data[i + 1] += wd_imag
                }
            }
            dual *= 2
        }
    }

    static bitreverse(data) {
        /* This is the Goldrader bit-reversal algorithm */
        def n = data.length / 2
        def nm1 = n - 1
        def i = 0
        def j = 0
        for (; i < nm1; i++) {
            def ii = 2*i
            def jj = 2*j
            def k = n / 2
            if (i < j) {
                def tmp_real = data[ii    ]
                def tmp_imag = data[ii + 1]
                data[ii    ] = data[jj    ]
                data[ii + 1] = data[jj + 1]
                data[jj    ] = tmp_real
                data[jj + 1] = tmp_imag
            }

            while (k <= j) {
                j = j - k
                k = k / 2
            }
            j += k
        }
    }
}
