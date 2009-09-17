package org.codehaus.groovy.runtime;

import java.util.Stack;

public class TicTocStack extends Stack<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final ThreadLocal<TicTocStack> instance =
        new ThreadLocal <TicTocStack> () {
        @Override protected TicTocStack initialValue() {
            return new TicTocStack();
        }
    };

    public static TicTocStack v() {
        return instance.get();
    }

    public static void tic() {
        instance.get().push(System.currentTimeMillis());
    }

    public static long toc() {
        long ticValue = instance.get().pop();
        return System.currentTimeMillis()-ticValue;
    }

}
