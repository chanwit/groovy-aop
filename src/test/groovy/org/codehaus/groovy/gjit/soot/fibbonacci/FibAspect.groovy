package org.codehaus.groovy.gjit.soot.fibbonacci

import org.codehaus.groovy.runtime.ScriptBytecodeAdapter as SBA
class FibAspect {

	static aspect = {
		def fib = Fib.class.name
		def pc0 = pcall("${SBA.class.name}.compareLessThan") &
		          within("${fib}.fib") & args(i, j)
		typing(pc0) {
			i >>   int
			j >>   int
			return int
		}

		def pc1 = pcall("${fib}.fib") & args(n)
		typing(pc1) {
			n >>   int
			return int
		}

	}

}
