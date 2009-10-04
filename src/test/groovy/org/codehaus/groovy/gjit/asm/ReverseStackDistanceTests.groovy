package org.codehaus.groovy.gjit.asm;

import groovy.util.GroovyTestCase;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class ReverseStackDistanceTests extends GroovyTestCase {

//    ALOAD 2
//    LDC 80
//    AALOAD
//    ALOAD 2
//    LDC 81
//    AALOAD
//    ALOAD 2
//    LDC 82
//    AALOAD
//    ALOAD 0
//    ALOAD 2
//    LDC 83
//    AALOAD
//    ALOAD 14
//    GETSTATIC org/codehaus/groovy/gjit/soot/fft/FFT.$const$2 : Ljava/lang/Integer;
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//    DUP2_X2
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//    ALOAD 17
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//    ASTORE 18
//    ALOAD 18
//    INVOKEINTERFACE org/codehaus/groovy/runtime/callsite/CallSite.call(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//    POP
//    ALOAD 18
//    POP

	void testFindStartingNode() {

		InsnListHelper.install()

		def mn = new MethodNode()
		def u = mn.instructions
		u.append {
		    aload 2
		    ldc 80
		    aaload
		    aload 2 // 3 for 19
		    ldc 81
		    aaload
		    aload 2 // 6 for 17
		    ldc 82
		    aaload
		    aload 0 // index = 9
		    aload 2 // 10 for 15
		    ldc 83
		    aaload
		    aload 14
		    getstatic "org/codehaus/groovy/gjit/soot/fft/FFT",'$const$2',Integer
			invokeinterface "org/codehaus/groovy/runtime/callsite/CallSite","call",[Object.class, Object.class],Object.class
		    dup2_x2
			invokeinterface "org/codehaus/groovy/runtime/callsite/CallSite","call",[Object.class, Object.class],Object.class
		    aload 17
			invokeinterface "org/codehaus/groovy/runtime/callsite/CallSite","call",[Object.class, Object.class],Object.class
		    astore 18
		    aload 18
			invokeinterface "org/codehaus/groovy/runtime/callsite/CallSite","call",[Object.class, Object.class, Object.class],Object.class
		    pop
		    aload 18
		    pop
		}
		def rsd = new ReverseStackDistance(u[15])
		def r1 = rsd.findStartingNode()
		assert u.indexOf(r1) == 10

        def rsd2 = new ReverseStackDistance(u[17])
		def r2 = rsd2.findStartingNode()
		assert u.indexOf(r2) == 6

        def rsd3 = new ReverseStackDistance(u[19])
		def r3 = rsd3.findStartingNode()
		assert u.indexOf(r3) == 3

		def rsd4 = new ReverseStackDistance(u[22])
		def r4 = rsd4.findStartingNode()
		assert u.indexOf(r4) == 0
	}

}
