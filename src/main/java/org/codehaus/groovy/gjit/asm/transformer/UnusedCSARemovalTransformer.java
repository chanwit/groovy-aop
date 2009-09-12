package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class UnusedCSARemovalTransformer implements Transformer, Opcodes {

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {

        //
        // do not perform this removal on special methods
        //
        if(body.name.equals("$getCallSiteArray")) return;

        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();

        boolean found = false;
        int csaVar = -1;
        MethodInsnNode m = null;

        while(s != null) {
            if(s.getOpcode() != INVOKESTATIC) { s = s.getNext(); continue; }

            m = (MethodInsnNode)s;
            if(m.name.equals("$getCallSiteArray") &&
               m.desc.equals("()[Lorg/codehaus/groovy/runtime/callsite/CallSite;")) {
                AbstractInsnNode s0 = m.getNext();
                assert s0.getOpcode() == ASTORE;
                csaVar = ((VarInsnNode)s0).var;
                break;
            } else {
                s = s.getNext();
            }
        }

        //
        // cannot find the call site array
        // quietly exit
        //
        if(csaVar == -1) return;

        //
        // check to see if ALOAD csaVar is used
        //
        for(;s != null; s = s.getNext()) {
            if(s.getOpcode() != ALOAD)     continue;
            VarInsnNode aload = (VarInsnNode)s;
            if(aload.var != csaVar) continue;
            found = true;
            break;
        }

        //
        // if no ALOAD csaVar
        // and there are instructions to be removed, do so
        //
        if(!found && m != null) {
            units.remove(m.getNext());
            units.remove(m);
        }
    }

}
