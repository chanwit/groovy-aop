package org.codehaus.groovy.gjit.asm.transformer;

import java.util.Map;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.Opcodes;

import org.codehaus.groovy.gjit.asm.TypeAdvisedClassGenerator;
import org.codehaus.groovy.gjit.asm.PartialDefUseAnalyser;
import org.codehaus.groovy.gjit.asm.TypeAdvisedClassGenerator.Result;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


public class AspectAwareTransformer implements Transformer, Opcodes {

    private static class Location {
        public final AbstractInsnNode invokeStmt;
        public final Map<AbstractInsnNode, AbstractInsnNode[]> usedMap;
        public Location(AbstractInsnNode invoke,
                Map<AbstractInsnNode, AbstractInsnNode[]> usedMap) {
            this.invokeStmt = invoke;
            this.usedMap = usedMap;
        }
    }

    private String withInMethodName;
    private MethodNode body;
    private InsnList units;
    private CallSite callSite;

    private Class<?> advisedReturnType;
    private Class<?>[] advisedTypes;

    @Override
    public void internalTransform(MethodNode body, Map<String, Object> options) {
        if(body.name.equals(withInMethodName)) {
            this.body  = body;
            this.units = body.instructions;

            VarInsnNode acallsite = findCallSiteArray(units);
            Location location = locateCallSiteByIndex(units, acallsite, callSite.getIndex());
            if(location.invokeStmt== null) return;
            if(!recursive(location, callSite)) {
                MethodInsnNode newInvokeStmt = typePropagate(callSite);
                replaceCallSite(location, newInvokeStmt);
            } else {
                replaceRecursion(location);
            }
        }
    }

    private void replaceRecursion(Location location) {
        String owner = callSite.getClass().getName().split("\\$")[0].replace('.', '/');
        MethodInsnNode newInvokeStmt = new MethodInsnNode(INVOKESTATIC, owner, body.name, body.desc);
        replaceCallSite(location, newInvokeStmt);
    }

    private boolean recursive(Location location, CallSite callSite) {
        String typeOfCall = ((MethodInsnNode)location.invokeStmt).name;
        // callSite is Fib_fib_x$fib
        if(typeOfCall.equals("callStatic")) {
            // 1. check recursive of callStatic
            String callSiteClassName = callSite.getClass().getName();
            String names[] = callSiteClassName.split("\\$|_");
            if(names.length != 4) throw new RuntimeException("Name pattern not support: " + callSiteClassName);
            //   1.1 same call site name
            if(names[1].equals(names[3])==false) return false;
            //   1.2 same class
            AbstractInsnNode[] array = location.usedMap.get(location.invokeStmt);
            // array[1] must be an invokestatic returns Class<?>
            names[0] = names[0].replace('.', '/');
            String classFromFirstArg = convertFromGetClassToInternalName(((MethodInsnNode)array[1]).name);
            return names[0].equals(classFromFirstArg);
        } else {
            return false;
            // 2. check recursive of call, callCurrent
            // throw new RuntimeException("NYI");
        }
    }

    private String convertFromGetClassToInternalName(String name) {
        // pattern:
        // $get$$class$org$codehaus$groovy$gjit$soot$fibbonacci$Fib
        // output:
        // org/codehaus/groovy/gjit/soot/fibbonacci/Fib
        return name.substring("$get$$class$".length(), name.length()).replace('$', '/');
    }

    private void replaceCallSite(Location location, MethodInsnNode newInvokeStmt) {
        // old
        // INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite
        // call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        // new
        // INVOKESTATIC org/codehaus/groovy/gjit/soot/fibbonacci/Fib$fib$x
        // fib(I)I

        MethodInsnNode invokeStmt = (MethodInsnNode)location.invokeStmt;

        AbstractInsnNode[] array = location.usedMap.get(invokeStmt);

        //
        // remove unused ALOAD, LDC and AALOAD
        //
        AbstractInsnNode aaload = array[0];
        AbstractInsnNode ldc = aaload.getPrevious();
        AbstractInsnNode aload = ldc.getPrevious();

        units.remove(aaload);
        units.remove(ldc);
        units.remove(aload);

        Type[] src = Type.getArgumentTypes(invokeStmt.desc);
        Type[] dst = Type.getArgumentTypes(newInvokeStmt.desc);
        if(src.length == dst.length) {
            // instance-level
            for(int i=0; i<dst.length; i++) {
                unboxOrCast(src[i], dst[i], array[i+1]);
            }
        } else if(src.length == dst.length + 1){
            // class-level

            //
            // POP insertion maybe not required
            // if there is no array[1]'s dependency in the usedMap
            //
            AbstractInsnNode[] a1used = location.usedMap.get(array[1]);
            if(a1used.length == 0) {
                // no dependency,
                // safe to remove
                units.remove(array[1]);
            } else if(a1used.length >= 0){
                // looks not safe,
                // used pop to discard it instead
                units.insert(array[1], new InsnNode(POP));
            }
            for(int i=0; i<dst.length; i++) {
                unboxOrCast(src[i+1], dst[i], array[i+2]);
            }
        }

        units.set(invokeStmt, newInvokeStmt);
        //
        // match old and new arguments
        //
        Type newReturnType = Type.getReturnType(newInvokeStmt.desc);
        if(newReturnType.getSort() >= Type.BOOLEAN && newReturnType.getSort() <= Type.DOUBLE) {
            // it's primitive
            // need boxing
            units.insert(newInvokeStmt, Utils.getBoxNode(newReturnType));
        } else if(newReturnType.getSort() == Type.VOID) {
            // it's expected to have something in TOS, so pushing null
            units.insert(newInvokeStmt, new InsnNode(ACONST_NULL));
        } else {
            // TODO: OBJECT and ARRAY should be fine here
        }
    }

    private void unboxOrCast(Type src, Type dst, AbstractInsnNode nodeLocation) {
        if(src.getSort() == Type.OBJECT  &&
           dst.getSort() >= Type.BOOLEAN && dst.getSort() <= Type.DOUBLE) {
            units.insert(nodeLocation, Utils.getUnboxNodes(dst));
        }
    }

    private MethodInsnNode typePropagate(CallSite callSite) {
        TypeAdvisedClassGenerator atacg = new TypeAdvisedClassGenerator();
        atacg.setAdvisedTypes(advisedTypes);
        atacg.setAdvisedReturnType(advisedReturnType);
        Result result = atacg.perform(callSite);
        //
        // do caching the generated class for further optimisation
        //

        Utils.defineClass(result.owner.replace('/', '.'), result.body);
        return new MethodInsnNode(INVOKESTATIC, result.owner, result.name, result.desc);
    }

    private Location locateCallSiteByIndex(InsnList units,
            VarInsnNode acallsite, int callSiteIndex) {
        //
        // finding this pattern
        //      ALOAD 1
        //      LDC 0
        //      AALOAD
        //
        AbstractInsnNode found = null;
        for (int i = 0; i < units.size(); i ++) {
            AbstractInsnNode s = units.get(i);
            if(s.getOpcode() != ALOAD) continue;
            VarInsnNode aload = (VarInsnNode)s;
            if(aload.var != acallsite.var) continue;
            AbstractInsnNode s0 = units.get(i + 1);
            if(s0.getOpcode() != LDC) continue;
            LdcInsnNode ldc = (LdcInsnNode)s0;
            if((Integer)ldc.cst != callSiteIndex) continue;
            AbstractInsnNode s1 = units.get(i + 2);
            if(s1.getOpcode() != AALOAD) continue;
            found = s;
            break;
        }

        if(found == null) return null;

        PartialDefUseAnalyser pdua = new PartialDefUseAnalyser(
                                            body, found,
                                            INVOKEINTERFACE);
        AbstractInsnNode invoke = pdua.analyse0();
        return new Location(invoke, pdua.getUsedMap());
    }

    private static final String GET_CALL_SITE_ARRAY =
        "$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;";

    private VarInsnNode findCallSiteArray(InsnList units) {
//      ALOAD 0
//      INVOKESPECIAL java/lang/Object.<init>()V
//     L0
//      INVOKESTATIC org/codehaus/groovy/gjit/soot/Subject.$getCallSiteArray()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
//      ASTORE 1
        for (int i = 0; i < units.size(); i ++) {
            AbstractInsnNode node = units.get(i);
            if(node.getOpcode() != INVOKESTATIC) continue;
            MethodInsnNode invNode = (MethodInsnNode) node;
            if( GET_CALL_SITE_ARRAY.equals(invNode.name + invNode.desc)) {
                AbstractInsnNode s0 = units.get(i + i);
                if(s0.getOpcode() == ASTORE) return (VarInsnNode)s0;
            }
        }

        return null;
    }

    public void setAdvisedTypes(Class<?>[] argTypeOfBinding) {
        this.advisedTypes = argTypeOfBinding;
    }

    public void setAdvisedReturnType(Class<?> returnType) {
        this.advisedReturnType = returnType;
    }

    public void setCallSite(CallSite callSite) {
        this.callSite = callSite;
    }

    public void setWithInMethodName(String withInMethodName) {
        this.withInMethodName = withInMethodName;
    }

}
