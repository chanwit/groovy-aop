package org.codehaus.groovy.gjit.soot.factorial;


	static fac(n) {
		if(n > 1)
			n * fac(n-1)
		else
			n
	}

}