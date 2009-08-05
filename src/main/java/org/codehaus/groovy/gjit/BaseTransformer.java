package org.codehaus.groovy.gjit;

import java.util.ListIterator;
import java.util.Map;

import org.codehaus.groovy.gjit.MyBasicInterpreter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Value;

public class BaseTransformer implements Opcodes {

    private String owner;
    //private Interpreter interpreter;

    protected MethodNode node;
    protected InsnList units;
    protected Map<AbstractInsnNode, Value[]> use;
    protected int callSiteVar;
    protected Map<AbstractInsnNode, Frame> frames;

    protected boolean preTransformationOnly=false;

    protected Interpreter interpreter;

    public BaseTransformer(String owner, MethodNode mn) {
        super();
        this.owner = owner;
        this.node = mn;
        this.units = node.instructions;
    }

    public BaseTransformer(Interpreter interpreter, String owner, MethodNode mn) {
        //super(interpreter);
        this.interpreter = interpreter;
        this.owner = owner;
        this.node = mn;
        this.units = node.instructions;
    }

    public void transform() throws AnalyzerException {
        if(interpreter instanceof MyBasicInterpreter) {
            this.use = ((MyBasicInterpreter)interpreter).use;
        }
        pretransform();
        if(preTransformationOnly) return;
        // frames = this.analyze(this.owner, this.node);
        posttransform();
    }

    protected void posttransform() {

    }

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

    protected void pretransform() {
        ListIterator<?> stmts = units.iterator();
        while(stmts.hasNext()) {
            AbstractInsnNode s = (AbstractInsnNode)stmts.next();
            switch(phase) {
                case PHASE_CALLSITE: phase = processPhaseCallSite(s); break;
            }
        }
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

}
