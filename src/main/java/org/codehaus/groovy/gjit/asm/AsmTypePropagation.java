package org.codehaus.groovy.gjit.asm;

import org.codehaus.groovy.runtime.callsite.CallSite;

public class AsmTypePropagation {

    private Class<?>   advisedReturnType;
    private Class<?>[] advisedTypes;

    public static class Result {
        public final String methodSignature;
        public final byte[] body;
        public Result(String methodSignature, byte[] body) {
            super();
            this.methodSignature = methodSignature;
            this.body = body;
        }
    }

    public Result typePropagate(CallSite callSite) {
    	
        return new Result("", new byte[]{});
    }
}
