package org.codehaus.groovy.gjit.soot.sieve;

public class Sieve {

	static sieve(m,n) {
		def i,k,ci
		def count,size
		def prime=0
		def N_Prime, L_Prime
		def flags = new boolean[m]
		def iter,j
		def ptr=0

		size = m - 1

		N_Prime = 0L
		L_Prime = 0L

		j = 0L
		for (iter=1; iter<=n; iter++) {
			count = 0
			for(i=0 ; i<=size ; i++) flags[ptr+i] = true
			ci = 0
			for(i=0 ; i<=size ; i++) {
				if(flags[ptr+i]) {
					count++
					prime = i + i + 3
					for(k = i + prime ; k<=size ; k+=prime) {
						ci++
						flags[ptr+k]=false
					}
				}
			}
			j = j + count
		}

		N_Prime = j / n
		L_Prime = prime
	}

	static void main(args) {
		sieve(100, 100)
	}
}
