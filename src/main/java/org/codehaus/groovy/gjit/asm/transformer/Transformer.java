package org.codehaus.groovy.gjit.asm.transformer;

import org.objectweb.asm.tree.MethodNode;

public interface Transformer {

	public abstract void internalTransform(MethodNode body);

}
