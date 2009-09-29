package org.codehaus.groovy.gjit.asm.transformer;

import java.util.ArrayList;
import java.util.Map;

import org.codehaus.groovy.gjit.asm.CallSiteNameHolder;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CallSiteNameCollector implements Transformer, Opcodes {

//	  // access flags 4106
//	  private static $createCallSiteArray()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;
//	    NEW org/codehaus/groovy/runtime/callsite/CallSiteArray
//	    DUP
//	    GETSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$ownClass : Ljava/lang/Class;
//	    LDC 7
//	    ANEWARRAY java/lang/String
//	    DUP
//	    LDC 0
//	    LDC "plus"
//	    AASTORE
//	    DUP
//      ...
//	    AASTORE
//	    INVOKESPECIAL org/codehaus/groovy/runtime/callsite/CallSiteArray.<init>(Ljava/lang/Class;[Ljava/lang/String;)V
//	    ARETURN
//	    MAXSTACK = 7
//	    MAXLOCALS = 0

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        if(body.name.equals("$createCallSiteArray") == false) return;
        if(body.desc.equals("()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;")==false) return;

        InsnList units = body.instructions;
        AbstractInsnNode s = units.getFirst();
        ArrayList<String> names = new ArrayList<String>();
        String owner = null;
        while(s != null) {
            if(s.getOpcode() == GETSTATIC && ((FieldInsnNode)s).name.equals("$ownClass")) {
                FieldInsnNode f = (FieldInsnNode)s;
                owner = f.owner;
            } else if(s.getOpcode() == LDC) {
                 LdcInsnNode ldc = ((LdcInsnNode)s);
                 if(ldc.cst instanceof String) {
                     names.add((String) ldc.cst);
                 }
            }
            s = s.getNext();
        }
        // System.out.println("owner: " + owner);
        if(owner != null) {
            CallSiteNameHolder.v().put(owner, names.toArray(new String[names.size()]));
        }
        else {            
            CallSiteNameHolder.v().put((String)options.get("name"), names.toArray(new String[names.size()]));
        }
            // throw new RuntimeException("owner is null: body's name=" + body.name + ": name array size=" + names.size());
            // else             
            // do nothing. probably a new generated class
    }

}
