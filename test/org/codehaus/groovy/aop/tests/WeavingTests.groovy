package org.codehaus.groovy.aop.tests

import org.codehaus.groovy.aop.Weaverimport org.codehaus.groovy.aop.tests.subject.SubjectAspect
import org.codehaus.groovy.aop.tests.subject.NotContainAspect

import org.codehaus.groovy.aop.AspectMetaClassNotEnabledExceptionimport org.codehaus.groovy.aop.metaclass.AspectMetaClassimport org.codehaus.groovy.aop.metaclass.AspectMetaclassCreationHandleimport org.codehaus.groovy.aop.AspectRegistryimport org.codehaus.groovy.aop.abstraction.Aspect

class WeavingTests extends GroovyTestCase {

    void testInstallWithOutEnabling() {
        try {
            Weaver.install(SubjectAspect)
        } catch(e) {
            assert e instanceof AspectMetaClassNotEnabledException
        }
    }

    void testInstall() {
        //AspectMetaClass.enableGlobally()
        assert AspectMetaclassCreationHandle.isEnabled()
        Weaver.install(SubjectAspect.class)
        def a = AspectRegistry.v().get(SubjectAspect.class)
        assert a instanceof Aspect

        Weaver.uninstall(SubjectAspect.class)
        def a2 = AspectRegistry.v().get(SubjectAspect.class)
        assert a2 == null

        //AspectMetaClass.disableGlobally()
        //assert AspectMetaclassCreationHandle.isEnabled() == false
    }

    void testInstallThenFailed() {
        //AspectMetaClass.enableGlobally()
        assert AspectMetaclassCreationHandle.isEnabled()

        Weaver.install(NotContainAspect.class)
        def a = AspectRegistry.v().get(NotContainAspect.class)
        assert a == null

        //AspectMetaClass.disableGlobally()
        //assert AspectMetaclassCreationHandle.isEnabled() == false
    }

    void testReInstall() {
        //AspectMetaClass.enableGlobally()
        assert AspectMetaclassCreationHandle.isEnabled()
        Weaver.install(SubjectAspect.class)
        def a = AspectRegistry.v().get(SubjectAspect.class)
        assert a instanceof Aspect

        Weaver.install(SubjectAspect.class)
        def a2 = AspectRegistry.v().get(SubjectAspect.class)
        assert a2 instanceof Aspect
        assert a != a2

        Weaver.uninstall(SubjectAspect.class)
        def a3 = AspectRegistry.v().get(SubjectAspect.class)
        assert a3 == null

        //AspectMetaClass.disableGlobally()
        //assert AspectMetaclassCreationHandle.isEnabled() == false
    }

}