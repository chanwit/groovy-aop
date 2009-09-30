package org.codehaus.groovy.gjit.asm.transformer;

import java.io.PrintWriter;
import java.util.Map;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.objectweb.asm.ClassReader;
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
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.AbstractVisitor;
import org.objectweb.asm.util.CheckClassAdapter;


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
            if(location == null) return;
            if(location.invokeStmt == null) return;
            DGMResult dmg = isDGMCallSite(callSite);
            if(dmg != null) {
                replaceBinaryCallSite(location, dmg);
            } else if(!recursive(location, callSite)) {
                MethodInsnNode newInvokeStmt = typePropagate(callSite);
                replaceCallSite(location, newInvokeStmt);
            } else {
                // System.out.println("replacing recurion " + callSite);                
                replaceRecursion(location);
            }
        }
    }

    private void replaceBinaryCallSite(Location location, DGMResult dmg) {
        if(dmg.desc.equals("II")) {
            int opcode;
            final String op = dmg.op;
            if(op.equals("Plus"))     { opcode = IADD; } else
            if(op.equals("Minus"))    { opcode = ISUB; } else
            if(op.equals("Multiply")) { opcode = IMUL; } else
            if(op.equals("Div"))      { opcode = IDIV; } else throw new RuntimeException("NYI");

            AbstractInsnNode[] array = location.usedMap.get(location.invokeStmt);

            units.insert(array[1], Utils.getUnboxNodes(dmg.operand1));
            units.insert(array[2], Utils.getUnboxNodes(dmg.operand2));

            //
            // replace with native op, and box it back to an object
            //
            InsnNode newS = new InsnNode(opcode);
            units.set(location.invokeStmt, newS);
            units.insert(newS, Utils.getBoxNode(dmg.operand1));

            //
            // clean unused ALOAD, LDC, AALOAD
            //
            AbstractInsnNode aaload = array[0];
            units.remove(aaload.getPrevious().getPrevious());
            units.remove(aaload.getPrevious());
            units.remove(aaload);

        } else throw new RuntimeException("NYI");
    }

    static class DGMResult {
        final String op;
        final String operand1;
        final String operand2;
        final String desc;
        public DGMResult(String op, String[] desc) {
            this.op = op;
            this.desc = ""+ desc[0].charAt(0) + desc[1].charAt(0);
            this.operand1 = "Ljava/lang/"+desc[0]+";";
            this.operand2 = "Ljava/lang/"+desc[1]+";";
        }
        @Override
        public String toString() {
            return "DGMResult [op=" + op + ", operand1=" + operand1
                    + ", operand2=" + operand2 + "]";
        }
    }

    private DGMResult isDGMCallSite(CallSite callSite) {
        // NumberNumberPlus$IntegerInteger
        // System.out.println("DGM detected");
        String name = callSite.getClass().getName();
        String[] names = name.split("\\.|\\$");
        int len = names.length;
        String operator = names[len-2];
        String desc = names[len-1];
        if(operator.startsWith("NumberNumber")) {
            String op = operator.substring("NumberNumber".length(), operator.length());
            for(int i=0;i < desc.length(); i++) {
                if(Character.isUpperCase(desc.charAt(i)) && i > 0) {
                    String t1 = desc.substring(0,  i);
                    String t2 = desc.substring(i, desc.length());
                    return new DGMResult(op, new String[]{t1, t2});
                }
            }
        }
        return null;
    }

    private void replaceRecursion(Location location) {
        String owner = callSite.getArray().owner.getName().replace('.', '/');
        //System.out.println("call site to replace: " + owner);
        MethodInsnNode newInvokeStmt = new MethodInsnNode(INVOKESTATIC, owner, body.name, body.desc);
        replaceCallSite(location, newInvokeStmt);
    }

    private boolean recursive(Location location, CallSite callSite) {
        String typeOfCall = ((MethodInsnNode)location.invokeStmt).name;
        // owner is Fib_fib_x
        // callSite is Fib_fib_x$fib
        // old callSite is Fib$fib
        
        String callSiteClassName = callSite.getArray().owner.getName() + "$" + callSite.getName();

        // System.out.println(">> debug ==");        
        // System.out.println("name pattern " + callSiteClassName);
        // System.out.println("type of call " + typeOfCall);
        
        if(typeOfCall.equals("callStatic")) {
            String names[] = callSiteClassName.split("\\$|_");
            if(names.length != 4) {
                // System.out.println("Name pattern not support: " + callSiteClassName);
                return false;
                //throw new RuntimeException("Name pattern not support: " + callSiteClassName);
            }
            //   1.1 same call site name
            if(names[1].equals(names[3])==false) return false;
            //   1.2 same class
            AbstractInsnNode[] array = location.usedMap.get(location.invokeStmt);
            // array[1] must be an invokestatic returns Class<?>
            names[0] = names[0].replace('.', '/');
            String classFromFirstArg = convertFromGetClassToInternalName(((MethodInsnNode)array[1]).name);                        
            return names[0].equals(classFromFirstArg);
        } else if(typeOfCall.equals("call")) {
            System.out.println("call : not check for recursion");
            return false;
        } else if(typeOfCall.equals("callCurrent")) {
            System.out.println("callCurrent : not check for recursion");
            return false;
        }

        return false;
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
        // remove unused ALOAD, LDC and AALOAD in the reverse order
        //
        AbstractInsnNode aaload = array[0];
        units.remove(aaload.getPrevious().getPrevious());
        units.remove(aaload.getPrevious());
        units.remove(aaload);

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
        } else if(src.getSort() == Type.OBJECT && dst.getSort() == Type.OBJECT){
            units.insert(nodeLocation, new TypeInsnNode(CHECKCAST, dst.getInternalName()));
        }
    }

    private MethodInsnNode typePropagate(CallSite callSite) {
        TypeAdvisedClassGenerator tacGen = new TypeAdvisedClassGenerator();
        tacGen.setAdvisedTypes(advisedTypes);
        tacGen.setAdvisedReturnType(advisedReturnType);
        Result result = tacGen.perform(callSite);

        // DEBUG: CheckClassAdapter.verify(new ClassReader(result.body), true, new PrintWriter(System.out));

        //
        // do caching the generated class for further optimisation
        //
        if(result.firstTime) {
            Utils.defineClass(result.owner.replace('/', '.'), result.body);
        } else {
            // do nothing
        }
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
