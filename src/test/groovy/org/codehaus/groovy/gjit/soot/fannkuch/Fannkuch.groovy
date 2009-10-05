package org.codehaus.groovy.gjit.soot.fannkuch

class Fannkuch {

	static void main(args) {
		for (int i = 0; i < args.length; i++) {
			int n = Integer.parseInt(args[i])
			long t1 = System.nanoTime()
			System.out.println("Pfannkuchen(" + n + ") = " + fannkuch(n))
			long t2 = System.nanoTime()
			System.out.println("Duration " + (t2 - t1) / 1000.0 / 1000.0)
		}
	}

	static fannkuch(n) {
		def perm    = new int[n]
		def perm1   = new int[n]
		def count   = new int[n]
		def maxPerm = new int[n]
		def maxFlipsCount = 0
		def m = n - 1

		for(i in 0..<n) perm1[i] = i

		def r = n

		while(true) {

			while (r != 1) {
				count[r - 1] = r
				r--
			}

			if (!(perm1[0] == 0 || perm1[m] == m)) {
				for(i in 0..<n)
					perm[i] = perm1[i]

				def flipsCount = 0
				def k

				while (!((k = perm[0]) == 0)) {
					def k2 = (k + 1) >> 1
					for(i in 0..<k2) {
						def temp    = perm[i]
						perm[i]     = perm[k - i]
						perm[k - i] = temp
					}
					flipsCount++
				}

				if (flipsCount > maxFlipsCount) {
					maxFlipsCount = flipsCount
					for(i in 0..<n)
						maxPerm[i] = perm1[i]
				}
			}

			while (true) {
				if (r == n)
					return maxFlipsCount
				def perm0 = perm1[0]
				def i = 0
				while (i < r) {
					def j = i + 1
					perm1[i] = perm1[j]
					i = j
				}
				perm1[r] = perm0

				count[r] = count[r] - 1
				if (count[r] > 0)
					break
				r++
			}
		}
	}
}
