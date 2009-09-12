package org.codehaus.groovy.gjit.asm;

import java.util.List;

import org.codehaus.groovy.gjit.AbstractSingleClassOptimizer;
import org.codehaus.groovy.gjit.asm.transformer.Transformer;
import org.codehaus.groovy.gjit.asm.transformer.Utils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public class AsmSingleClassOptimizer extends AbstractSingleClassOptimizer {

    @SuppressWarnings("unchecked")
    protected void applyTransformers() {
        //
        // prepare constant and call site name array
        //
        Utils.prepareClassInfo(this.classNode);

        List<MethodNode> methods = this.classNode.methods;
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
