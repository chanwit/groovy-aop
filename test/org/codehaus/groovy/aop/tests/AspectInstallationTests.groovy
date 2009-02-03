/**
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Chanwit Kaewkasi
 *
 **/
package org.codehaus.groovy.aop.tests;

import org.codehaus.groovy.aop.*
import org.codehaus.groovy.aop.metaclass.*
import org.codehaus.groovy.aop.abstraction.*
import org.codehaus.groovy.aop.abstraction.advice.*
import org.codehaus.groovy.aop.abstraction.pcd.*

class AspectInstallationTests extends GroovyTestCase {
	
static code = '''
package org.codehaus.groovy.aop.tests;

class Test1Aspect {

	static aspect = {
		def pc = pcall('pattern1') & within('pattern2')
		around(pc) { inv ->
			inv.proceed(inv.args)
		}
	}
	
}
'''
	void testInstall() {
		AspectMetaClass.enableGlobally()	
		def gcl = new GroovyClassLoader(this.getClass().getClassLoader())
		assert gcl != null
		def c = gcl.parseClass(code)
		assert c != null
		
		Weaver.install(c)
		def a = AspectRegistry.v().get(c)
		assert a instanceof Aspect
		def advice = a.advices[0] 
		assert advice instanceof AroundAdvice
		assert advice.pointcut !=null
		assert advice.pointcut.root instanceof PCallPCD 
		assert advice.pointcut.root.nextNode instanceof WithInPCD
		Weaver.uninstall(c)
		AspectMetaClass.disableGlobally()				
	}
	
	void testUninstall() {
		AspectMetaClass.enableGlobally()		
		def gcl = new GroovyClassLoader(this.getClass().getClassLoader())
		def c = gcl.parseClass(code)
		
		Weaver.install(c)
		Weaver.uninstall(c)
		def a = AspectRegistry.v().get(c)
		assert a == null
		AspectMetaClass.disableGlobally()		
	}

}
