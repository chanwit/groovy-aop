package org.codehaus.groovy.gjit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Value;

public class MyBasicInterpreter extends SimpleVerifier {

    public final Map<AbstractInsnNode, Value[]> use = new HashMap<AbstractInsnNode, Value[]>();

    @Override
    public Value binaryOperation(AbstractInsnNode insn, Value value1,
            Value value2) throws AnalyzerException {
        use.put(insn, new Value[]{value1, value2});
        Value v = super.binaryOperation(insn, value1, value2);
        return def(insn, v);
    }

    @Override
    public Value copyOperation(AbstractInsnNode insn, Value value)
            throws AnalyzerException {
        use.put(insn, new Value[]{value});
        Value v = super.copyOperation(insn, value);
        return def(insn, v);
    }

    @Override
    public Value naryOperation(AbstractInsnNode insn, List<Value> values)
            throws AnalyzerException {
        use.put(insn, values.toArray(new Value[values.size()]));
        Value v = super.naryOperation(insn, values);
        return def(insn, v);
    }

    @Override
    public Value newOperation(AbstractInsnNode insn) {
        Value v = super.newOperation(insn);
        return def(insn, v);
    }

    @Override
    public Value ternaryOperation(AbstractInsnNode insn, Value value1,
            Value value2, Value value3) throws AnalyzerException {
        use.put(insn, new Value[]{value1,value2,value3});
        Value v = super.ternaryOperation(insn, value1, value2, value3);
        return def(insn, v);
    }

    @Override
    public Value unaryOperation(AbstractInsnNode insn, Value value)
            throws AnalyzerException {
        use.put(insn, new Value[]{value});
        Value v = super.unaryOperation(insn, value);
        return def(insn, v);
    }

    private DefValue def(AbstractInsnNode insn, Value value) {
        if(value == null) return new DefValue(insn, null);
        return new DefValue(insn, ((BasicValue)value).getType());
    }

}