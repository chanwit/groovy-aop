package org.codehaus.groovy.gjit.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.codehaus.groovy.gjit.Optimiser;
import org.codehaus.groovy.gjit.PreProcess;
//import org.codehaus.groovy.gjit.agent.instrumentor.CallSiteArrayInstrumentor;
//import org.codehaus.groovy.gjit.agent.instrumentor.MetaClassInstumentor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Transformer implements ClassFileTransformer {

    public Transformer() {
        super();
    }

    public byte[] transform(ClassLoader loader, String className,
            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        //
        // check the condition which class is going to be transform
        // do transformation
        //
        if(className.equals("groovy/lang/MetaClassImpl")) {
            return instrumentingMetaClass(classfileBuffer);
        }else if(className.equals("org/codehaus/groovy/runtime/callsite/CallSiteArray")) {
            return instrumentingCallSiteArray(classfileBuffer);
        } else if(className.startsWith("java") || className.startsWith("sun") || className.startsWith("soot")) {
            return classfileBuffer;
        } else {
            return optimisingGroovyClass(classfileBuffer);
        }
    }

    private byte[] optimisingGroovyClass(byte[] classfileBuffer) {
        try {
            PreProcess cv = PreProcess.perform(classfileBuffer);
            if(cv.isGroovyClassFile()==false) {
                return classfileBuffer;
            } else { // if it's a groovy compiled class, try optimising
                return Optimiser.perform(cv, classfileBuffer);
            }
        } catch(Throwable e) {
            e.printStackTrace();
            return classfileBuffer;
        }
    }

    //
    // skip this transformation atm
    //
    private byte[] instrumentingCallSiteArray(byte[] classfileBuffer) {
        return classfileBuffer;
//        ClassReader cr = new ClassReader(classfileBuffer);
//        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
//        CallSiteArrayInstrumentor csai = new CallSiteArrayInstrumentor(cw);
//        cr.accept(csai, 0);
//        return cw.toByteArray();
    }

    //
    // skip this transformation atm
    //
    private byte[] instrumentingMetaClass(byte[] classfileBuffer) {
        return classfileBuffer;
//        ClassReader cr = new ClassReader(classfileBuffer);
//        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
//        MetaClassInstumentor mci = new MetaClassInstumentor(cw);
//        cr.accept(mci, 0);
//        return cw.toByteArray();
    }

}
