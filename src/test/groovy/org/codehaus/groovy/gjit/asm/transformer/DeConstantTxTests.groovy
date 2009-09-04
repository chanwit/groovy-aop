package org.codehaus.groovy.gjit.asm.transformer

import org.objectweb.asm.tree.LabelNode;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter as SBA
import org.objectweb.asm.tree.*
import org.objectweb.asm.*
import org.codehaus.groovy.gjit.asm.InsnListHelper;
import org.codehaus.groovy.gjit.asm.transformer.*;
import org.codehaus.groovy.gjit.soot.fibbonacci.Fib

public class DeConstantTxTests extends GroovyTestCase implements Opcodes {

    static FIB_NAME = "org/codehaus/groovy/gjit/soot/fibbonacci/Fib"

    private loadConstantsFromFib() {
        InsnListHelper.install()
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
        units.append {
            aload 0
            getstatic    Fib, '$const$0', Integer
            invokestatic SBA, 'compareLessThan', [Object,Object], boolean
            ifeq new LabelNode()
        }

        new DeConstantTransformer().internalTransform(mn, null)

        assert units[1].opcode == ICONST_2

        assert units[2].opcode == INVOKESTATIC
        assert units[2].owner  == Integer.internalName
        assert units[2].name   == "valueOf"
        assert units[2].desc   == "(I)Ljava/lang/Integer;"
    }

    void testDeConsant_Const_2() {
        loadConstantsFromFib()

        def mn = new MethodNode()
        def units = mn.instructions
        units.append {
            aload 0
            getstatic    Fib, '$const$2', Integer
            invokestatic SBA, 'compareLessThan', [Object,Object], boolean
            ifeq new LabelNode()
        }

        new DeConstantTransformer().internalTransform(mn, null)

        assert units[1].opcode  == BIPUSH
        assert units[1].operand == 40

        assert units[2].opcode == INVOKESTATIC
        assert units[2].owner  == Integer.internalName
        assert units[2].name   == "valueOf"
        assert units[2].desc   == "(I)Ljava/lang/Integer;"
    }
}