package org.codehaus.groovy.gjit.asm.fannkuch

class Caller {

    static realTest(n) {
        def start = System.nanoTime()
        int f = Fannkuch.fannkuch(n)
        def stop = System.nanoTime()
        println "Fannkuch($n) result=$f, time: ${stop-start}"
    }

}