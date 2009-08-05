package org.codehaus.groovy.gjit;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

// import org.codehaus.groovy.gjit.db.SiteTypePersistentCache;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.tree.MethodNode;

public class PreProcess extends ClassAdapter {

    private String className;
    private boolean groovyClassFile = false;
    private Map<String, MethodNode> methods = new HashMap<String, MethodNode>();

    public PreProcess(ClassVisitor cv) {
        super(cv);
    }

    enum CallSiteCollectingState {
        START,
        GOT_SIZE,
        GOT_INDEX,
        GOT_NAME
    };

    class CallSiteCollectorMV extends MethodAdapter {

        public CallSiteCollectorMV(MethodVisitor mv) {
            super(mv);
        }

        CallSiteCollectingState state = CallSiteCollectingState.START;

        private int size;
        private int index;
        private String[] names;

//		public CallSiteCollectorMV(int access, String name, String desc,
//				String signature, String[] exceptions) {
//			super(access, name, desc, signature, exceptions);
//		}



        @Override
        public void visitLdcInsn(Object cst) {
            switch(state) {
                case START: size = (Integer)cst;
                            names = new String[size];
                            state = CallSiteCollectingState.GOT_SIZE;
                            break;
                case GOT_SIZE:
                case GOT_NAME:	index = (Integer)cst;
                                state = CallSiteCollectingState.GOT_INDEX;
                                break;
                case GOT_INDEX: String name = (String)cst;
                                names[index] = name;
                                state = CallSiteCollectingState.GOT_NAME;
                                break;
            }
            super.visitLdcInsn(cst);
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            CallSiteArrayPack.v().put(className, names);
        }

    }

    enum ConstantCollectingState {
        START,
        GOT_VALUE,
        GOT_NAME
    };

    class ConstantCollectorMV extends MethodAdapter {

        public ConstantCollectorMV(MethodVisitor mv) {
            super(mv);
        }

        private ConstantCollectingState state = ConstantCollectingState.START;
        private ConstantPack pack = new ConstantPack();
        private Object value;

//		public ConstantCollectorMV(int access, String name, String desc,
//				String signature, String[] exceptions) {
//			super(access, name, desc, signature, exceptions);
//		}


        @Override
        public void visitFieldInsn(int opcode, String owner, String name,String desc) {
            if( state == ConstantCollectingState.GOT_VALUE) {
                if(name.startsWith("$const$")) {
                    // System.out.println("current type: " + desc);
                    // System.out.println("cst: " + value.getClass());
                    if(desc.equals("Ljava/lang/Integer;") && value instanceof Long) {
                        pack.put(name, ((Long)value).intValue());
                    } else {
                        pack.put(name, value);
                    }
                    state = ConstantCollectingState.GOT_NAME;
                } else if(name.startsWith("__timeStamp")) {
                    // TODO here is the call to SiteTypePersistentCache
                    // DebugUtils.println("owner: " + owner);
                    // SiteTypePersistentCache.v().add(owner, (Long)value);
                }
                // TODO get __timeStamp
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitLdcInsn(Object cst) {
            if( state == ConstantCollectingState.START ||
                state == ConstantCollectingState.GOT_NAME) {
                value = cst;
                state = ConstantCollectingState.GOT_VALUE;
            }
            super.visitLdcInsn(cst);
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            ConstantRecord.v().put(className, pack);
        }
    }

    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,String signature, Object value) {
        if(name.startsWith("__timeStamp__")==true) {
            this.groovyClassFile = true;
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
        // DebugUtils.println(name);
        if(this.groovyClassFile == true) {
            if(name.equals("$createCallSiteArray")) {
                return new CallSiteCollectorMV(super.visitMethod(access, name, desc, signature, exceptions));
            } else if(name.equals("<clinit>")) {
                return new ConstantCollectorMV(super.visitMethod(access, name, desc, signature, exceptions));
            } else if(isSkippable(name,desc)) {
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
            MethodNode mn = new MethodNode(access, name, desc, signature, exceptions);
            methods.put(name+desc, mn);
            return mn;
        } else {
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }

    private boolean isSkippable(String name, String desc) {
        if(name.startsWith("$get$$class$")) return true;
        if(name.equals("class$")) return true;
        if(name.equals("$getCallSiteArray")) return true;
        if(name.equals("$getStaticMetaClass")) return true;
        if(name.startsWith("super$1$")) return true;
        if(name.startsWith("this$2$")) return true;
        if((name+desc).equals("getMetaClass()Lgroovy/lang/MetaClass;")) return true;
        if((name+desc).equals("setMetaClass(Lgroovy/lang/MetaClass;)V")) return true;
        if((name+desc).equals("invokeMethod(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;")) return true;
        if((name+desc).equals("getProperty(Ljava/lang/String;)Ljava/lang/Object;")) return true;
        if((name+desc).equals("setProperty(Ljava/lang/String;Ljava/lang/Object;)V")) return true;
        return false;
    }

    public Map<String, MethodNode> getMethods() {
        return methods;
    }

    public boolean isGroovyClassFile() {
        return groovyClassFile;
    }

    public static PreProcess perform(byte[] bytes, int mode) {
        ClassReader cr = new ClassReader(bytes);
        PreProcess cv = new PreProcess(new EmptyVisitor());
        cr.accept(cv, mode);
        return cv;
    }

    public static PreProcess perform(byte[] bytes) {
        return perform(bytes, 0);
    }

    public static void main(String[] args) throws Throwable {
        RandomAccessFile r = new RandomAccessFile(new File("C:\\groovy-ck1\\groovy\\my\\TreeNode.class"), "r");
        byte[] bytes = new byte[(int) r.length()];
        r.readFully(bytes);
        PreProcess.perform(bytes);
    }


}
