package org.codehaus.groovy.gjit.asm;

import groovy.lang.IntRange;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList

class InsnListRange {

    IntRange range
    InsnList list

    public int size() {
        range.size()
    }
    public AbstractInsnNode get(int i) {
        list.get(range.fromInt + i)
    }
}
