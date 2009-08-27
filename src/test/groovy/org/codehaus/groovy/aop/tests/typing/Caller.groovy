package org.codehaus.groovy.aop.tests.typing;


	void realTest() {
        def result = []
        def start = System.currentTimeMillis()
        for(i in 1..100000) {
        	def target = new Target()
        	result << target.method(i)
        }
        def stop = System.currentTimeMillis()
        println "time: ${stop-start}"
	}

}