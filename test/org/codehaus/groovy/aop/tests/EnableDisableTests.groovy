package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.metaclass.AspectMetaClass
import org.codehaus.groovy.aop.metaclass.AspectMetaclassCreationHandle
class EnableDisableTests extends GroovyTestCase {
	
	void testEnableDisable() {		
		AspectMetaClass.enableGlobally()
		assert AspectMetaclassCreationHandle.isEnabled()
		AspectMetaClass.disableGlobally()
		assert AspectMetaclassCreationHandle.isEnabled() == false
	}
	
	void testMultipleEnableDisable() {		
		AspectMetaClass.disableGlobally()
		AspectMetaClass.enableGlobally()
		AspectMetaClass.enableGlobally()
		assert AspectMetaclassCreationHandle.isEnabled()
		AspectMetaClass.disableGlobally()
		AspectMetaClass.disableGlobally()
		assert AspectMetaclassCreationHandle.isEnabled() == false
	}
		
}