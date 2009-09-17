package org.codehaus.groovy.geelab;

import java.util.Map;

import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.Script;

import javax.swing.UIManager;

import org.codehaus.groovy.geelab.linearalgebra.Complex;
import org.codehaus.groovy.geelab.linearalgebra.Complex.Imaginary;
import org.codehaus.groovy.runtime.InvokerHelper;

public class Console {

    @SuppressWarnings("serial")
    private static void registerMethods() throws Throwable {
        ExpandoMetaClass.enableGlobally();

        ExpandoMetaClass Script_metaClass = (ExpandoMetaClass) InvokerHelper.getMetaClass(Script.class);
        Script_metaClass.registerInstanceMethod("getWhos", new Closure(Console.class) {
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

        Script_metaClass.registerBeanProperty("all", new Symbol("all"));

        Script_metaClass.registerInstanceMethod("clear", new Closure(Console.class){
            @Override
            public Object call(Object[] args) {
                Script self = (Script) getDelegate();
                Map<String,Object> vars = self.getBinding().getVariables();
                if(args[0] instanceof Symbol && ((Symbol)args[0]).name.equals("all")) {
                    Object temp1 = vars.get("_outputTransforms");
                    Object temp2 = vars.get("__");
                    Object temp3 = vars.get("_");
                    Object temp4 = vars.get("args");
                    vars.clear();
                    vars.put("_outputTransforms", temp1);
                    vars.put("__",   temp2);
                    vars.put("_",    temp3);
                    vars.put("args", temp4);
                } else {
                    if(vars.containsValue(args[0])) {
                        vars.values().remove(args[0]);
                        vars.values().remove(args[0]);
                    }
                }
                return null;
            }

            @Override
            public Class[] getParameterTypes() {
                return new Class[]{Object.class};
            }

        });

        ExpandoMetaClass Number_metaClass = (ExpandoMetaClass) InvokerHelper.getMetaClass(Number.class);
        Number_metaClass.registerInstanceMethod("plus", new Closure(Console.class) {
            @Override
            public Object call(Object[] arguments) {
                double real = ((Number)getDelegate()).doubleValue();
                Imaginary n = (Imaginary)arguments[0];
                return new Complex(real, n.v);
            }

            @Override
            public Class[] getParameterTypes() {
                return new Class[]{Imaginary.class};
            }
        });

        Number_metaClass.registerInstanceMethod("getI", new Closure(Console.class) {
            @Override
            public Object call(Object[] args) {
                return new Imaginary(((Number)getDelegate()).doubleValue());
            }

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
