package org.codehaus.groovy.gjit.soot.partialsum
class PartialSums {

	static void main(args) {
		def n = Integer.parseInt(args[0])

		def twothirds = 2.0/3.0
		def a1 = 0.0D
		def a2 = 0.0D
		def a3 = 0.0D
		def a4 = 0.0D
		def a5 = 0.0D
		def a6 = 0.0D
		def a7 = 0.0D
		def a8 = 0.0D
		def a9 = 0.0D

		def alt = -1.0D
		def k   =  1.0D

		while (k <= n) {
		   def k2 = k * k
		   def k3 = k2 * k
		   def sk = Math.sin(k)
		   def ck = Math.cos(k)
		   alt = -alt

		   a1 += twothirds**(k-1.0)
		   a2 += 1.0 / Math.sqrt(k)
		   a3 += 1.0 / (k*(k+1.0))
		   a4 += 1.0 / (k3*sk*sk)
		   a5 += 1.0 / (k3*ck*ck)
		   a6 += 1.0 / k
		   a7 += 1.0 / k2
		   a8 += alt / k
		   a9 += alt / (2.0*k - 1.0)

		   k += 1.0
		}

		println "${a1}\t(2/3)^k"
		println "${a2}\tk^-0.5"
		println "${a3}\t1/k(k+1)"
		println "${a4}\tFlint Hills"
		println "${a5}\tCookson Hills"
		println "${a6}\tHarmonic"
		println "${a7}\tRiemann Zeta"
		println "${a8}\tAlternating Harmonic"
		println "${a9}\tGregory"
	}

}

