package org.codehaus.groovy.gjit.soot.factorial;
class FactorialAspect {

	static aspect = {
		def pc1 = pcall("${Factorial.class.name}.fac") & args(n)
		typing(pc1) {
			n >> int
		}

		def pc2 = ( pcall("${Integer.class.name}.multiply") |
				    pcall("${Integer.class.name}.minus")    )  &
		            within("${Factorial.class.name}.fac")
		typing(pc2 & pthis(i) & args(j)) {
			[i, j] >> int
		}
	}

}
