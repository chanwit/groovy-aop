package org.codehaus.groovy.gjit.soot.fibbonacci;
class Fib {

	static fib(n) {
		if (n < 2)
			n
		else
			fib(n-1) + fib(n-2)
	}

	static void main(args) {
		Fib.fib(5)
	}

}
