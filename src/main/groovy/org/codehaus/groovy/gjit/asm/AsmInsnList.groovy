package org.codehaus.groovy.gjit.asm;

import groovy.lang.Closure

import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
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
        Object.metaClass.asm = { Closure c ->
            c.delegate = new AsmNodeBuilder()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c.call()
            if(c.delegate.list.size() == 1)
                c.delegate.list.get(0)
            else
                c.delegate.list
        }
        VarInsnNode.metaClass.equals = { obj ->
            return delegate.opcode == obj.opcode &&
                   delegate.var    == obj.var
        }
        MethodInsnNode.metaClass.equals = { obj ->
            return delegate.opcode == obj.opcode &&
                   delegate.owner  == obj.owner  &&
                   delegate.name   == obj.name   &&
                   delegate.desc   == obj.desc
        }
        TypeInsnNode.metaClass.equals = { obj ->
            return delegate.opcode == obj.opcode &&
                   delegate.desc   == obj.desc
        }
        LdcInsnNode.metaClass.equals = { obj ->
            return delegate.opcode == obj.opcode &&
                   delegate.cst    == obj.cst
        }
        installed = true
    }

}