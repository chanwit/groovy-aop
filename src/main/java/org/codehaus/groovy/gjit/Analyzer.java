package org.codehaus.groovy.gjit;
/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

/**
 * A semantic bytecode analyzer. <i>This class does not fully check that JSR and
 * RET instructions are valid.</i>
 *
 * @author Eric Bruneton
 * @author Chanwit Kaewkasi
 */
public class Analyzer implements Opcodes {

    protected Interpreter interpreter;

    private InsnList insns;

    private Map<AbstractInsnNode, List<TryCatchBlockNode>> handlers = new HashMap<AbstractInsnNode, List<TryCatchBlockNode>>();
    private Map<AbstractInsnNode, Frame> frames = new HashMap<AbstractInsnNode, Frame>();
    private Map<AbstractInsnNode, Subroutine> subroutines = new HashMap<AbstractInsnNode, Subroutine>();
    private Map<AbstractInsnNode, Boolean> queued = new HashMap<AbstractInsnNode, Boolean>();
    private Map<AbstractInsnNode, AbstractInsnNode> queue = new HashMap<AbstractInsnNode, AbstractInsnNode>();
    private AbstractInsnNode top;

    /**
     * Constructs a new {@link Analyzer}.
     *
     * @param interpreter the interpreter to be used to symbolically interpret
     *        the bytecode instructions.
     */
    public Analyzer(final Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Analyzer() {
    }

    public enum Action {
        NONE,
        ADD,
        REMOVE,
        REPLACE
    }

    /**
     * Analyzes the given method.
     *
     * @param owner the internal name of the class to which the method belongs.
     * @param m the method to be analyzed.
     * @return the symbolic state of the execution stack frame at each bytecode
     *         instruction of the method. The size of the returned array is
     *         equal to the number of instructions (and labels) of the method. A
     *         given frame is <tt>null</tt> if and only if the corresponding
     *         instruction cannot be reached (dead code).
     * @throws AnalyzerException if a problem occurs during the analysis.
     */

//    private enum ExecutionState {
//        NORMAL,
//        HANDLED,
//        HANDLING
//    }

    // private ExecutionState state = ExecutionState.NORMAL;

    private final static Map<AbstractInsnNode, Frame> EMPTY_FRAMES =  new HashMap<AbstractInsnNode, Frame>();

    public Map<AbstractInsnNode, Frame> analyze(final String owner, final MethodNode m)
            throws AnalyzerException
    {
        if ((m.access & (ACC_ABSTRACT | ACC_NATIVE)) != 0) {
            return EMPTY_FRAMES;
        }
        insns = m.instructions;
        if(insns.size() == 0) {
            return EMPTY_FRAMES;
        }
        top = insns.get(0);

        // computes exception handlers for each instruction
        for (int i = 0; i < m.tryCatchBlocks.size(); ++i) {
            TryCatchBlockNode tcb = (TryCatchBlockNode) m.tryCatchBlocks.get(i);
            int begin = insns.indexOf(tcb.start);
            int end = insns.indexOf(tcb.end);
            for (int j = begin; j < end; ++j) {
                AbstractInsnNode jth = insns.get(j);
                List<TryCatchBlockNode> insnHandlers = handlers.get(jth);
                if (insnHandlers == null) {
                    insnHandlers = new ArrayList<TryCatchBlockNode>();
                    handlers.put(jth, insnHandlers);
                }
                insnHandlers.add(tcb);
            }
        }

        // computes the subroutine for each instruction:
        Subroutine main = new Subroutine(null, m.maxLocals, null);
        List<AbstractInsnNode> subroutineCalls = new ArrayList<AbstractInsnNode>();
        Map<LabelNode, Subroutine> subroutineHeads = new HashMap<LabelNode, Subroutine>();
        findSubroutine(top, main, subroutineCalls);
        while (!subroutineCalls.isEmpty()) {
            JumpInsnNode jsr = (JumpInsnNode) subroutineCalls.remove(0);
            Subroutine sub = subroutineHeads.get(jsr.label);
            if (sub == null) {
                sub = new Subroutine(jsr.label, m.maxLocals, jsr);
                subroutineHeads.put(jsr.label, sub);
                findSubroutine(jsr.label, sub, subroutineCalls);
            } else {
                sub.callers.add(jsr);
            }
        }
        for (int i = 0; i < insns.size(); ++i) {
            AbstractInsnNode in = insns.get(i);
            if(subroutines.containsKey(in) && subroutines.get(in).start == null) {
                subroutines.remove(in);
            }
        }

        // initializes the data structures for the control flow analysis
        Frame current = newFrame(m.maxLocals, m.maxStack);
        Frame handler = newFrame(m.maxLocals, m.maxStack);
        Type[] args = Type.getArgumentTypes(m.desc);
        int local = 0;
        if ((m.access & ACC_STATIC) == 0) {
            Type ctype = Type.getObjectType(owner);
            current.setLocal(local++, interpreter.newValue(ctype));
        }
        for (int i = 0; i < args.length; ++i) {
            current.setLocal(local++, interpreter.newValue(args[i]));
            if (args[i].getSize() == 2) {
                current.setLocal(local++, interpreter.newValue(null));
            }
        }
        while (local < m.maxLocals) {
            current.setLocal(local++, interpreter.newValue(null));
        }
        merge(top, current, null);

        // AbstractInsnNode bookmarkNode = null;
        // AbstractInsnNode rollbackNode = null;
        List<AbstractInsnNode> finished = new ArrayList<AbstractInsnNode>();
        // Map<AbstractInsnNode,AbstractInsnNode> saveTops = new HashMap<AbstractInsnNode, AbstractInsnNode>();

        // control flow analysis
        while (top != null) {
            AbstractInsnNode insn=null;
            top = top.getPrevious();
            insn = queue.get(top);
            if(insn == null) insn = insns.get(0); // because default value in queue will be always 0

//        	switch(state) {
//        		case NORMAL:
//        			saveTops.put(insn, top);
//        		case HANDLING:
//        			top = top.getPrevious();
//		        	insn = queue.get(top);
//		        	if(insn == null) insn = insns.get(0); // because default value in queue will be always 0
//		        	break;
//        		case HANDLED:
//        			//top = saveTop;
//		        	//insn = rollbackNode;
//        			top = saveTops.get(rollbackNode);
//        			insn = rollbackNode;
//        			for(int i=0;i<finished.size(); i++) {
//        				queued.put(finished.get(i), Boolean.TRUE);
//        			}
//        			finished.clear();
//		        	if(insn == null) insn = insns.get(0); // because default value in queue will be always 0
//		        	rollbackNode = null;
//		        	state = ExecutionState.HANDLING;
//        			break;
//        	}

            Frame f = null;//frames.get(insn);
            Subroutine subroutine = null; //subroutines.get(insn);
            // queued.put(insn, Boolean.FALSE);

            try {
                // DebugUtils.println(insn);
                int oldIndex = insns.indexOf(insn);
                // DebugUtils.println(">> ORG F: at " + oldIndex + "]["+ f);
                boolean done = false;
                while(done==false) {
                    f = frames.get(insn);
                    subroutine = subroutines.get(insn);
                    queued.put(insn, Boolean.FALSE);
                    finished.add(insn);

//        			if(state == ExecutionState.NORMAL) {

                    Action action = process(insn, frames);

                    switch(action) {
                        case REPLACE:
                            insn = insns.get(oldIndex);
                            // DebugUtils.println(">> F: " + f);
                            frames.put(insn, f);
                            subroutines.put(insn, subroutine);
                            queued.put(insn, Boolean.FALSE);
                            done = true;
                            break;
                        case REMOVE:
                            insn = insns.get(oldIndex);
                            frames.put(insn, f);
                            subroutines.put(insn, subroutine);
                            queued.put(insn, Boolean.FALSE);
                            continue;
                        default:
                            done = true;
                    }
//        			} else if(state == ExecutionState.HANDLING) {
//        				// executing only, pass through preprocessing
//        				done = true;
//        			}
                }
                AbstractInsnNode insnNode = insn;
                // m.instructions.get(insn);
                // DebugUtils.println(insn + ":" + insnNode);

                int insnOpcode = insnNode.getOpcode();
                int insnType = insnNode.getType();

                if(insnOpcode != -1) {
                    //DebugUtils.println("Opcode: " + AbstractVisitor.OPCODES[insnOpcode]);
                    // DebugUtils.println("Frame: " + f);
                    //debug(insn);
                    DebugUtils.dump(insn);
                }

                if (insnType == AbstractInsnNode.LABEL || insnType == AbstractInsnNode.LINE || insnType == AbstractInsnNode.FRAME) {
                    merge(insn.getNext(), f, subroutine);
                    newControlFlowEdge(insn, insn.getNext());
                } else {

                    current.init(f).execute(insnNode, interpreter);

                    subroutine = subroutine == null ? null : subroutine.copy();

                    if (insnNode instanceof JumpInsnNode) {
                        JumpInsnNode j = (JumpInsnNode) insnNode;
                        if (insnOpcode != GOTO && insnOpcode != JSR) {
                            merge(insn.getNext(), current, subroutine);
                            newControlFlowEdge(insn, insn.getNext());
                        }
                        AbstractInsnNode jump = j.label;
                        if (insnOpcode == JSR) {
                            merge(jump, current, new Subroutine(j.label, m.maxLocals, j));
                        } else {
                            merge(jump, current, subroutine);
                        }
                        newControlFlowEdge(insn, jump);
                    } else if (insnNode instanceof LookupSwitchInsnNode) {
                        LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insnNode;
                        AbstractInsnNode jump = lsi.dflt;
                        merge(jump, current, subroutine);
                        newControlFlowEdge(insn, jump);
                        for (int j = 0; j < lsi.labels.size(); ++j) {
                            LabelNode label = (LabelNode) lsi.labels.get(j);
                            jump = label;
                            merge(jump, current, subroutine);
                            newControlFlowEdge(insn, jump);
                        }
                    } else if (insnNode instanceof TableSwitchInsnNode) {
                        TableSwitchInsnNode tsi = (TableSwitchInsnNode) insnNode;
                        AbstractInsnNode jump = tsi.dflt;
                        merge(jump, current, subroutine);
                        newControlFlowEdge(insn, jump);
                        for (int j = 0; j < tsi.labels.size(); ++j) {
                            LabelNode label = (LabelNode) tsi.labels.get(j);
                            jump = label;
                            merge(jump, current, subroutine);
                            newControlFlowEdge(insn, jump);
                        }
                    } else if (insnOpcode == RET) {
                        if (subroutine == null) {
                            throw new AnalyzerException("RET instruction outside of a sub routine");
                        }
                        for (int i = 0; i < subroutine.callers.size(); ++i) {
                            Object caller = subroutine.callers.get(i);
                            AbstractInsnNode call = (AbstractInsnNode) caller;
                            if (frames.get(call) != null) {
                                merge(call.getNext(),
                                        frames.get(call),
                                        current,
                                        subroutines.get(call),
                                        subroutine.access);
                                newControlFlowEdge(insn, call.getNext());
                            }
                        }
                    } else if (insnOpcode != ATHROW && (insnOpcode < IRETURN || insnOpcode > RETURN)) {
                        if (subroutine != null) {
                            if (insnNode instanceof VarInsnNode) {
                                int var = ((VarInsnNode) insnNode).var;
                                subroutine.access[var] = true;
                                if (insnOpcode == LLOAD || insnOpcode == DLOAD
                                        || insnOpcode == LSTORE
                                        || insnOpcode == DSTORE)
                                {
                                    subroutine.access[var + 1] = true;
                                }
                            } else if (insnNode instanceof IincInsnNode) {
                                int var = ((IincInsnNode) insnNode).var;
                                subroutine.access[var] = true;
                            }
                        }
                        merge(insn.getNext(), current, subroutine);
                        newControlFlowEdge(insn, insn.getNext());
                    }
                }

                List<TryCatchBlockNode> insnHandlers = handlers.get(insn);
                if (insnHandlers != null) {
                    for (int i = 0; i < insnHandlers.size(); ++i) {
                        TryCatchBlockNode tcb = insnHandlers.get(i);
                        Type type;
                        if (tcb.type == null) {
                            type = Type.getObjectType("java/lang/Throwable");
                        } else {
                            type = Type.getObjectType(tcb.type);
                        }
                        AbstractInsnNode jump = tcb.handler;
                        if (newControlFlowExceptionEdge(insn, jump)) {
                            handler.init(f);
                            handler.clearStack();
                            handler.push(interpreter.newValue(type));
                            merge(jump, handler, subroutine);
                        }
                    }
                }
            } catch (AnalyzerException e) {
//            	state = ExecutionState.HANDLED;
//                rollbackNode = handle(insn, frames, e);
//                bookmarkNode = insn;
//                if(rollbackNode == null) {
                    throw new AnalyzerException("Error at instruction " + insn + ": " + e.getMessage(), e);
//                }
            } catch (Exception e) {
                throw new AnalyzerException("Error at instruction " + insn + ": " + e.getMessage(), e);
            }
        }

        return frames;
    }

    protected AbstractInsnNode handle(AbstractInsnNode insn, Map<AbstractInsnNode, Frame> frames, AnalyzerException e) {
        return null;
    }

    protected void postprocess(final AbstractInsnNode insnNode, final Interpreter interpreter) {
    }

    protected Action process(AbstractInsnNode s, Map<AbstractInsnNode, Frame> frames) {
        return Action.NONE;
    }

    private void findSubroutine(AbstractInsnNode insn, final Subroutine sub, final List<AbstractInsnNode> calls)
            throws AnalyzerException
    {
        while (true) {
            if(insn == null) {
                throw new AnalyzerException("Execution can fall off end of the code");
            }
            if(subroutines.get(insn) != null) {
                return;
            }
            subroutines.put(insn, sub.copy());
            AbstractInsnNode node = insn;

            // calls findSubroutine recursively on normal successors
            if (node instanceof JumpInsnNode) {
                if (node.getOpcode() == JSR) {
                    // do not follow a JSR, it leads to another subroutine!
                    calls.add(node);
                } else {
                    JumpInsnNode jnode = (JumpInsnNode) node;
                    findSubroutine(jnode.label, sub, calls);
                }
            } else if (node instanceof TableSwitchInsnNode) {
                TableSwitchInsnNode tsnode = (TableSwitchInsnNode) node;
                findSubroutine(tsnode.dflt, sub, calls);
                for (int i = tsnode.labels.size() - 1; i >= 0; --i) {
                    LabelNode l = (LabelNode) tsnode.labels.get(i);
                    findSubroutine(l, sub, calls);
                }
            } else if (node instanceof LookupSwitchInsnNode) {
                LookupSwitchInsnNode lsnode = (LookupSwitchInsnNode) node;
                findSubroutine(lsnode.dflt, sub, calls);
                for (int i = lsnode.labels.size() - 1; i >= 0; --i) {
                    LabelNode l = (LabelNode) lsnode.labels.get(i);
                    findSubroutine(l, sub, calls);
                }
            }

            // calls findSubroutine recursively on exception handler successors
            List<TryCatchBlockNode> insnHandlers = handlers.get(insn);
            if (insnHandlers != null) {
                for (int i = 0; i < insnHandlers.size(); ++i) {
                    TryCatchBlockNode tcb = insnHandlers.get(i);
                    findSubroutine(tcb.handler, sub, calls);
                }
            }

            // if insn does not falls through to the next instruction, return.
            switch (node.getOpcode()) {
                case GOTO:
                case RET:
                case TABLESWITCH:
                case LOOKUPSWITCH:
                case IRETURN:
                case LRETURN:
                case FRETURN:
                case DRETURN:
                case ARETURN:
                case RETURN:
                case ATHROW:
                    return;
            }
            insn = insn.getNext();
        }
    }

    /**
     * Returns the symbolic stack frame for each instruction of the last
     * recently analyzed method.
     *
     * @return the symbolic state of the execution stack frame at each bytecode
     *         instruction of the method. The size of the returned array is
     *         equal to the number of instructions (and labels) of the method. A
     *         given frame is <tt>null</tt> if the corresponding instruction
     *         cannot be reached, or if an error occured during the analysis of
     *         the method.
     */
    public Map<AbstractInsnNode, Frame> getFrames() {
        return frames;
    }

    /**
     * Returns the exception handlers for the given instruction.
     *
     * @param insn the index of an instruction of the last recently analyzed
     *        method.
     * @return a list of {@link TryCatchBlockNode} objects.
     */
    public List<TryCatchBlockNode> getHandlers(final AbstractInsnNode insn) {
        return handlers.get(insn);
    }

    /**
     * Constructs a new frame with the given size.
     *
     * @param nLocals the maximum number of local variables of the frame.
     * @param nStack the maximum stack size of the frame.
     * @return the created frame.
     */
    protected Frame newFrame(final int nLocals, final int nStack) {
        return new Frame(nLocals, nStack);
    }

    /**
     * Constructs a new frame that is identical to the given frame.
     *
     * @param src a frame.
     * @return the created frame.
     */
    protected Frame newFrame(final Frame src) {
        return new Frame(src);
    }

    /**
     * Creates a control flow graph edge. The default implementation of this
     * method does nothing. It can be overriden in order to construct the
     * control flow graph of a method (this method is called by the
     * {@link #analyze analyze} method during its visit of the method's code).
     *
     * @param insn an instruction index.
     * @param successor index of a successor instruction.
     */
    protected void newControlFlowEdge(final AbstractInsnNode insn, final AbstractInsnNode successor) {
    }

    /**
     * Creates a control flow graph edge corresponding to an exception handler.
     * The default implementation of this method does nothing. It can be
     * overriden in order to construct the control flow graph of a method (this
     * method is called by the {@link #analyze analyze} method during its visit
     * of the method's code).
     *
     * @param insn an instruction index.
     * @param successor index of a successor instruction.
     * @return true if this edge must be considered in the data flow analysis
     *         performed by this analyzer, or false otherwise. The default
     *         implementation of this method always returns true.
     */
    protected boolean newControlFlowExceptionEdge(
        final AbstractInsnNode insn,
        final AbstractInsnNode successor)
    {
        return true;
    }

    // -------------------------------------------------------------------------

    private void merge(
        final AbstractInsnNode insn,
        final Frame frame,
        final Subroutine subroutine) throws AnalyzerException
    {
        Frame oldFrame = frames.get(insn);
        Subroutine oldSubroutine = subroutines.get(insn);
        boolean changes;

        if (oldFrame == null) {
            frames.put(insn, newFrame(frame));
            changes = true;
        } else {
            changes = oldFrame.merge(frame, interpreter);
        }

        if (oldSubroutine == null) {
            if (subroutine != null) {
                subroutines.put(insn, subroutine.copy());
                changes = true;
            }
        } else {
            if (subroutine != null) {
                changes |= oldSubroutine.merge(subroutine);
            }
        }
        if (changes && (!queued.containsKey(insn) || queued.get(insn).equals(Boolean.FALSE))) {
            queued.put(insn, Boolean.TRUE);
            queue.put(top, insn);
            top = top.getNext();
        }
    }

    private void merge(
        final AbstractInsnNode insn,
        final Frame beforeJSR,
        final Frame afterRET,
        final Subroutine subroutineBeforeJSR,
        final boolean[] access) throws AnalyzerException
    {
        Frame oldFrame = frames.get(insn);
        Subroutine oldSubroutine = subroutines.get(insn);
        boolean changes;

        afterRET.merge(beforeJSR, access);

        if (oldFrame == null) {
            frames.put(insn, newFrame(afterRET));
            changes = true;
        } else {
            changes = oldFrame.merge(afterRET, access);
        }

        if (oldSubroutine != null && subroutineBeforeJSR != null) {
            changes |= oldSubroutine.merge(subroutineBeforeJSR);
        }
        if (changes && (!queued.containsKey(insn) || queued.get(insn).equals(Boolean.FALSE))) {
                queued.put(insn, Boolean.TRUE);
                queue.put(top, insn);
                top = top.getNext();
            }
    }
}
