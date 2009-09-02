package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.*
import org.objectweb.asm.*
import org.codehaus.groovy.gjit.asm.transformer.*;

public class DeConstantTxTests extends GroovyTestCase implements Opcodes {

    static FIB_NAME = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib"

    private loadConstantsFromFib() {
        ConstantHolder.v().clear()
        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.fibbonacci.Fib");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == FIB_NAME
        def clinit = cn.@methods.find { it.name == "<clinit>" }
        new ConstantCollector().internalTransform(clinit, null);
    }

    void testCollector() {
        loadConstantsFromFib()

        assert ConstantHolder.v().containsKey(FIB_NAME) == true
        def pack = ConstantHolder.v().get(FIB_NAME)

        assert pack['$const$0'] == 2
        assert pack['$const$1'] == 1
        assert pack['$const$2'] == 40
    }

    void testDeConsant_Const_0() {
        loadConstantsFromFib()

        def mn = new MethodNode()
        def units = mn.instructions

        units.add(new VarInsnNode(ALOAD, 0))
        units.add(new FieldInsnNode(GETSTATIC,
                      "org/codehaus/groovy/gjit/soot/fibbonacci/Fib",
                      '$const$0',
                      "Ljava/lang/Integer;"))
        units.add(new MethodInsnNode(INVOKESTATIC,
                      "org/codehaus/groovy/runtime/ScriptBytecodeAdapter",
                      "compareLessThan",
                      "(Ljava/lang/Object;Ljava/lang/Object;)Z"))
        units.add(new JumpInsnNode(IFEQ, new LabelNode()))

        new DeConstantTransformer().internalTransform(mn, null)

        assert units.get(1).opcode == ICONST_2

        assert units.get(2).opcode == INVOKESTATIC
        assert units.get(2).owner == "java/lang/Integer"
        assert units.get(2).name  == "valueOf"
        assert units.get(2).desc  == "(I)Ljava/lang/Integer;"
    }

    void testDeConsant_Const_2() {
        loadConstantsFromFib()

        def mn = new MethodNode()
        def units = mn.instructions

        units.add(new VarInsnNode(ALOAD, 0))
        units.add(new FieldInsnNode(GETSTATIC,
                      "org/codehaus/groovy/gjit/soot/fibbonacci/Fib",
                      '$const$2',
                      "Ljava/lang/Integer;"))
        units.add(new MethodInsnNode(INVOKESTATIC,
                      "org/codehaus/groovy/runtime/ScriptBytecodeAdapter",
                      "compareLessThan",
                      "(Ljava/lang/Object;Ljava/lang/Object;)Z"))
        units.add(new JumpInsnNode(IFEQ, new LabelNode()))

        new DeConstantTransformer().internalTransform(mn, null)

        assert units.get(1).opcode  == BIPUSH
        assert units.get(1).operand == 40

        assert units.get(2).opcode  == INVOKESTATIC
        assert units.get(2).owner   == "java/lang/Integer"
        assert units.get(2).name    == "valueOf"
        assert units.get(2).desc    == "(I)Ljava/lang/Integer;"
    }
}