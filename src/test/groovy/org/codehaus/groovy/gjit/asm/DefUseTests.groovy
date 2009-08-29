package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.tree.MethodNode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.AbstractVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import groovy.util.GroovyTestCase

class DefUseTests extends GroovyTestCase {

    void testAnalysisOnSubject() {
        def cr = new ClassReader("org.codehaus.groovy.gjit.soot.Subject");
        def cn = new ClassNode()
        cr.accept cn, 0
        assert cn.name == "org/codehaus/groovy/gjit/soot/Subject"

        def two = cn.@methods.find { it.name == "two" }
        def ins = two.instructions
        def aload  = ins.get(5)
        def invoke = ins.get(15)
        //	two.instructions.eachWithIndex { it, i ->
        //		println "$i\t: ${AbstractVisitor.OPCODES[it.opcode]}"
        //	}
        assert aload.opcode == Opcodes.ALOAD
        def p = new PartialDefUseAnalyser(two, aload, Opcodes.INVOKEINTERFACE)
        def result = p.analyse0()
        assert result == invoke
        //	result.each { k, v ->
        //		print   "${ins.indexOf(k)}\t:"
        // 		print   "${AbstractVisitor.OPCODES[k.opcode]}:"
        //      println "${v.collect{ ins.indexOf(it) }.join(',')}"
        //	}
    }

}
