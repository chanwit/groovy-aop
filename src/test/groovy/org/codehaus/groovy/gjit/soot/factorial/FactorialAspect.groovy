package org.codehaus.groovy.gjit.soot.factorial;
class FactorialAspect {

	static aspect = {
		def pc1 = pcall("Factorial.fac") & args(n)
		typing(pc1) {
			n >> int
		}

		def pc2 = ( pcall("Number.multiply") | pcall("Number.minus") ) &
		            within("Factorial.fac")
		typing(pc2 & pthis(i) & args(j)) {
			[i, j] >> int
		}
	}

}
