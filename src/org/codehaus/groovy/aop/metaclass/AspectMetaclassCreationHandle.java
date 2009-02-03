package org.codehaus.groovy.aop.metaclass;

import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaClassRegistry.MetaClassCreationHandle;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.builder.AspectBuilderMetaClass;
import org.codehaus.groovy.aop.cache.AdviceCacheL1;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;

public class AspectMetaclassCreationHandle extends MetaClassCreationHandle {

    private final static AspectMetaclassCreationHandle instance = new AspectMetaclassCreationHandle();
    private static MetaClassCreationHandle previousMCH=null;

	@SuppressWarnings("unchecked")
	protected MetaClass createNormalMetaClass(Class theClass, MetaClassRegistry registry) {
        final String className = theClass.getName();
        if (!className.equals("org.codehaus.groovy.aop.abstraction.Aspect") &&
                (className.endsWith("Aspect")
                        || className.indexOf("Aspect$") > 0))
            return new AspectBuilderMetaClass(registry, theClass);
        else if ((className.startsWith("java.")
                || className.startsWith("groovy.")
                || className.startsWith("org.codehaus.groovy.")) &&
                !className.startsWith("org.codehaus.groovy.aop.tests."))
            return previousMCH.create(theClass, registry);
        else if (theClass != AspectMetaClass.class) {
            return new AspectMetaClass(registry, theClass);
        } else {
        	return previousMCH.create(theClass, registry);
        }
    }
    
    public static void enable() {
        final MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
        if (!isEnabled()) {
           previousMCH = metaClassRegistry.getMetaClassCreationHandler();
           metaClassRegistry.setMetaClassCreationHandle(instance);
           AspectRegistry.reset();
           AdviceCacheL1.reset();
           AdviceCacheL2.reset();
        }
    }
    
    public static void disable() {
        final MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
        if (isEnabled()) {        	
            metaClassRegistry.setMetaClassCreationHandle(previousMCH);
        }    	
    }
    
    public static boolean isEnabled() {
        final MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
    	return metaClassRegistry.getMetaClassCreationHandler() == instance;
    }
	
}
