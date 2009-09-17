package org.codehaus.groovy.geelab;

import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.MetaClass;
import groovy.lang.Script;

import javax.swing.UIManager;

import org.codehaus.groovy.runtime.InvokerHelper;

public class Console {

    @SuppressWarnings("serial")
    private static void registerMethods() throws Throwable {
        ExpandoMetaClass.enableGlobally();

        ExpandoMetaClass emc = (ExpandoMetaClass) InvokerHelper.getMetaClass(Script.class);
        emc.registerInstanceMethod("getWhos", new Closure(Console.class) {
            @Override
            public Object call(Object[] args) {
                Script self = (Script) getDelegate();
                return DefaultMethods.getWhos(self);
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class[] getParameterTypes() {
                return new Class[]{};
            }
        });
    }

    public static void main(String[] args) throws Throwable {
        registerMethods();

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        groovy.ui.Console console = new groovy.ui.Console();
        console.run();
    }

}
