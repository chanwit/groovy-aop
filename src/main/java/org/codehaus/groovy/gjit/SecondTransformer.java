package org.codehaus.groovy.gjit;

import static org.codehaus.groovy.gjit.DebugUtils.dump;
import static org.codehaus.groovy.gjit.DebugUtils.print;
import static org.codehaus.groovy.gjit.DebugUtils.println;
import static org.codehaus.groovy.gjit.DebugUtils.toggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

// import org.codehaus.groovy.gjit.db.ClassEntry;
// import org.codehaus.groovy.gjit.db.SiteTypePersistentCache;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.AbstractVisitor;

public class SecondTransformer extends BaseTransformer {

    private ConstantPack pack;
    private String[] siteNames;
    private int[] localTypes;
    private Integer currentSiteIndex;
    private Stack<Integer> callSiteIndexStack = new Stack<Integer>();
    private Map<Integer, AbstractInsnNode> callSiteInsnLocations = new HashMap<Integer, AbstractInsnNode>();
    private List<Integer> unusedCallSites = new ArrayList<Integer>();
    //private List<AbstractInsnNode> fixed = new ArrayList<AbstractInsnNode>();
    // private ClassEntry ce;
    private HashMap<AbstractInsnNode, Integer> instToCallsiteIndex = new HashMap<AbstractInsnNode, Integer>();

    private static final String SCRIPT_BYTECODE_ADAPTER = "org/codehaus/groovy/runtime/ScriptBytecodeAdapter";
    private static final String CALL_SITE_INTERFACE = "org/codehaus/groovy/runtime/callsite/CallSite";
    private static final String DEFAULT_TYPE_TRANSFORMATION = "org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation";

    public SecondTransformer(String owner, MethodNode mn, ConstantPack pack, String[] siteNames) {
        super(owner, mn);
        this.pack = pack;
        this.siteNames = siteNames;
        this.localTypes = new int[mn.maxLocals];
        this.interpreter = new MyBasicInterpreter();// new FixableInterpreter();
        try {
           //  this.ce = SiteTypePersistentCache.v().find(owner);
        } catch (Throwable e) {
            DebugUtils.println("error cannot get cache entry of sitetype");
            // this.ce = null;
        }
    }

    @Override
    protected void pretransform() {
        preTransformationOnly = true;
        super.pretransform();
        int i = -1;
        while (true) {
            i++;
            if (i >= units.size())
                break;
            AbstractInsnNode s = units.get(i);
            DebugUtils.dump(s);
            if (extractCallSiteName(s)) continue;
            recordUnusedCallSite(s);
            if (unwrapBinOp(s)) continue;
            if (eliminateBoxCastUnbox(s)) {
                i--;
                continue;
            }
            if (unwrapConst(s)) continue;
            if (unwrapBooleanAndIF(s)) continue;
            if (unwrapBoxOrUnbox(s)) {
                i--;
                continue;
            }
            if (unwrapCompare(s)) {
                i--;
                continue;
            }
            if (clearWrapperCast(s)) {
                i--;
                continue;
            }
            if (fixALOAD(s)) {
                i--;
                continue;
            }
            if (fixASTORE(s)) {
                i--;
                continue;
            }
            if (fixHasNext(s)) continue;
            if (fixAASTORE(s)) continue;
            if (fix_XRETURN(s)) {
                i++;
                continue;
            }
            if (fix_DUP(s)) {
                i--;
                continue;
            }
            if (fix_POP(s)) continue;
        }
        DebugUtils.println("===== pre-transformed");
        relocateLocalVars();
        removeUnusedCallSite();
        i = -1;
        DebugUtils.println("===== phase 2");
        while (true) {
            i++;
            if (i >= units.size())
                break;
            AbstractInsnNode s = units.get(i);
            if (correctCall(s)) {
                i = units.indexOf(s);
                continue;
            }
            if (correctSBAMethods(s)) {
                i = units.indexOf(s);
                continue;
            }
            if (fix_DUP(s)) {
                i = units.indexOf(s);
                continue;
            }
            if (fix_POP(s)) {
                i = units.indexOf(s);
                continue;
            }
        }
    }

    private boolean unwrapBooleanAndIF(AbstractInsnNode s) {
//    GETSTATIC java/lang/Boolean.TRUE : Ljava/lang/Boolean;
//    INVOKESTATIC org/codehaus/groovy/runtime/typehandling/DefaultTypeTransformation.booleanUnbox (Ljava/lang/Object;)Z
//    IFEQ L15
        if(s.getOpcode() != GETSTATIC) return false;
        AbstractInsnNode s1 = s.getNext();
        if(s1.getOpcode()==-1) s1 = s1.getNext();
        if(s1.getOpcode() != INVOKESTATIC) return false;
        AbstractInsnNode s2 = s1.getNext();
        if(s2 instanceof JumpInsnNode == false) return false;
        FieldInsnNode f = ((FieldInsnNode)s);
        MethodInsnNode iv1 = (MethodInsnNode)s1;
        if(f.owner.equals("java/lang/Boolean") && iv1.owner.equals(DEFAULT_TYPE_TRANSFORMATION) && iv1.name.equals("booleanUnbox")) {
            if(f.name.equals("TRUE")) {
                units.set(s, new InsnNode(ICONST_1));
                units.remove(s1);
                return true;
            } else if(f.name.equals("FALSE")) {
                units.set(s, new InsnNode(ICONST_0));
                units.remove(s1);
                return true;
            }
        }
        return false;
    }

    private boolean fix_POP(AbstractInsnNode s) {
        if(s.getOpcode() != POP) return false;
        AbstractInsnNode p1 = s.getPrevious();
        AbstractInsnNode p2 = p1.getPrevious();
//		DebugUtils.dump = true;
//		DebugUtils.dump(p2);
//		DebugUtils.dump(p1);
//		DebugUtils.dump(s);
//		DebugUtils.dump = false;
        if(p2.getOpcode() == DUP2_X1) {
            units.set(s, new InsnNode(POP2));
            return true;
        } else if(p2.getOpcode() == DUP2 && getBytecodeType(p1).getSize()==2) {
            units.set(s, new InsnNode(POP2));
            return true;
        } else if(p1.getOpcode() == DLOAD || p1.getOpcode() == LLOAD) {
            units.set(s, new InsnNode(POP2));
            return true;
        }
        return false;
    }

    private boolean fix_DUP(AbstractInsnNode s) {
        if(s.getOpcode()==DUP || s.getOpcode()==DUP2) {
            AbstractInsnNode p = s.getPrevious();
            AbstractInsnNode s1 = s.getNext();
            AbstractInsnNode s2 = s1.getNext();
            Type t = getBytecodeType(p);
            if(s1.getOpcode() == ALOAD && s2.getOpcode() == SWAP) {
                if(t==null && s.getOpcode()==DUP) return false;
                if((t != null && t.getSize() == 2) || s.getOpcode() == DUP2) {
                    units.remove(s);
                    units.insertBefore(s2, new InsnNode(DUP_X2));
                    units.insertBefore(s2, new InsnNode(POP));
                    units.set(s2, new InsnNode(DUP2_X1));
                    return true;
                }
            } else if(p.getOpcode() == INVOKESTATIC && t == null && s.getOpcode() == DUP2) {
                units.set(s, new InsnNode(DUP));
                return false;
            }
        }
        return false;
    }

    private boolean fix_XRETURN(AbstractInsnNode s) {
        if(s.getOpcode() >= IRETURN && s.getOpcode() <= DRETURN) {
            AbstractInsnNode p = s.getPrevious();
            while(p instanceof LabelNode) p = p.getPrevious();
            if(p.getOpcode()==ACONST_NULL) {
                switch (s.getOpcode()) {
                case IRETURN:
                    units.set(p, new InsnNode(ICONST_0));
                    return true;
                case LRETURN:
                    units.set(p, new InsnNode(LCONST_0));
                    return true;
                case FRETURN:
                    units.set(p, new InsnNode(FCONST_0));
                    return true;
                case DRETURN:
                    units.set(p, new InsnNode(DCONST_0));
                    return true;
                }
            }
            int opcode = getConverterOpCode(getBytecodeType(p), getBytecodeType(s));
            if(opcode != 0) {
                units.insertBefore(s, new InsnNode(opcode));
                return true;
            }

            // case of the result is from call(/2)
            if(p.getOpcode() == INVOKEINTERFACE) {
                MethodInsnNode m = (MethodInsnNode)p;
                if(Type.getArgumentTypes(m.desc).length==2 && m.name.equals("call")) {
                    Type t = getBytecodeType(s);
                    unbox(s, t);
                    return true;
                }
            }
        } else if(s.getOpcode() == ARETURN) {
            AbstractInsnNode p = s.getPrevious();
            while(p instanceof LabelNode) p = p.getPrevious();
            if(p.getOpcode() == PUTFIELD) {
                FieldInsnNode f = (FieldInsnNode)p;
                if(f.desc.length()==1) {
                    switch(f.desc.charAt(0)) {
                        case 'I': box(p, Type.INT_TYPE); return true;
                        case 'L': box(p, Type.LONG_TYPE); return true;
                        case 'F': box(p, Type.FLOAT_TYPE); return true;
                        case 'D': box(p, Type.DOUBLE_TYPE); return true;
                    }
                }
            } else {
                switch(p.getOpcode()) {
                    case ILOAD: box(p, Type.INT_TYPE); return true;
                    case LLOAD: box(p, Type.LONG_TYPE); return true;
                    case FLOAD: box(p, Type.FLOAT_TYPE); return true;
                    case DLOAD: box(p, Type.DOUBLE_TYPE); return true;
                }
            }
        }
        return false;
    }

    private void relocateLocalVars() {
        int[] localIndex = new int[localTypes.length];
        int j = 0;
        DebugUtils.println(localTypes.length);
        for (int i = 0; i < localIndex.length; i++) {
            localIndex[i] = j;
            if (localTypes[i] == Type.LONG || localTypes[i] == Type.DOUBLE) {
                j++;
            }
            j++;
        }
        int i = -1;
        while (true) {
            i++;
            if (i >= units.size())
                break;
            AbstractInsnNode s = units.get(i);
            if (s instanceof VarInsnNode) {
                VarInsnNode v = ((VarInsnNode) s);
                v.var = localIndex[v.var];
            }
        }
    }

    @Override
    protected void posttransform() {
        DebugUtils.println(use.size());
    }

    private boolean correctSBAMethods(AbstractInsnNode s) {
        if (s.getOpcode() != INVOKESTATIC)
            return false;
        MethodInsnNode iv = ((MethodInsnNode) s);
        if (iv.owner.equals(SCRIPT_BYTECODE_ADAPTER) == false)
            return false;
        if (iv.name.equals("unwrap"))
            return false;
        DebugUtils.println(">>> === correctSBAMethods at " + s);
        fixByArguments_specialCase1(iv);
        unboxForCorrectCall(s);
        return true;
    }

    private boolean correctCall(AbstractInsnNode s) {
        if (s.getOpcode() != INVOKEINTERFACE)
            return false;
        MethodInsnNode iv = ((MethodInsnNode) s);
        if (iv.owner.equals(CALL_SITE_INTERFACE) == false)
            return false;
        if (iv.name.startsWith("call") == false)
            return false;
        DebugUtils.println(">>> === correctCall");
        fixByArguments_specialCase1(iv);
        unboxForCorrectCall(s);
        return true;
    }

    private void unboxForCorrectCall(AbstractInsnNode s) {
        AbstractInsnNode s1 = s.getNext();
        if(s1.getOpcode() == DUP) s1 = s1.getNext();
        int s1_opcode = s1.getOpcode();
        // TODO special case, need checking with ALOAD without SWAP
        if(s1_opcode>=ILOAD && s1_opcode <= DLOAD) return;
        if(s1_opcode>=ICONST_M1 && s1_opcode <= ICONST_5) return;
        if(s1_opcode>=LCONST_0 && s1_opcode <= LCONST_1) return;
        if(s1_opcode>=FCONST_0 && s1_opcode <= FCONST_1) return;
        if(s1_opcode>=DCONST_0 && s1_opcode <= DCONST_1) return;
        if(s1_opcode==LDC) return;
        if(s1_opcode==GETFIELD) return;
        if(s1_opcode==GETSTATIC) return;
        Type t = getBytecodeType(s1);
        if(t == null) {
            // try again looking for sequence of ALOAD, SWAP, XX
            AbstractInsnNode s2 = s1.getNext();
            AbstractInsnNode s3 = s2.getNext();
            if(s1_opcode==ALOAD && s2.getOpcode() == SWAP) {
                t = getBytecodeType(s3);
            }
        }
        if(t!=null) {
            s1 = s.getNext(); // re-check
            s1_opcode = s1.getOpcode();
            unbox(s1, t);
            if(s1_opcode == DUP && t.getSize()==2) {
                units.set(s1, new InsnNode(DUP2));
            }
        }

    }

    private AbstractInsnNode findStartingInsn(MethodInsnNode s) {
        ReverseStackDistance r = new ReverseStackDistance(s);
        return r.findStartingNode();
    }

    private void fixByArguments_specialCase1(MethodInsnNode iv) {
        //toggle();
        dump(iv);
        AbstractInsnNode s0 = findStartingInsn(iv);
        SimpleInterpreter in = new SimpleInterpreter(this.node, s0, iv);
        AbstractInsnNode[] useBox = in.analyse().get(iv);
        Type[] argTypes = Type.getArgumentTypes(iv.desc);
        if (iv.getOpcode() == INVOKESTATIC) {
            for (int i = 0; i < argTypes.length; i++) {
                if (useBox[i] == null)
                    continue;
                print("   use box " + i);
                dump(useBox[i]);
                if (argTypes[i].getSort() == Type.OBJECT
                        || argTypes[i].getSort() == Type.ARRAY) {
                    Type t = getBytecodeType(useBox[i]);
                    if (t != null) {
                        box(useBox[i], t);
                    }
                }
            }
        } else {
            for (int i = 0; i < argTypes.length; i++) {
                if (useBox[i + 1] == null) continue;
                print("   use box " + (i + 1));
                dump(useBox[i + 1]);
                if (argTypes[i].getSort() == Type.OBJECT
                        || argTypes[i].getSort() == Type.ARRAY) {
                    Type t = getBytecodeType(useBox[i + 1]);
                    if (t != null) {
                        dump(iv);
                        print(", to box ");
                        dump(useBox[i + 1]);
                        box(useBox[i + 1], t);
                    }
                }
            }
        }
        //toggle();
    }

    private boolean unwrapBinOp(AbstractInsnNode s) {
        if (s.getOpcode() == INVOKESTATIC) {
            MethodInsnNode iv = ((MethodInsnNode) s);
            if (iv.owner.equals(CALL_SITE_INTERFACE) == false)
                return false;
            if (iv.desc.equals(CALL_SITE_BIN_SIGNATURE) == false)
                return false;
            BinOp op = null;
            try {
                op = BinOp.valueOf(iv.name);
            } catch (Exception e) {
                op = null;
            }
            if (op == null)
                return false;
            AbstractInsnNode s_op2 = s.getPrevious();
            AbstractInsnNode s_op1 = s_op2.getPrevious();
            if (s_op1 instanceof MethodInsnNode || s_op2 instanceof MethodInsnNode ||
                isString(s_op1) || isString(s_op2)) {
                // use op1 as InsnNode to get its index
                // use op2 as InsnNode to get its index
                Integer op1_index = instToCallsiteIndex.get(s_op1);
                Type op1_rtype=null;
                try {
                    if(op1_index == null) {
                        op1_rtype = getBytecodeType(s_op1);
                    } else {
                        // TODO get type information from type advice
                        // op1_rtype = Type.getType(ce.getReturnType(op1_index));
                    }
                    Integer op2_index = instToCallsiteIndex.get(s_op2);
                    Type op2_rtype=null;
                    if(op1_index == null) {
                        op2_rtype = getBytecodeType(s_op2);
                    } else {
                        // TODO get type information from type advice
                        // op2_rtype = Type.getType(ce.getReturnType(op2_index));
                    }
                    // TODO dealing wtih callsite data here
                } catch(Throwable e) {
                    DebugUtils.println("callsite entry not available: " + e.getMessage());
                    iv.setOpcode(INVOKEINTERFACE);
                    iv.name = "call";
                    unusedCallSites.remove(currentSiteIndex);
                    return true;
                }
                iv.setOpcode(INVOKEINTERFACE);
                iv.name = "call";
                unusedCallSites.remove(currentSiteIndex);
                return true;
            }
            Type t2 = getBytecodeType(s_op2);
            Type t1 = getBytecodeType(s_op1);
            Type toType=t1;
            Type fromType = null;
            if (t1 != null && t2 != null) {
                if(t1 != t2) {
                    AbstractInsnNode op_to_promote;
                    if(t1.getSort() > t2.getSort()) {
                        fromType = t2;
                        toType = t1;
                        op_to_promote = s_op2;
                    } else {
                        fromType = t1;
                        toType = t2;
                        op_to_promote = s_op1;
                    }
                    InsnNode converter=new InsnNode(getConverterOpCode(fromType, toType));
                    if(converter.getOpcode()!=NOP) {
                        units.insert(op_to_promote, converter);
                    }
                }
                int offset = 0;
                if (toType == Type.LONG_TYPE) offset = 1;
                else if (toType == Type.FLOAT_TYPE) offset = 2;
                else if (toType == Type.DOUBLE_TYPE) offset = 3;
                AbstractInsnNode newS = null;
                switch (op) {
                    case minus:
                        newS = new InsnNode(ISUB + offset);
                        units.set(s, newS);
                        break;
                    case plus:
                        newS = new InsnNode(IADD + offset);
                        units.set(s, newS);
                        break;
                    case multiply:
                        newS = new InsnNode(IMUL + offset);
                        units.set(s, newS);
                        break;
                    case div:
                        newS = new InsnNode(IDIV + offset);
                        units.set(s, newS);
                        break;
                    case leftShift:
                        newS = new InsnNode(ISHL + offset);
                        units.set(s, newS);
                        break;
                    case rightShift:
                        newS = new InsnNode(ISHR + offset);
                        units.set(s, newS);
                        break;
                }
                if(toType.getSize() == 2 && newS.getNext().getOpcode()==DUP) {
                    units.set(newS.getNext(), new InsnNode(DUP2));
                }
                return true;
            }
        }
        return false;
    }

    private boolean isString(AbstractInsnNode op) {
        if(op.getOpcode()==LDC) {
            return ((LdcInsnNode)op).cst instanceof String;
        }
        return false;
    }

    private Type getBytecodeType(AbstractInsnNode op) {
        int opcode = op.getOpcode();
        if (opcode == GETFIELD || opcode == GETSTATIC || opcode == PUTFIELD || opcode == PUTSTATIC) {
            Type t = Type.getType(((FieldInsnNode) op).desc);
            if (t.getSort() == Type.OBJECT || t.getSort() == Type.ARRAY)
                return null;
            return t;
        }
        if (opcode >= ICONST_M1 && opcode <= ICONST_5)
            return Type.INT_TYPE;
        if (opcode == ILOAD
                || opcode == BIPUSH
                || opcode == SIPUSH
                || opcode == IALOAD
                || opcode == IADD
                || opcode == ISUB
                || opcode == IMUL
                || opcode == IDIV
                || opcode == IRETURN
                || opcode == ISTORE
                || (opcode == LDC && ((LdcInsnNode) op).cst instanceof Integer))
            return Type.INT_TYPE;
        if (opcode == LLOAD
                || opcode == LCONST_0
                || opcode == LCONST_1
                || opcode == LALOAD
                || opcode == LADD
                || opcode == LSUB
                || opcode == LMUL
                || opcode == LDIV
                || opcode == LRETURN
                || opcode == LSTORE
                || (opcode == LDC && ((LdcInsnNode) op).cst instanceof Long))
            return Type.LONG_TYPE;
        if (opcode == FLOAD
                || opcode == FCONST_0
                || opcode == FCONST_1
                || opcode == FALOAD
                || opcode == FADD
                || opcode == FSUB
                || opcode == FMUL
                || opcode == FDIV
                || opcode == FRETURN
                || opcode == FSTORE
                || (opcode == LDC && ((LdcInsnNode) op).cst instanceof Float))
            return Type.FLOAT_TYPE;
        if (opcode == DLOAD
                || opcode == DCONST_0
                || opcode == DCONST_1
                || opcode == DALOAD
                || opcode == DADD
                || opcode == DSUB
                || opcode == DMUL
                || opcode == DDIV
                || opcode == DRETURN
                || opcode == DSTORE
                || (opcode == LDC && ((LdcInsnNode) op).cst instanceof Double))
            return Type.DOUBLE_TYPE;
        return null;
    }

    private void removeUnusedCallSite() {
        // Set<Entry<Integer, AbstractInsnNode>> set =
        // unusedCallSites.entrySet();
        for (Integer index : unusedCallSites) {
            AbstractInsnNode s = callSiteInsnLocations.get(index);
            AbstractInsnNode s1 = s.getNext(); // LDC
            AbstractInsnNode s2 = s1.getNext(); // AALOAD
            DebugUtils.println(">>>> ===");
            DebugUtils.println(index);
            DebugUtils.println(siteNames[index]);
            DebugUtils.println(AbstractVisitor.OPCODES[s.getOpcode()]);
            DebugUtils.println(AbstractVisitor.OPCODES[s1.getOpcode()]);
            DebugUtils.println(AbstractVisitor.OPCODES[s2.getOpcode()]);
            units.remove(s);
            units.remove(s1);
            units.remove(s2);
        }
    }

    private void recordUnusedCallSite(AbstractInsnNode s) {
        if (s.getOpcode() != INVOKEINTERFACE)
            return;
        MethodInsnNode iv = (MethodInsnNode) s;
        if (iv.owner.equals(CALL_SITE_INTERFACE) == false)
            return;
        if (iv.name.equals("call") == false)
            return;
        currentSiteIndex = callSiteIndexStack.pop();
        instToCallsiteIndex.put(s, currentSiteIndex);
        // AbstractInsnNode p2 = s.getPrevious();
        // AbstractInsnNode p1 = p2.getPrevious();
        if (isBinOpPrimitiveCall(s) == true) {
            iv.setOpcode(INVOKESTATIC);
            iv.name = siteNames[currentSiteIndex];
            unusedCallSites.add(currentSiteIndex);
        }
    }

    private boolean fixAASTORE(AbstractInsnNode s) {
        if (s.getOpcode() != AASTORE)
            return false;
        AbstractInsnNode p = s.getPrevious();
        switch (p.getOpcode()) {
        case ILOAD:
        case IADD:
        case ISUB:
        case IMUL:
        case IDIV:
        case ISHL:
        case ISHR:
            box(p, Type.INT_TYPE);
            break;
        case LLOAD:
        case LADD:
        case LSUB:
        case LMUL:
        case LDIV:
        case LSHL:
        case LSHR:
            box(p, Type.LONG_TYPE);
            break;
        case FLOAD:
        case FADD:
        case FSUB:
        case FMUL:
        case FDIV:
            box(p, Type.FLOAT_TYPE);
            break;
        case DLOAD:
        case DADD:
        case DSUB:
        case DMUL:
        case DDIV:
            box(p, Type.DOUBLE_TYPE);
            break;
        }
        return true;
    }

//    @Override
//    public Action process(AbstractInsnNode s,
//            Map<AbstractInsnNode, Frame> frames) {
//        // if(extractCallSiteName(s)) return Action.NONE;
//        // if(eliminateBoxCastUnbox(s)) return Action.REMOVE;
//        // if(unwrapConst(s)) return Action.REPLACE;
//        // if(unwrapBoxOrUnbox(s)) return Action.REMOVE;
//        // if(unwrapBinaryPrimitiveCall(s, frames.get(s))) return
//        // Action.REPLACE;
//        // if(unwrapCompare(s,frames.get(s))) return Action.REMOVE;
//        // if(clearWrapperCast(s)) return Action.REMOVE;
//        // if(fixASTORE(s,frames.get(s))) return Action.REPLACE;
//        // if(fixALOAD(s)) return Action.REPLACE;
//        // if(fixHasNext(s)) return Action.REPLACE; // workaround ASM verifier
//        // if(fixAASTORE(s,frames.get(s))) return Action.ADD;
//        return Action.NONE;
//    }

    private boolean fixHasNext(AbstractInsnNode s) {
        if (s.getOpcode() != INVOKEINTERFACE)
            return false;
        MethodInsnNode m = ((MethodInsnNode) s);
        // mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext",
        // "()Z");
        if (m.owner.equals("java/util/Iterator")
                && s.getPrevious().getOpcode() != CHECKCAST) {
            units.insertBefore(s, new TypeInsnNode(CHECKCAST, m.owner));
            return true;
        }
        return false;
    }

    // private boolean fixASTORE(AbstractInsnNode s, Frame frame) {
    // if(s.getOpcode()!=ASTORE) return false;
    // BasicValue top = (BasicValue)frame.getStack(frame.getStackSize()-1);
    // return fixASTORE(s, top.getType());
    // }

    private boolean fixASTORE(AbstractInsnNode s) {
        if (s.getOpcode() != ASTORE)
            return false;
        int sort = localTypes[((VarInsnNode) s).var];
        if (sort != 0) { // special case, done fixing before
            return fixASTORE(s, sort);
        }
        AbstractInsnNode p = s.getPrevious();
        switch (p.getOpcode()) {
        case ILOAD:
        case IADD:
        case ISUB:
        case IMUL:
        case IDIV:
            return fixASTORE(s, Type.INT_TYPE);
        case LLOAD:
        case LADD:
        case LSUB:
        case LMUL:
        case LDIV:
            return fixASTORE(s, Type.LONG_TYPE);
        case FLOAD:
        case FADD:
        case FSUB:
        case FMUL:
        case FDIV:
            return fixASTORE(s, Type.FLOAT_TYPE);
        case DLOAD:
        case DADD:
        case DSUB:
        case DMUL:
        case DDIV:
            return fixASTORE(s, Type.DOUBLE_TYPE);
        }
        return false;
    }

    private boolean fixASTORE(AbstractInsnNode s, int sort) {
        VarInsnNode v = (VarInsnNode) s;
        Type t = null;
        AbstractInsnNode p = s.getPrevious();
        VarInsnNode newS;
        switch (sort) {
            case Type.INT:
                if(p.getOpcode()==ACONST_NULL) units.set(p, new InsnNode(ICONST_0));
                newS = new VarInsnNode(ISTORE, v.var);
                units.set(s, newS);
                localTypes[v.var] = Type.INT;
                t = Type.INT_TYPE;
                break;
            case Type.LONG:
                if(p.getOpcode()==ACONST_NULL) units.set(p, new InsnNode(LCONST_0));
                newS = new VarInsnNode(LSTORE, v.var);
                units.set(s, newS);
                localTypes[v.var] = Type.LONG;
                t = Type.LONG_TYPE;
                break;
            case Type.FLOAT:
                if(p.getOpcode()==ACONST_NULL) units.set(p, new InsnNode(FCONST_0));
                newS = new VarInsnNode(FSTORE, v.var);
                units.set(s, newS);
                localTypes[v.var] = Type.FLOAT;
                t = Type.FLOAT_TYPE;
                break;
            case Type.DOUBLE:
                if(p.getOpcode()==ACONST_NULL) units.set(p, new InsnNode(DCONST_0));
                newS = new VarInsnNode(DSTORE, v.var);
                units.set(s, newS);
                localTypes[v.var] = Type.DOUBLE;
                t = Type.DOUBLE_TYPE;
                break;
            default:
                return false;
        }
        if (t != null) {
            p = newS.getPrevious();
            if (p != null) {
                if (p.getOpcode() == DUP) p = p.getPrevious();
                if (p instanceof MethodInsnNode) {
                    MethodInsnNode iv = ((MethodInsnNode) p);
                    if (iv.name.equals("call") && iv.desc.equals(CALL_SITE_BIN_SIGNATURE)) {
                        unbox(newS, t);
                    } else if(iv.name.endsWith("next") &&
                              iv.owner.equals("java/util/Iterator") &&
                              iv.desc.equals(ITERATOR_NEXT_SIGNATURE)) {
                        unbox(newS, t);
                    }
                } else if(getBytecodeType(p) != getBytecodeType(newS)) {
//					DebugUtils.dump = true;
//					DebugUtils.dump(p);
//					DebugUtils.dump(newS);
//					DebugUtils.dump = false;
                    int converterOpcode = getConverterOpCode(getBytecodeType(p),getBytecodeType(newS));
                    if(converterOpcode != 0) {
                        InsnNode converter = new InsnNode(converterOpcode);
                        units.insertBefore(newS, converter);
                    }
                }
            }
        }
        return true;
    }


    private int getConverterOpCode(Type fromType, Type toType) {
        if(fromType == null) return 0;
        if(toType == null) return 0;
        switch(fromType.getSort()) {
            case Type.INT: return getConvertIntTo(toType);
            case Type.LONG: return getConvertLongTo(toType);
            case Type.FLOAT: return getConvertFloatTo(toType);
            case Type.DOUBLE: return getConvertDoubleTo(toType);
        }
        return 0;
    }

    private int getConvertIntTo(Type toType) {
        switch(toType.getSort()) {
            case Type.LONG: return I2L;
            case Type.FLOAT: return I2F;
            case Type.DOUBLE: return I2D;
        }
        return 0;
    }

    private int getConvertLongTo(Type toType) {
        switch(toType.getSort()) {
            case Type.INT: return L2I;
            case Type.FLOAT: return L2F;
            case Type.DOUBLE: return L2D;
        }
        return 0;
    }

    private int getConvertFloatTo(Type toType) {
        switch(toType.getSort()) {
            case Type.INT: return F2I;
            case Type.LONG: return F2L;
            case Type.DOUBLE: return F2D;
        }
        return 0;
    }

    private int getConvertDoubleTo(Type toType) {
        switch(toType.getSort()) {
            case Type.INT: return D2I;
            case Type.LONG: return D2L;
            case Type.FLOAT: return D2F;
        }
        return 0;
    }

    private boolean fixASTORE(AbstractInsnNode s, Type type) {
        if (type == null)
            return false;
        // VarInsnNode v = (VarInsnNode)s;
        return fixASTORE(s, type.getSort());
    }

    private boolean fixALOAD(AbstractInsnNode s) {
        if (s.getOpcode() != ALOAD)
            return false;
        VarInsnNode v = (VarInsnNode) s;
        switch (localTypes[v.var]) {
        case Type.INT:
            units.set(s, new VarInsnNode(ILOAD, v.var));
            return true;
        case Type.LONG:
            units.set(s, new VarInsnNode(LLOAD, v.var));
            return true;
        case Type.FLOAT:
            units.set(s, new VarInsnNode(FLOAD, v.var));
            return true;
        case Type.DOUBLE:
            units.set(s, new VarInsnNode(DLOAD, v.var));
            return true;
        default:
            return false;
        }
    }

    private void box(AbstractInsnNode source, Type t) {
        String boxType = null;
        String primType = null;
        switch (t.getSort()) {
        case Type.INT:
            boxType = "java/lang/Integer";
            primType = "I";
            break;
        case Type.LONG:
            boxType = "java/lang/Long";
            primType = "J";
            break;
        case Type.FLOAT:
            boxType = "java/lang/Float";
            primType = "F";
            break;
        case Type.DOUBLE:
            boxType = "java/lang/Double";
            primType = "D";
            break;
        default:
            throw new RuntimeException("I'm trying to catch you" + t);
            // break;
        }
        MethodInsnNode iv = new MethodInsnNode(INVOKESTATIC, boxType,
                "valueOf", "(" + primType + ")L" + boxType + ";");
        // if(source.getOpcode()==SWAP) source = source.getPrevious(); // work
        // around for inserted SWAP,POP
        // else if(source.getOpcode()==DUP2_X1) source =
        // source.getNext().getNext();// POP2, POP,|
        units.insert(source, iv);
    }

    private void unbox(AbstractInsnNode s, Type t) {
        String boxType = null;
        String primType = null;
        String primTypeName = null;
        switch (t.getSort()) {
        case Type.INT:
            boxType = "java/lang/Integer";
            primType = "I";
            primTypeName = "int";
            break;
        case Type.LONG:
            boxType = "java/lang/Long";
            primType = "J";
            primTypeName = "long";
            break;
        case Type.FLOAT:
            boxType = "java/lang/Float";
            primType = "F";
            primTypeName = "float";
            break;
        case Type.DOUBLE:
            boxType = "java/lang/Double";
            primType = "D";
            primTypeName = "double";
            break;
        }
        TypeInsnNode cast = new TypeInsnNode(CHECKCAST, boxType);
        MethodInsnNode iv = new MethodInsnNode(INVOKEVIRTUAL, boxType,
            primTypeName + "Value", "()" + primType);
        AbstractInsnNode p = s.getPrevious();
        if (p instanceof LabelNode) {
            s = p;
        }
        units.insertBefore(s, cast);
        units.insert(cast, iv);
    }

    private boolean extractCallSiteName(AbstractInsnNode s) {
        if (s.getOpcode() != ALOAD)
            return false;
        VarInsnNode v = (VarInsnNode) s;
        if (v.var != callSiteVar)
            return false;
        AbstractInsnNode s1 = s.getNext();
        AbstractInsnNode s2 = s1.getNext();
        if (s1.getOpcode() != LDC)
            return false;
        if (s2.getOpcode() != AALOAD)
            return false;
        LdcInsnNode l = (LdcInsnNode) s1;
        callSiteIndexStack.push((Integer) l.cst);
        callSiteInsnLocations.put((Integer) l.cst, s);
        return true;
    }

    private boolean eliminateBoxCastUnbox(AbstractInsnNode s) {
        if (s.getOpcode() != INVOKESTATIC)
            return false;
        AbstractInsnNode s1 = s.getNext();
        if (s1 == null)
            return false;
        if (s1.getOpcode() != INVOKESTATIC)
            return false;
        AbstractInsnNode s2 = s1.getNext();
        if (s2 == null)
            return false;
        if (s2.getOpcode() != INVOKESTATIC)
            return false;
        AbstractInsnNode s3 = s2.getNext();
        if (s3 == null)
            return false;
        if (s3.getOpcode() != CHECKCAST)
            return false;
        AbstractInsnNode s4 = s3.getNext();
        if (s4 == null)
            return false;
        if (s4.getOpcode() != INVOKESTATIC)
            return false;
        MethodInsnNode m = (MethodInsnNode) s;
        MethodInsnNode m1 = (MethodInsnNode) s1;
        MethodInsnNode m2 = (MethodInsnNode) s2;
        MethodInsnNode m4 = (MethodInsnNode) s4;
        if (m.owner.equals(DEFAULT_TYPE_TRANSFORMATION) == false)
            return false;
        if (m.name.equals("box") == false)
            return false;
        if (m1.name.startsWith("$get$$class$") == false)
            return false;
        if (m2.name.startsWith("castToType") == false)
            return false;
        if (m4.name.endsWith("Unbox") == false)
            return false;
        units.remove(s);
        units.remove(s1);
        units.remove(s2);
        units.remove(s3);
        units.remove(s4);
        return true;
    }

    private boolean unwrapBoxOrUnbox(AbstractInsnNode s) {
        if (s.getOpcode() != INVOKESTATIC)
            return false;
        MethodInsnNode m = (MethodInsnNode) s;
        if (m.owner.equals(DEFAULT_TYPE_TRANSFORMATION) == false)
            return false;
        if (m.name.equals("box")) {
            // unit_remove(s,s.getPrevious());
            units.remove(s);
            return true;
        } else if (m.name.endsWith("Unbox")) {
            // unit_remove(s,s.getPrevious());
            units.remove(s);
            return true;
        }
        return false;
    }

    private boolean unwrapConst(AbstractInsnNode s) {
        if (s.getOpcode() != GETSTATIC)
            return false;
        FieldInsnNode f = (FieldInsnNode) s;
        if (f.name.startsWith("$const$")) {
            // special case, not unwrap
            AbstractInsnNode s11 = s.getNext();
            if(s11.getOpcode()==NEW && ((TypeInsnNode)s11).desc.equals("groovy/lang/Reference")) return false;

            DebugUtils.println(">>> pass $const$");
            DebugUtils.println(f.name);
            Object constValue = pack.get(f.name);
            DebugUtils.println("const type: " + constValue.getClass());
            AbstractInsnNode newS = new LdcInsnNode(constValue);
            if (constValue instanceof Integer) {
                int c = (Integer) constValue;
                if (c >= -1 && c <= 5) {
                    switch (c) {
                    case -1:
                        newS = new InsnNode(ICONST_M1);
                        break;
                    case 0:
                        newS = new InsnNode(ICONST_0);
                        break;
                    case 1:
                        newS = new InsnNode(ICONST_1);
                        break;
                    case 2:
                        newS = new InsnNode(ICONST_2);
                        break;
                    case 3:
                        newS = new InsnNode(ICONST_3);
                        break;
                    case 4:
                        newS = new InsnNode(ICONST_4);
                        break;
                    case 5:
                        newS = new InsnNode(ICONST_5);
                        break;
                    }
                } else if (c >= -128 && c <= 127) {
                    newS = new IntInsnNode(BIPUSH, c);
                } else if (c >= -32768 && c <= 32767) {
                    newS = new IntInsnNode(SIPUSH, c);
                }
            }
            AbstractInsnNode s1 = s.getNext();

            if(s1.getOpcode() == DUP) {
                AbstractInsnNode temp = s1.getNext(); // sometime the compiler use DUP to reuse TOS
                if(newS instanceof LdcInsnNode && (constValue instanceof Long || constValue instanceof Double)) {
                    units.set(s1, new InsnNode(DUP2));
                }
                s1 = temp;
            }

            units.set(s, newS);
            if (s1.getOpcode() == ASTORE) {
                Type type = Type.getType(constValue.getClass());
                fixASTORE(s1, getPrimitiveType(type));
            }
            DebugUtils.println("unwrap const");
            return true;
        }
        return false;
    }

    private Type getPrimitiveType(Type type) {
        return getPrimitiveType(type.getDescriptor());
    }

    private Type getPrimitiveType(String desc) {
        if (desc.charAt(0) != 'L')
            desc = 'L' + desc + ';';
        if (desc.equals("Ljava/lang/Integer;"))
            return Type.INT_TYPE;
        if (desc.equals("Ljava/lang/Long;"))
            return Type.LONG_TYPE;
        if (desc.equals("Ljava/lang/Float;"))
            return Type.FLOAT_TYPE;
        if (desc.equals("Ljava/lang/Double;"))
            return Type.DOUBLE_TYPE;
        return null;
    }

    private enum BinOp {
        minus, plus, multiply, div, leftShift, rightShift
    }

    private static final String CALL_SITE_BIN_SIGNATURE = "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";
    private static final String ITERATOR_NEXT_SIGNATURE = "()Ljava/lang/Object;";

    private boolean isBinOpPrimitiveCall(AbstractInsnNode s) {
        if (s.getOpcode() != INVOKEINTERFACE)
            return false;
        MethodInsnNode iv = (MethodInsnNode) s;
        if (iv.owner.equals(CALL_SITE_INTERFACE) == false)
            return false;
        if (iv.name.equals("call") == false)
            return false;
        if (iv.desc.equals(CALL_SITE_BIN_SIGNATURE) == false)
            return false;
        String name = siteNames[currentSiteIndex];
        BinOp op = null;
        try {
            op = BinOp.valueOf(name);
        } catch (IllegalArgumentException e) {}
        if (op == null) return false;
        return true;
    }

    private boolean clearWrapperCast(AbstractInsnNode s) {
        // INVOKESTATIC
        // TreeNode.$get$$class$java$lang$Integer()Ljava/lang/Class;
        // INVOKESTATIC
        // org/codehaus/groovy/runtime/ScriptBytecodeAdapter.castToType(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
        // CHECKCAST java/lang/Integer
        if (s.getOpcode() != INVOKESTATIC)
            return false;
        MethodInsnNode m = (MethodInsnNode) s;
        if (m.name.startsWith("$get$$class$java$lang$") == false)
            return false;
        AbstractInsnNode s1 = s.getNext();
        if (s1 == null)
            return false;
        if (s1.getOpcode() != INVOKESTATIC)
            return false;
        MethodInsnNode m1 = (MethodInsnNode) s1;
        if (m1.name.equals("castToType") == false)
            return false;
        AbstractInsnNode s2 = s1.getNext();
        if (s2 == null)
            return false;
        if (s2.getOpcode() != CHECKCAST)
            return false;
        TypeInsnNode t2 = (TypeInsnNode) s2;
        if (t2.desc.startsWith("java/lang") == false)
            return false;
        AbstractInsnNode s3 = s2.getNext();
        AbstractInsnNode s4 = s3.getNext();
        AbstractInsnNode s0 = s.getPrevious();
        if (s0 instanceof LabelNode)
            s0 = s0.getPrevious();
        units.remove(s);
        units.remove(s1);
        units.remove(s2);
        // DebugUtils.println(t2.desc);
//		DebugUtils.print("clear >>>>>>> s0 : ");
//		DebugUtils.dump(s0);
//		DebugUtils.print("clear >>>>>>> s3 : ");
//		DebugUtils.dump(s3.getNext());
        if (s3.getOpcode() == ASTORE) {
            if (s0 instanceof MethodInsnNode) {
                // DebugUtils.println(siteNames[currentSiteIndex]);
                if (isBinOpPrimitiveCall(s0) == false) {
                    unbox(s3, getPrimitiveType(t2.desc));
                }
            }
            fixASTORE(s3, getPrimitiveType(t2.desc));
        } else if(
                s0.getOpcode() == INVOKEVIRTUAL &&
                ((MethodInsnNode)s0).name.equals("get") &&
                ((MethodInsnNode)s0).owner.equals("groovy/lang/Reference") &&
                  s4.getOpcode() >= IRETURN &&
                  s4.getOpcode() <= DRETURN) {
            switch(s4.getOpcode()) {
                case IRETURN: unbox(s3, Type.INT_TYPE);
                    break;
                case LRETURN: unbox(s3, Type.LONG_TYPE);
                    break;
                case FRETURN: unbox(s3, Type.FLOAT_TYPE);
                    break;
                case DRETURN: unbox(s3, Type.DOUBLE_TYPE);
                    break;
            }
        }
        return true;
    }

    private enum ComparingMethod {
        compareLessThan,
        compareGreaterThan,
        compareLessThanEqual,
        compareGreaterThanEqual
    };

    private boolean unwrapCompare(AbstractInsnNode s) {
        if (s.getOpcode() != Opcodes.INVOKESTATIC)
            return false;
        MethodInsnNode m = (MethodInsnNode) s;
        if (m.owner.equals(SCRIPT_BYTECODE_ADAPTER) == false)
            return false;
        if (m.name.startsWith("compare") == false)
            return false;
        if (m.desc.equals("(Ljava/lang/Object;Ljava/lang/Object;)Z") == false)
            return false;
        AbstractInsnNode p2 = s.getPrevious();
        AbstractInsnNode p1 = p2.getPrevious();
        Type t1 = getBytecodeType(p1);
        Type t2 = getBytecodeType(p2);
        if(t1 == null || t2 == null) return false;

        Type fromType = null;
        Type toType = null;
        AbstractInsnNode whereToInsert = null;
        Type promotedType=null;
        // TODO doing type promotion
        if(t1.getSort() != t2.getSort()) {
            if(t1.getSort() < t2.getSort()) {
                fromType = t1;
                toType = t2;
                whereToInsert = p1;
                promotedType = t2;
            } else if(t2.getSort() < t1.getSort()) {
                fromType = t2;
                toType = t1;
                whereToInsert = p2;
                promotedType = t1;
            }
            InsnNode converter = new InsnNode(getConverterOpCode(fromType, toType));
            if(converter.getOpcode() != NOP) {
                units.insert(whereToInsert, converter);
            }
        } else {
            promotedType = t1;
        }
        ComparingMethod compare;
        try {
            compare = ComparingMethod.valueOf(m.name);
        } catch (IllegalArgumentException e) {
            return false;
        }
        switch(promotedType.getSort()) {
            case Type.INT: convertCompareForInt(compare, s); break;
            case Type.LONG: convertCompare(LCMP, compare, s); break;
            case Type.FLOAT: convertCompare(FCMPL, compare, s); break;
            case Type.DOUBLE: convertCompare(DCMPL, compare, s); break;
            default: return false;
        }
        return true;
    }

    private void convertCompare(int opcode, ComparingMethod compare,
            AbstractInsnNode s) {
        JumpInsnNode s1 = (JumpInsnNode) s.getNext();
        units.set(s, new InsnNode(opcode));
        switch (compare) {
            case compareGreaterThan:
                units.set(s1, new JumpInsnNode(IFLE, s1.label));
                break;
            case compareGreaterThanEqual:
                units.set(s1, new JumpInsnNode(IFLT, s1.label));
                break;
            case compareLessThan:
                units.set(s1, new JumpInsnNode(IFGE, s1.label));
                break;
            case compareLessThanEqual:
                units.set(s1, new JumpInsnNode(IFGT, s1.label));
                break;
        }
    }

    private void convertCompareForInt(ComparingMethod compare,
            AbstractInsnNode s) {
        JumpInsnNode s1 = (JumpInsnNode) s.getNext();
        switch (compare) {
            case compareGreaterThan:
                units.set(s1, new JumpInsnNode(IF_ICMPLE, s1.label));
                break;
            case compareGreaterThanEqual:
                units.set(s1, new JumpInsnNode(IF_ICMPLT, s1.label));
                break;
            case compareLessThan:
                units.set(s1, new JumpInsnNode(IF_ICMPGE, s1.label));
                break;
            case compareLessThanEqual:
                units.set(s1, new JumpInsnNode(IF_ICMPGT, s1.label));
                break;
        }
        units.remove(s);
    }

}
