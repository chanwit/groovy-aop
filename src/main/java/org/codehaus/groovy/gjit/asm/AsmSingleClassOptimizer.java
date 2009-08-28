package org.codehaus.groovy.gjit.asm;

import java.util.List;

import org.codehaus.groovy.gjit.SingleClassOptimizer;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmSingleClassOptimizer implements SingleClassOptimizer {

	private List<Transformer> transformers;
	private ClassNode classNode;

	@Override
	public byte[] optimize(Class<?> c) {
		this.classNode = loadClass(c);
		applyTransformers();
		return writeClass(c);
	}

	private byte[] writeClass(Class<?> c) {
		// TODO Auto-generated method stub
		return null;
	}

	private void applyTransformers() {
		List<MethodNode> methods = this.classNode.methods;
		for(MethodNode method: methods) {
			for(Transformer t: transformers) {
				t.internalTransform(method);
			}
		}
	}

	private ClassNode loadClass(Class<?> c) {
		// TODO Auto-generated method stub
		return null;
	}

}
