package org.codehaus.groovy.gjit.asm;

import java.util.ArrayList;
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

    @SuppressWarnings("unchecked")
    private void applyTransformers() {
        List<MethodNode> methods = this.classNode.methods;
        for(MethodNode method: methods) {
            for(Transformer t: transformers) {
                t.internalTransform(method, null);
            }
        }
    }

    private ClassNode loadClass(Class<?> c) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setTransformers(Transformer[] transformers) {
        this.transformers.clear();
        for (int i = 0; i < transformers.length; i++) {
            this.transformers.add(transformers[i]);
        }
    }

}
