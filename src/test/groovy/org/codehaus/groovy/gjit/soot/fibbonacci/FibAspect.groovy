package org.codehaus.groovy.gjit.soot.fibbonacci;
class FibAspect {

	static aspect = {
		def pc1 = pcall("Fib.fib") & within("static Fib.main") & args(n)
		def pc2 = pcall("Fib.fib") & args(n)
		typing(pc1, pc2) {
			n >> int
		}
	}

}
