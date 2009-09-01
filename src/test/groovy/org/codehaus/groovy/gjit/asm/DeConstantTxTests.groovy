package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.*
import org.objectweb.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*;

public class DeConstantTxTests extends GroovyTestCase implements Opcodes {

    static FIB_NAME = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib"

    void testCollector() {
        ConstantHolder.v().clear()
        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.fibbonacci.Fib");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == FIB_NAME

        def clinit = cn.@methods.find { it.name == "<clinit>" }
        new ConstantCollector().internalTransform(clinit, null);

        assert ConstantHolder.v().containsKey(FIB_NAME) == true
        def pack = ConstantHolder.v().get(FIB_NAME)

        assert pack['$const$0'] == 2
        assert pack['$const$1'] == 1
        assert pack['$const$2'] == 40
    }

    void testDeConsant() {
        ConstantHolder.v().clear()
        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.fibbonacci.Fib");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == FIB_NAME
        def clinit = cn.@methods.find { it.name == "<clinit>" }
        new ConstantCollector().internalTransform(clinit, null)

        MethodNode mn = new MethodNode()
        mn.instructions.add(new VarInsnNode(ALOAD, 0))
        mn.instructions.add(new FieldInsnNode(GETSTATIC,"org/codehaus/groovy/gjit/soot/fibbonacci/Fib", '$const$0', "Ljava/lang/Integer;"))
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "org/codehaus/groovy/runtime/ScriptBytecodeAdapter", "compareLessThan", "(Ljava/lang/Object;Ljava/lang/Object;)Z"))
        mn.instructions.add(new JumpInsnNode(IFEQ, new LabelNode()))

        new DeConstantTransformer().internalTransform(mn, null)

        assert mn.instructions.get(1).opcode == LDC
        assert mn.instructions.get(2).opcode == INVOKESTATIC
        assert mn.instructions.get(2).owner == "java/lang/Integer"
        assert mn.instructions.get(2).name  == "valueOf"
        assert mn.instructions.get(2).desc  == "(I)Ljava/lang/Integer;"

        /*
        assert ConstantHolder.v().containsKey(FIB_NAME) == true
        def pack = ConstantHolder.v().get(FIB_NAME)

        assert pack['$const$0'] == 2
        assert pack['$const$1'] == 1
        assert pack['$const$2'] == 40
        */
    }

}