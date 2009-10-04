package org.codehaus.groovy.gjit.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.AbstractVisitor;

public class DebugUtils {

    public static boolean enableDebug = true;
    public static boolean enableDump = true;

    public static void toggle() {
        enableDebug = !enableDebug;
        enableDump = !enableDump;
    }

    public static void dump(AbstractInsnNode insn) {

        if(enableDebug == false) return;
        if(enableDump  == false) return;

        int opcode = insn.getOpcode();
        if(insn.getOpcode() != -1) System.out.print(":: " + AbstractVisitor.OPCODES[opcode]);
        if(insn instanceof InsnNode) {
            System.out.println();
        } else if(insn instanceof IntInsnNode) {
            int operand = ((IntInsnNode)insn).operand;
            System.out.print(' ');
            System.out.println(opcode == Opcodes.NEWARRAY ? AbstractVisitor.TYPES[operand] : Integer.toString(operand));
        } else if(insn instanceof VarInsnNode) {
            int var = ((VarInsnNode)insn).var;
            System.out.print(' ');
            System.out.println(var);
        } else if(insn instanceof TypeInsnNode) {
            String desc = ((TypeInsnNode)insn).desc;
            System.out.print(' ');
            System.out.println(desc);
        } else if(insn instanceof FieldInsnNode) {
            System.out.print(' ');
            System.out.print(((FieldInsnNode)insn).owner);
            System.out.print('.');
            System.out.print(((FieldInsnNode)insn).name);
            System.out.print(" : ");
            System.out.println(((FieldInsnNode)insn).desc);
        } else if(insn instanceof MethodInsnNode){
            System.out.print(' ');
            System.out.print(((MethodInsnNode)insn).owner);
            System.out.print('.');
            System.out.print(((MethodInsnNode)insn).name);
            System.out.println(((MethodInsnNode)insn).desc);
        } else if(insn instanceof JumpInsnNode) {
            System.out.print(' ');
            System.out.println(((JumpInsnNode)insn).label.getLabel());
        } else if(insn instanceof LdcInsnNode) {
            System.out.print(' ');
            System.out.println(((LdcInsnNode)insn).cst);
        } else if(insn instanceof IincInsnNode) {
            System.out.print(' ');
            System.out.print(((IincInsnNode)insn).var);
            System.out.print(' ');
            System.out.println(((IincInsnNode)insn).incr);
        } else {
        	System.out.println();
        }
    }

    public static void println(Object o) {
        if(enableDebug == false) return;
        System.out.println(o.toString());
    }

    public static void print(Object o) {
        if(enableDebug == false) return;
        System.out.print(o);
    }

    public static void println() {
        if(enableDebug == false) return;
        System.out.println();
    }

}
