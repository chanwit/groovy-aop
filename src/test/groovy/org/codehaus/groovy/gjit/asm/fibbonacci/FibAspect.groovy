package org.codehaus.groovy.gjit.asm.fibbonacci
class FibAspect {

    static aspect = {
        def fib = Fib.class.name
        def pc1 = pcall("org.codehaus.groovy.gjit.asm.fibbonacci.Fib.fib") & args(n)
        typing(pc1) {
            n >>   int
            return int
        }
        def pc2 = (pcall("java.lang.Integer.plus" ) |
                   pcall("java.lang.Integer.minus") ) & args(i, j)
        typing(pc2) {
            i >>   int
            j >>   int
            return int
        }
    }

}
