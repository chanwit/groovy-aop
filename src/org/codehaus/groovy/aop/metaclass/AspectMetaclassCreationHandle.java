package org.codehaus.groovy.aop.metaclass;

import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaClassRegistry.MetaClassCreationHandle;

public class AspectMetaclassCreationHandle extends MetaClassCreationHandle {

    @SuppressWarnings("unchecked")
    protected MetaClass createNormalMetaClass(Class theClass, MetaClassRegistry registry) {
        final String className = theClass.getName();
        if ((className.startsWith("java.") ||
             className.startsWith("groovy.") ||
             className.startsWith("org.codehaus.groovy."))
             && !className.startsWith("org.codehaus.groovy.aop.tests.")) {
            return super.createNormalMetaClass(theClass, registry);
        }
        else if (theClass != AspectMetaClass.class) {
            return new AspectMetaClass(registry, theClass);
        } else {
            return super.createNormalMetaClass(theClass, registry);
        }
    }

    public static boolean isEnabled() {
        final MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
        return metaClassRegistry.getMetaClassCreationHandler() instanceof AspectMetaclassCreationHandle;
    }

}
