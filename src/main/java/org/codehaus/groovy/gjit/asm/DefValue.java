package org.codehaus.groovy.gjit.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.BasicValue;

public class DefValue extends BasicValue {

    public final AbstractInsnNode source;

    public DefValue(AbstractInsnNode insn, Type type) {
        super(type);
        this.source = insn;
    }

    @Override
    public String toString() {
        if (this == UNINITIALIZED_VALUE) {
            return ".";
        } else if (this.getType() == ((BasicValue)RETURNADDRESS_VALUE).getType()) {
            return "A";
        } else if (this.getType() == ((BasicValue)REFERENCE_VALUE).getType()) {
            return "R";
        } else {
            return getType().getDescriptor();
        }
    }




}
