package org.codehaus.groovy.gjit.asm.fannkuch

class FannkuchAspect {
    static aspect = {
        def pc = pcall('org.codehaus.groovy.gjit.asm.fannkuch.Fannkuch.fannkuch') & args(n)
        typing(pc) {
            n >> int
            // return int
        }
    }
}