package org.codehaus.groovy.gjit;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

public class Optimiser extends ClassAdapter {

    private ConstantPack pack;
    private String[] siteNames;
    private Map<String, MethodNode> methods;
    private String owner;

    public Optimiser(ClassVisitor cv) {
        super(cv);
    }

    public Optimiser(ClassVisitor cv, Map<String, MethodNode> methods) {
        super(cv);
        this.methods = methods;
    }

    enum State {
        START,
        FOUND_COMPARE,
        FOUND_CALLSITE_VAR,
        LOAD_CALLSITE_VAR
    };

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
        MethodNode mn = methods.get(name+desc);
        if(mn != null) {
            try {
                //DebugUtils.println("method name: " + name + desc);
                //if(name.equals("bottomUpTree"))// || name.equals("itemCheck"))
                //if(name.equals("main"))
                  new SecondTransformer(owner, mn, pack, siteNames).transform();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            mn.accept(cv);
            return mn;
        } else {
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

    }

    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        this.owner = name;
        pack = ConstantRecord.v().get(this.owner);
        siteNames = CallSiteArrayPack.v().get(this.owner);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public static void main(String[] args) throws Throwable {
        RandomAccessFile r = new RandomAccessFile(new File("C:\\groovy-ck1\\gjit\\subject\\" +args[0]+".class"), "r");
        byte[] bytes = new byte[(int) r.length()];
        r.readFully(bytes);
//		ClassReader cr = new ClassReader(bytes);
//		PreProcess cv = new PreProcess(new EmptyVisitor());
//		//final int mode = 0;
        final int mode = ClassReader.SKIP_DEBUG;
//		cr.accept(cv, mode);
        PreProcess cv = PreProcess.perform(bytes, mode);
        if(cv.isGroovyClassFile()) {
            ClassReader cr = new ClassReader(bytes);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            Optimiser cv2 = new Optimiser(cw, cv.getMethods());
            cr.accept(cv2, mode);
            byte[] outBytes = cw.toByteArray();
            try {
                new File("C:\\groovy-ck1\\gjit\\subject\\out\\"+args[0]+".class").delete();
            } catch(Throwable e){}
            RandomAccessFile ro = new RandomAccessFile(new File("C:\\groovy-ck1\\gjit\\subject\\out\\"+args[0]+".class"), "rw");
            ro.write(outBytes);
            ro.close();
        }
    }

    public static byte[] perform(PreProcess cv, byte[] classfileBuffer) {
        return perform(cv, classfileBuffer, 0);
    }

    public static byte[] perform(PreProcess cv, byte[] classfileBuffer, int mode) {
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        Optimiser cv2 = new Optimiser(cw, cv.getMethods());
        cr.accept(cv2, mode);
        return cw.toByteArray();
    }

}
