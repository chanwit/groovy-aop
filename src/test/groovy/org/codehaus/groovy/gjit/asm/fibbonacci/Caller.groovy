package org.codehaus.groovy.gjit.asm.fibbonacci;

public class Caller {

    static realTest(i) {
        def start = System.currentTimeMillis()
        def result = Fib.fib(i)
        def stop = System.currentTimeMillis()
        println "Fib($i) = $result, time: ${stop-start}"
    }

}