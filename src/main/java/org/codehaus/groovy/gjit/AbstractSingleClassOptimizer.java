package org.codehaus.groovy.gjit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.gjit.asm.ClassBodyCache;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public abstract class AbstractSingleClassOptimizer implements SingleClassOptimizer {

    protected List<Transformer> transformers = new ArrayList<Transformer>();
    protected ClassNode classNode;

    protected abstract void applyTransformers();

    public void setTransformers(Object[] transformers) {
        this.transformers.clear();
        for (int i = 0; i < transformers.length; i++) {
            if(transformers[i] instanceof Transformer) {
                this.transformers.add((Transformer)transformers[i]);
            } else if (transformers[i] instanceof Class<?>) {
                Class<?> c = (Class<?>)transformers[i];
                try {
                    this.transformers.add((Transformer)c.newInstance());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public byte[] optimize(String className) {
        this.classNode = loadClass(className);
        applyTransformers();
        return writeClass();
    }

    protected byte[] writeClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        this.classNode.accept(cw);
        byte[] bytes = cw.toByteArray();
        ClassBodyCache.v().put(this.classNode.name, bytes);
        return bytes;
    }

    protected ClassNode loadClass(String className) {
        ClassReader cr;
        try {
            String internalName = className.replace('.', '/');
            if(ClassBodyCache.v().containsKey(internalName)) {
                cr = new ClassReader(ClassBodyCache.v().get(internalName));
            } else {
                cr = new ClassReader(className);
            }
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            return cn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
