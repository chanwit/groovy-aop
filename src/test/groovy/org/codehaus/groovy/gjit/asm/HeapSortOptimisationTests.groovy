package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.*
import org.objectweb.asm.tree.*

import groovy.util.GroovyTestCase
import org.codehaus.groovy.gjit.soot.heapsort.*
import org.codehaus.groovy.gjit.asm.transformer.*

public class HeapSortOptimisationTests extends GroovyTestCase implements Opcodes {

	void testOptimiseOnHeapSort() {
		InsnListHelper.install()

		def sender = HeapSort.class
		def sco  = new AsmSingleClassOptimizer()
		def aatf = new AspectAwareTransformer(
			advisedTypes:[int, double[]] as Class[],
			advisedReturnType: void,
			callSite: new HeapSort$heapsort(),
			withInMethodName: "main"
		)
		sco.transformers = [
		  DeConstantTransformer.class,
		  aatf,
		  AutoBoxEliminatorTransformer.class
		]
		byte[] bytes = sco.optimize(sender.name)
		def cr = new ClassReader(bytes)
		def cn = new ClassNode()
		cr.accept cn, 0
		def main = cn.methods.find { it.name == "main" }
		assert main.name == "main"
	}

}
