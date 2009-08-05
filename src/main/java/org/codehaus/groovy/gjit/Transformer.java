package org.codehaus.groovy.gjit;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Value;
import org.objectweb.asm.util.AbstractVisitor;


public class Transformer extends Analyzer implements Opcodes {

    private static final String SCRIPT_BYTECODE_ADAPTER = "org/codehaus/groovy/runtime/ScriptBytecodeAdapter";
    private static final String CALL_SITE_INTERFACE = "org/codehaus/groovy/runtime/callsite/CallSite";
    private static final String DEFAULT_TYPE_TRANSFORMATION = "org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation";
    private InsnList units;
    private MethodNode node;

    private enum Phase {
        PHASE_CALLSITE,
        PHASE_NEXT_1,
        PHASE_NEXT_2,

        PHASE_ERROR
    }

    private enum CallSiteState {
        START,
        FOUND_CALLSITE_INST,
        END,

        ERROR,
    };

    private Phase phase = Phase.PHASE_CALLSITE;
    // private CallSiteState state = CallSiteState.START;

    private int callSiteVar = -1;
    private ConstantPack pack;
    private String[] siteNames;
    private Integer currentSiteIndex = -1;
    private String owner;
    private int[] localTypes;

    private Map<AbstractInsnNode, Type> markForLaterBox = new HashMap<AbstractInsnNode, Type>();
    private Map<AbstractInsnNode, Type> markForLaterUnbox = new HashMap<AbstractInsnNode, Type>();

    public Transformer(String owner, MethodNode mn, ConstantPack pack, String[] siteNames) {
        super(new MyBasicInterpreter());
        this.owner = owner;
        this.node = mn;
        this.units = node.instructions;
        this.pack = pack;
        this.siteNames = siteNames;
        this.localTypes = new int[mn.maxLocals];
    }

    public void transform() throws AnalyzerException {
        preTransform();
        this.analyze(this.owner, this.node);
        postTransform();
//		TraceMethodVisitor t = new TraceMethodVisitor(null);
//		node.accept(t);
//		DebugUtils.println(t.text);
    }

    private void postTransform() {
        Set<Entry<AbstractInsnNode, Type>> set = markForLaterBox.entrySet();
        for (Entry<AbstractInsnNode, Type> entry : set) {
            AbstractInsnNode s = entry.getKey();
            Type t = entry.getValue();
            String boxType=null;
            String primType=null;
            switch(t.getSort()) {
                case Type.INT: boxType = "java/lang/Integer";
                               primType = "I";
                               break;
                case Type.LONG: boxType = "java/lang/Long";
                               primType = "J";
                               break;
                // TODO: other types
            }
            MethodInsnNode iv = new MethodInsnNode(INVOKESTATIC, boxType, "valueOf", "("+ primType +")L"+boxType+";");
            if(s.getOpcode()==SWAP) s = s.getPrevious(); // work around for inserted SWAP,POP
            units.insert(s, iv);
        }
        Set<Entry<AbstractInsnNode, Type>> set2 = markForLaterUnbox.entrySet();
        for (Entry<AbstractInsnNode, Type> entry : set2) {
            AbstractInsnNode s = entry.getKey();
            Type t = entry.getValue();
            String boxType=null;
            String primType = null;
            String primTypeName = null;
            switch(t.getSort()) {
                case Type.INT:  boxType = "java/lang/Integer";
                                primType = "I";
                                primTypeName = "int";
                                break;
                case Type.LONG:  boxType = "java/lang/Long";
                                primType = "J";
                                primTypeName = "long";
                                break;
                case Type.FLOAT:  boxType = "java/lang/Float";
                                primType = "F";
                                primTypeName = "float";
                                break;
                case Type.DOUBLE:  boxType = "java/lang/Double";
                                primType = "D";
                                primTypeName = "double";
                                break;
            }
            TypeInsnNode tnode = new TypeInsnNode(CHECKCAST, boxType);
            MethodInsnNode iv = new MethodInsnNode(INVOKEVIRTUAL, boxType, primTypeName + "Value", "()" + primType);
            units.insertBefore(s, tnode);
            units.insert(tnode, iv);
        }
    }

    @Override
    public Action process(AbstractInsnNode s, Map<AbstractInsnNode, Frame> frames) {
        Frame frame = frames.get(s);
//		 DebugUtils.println(frame);
//		 DebugUtils.println("index: " + units.indexOf(s));
        if(extractCallSiteName(s)) return Action.NONE;
        if(eliminateBoxCastUnbox(s)) return Action.REMOVE;
        if(unwrapConst(s)) return Action.REPLACE;
        if(unwrapBoxOrUnbox(s)) return Action.REMOVE;
        if(unwrapBinaryPrimitiveCall(s, frame)) return Action.REPLACE;
        if(unwrapCompare(s,frame)) return Action.REMOVE;
        if(clearCast(s)) return Action.REMOVE;
        if(correctNormalCall(s)) return Action.NONE;
        if(correctLocalType(s)) return Action.REPLACE;
        if(correctUnbox(s)) return Action.NONE;
        return Action.NONE;
    }

    boolean flag = false;
    boolean flag2 = false;

    @Override
    protected void postprocess(AbstractInsnNode insnNode,Interpreter interpreter) {
        if(flag == true) {
            MyBasicInterpreter i = (MyBasicInterpreter)interpreter;
            MethodInsnNode m = (MethodInsnNode)insnNode;
            // DebugUtils.println(m.desc);
            Type[] types = Type.getArgumentTypes(m.desc);
            Value[] values = i.use.get(insnNode);
            if(m.getOpcode() == INVOKESTATIC) {
                for (int j = 0; j < values.length; j++) {
                    Type t = types[j];
                    BasicValue bv = ((BasicValue)values[j]);
                    //DebugUtils.print(j + ". ");
                    if(t.equals(bv.getType())==false && bv.isReference()==false) {
//						DebugUtils.print("expected: " + t + ", found: ");
//						DebugUtils.print(values[j]+", ");
                        markForLaterBox.put(((DefValue)bv).source, bv.getType());
                    }
//					DebugUtils.println(AbstractVisitor.OPCODES[((DefValue)values[j]).source.getOpcode()]);
                }
            } else if(m.getOpcode() == INVOKEINTERFACE){
                for (int j = 1; j < values.length; j++) {
                    Type t = types[j-1];
                    BasicValue bv = ((BasicValue)values[j]);
                    //DebugUtils.print(j + ". ");
                    if(t.equals(bv.getType())==false && bv.isReference()==false) {
//						DebugUtils.print("expected: " + t + ", found: ");
//						DebugUtils.print(values[j]+", ");
                        try {
                            markForLaterBox.put(((DefValue)bv).source, bv.getType());
                        } catch(ClassCastException e) {
                            DebugUtils.print(m.owner+".");
                            DebugUtils.print(m.name);
                            DebugUtils.println(m.desc);
                            DebugUtils.println(bv);
                            // e.printStackTrace();
                            //throw new RuntimeException(e);
                        }
                    }
//					DebugUtils.println(AbstractVisitor.OPCODES[((DefValue)values[j]).source.getOpcode()]);
                }
            }
//			DebugUtils.println("=================");
            flag = false;
        }
        if(flag2 == true) {
            MyBasicInterpreter i = (MyBasicInterpreter)interpreter;
            // MethodInsnNode m = (MethodInsnNode)insnNode;
            // Type[] types = Type.getArgumentTypes(m.desc);
            Value[] values = i.use.get(insnNode);
            DebugUtils.println(AbstractVisitor.OPCODES[insnNode.getOpcode()]);
            try {
                DefValue defValue = (DefValue)values[0];
                if(defValue.isReference()) {
                    markForLaterUnbox.put(insnNode, flag2Type);
                }
            } catch(ClassCastException e) {
                // TODO: what's happening?
            }
            flag2 = false;
        }
    }

    private Type flag2Type;

    private boolean correctUnbox(AbstractInsnNode s) {
        flag2Type = null;
        switch(s.getOpcode()) {
            case ISTORE:
            case IRETURN:
                    flag2Type = Type.INT_TYPE;
                    break;
            case LRETURN:
            case LSTORE:
                    flag2Type = Type.LONG_TYPE;
                    break;
            case FRETURN:
            case FSTORE:
                    flag2Type = Type.FLOAT_TYPE;
                    break;
            case DRETURN:
            case DSTORE:
                    flag2Type = Type.DOUBLE_TYPE;
                    break;
        }
        if(flag2Type != null) {
            flag2 = true;
            return true;
        }
        return false;
    }


    private boolean correctNormalCall(AbstractInsnNode s) {
        if(s.getOpcode() != INVOKEINTERFACE &&
           s.getOpcode() != INVOKESTATIC) return false;
        MethodInsnNode iv = (MethodInsnNode)s;
        if(iv.owner.equals(CALL_SITE_INTERFACE) && iv.name.startsWith("call")) {
            //DebugUtils.println(iv.name);
            flag = true;
            return true;
        } else if (iv.owner.equals(SCRIPT_BYTECODE_ADAPTER) && !iv.name.equals("unwrap")) {
            //DebugUtils.println(iv.name);
            flag = true;
            return true;
        }
        return false;
    }

    private boolean correctLocalType(AbstractInsnNode s) {
        if(s.getOpcode() != ALOAD) return false;
        VarInsnNode v = (VarInsnNode)s;
        int type = localTypes[v.var];
        if(type==0) return false;
        switch(type) {
            case 'I':
            case 'B':
            case 'S':
            case 'Z':
            case 'C':
                units.set(s, new VarInsnNode(ILOAD, v.var));
                break;
            case 'J':
                units.set(s, new VarInsnNode(LLOAD, v.var));
                break;
            case 'F':
                units.set(s, new VarInsnNode(FLOAD, v.var));
                break;
            case 'D':
                units.set(s, new VarInsnNode(DLOAD, v.var));
                break;
        }
        return true;
    }

    private boolean eliminateBoxCastUnbox(AbstractInsnNode s) {
//	    INVOKESTATIC org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation.box(I)Ljava/lang/Object;
//	    INVOKESTATIC TreeNode.$get$$class$java$lang$Integer()Ljava/lang/Class;
//	    INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.castToType(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
//	    CHECKCAST java/lang/Integer
//	    INVOKESTATIC org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation.intUnbox(Ljava/lang/Object;)I
        if(s.getOpcode() != INVOKESTATIC) return false;
        AbstractInsnNode s1 = s.getNext();    if(s1 == null) return false;
        if(s1.getOpcode() != INVOKESTATIC) return false;
        AbstractInsnNode s2 = s1.getNext();   if(s2 == null) return false;
        if(s2.getOpcode() != INVOKESTATIC) return false;
        AbstractInsnNode s3 = s2.getNext();   if(s3 == null) return false;
        if(s3.getOpcode() != CHECKCAST) return false;
        AbstractInsnNode s4 = s3.getNext();	  if(s4 == null) return false;
        if(s4.getOpcode() != INVOKESTATIC) return false;
        MethodInsnNode m = (MethodInsnNode)s;
        MethodInsnNode m1 = (MethodInsnNode)s1;
        MethodInsnNode m2 = (MethodInsnNode)s2;
        MethodInsnNode m4 = (MethodInsnNode)s4;
//		if(m.owner.equals(DEFAULT_TYPE_TRANSFORMATION)==false) return false;
        if(m.name.equals("box") == false) return false;
        if(m1.name.startsWith("$get$$class$") == false) return false;
        if(m2.name.startsWith("castToType") == false) return false;
        if(m4.name.endsWith("Unbox")== false) return false;
        units.remove(s);
        units.remove(s1);
        units.remove(s2);
        units.remove(s3);
        units.remove(s4);
        return true;
    }

    private int getPrimitive(String className) {
        if(className.charAt(0)=='L' && className.charAt(className.length()-1)==';') {
            className = className.substring(1,className.length()-1);
        }
        // DebugUtils.println("getPrimitive: " + className);
        if(className.equals("java/lang/Integer")) return 'I';
        if(className.equals("java/lang/Long")) return 'J';
        if(className.equals("java/lang/Boolean")) return 'Z';
        if(className.equals("java/lang/Byte")) return 'B';
        if(className.equals("java/lang/Character")) return 'C';
        if(className.equals("java/lang/Short")) return 'S';
        if(className.equals("java/lang/Float")) return 'F';
        if(className.equals("java/lang/Double")) return 'D';
        return 0;
    }

    private boolean clearCast(AbstractInsnNode s) {
//	    INVOKESTATIC TreeNode.$get$$class$java$lang$Integer()Ljava/lang/Class;
//	    INVOKESTATIC org/codehaus/groovy/runtime/ScriptBytecodeAdapter.castToType(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
//	    CHECKCAST java/lang/Integer
        if(s.getOpcode() != INVOKESTATIC) return false;
        MethodInsnNode m = (MethodInsnNode)s;
        if(m.name.startsWith("$get$$class$java$lang$")==false) return false;
        AbstractInsnNode s1 = s.getNext(); if(s1 ==null ) return false;
        if(s1.getOpcode() != INVOKESTATIC) return false;
        MethodInsnNode m1 = (MethodInsnNode)s1;
        if(m1.name.equals("castToType")==false) return false;
        AbstractInsnNode s2 = s1.getNext(); if(s2 == null) return false;
        if(s2.getOpcode() != CHECKCAST) return false;
        TypeInsnNode t2 = (TypeInsnNode)s2;
        if(t2.desc.startsWith("java/lang")==false) return false;

        int type = getPrimitive(t2.desc);
        AbstractInsnNode s3 = s2.getNext();
        fixASTORE(type, s3);
        units.remove(s);
        units.remove(s1);
        units.remove(s2);
        // TODO: change the next instruction to deal with PRIMITIVE
        return true;
    }

    private void fixASTORE(int type, AbstractInsnNode nextS) {
        if(nextS.getOpcode()==ASTORE) {
            VarInsnNode v3 = (VarInsnNode)nextS;
            switch(type) {
                case 'I':
                case 'B':
                case 'S':
                case 'Z':
                case 'C':
                    units.set(nextS, new VarInsnNode(ISTORE, v3.var));
                    break;
                case 'J':
                    units.set(nextS, new VarInsnNode(LSTORE, v3.var));
                    break;
                case 'F':
                    units.set(nextS, new VarInsnNode(FSTORE, v3.var));
                    break;
                case 'D':
                    units.set(nextS, new VarInsnNode(DSTORE, v3.var));
                    break;
            }
            localTypes[v3.var] = type;
            correctLocalVarInfo(type, v3);
        }
    }

    private void correctLocalVarInfo(int type, VarInsnNode v3) {
        List<?> vars = node.localVariables;
        if(vars != null) {
            for(int i=0;i<vars.size();i++) {
                LocalVariableNode l = (LocalVariableNode)vars.get(i);
                if(l.index==v3.var) {
                    l.desc = String.valueOf((char)type);
                    break;
                }
            }
        }
    }

    private enum ComparingMethod {
        compareLessThan,
        compareGreaterThan,
        compareLessThanEqual,
        compareGreaterThanEqual
    };

    private boolean unwrapCompare(AbstractInsnNode s, Frame frame) {
        if(s.getOpcode() != Opcodes.INVOKESTATIC) return false;
        MethodInsnNode m = (MethodInsnNode)s;
        if(m.owner.equals(SCRIPT_BYTECODE_ADAPTER)==false) return false;
        if(m.name.startsWith("compare")==false) return false;
        if(m.desc.equals("(Ljava/lang/Object;Ljava/lang/Object;)Z")==false) return false;
        JumpInsnNode s1 = (JumpInsnNode)s.getNext();
        ComparingMethod compare;
        try { compare = ComparingMethod.valueOf(m.name); }
        catch(IllegalArgumentException e) {
            return false;
        }
//		DebugUtils.println(">>>>> did unwrapping compare");
        switch(compare) {
            case compareGreaterThan:
                units.set(s1, new JumpInsnNode(IF_ICMPLE, s1.label)); break;
            case compareGreaterThanEqual:
                units.set(s1, new JumpInsnNode(IF_ICMPLT, s1.label)); break;
            case compareLessThan:
                units.set(s1, new JumpInsnNode(IF_ICMPGE, s1.label)); break;
            case compareLessThanEqual:
                units.set(s1, new JumpInsnNode(IF_ICMPGT, s1.label)); break;
            }
        units.remove(s);
        return true;
    }

    private void preTransform() {
        ListIterator<?> stmts = units.iterator();
        while(stmts.hasNext()) {
            AbstractInsnNode s = (AbstractInsnNode)stmts.next();
            switch(phase) {
                case PHASE_CALLSITE: phase = processPhaseCallSite(s); break;
            }
        }
        //node.localVariables.add(null);
    }

    private Phase processPhaseCallSite(AbstractInsnNode s0) {
        CallSiteState state = CallSiteState.START;
        AbstractInsnNode s = s0;
        while(true) {
            switch(state) {
                case START:
                    state = detectCallSiteInst(state, s);
                    break;
                case FOUND_CALLSITE_INST:
                    state = detectCallSiteVar(state, s);
                    break;

                case END: return Phase.PHASE_NEXT_1;
                case ERROR: return Phase.PHASE_ERROR;
            }
            s = s.getNext();
            if(s == null) state = CallSiteState.ERROR;
        }
    }

    private CallSiteState detectCallSiteVar(CallSiteState state, AbstractInsnNode s) {
        if(s.getOpcode() != ASTORE) return state;
        VarInsnNode v = (VarInsnNode)s;
        callSiteVar = v.var;
        return CallSiteState.END;
    }

    private CallSiteState detectCallSiteInst(CallSiteState state, AbstractInsnNode s) {
        if(s.getOpcode() != INVOKESTATIC) return state;
        MethodInsnNode m = (MethodInsnNode)s;
        if(m.name.equals("$getCallSiteArray")) return CallSiteState.FOUND_CALLSITE_INST;
        return state;
    }

    private enum BinOp {
        minus,
        plus,
        multiply,
        div,
        leftShift,
        rightShift
    }

    private boolean unwrapBinaryPrimitiveCall(AbstractInsnNode s, Frame frame) {
        if(s.getOpcode() != INVOKEINTERFACE) return false;
        MethodInsnNode iv = (MethodInsnNode)s;
        if(iv.owner.equals(CALL_SITE_INTERFACE) == false) return false;
        if(iv.name.equals("call") == false) return false;
        if(iv.desc.equals("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;") == false) return false;
        String name = siteNames[currentSiteIndex];
        // DebugUtils.println("frame: " + frame);
        BinOp op=null;
        try {op = BinOp.valueOf(name);} catch(IllegalArgumentException e){}
        if(op == null) return false;
        // TODO check type from "frame"
        DebugUtils.println("frame: " + frame);
        int oldIndex = units.indexOf(s);
        if(s.getPrevious().getOpcode()==LLOAD) DebugUtils.println(">> Found it !!");
        Value v2 = frame.getStack(frame.getStackSize()-1); // peek
        Value v1 = frame.getStack(frame.getStackSize()-2); // peek
        // TODO if(v1.sort != v2.sort) do something
        int offset = 0;
        DebugUtils.println("v1:" +v1);
        DebugUtils.println("v2:" +v2);
        if(((BasicValue)v1).getType().equals(Type.LONG_TYPE)) offset = 1;
        else if(((BasicValue)v1).getType().equals(Type.FLOAT_TYPE)) offset = 2;
        else if(((BasicValue)v1).getType().equals(Type.DOUBLE_TYPE)) offset = 3;
        switch(op) {
            case minus:
                units.set(s, new InsnNode(ISUB + offset)); break;
            case plus:
                units.set(s, new InsnNode(IADD + offset)); break;
            case multiply:
                units.set(s, new InsnNode(IMUL + offset)); break;
            case div:
                units.set(s, new InsnNode(IDIV + offset)); break;
            case leftShift:
                units.set(s, new InsnNode(ISHL + offset)); break;
            case rightShift:
                units.set(s, new InsnNode(ISHR + offset)); break;
        }
        s = units.get(oldIndex);
        if(v1.getSize()==1) {
        // SWAP,
        // POP
            units.insert(s, new InsnNode(POP));
            units.insert(s, new InsnNode(SWAP));
        } else if(v1.getSize()==2){
            units.insert(s, new InsnNode(POP));
            units.insert(s, new InsnNode(POP2));
            units.insert(s, new InsnNode(DUP2_X1));
        }

        return true;
    }

    private boolean extractCallSiteName(AbstractInsnNode s) {
        if(s.getOpcode() != ALOAD) return false;
        VarInsnNode v = (VarInsnNode)s;
        if(v.var != callSiteVar) return false;
        AbstractInsnNode s1 = s.getNext();
        if(s1.getOpcode() != LDC) return false;
        LdcInsnNode l = (LdcInsnNode)s1;
        currentSiteIndex = (Integer)l.cst;
        return true;
    }

    private boolean unwrapConst(AbstractInsnNode s) {
        if(s.getOpcode() != GETSTATIC) return false;
        FieldInsnNode f = (FieldInsnNode)s;
        if(f.name.startsWith("$const$")) {
            LdcInsnNode newS = new LdcInsnNode(pack.get(f.name));
            AbstractInsnNode s1 = s.getNext();
            fixASTORE(getPrimitive(f.desc), s1);
            units.set(s, newS);
            return true;
        }
        return false;
    }

    private boolean unwrapBoxOrUnbox(AbstractInsnNode s) {
        if(s.getOpcode() != INVOKESTATIC) return false;
        MethodInsnNode m = (MethodInsnNode)s;
        if(m.owner.equals(DEFAULT_TYPE_TRANSFORMATION)==false) return false;
        if(m.name.equals("box")) {
            units.remove(s);
            return true;
        } else if(m.name.endsWith("Unbox")) {
            units.remove(s);
            return true;
        }
        return false;
    }

}
