package org.codehaus.groovy.gjit.asm.heapsort;

import org.codehaus.groovy.gjit.benchmarks.Random;

public class Caller {

    static realTest(N) {
		double[] ary = (double[]) java.lang.reflect.Array.newInstance(double.class, N)
		def rndnum = new Random(1729)
		for (i in 0..<N) {
		    ary[i] = rndnum.nextDouble()
		}
        def start = System.nanoTime()
		HeapSort.heapsort(N-1, ary)
        def stop = System.nanoTime()
        println "HeapSort($N) result=${ary[N-1]}, time: ${stop-start}"
        // println stop-start
    }

}