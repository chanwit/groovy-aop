package org.codehaus.groovy.gjit.asm;

import java.io.IOException;
import java.util.List;

import org.codehaus.groovy.gjit.AbstractSingleClassOptimizer;
import org.codehaus.groovy.gjit.asm.transformer.CallSiteNameCollector;
import org.codehaus.groovy.gjit.asm.transformer.ConstantCollector;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmSingleClassOptimizer extends AbstractSingleClassOptimizer {

    @SuppressWarnings("unchecked")
    protected void applyTransformers() {
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

}
