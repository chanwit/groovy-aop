package org.codehaus.groovy.gjit.soot.heapsort.b1

import org.codehaus.groovy.gjit.benchmarks.Random

/*
    The Computer Language Shootout
    http://shootout.alioth.debian.org/

    contributed by Jochen Hinrichsen
*/
class HeapSort {

    static heapsort(n, ra) {
        def l, j, ir, i
        def rra

        l = (n >> 1) + 1
        ir = n
        while (true) {
            if (l > 1) {
                rra = ra[--l]
            } else {
                rra = ra[ir]
                ra[ir] = ra[1]
            }
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
        println ary[0]
        println ary[N-2]
        println ary[N-1]
    }
}