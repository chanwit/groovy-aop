package org.codehaus.groovy.gjit.asm;

import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.Type;

public class CallSiteNameHolder extends ConcurrentHashMap<String, String[]> {

    /**
     *
     */
    private static final long serialVersionUID = -6780780650503305589L;
    private static CallSiteNameHolder instance;

    public static ConcurrentHashMap<String, String[]> v() {
        if(instance == null)
            instance = new CallSiteNameHolder();

        return instance;
    }

    @Override
    public String[] get(Object key) {
        if(key instanceof Class<?>) {
            key = Type.getInternalName((Class<?>)key);
        }
        return super.get(key);
    }

}
