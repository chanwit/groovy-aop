package org.codehaus.groovy.gjit.asm.sieve

class Caller {

    static realTest(n) {
        def start = System.nanoTime()
        Sieve.sieve(n, 10)
        def stop = System.nanoTime()
        println "Sieve($n), time: ${stop-start}"
    }

}