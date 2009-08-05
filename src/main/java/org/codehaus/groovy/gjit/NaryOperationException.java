package org.codehaus.groovy.gjit;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Value;

public class NaryOperationException extends AnalyzerException {

    private int index;

    public int getIndex() {
        return index;
    }

    public NaryOperationException(int index, Object expected, Value encountered) {
        super("", expected, encountered);
        this.index = index;
    }

    private static final long serialVersionUID = -561052802465541043L;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }

}
