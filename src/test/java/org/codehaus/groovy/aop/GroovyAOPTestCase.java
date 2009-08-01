package org.codehaus.groovy.aop;

import groovy.lang.GroovyClassLoader;
import junit.framework.TestCase;

public abstract class GroovyAOPTestCase extends TestCase {

    private GroovyClassLoader gcl;
    private Class<?> aspect=null;

    protected void setUp() throws Exception {
        //AspectMetaClass.enableGlobally();
        this.gcl = new GroovyClassLoader(this.getClass().getClassLoader());
    }

    protected void setupAspect(String code) throws Throwable {
        aspect = gcl.parseClass(code);
        assert aspect != null;
        Weaver.install(aspect);
    }

    protected void tearDown() throws Exception {
        this.gcl = null;
        if(aspect != null) Weaver.uninstall(aspect);
    }

    public GroovyClassLoader getGcl() {
        return gcl;
    }

}
