package org.codehaus.groovy.gjit.asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.gjit.SingleClassOptimizer;
import org.codehaus.groovy.gjit.asm.transformer.CallSiteNameCollector;
import org.codehaus.groovy.gjit.asm.transformer.ConstantCollector;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmSingleClassOptimizer implements SingleClassOptimizer {

    private List<Transformer> transformers = new ArrayList<Transformer>();
    private ClassNode classNode;

    @Override
    public byte[] optimize(Class<?> c) {
        this.classNode = loadClass(c);
        applyTransformers();
        return writeClass();
    }

    private byte[] writeClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        this.classNode.accept(cw);
        byte[] bytes = cw.toByteArray();
        ClassBodyCache.v().put(this.classNode.name, bytes);
        return bytes;
    }

    @SuppressWarnings("unchecked")
    private void applyTransformers() {
        List<MethodNode> methods = this.classNode.methods;
        for(MethodNode method: methods) {
            if(method.name.equals("<clinit>")) {
                new ConstantCollector().internalTransform(method, null);
            } else if(method.name.equals("$createCallSiteArray")) {
                new CallSiteNameCollector().internalTransform(method, null);
            }
        }
        for(MethodNode method: methods) {
            // skip a synthetic method
            if((method.access & Opcodes.ACC_SYNTHETIC) != 0) continue;
            // skip class init method (static { ... })
            if(method.name.equals("<clinit>")) continue;

            for(Transformer t: transformers) {
                t.internalTransform(method, null);
            }
        }
    }

    private ClassNode loadClass(Class<?> c) {
        ClassReader cr;
        try {
            String internalName = Type.getInternalName(c);
            if(ClassBodyCache.v().containsKey(internalName)) {
                cr = new ClassReader(ClassBodyCache.v().get(internalName));
            } else {
                cr = new ClassReader(c.getName());
            }
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            return cn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

}
