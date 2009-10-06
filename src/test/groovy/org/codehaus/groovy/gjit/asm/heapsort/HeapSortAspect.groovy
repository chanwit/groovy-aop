package org.codehaus.groovy.gjit.asm.heapsort

class HeapSortAspect {

	static aspect = {
		def pc = pcall('org.codehaus.groovy.gjit.asm.heapsort.HeapSort.heapsort') & args(n, data)
		typing(pc) {
			n >> int
			data >> double[]
            return void
		}
        def pc2 = (pcall("java.lang.Integer.plus" ) |
                   pcall("java.lang.Integer.minus") ) & args(i, j)
		typing(pc2) {
			i >>   int
			j >>   int
			return int
		}

        def pc3 = pcall("java.lang.Integer.next") & args(k)
        typing(pc3) { k >> int }
	}
}
