package org.codehaus.groovy.gjit.asm.fibbonacci;

public class Caller {

    static realTest(i) {
        println "calling Fib $i"
        def start = System.currentTimeMillis()
        def result = Fib.fib(i)
        def stop = System.currentTimeMillis()
        println "result $result"
        println "${stop-start}"
    }

}