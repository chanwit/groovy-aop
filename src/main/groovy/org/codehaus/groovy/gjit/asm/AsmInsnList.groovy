package org.codehaus.groovy.gjit.asm;

import groovy.lang.Closure

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList

public class AsmInsnList {

    static installed = false

    static install() {
        if(installed) return
        InsnList.metaClass.append = { Closure c ->
            c.delegate = new AsmNodeBuilder()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c.call()
            delegate.add( c.delegate.list )
        }
        InsnList.metaClass.getAt = { i ->
            delegate.get(i)
        }
        Class.metaClass.getDesc = { ->
            Type.getDescriptor(delegate)
        }
        Class.metaClass.getInternalName = { ->
            Type.getInternalName(delegate)
        }

        installed = true
    }

}