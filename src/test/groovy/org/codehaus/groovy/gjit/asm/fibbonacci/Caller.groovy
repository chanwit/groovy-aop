package org.codehaus.groovy.gjit.asm.fibbonacci;

public class Caller {

    static realTest(i) {
        def start = System.nanoTime()
        def result = Fib.fib(i)
        def stop = System.nanoTime()
        // println "Fib($i) = $result, time: ${stop-start}"
        println stop-start
    }

}