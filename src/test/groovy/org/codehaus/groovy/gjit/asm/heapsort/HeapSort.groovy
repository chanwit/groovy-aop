package org.codehaus.groovy.gjit.asm.heapsort

import org.codehaus.groovy.gjit.benchmarks.Random

/*
	The Computer Language Shootout
	http://shootout.alioth.debian.org/

	contributed by Jochen Hinrichsen
*/
class HeapSort {
    public static final long IM = 139968
    public static final long IA =   3877
    public static final long IC =  29573

    public static long last = 42
    static gen_random(double max) {
		max * (last = (last * IA + IC) % IM) / IM
    }

    static heapsort(int n, double[] ra) {
		int l, j, ir, i
		double rra

		l = (n >> 1) + 1
		ir = n
		while (true) {
		    if (l > 1) {
				rra = ra[--l]
		    } else {
				rra = ra[ir]
				ra[ir] = ra[1]
				if (--ir == 1) {
				    ra[1] = rra
				    return
				}
		    }
		    i = l
		    j = l << 1
		    while (j <= ir) {
				if (j < ir && ra[j] < ra[j+1]) { ++j }
				if (rra < ra[j]) {
				    ra[i] = ra[j]
				    j += (i = j)
				} else {
				    j = ir + 1
				}
		    }
		    ra[i] = rra
		}
	}
    static void main(args) {
    	def N = (args.length == 0) ? 1000 : args[0].toInteger()
		def nf = java.text.NumberFormat.getInstance()
		nf.setMaximumFractionDigits(10)
		nf.setMinimumFractionDigits(10)
		nf.setGroupingUsed(false)
		double[] ary = (double[]) java.lang.reflect.Array.newInstance(double.class, N)
		def rndnum = new Random(1729)
		for (i in 0..<N) {
		    ary[i] = rndnum.nextDouble()
		}
		heapsort(N-1, ary)
		//println nf.format(ary[0])
		//println nf.format(ary[N-2])
		//println nf.format(ary[N-1])
		println ary[0]
		println ary[N-2]
		println ary[N-1]
    }
}