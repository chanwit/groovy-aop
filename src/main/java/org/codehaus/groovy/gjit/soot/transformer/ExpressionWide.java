package org.codehaus.groovy.gjit.soot.transformer;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;

public class ExpressionWide extends BodyTransformer {
	
//	 $r2 = r1[0]
//	 $r3 = <Complex: java.lang.Integer $const$0>
//	 $r4 = r1[1]
//	 $r5 = r1[2]
//	 $r6 = r1[3]
//	 $r7 = r1[4]
//	 $r8 = r1[5]
//	 $r11 = interfaceinvoke $r8.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(int,int)>(i0, i1)
//	 $r12 = r1[6]
//	 $r13 = r1[7]
//	 $r16 = interfaceinvoke $r13.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(int,int)>(i0, i1)
//	 $r17 = <Complex: java.lang.Integer $const$0>
//	 $r18 = interfaceinvoke $r12.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object,java.lang.Object)>($r16, $r17)
//	 $r19 = interfaceinvoke $r7.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object,java.lang.Object)>($r11, $r18)
//	 $r20 = <Complex: java.lang.Double $const$1>
//	 $r21 = interfaceinvoke $r6.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object,java.lang.Object)>($r19, $r20)
//	 $r22 = staticinvoke <org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation: java.lang.Object box(int)>(i0)
//	 $r23 = interfaceinvoke $r5.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object,java.lang.Object)>($r21, $r22)
//	 $r24 = <Complex: java.lang.Integer $const$0>
//	 $r25 = interfaceinvoke $r4.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object,java.lang.Object)>($r23, $r24)
//	 $r26 = interfaceinvoke $r2.<org.codehaus.groovy.runtime.callsite.CallSite: java.lang.Object call(java.lang.Object,java.lang.Object)>($r3, $r25)
//	 $r27 = staticinvoke <Complex: java.lang.Class $get$$class$java$lang$Integer()>()
//	 $r28 = staticinvoke <org.codehaus.groovy.runtime.ScriptBytecodeAdapter: java.lang.Object castToType(java.lang.Object,java.lang.Class)>($r26, $r27)
//	 $r29 = (java.lang.Integer) $r28
//	 $i2 = staticinvoke <org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation: int intUnbox(java.lang.Object)>($r29)
//	 return $i2	

	protected void internalTransform(Body b, String phaseName, Map options) {
		// 1. detect all $rXX in the region
		// 2. detect callsite name
		// 3. First Argument is a RECEIVER
		// 4. Infer type | assume Integer -> int for all over the place
		
		
		// 1. find interfaceinvoke CallSite call(2)
		// 2. 
	}

}
