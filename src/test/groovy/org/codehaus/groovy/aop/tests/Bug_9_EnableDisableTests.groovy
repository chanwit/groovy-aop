package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.metaclass.AspectMetaClass
import org.codehaus.groovy.aop.metaclass.AspectMetaclassCreationHandle


    void testAutoEnable() {
        assert AspectMetaclassCreationHandle.isEnabled() == true
    }

}