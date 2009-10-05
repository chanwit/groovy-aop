package org.codehaus.groovy.gjit.asm.sieve

class SieveAspect {
    static aspect = {
        def pc = pcall('org.codehaus.groovy.gjit.asm.sieve.Sieve.sieve') & args(m, n)
        typing(pc) {
            m >> int
            n >> int
        }
    }
}