package org.codehaus.groovy.gjit.soot.fibbonacci;
class FibAspect {

	static aspect = {
		def Fib = "org.codehaus.groovy.gjit.soot.fibbonacci.Fib"
		def pc = pcall("${Fib}.fib") & args(n)
		typing(pc) {
			n >> int
		}
	}

}
