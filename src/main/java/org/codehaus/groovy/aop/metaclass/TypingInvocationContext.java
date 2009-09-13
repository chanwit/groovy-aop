package org.codehaus.groovy.aop.metaclass;

import groovy.lang.MissingPropertyException;

public class TypingInvocationContext extends InvocationContext {

    @Override
    public Object propertyMissing(String name) {
        int found = -1;
        for(int i=0; i < binding.length; i++) {
            if(binding[i].getName().equals(name)) {
                found = i;
                break;
            }
        }
        if(found == -1) throw new MissingPropertyException(name, InvocationContext.class);
        return binding[found];
    }

    public Class<?>[] getArgTypeOfBinding() {
        Class<?>[] result = new Class<?>[binding.length];
        for (int i = 0; i < binding.length; i++) {
            if(result[binding[i].getIndex()] == null) {
                result[binding[i].getIndex()] = binding[i].getType();
            }
        }
        return result;
    }
}
