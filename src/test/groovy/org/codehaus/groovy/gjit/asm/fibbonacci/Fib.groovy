package org.codehaus.groovy.gjit.asm.fibbonacci;
class Fib {

    static fib(n) {
        if (n < 2)
            n
        else
            fib(n-1) + fib(n-2)
    }

    static void main(args) {
        println Fib.fib(5)
        println Fib.fib(10)
        println Fib.fib(15)
        println Fib.fib(20)
        println Fib.fib(25)
        println Fib.fib(30)
        println Fib.fib(35)
    }

}
