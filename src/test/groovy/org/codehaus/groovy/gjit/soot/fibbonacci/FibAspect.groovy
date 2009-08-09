package org.codehaus.groovy.gjit.soot.fibbonacci;
class FibAspect {

	static aspect = {
		def pc = pcall("${Fib.class.name}.fib") & args(n)
		typing(pc) {
			n >> int
		}
	}

}
