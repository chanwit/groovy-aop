package org.codehaus.groovy.gjit.soot.partialsums;
public class PartialSumsAspect {

	static aspect = {
		def pc = pcall("Number+.plus")     |
		         pcall("Number+.minus")    |
		         pcall("Number+.div")      |
		         pcall("Number+.multiply")

		typing(pc & pthis(m) & args(n)) {
			[m, n] >> double
		}
	}

}
