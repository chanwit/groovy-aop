package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;

import org.codehaus.groovy.gjit.asm.ConstantHolder;
import org.codehaus.groovy.gjit.asm.ConstantHolder.ConstantPack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ConstantCollector implements Transformer, Opcodes {

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        //
        // TODO should be checking for __timeStamp first
        //
        if(body.name.equals("<clinit>")) {
            InsnList units = body.instructions;
            AbstractInsnNode s = units.getFirst();
            Object value=null;
            String owner=null;
            ConstantPack pack = new ConstantHolder.ConstantPack();
            while(s != null) {
                if(s.getOpcode() == LDC) {
                    value = ((LdcInsnNode)s).cst;
                } else if(s.getOpcode() == PUTSTATIC) {
                    FieldInsnNode f = (FieldInsnNode)s;
                    if(f.name.startsWith("$const$")) {
                        pack.put(f.name, value);
                    }
                    if(f.name.equals("__timeStamp")) {
                        owner = f.owner;
                    }
                }
                s = s.getNext();
            }
            if (pack.size() != 0) {
                ConstantHolder.v().put(owner, pack);
            }
        }
    }

//    static <clinit>()V
//    NEW java/lang/Long
//    DUP
//    LDC 0
//    INVOKESPECIAL java/lang/Long.<init>(J)V
//    DUP
//    CHECKCAST java/lang/Long
//    PUTSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.__timeStamp__239_neverHappen1251822915656 : Ljava/lang/Long;
//    POP
//    NEW java/lang/Long
//    DUP
//    LDC 1251822915656
//    INVOKESPECIAL java/lang/Long.<init>(J)V
//    DUP
//    CHECKCAST java/lang/Long
//    PUTSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.__timeStamp : Ljava/lang/Long;
//    POP
//    NEW java/lang/Integer
//    DUP
//    LDC 40
//    INVOKESPECIAL java/lang/Integer.<init>(I)V
//    DUP
//    CHECKCAST java/lang/Integer
//    PUTSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$2 : Ljava/lang/Integer;
//    POP
//    NEW java/lang/Integer
//    DUP
//    LDC 1
//    INVOKESPECIAL java/lang/Integer.<init>(I)V
//    DUP
//    CHECKCAST java/lang/Integer
//    PUTSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$1 : Ljava/lang/Integer;
//    POP
//    NEW java/lang/Integer
//    DUP
//    LDC 2
//    INVOKESPECIAL java/lang/Integer.<init>(I)V
//    DUP
//    CHECKCAST java/lang/Integer
//    PUTSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$const$0 : Ljava/lang/Integer;
//    POP
//    INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib()Ljava/lang/Class;
//    INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$get$$class$java$lang$Class()Ljava/lang/Class;
//    INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.castToType(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
//    CHECKCAST java/lang/Class
//    DUP
//    CHECKCAST java/lang/Class
//    PUTSTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib.$ownClass : Ljava/lang/Class;
//    POP
//    RETURN
//   L0
//    RETURN
//    GOTO L0
//    MAXSTACK = 4
//    MAXLOCALS = 0

}
