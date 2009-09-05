package org.codehaus.groovy.gjit.asm;

import java.util.concurrent.ConcurrentHashMap;

public class ClassBodyCache extends ConcurrentHashMap<String, byte[]> {

    private static final long serialVersionUID = 1L;

    private static ClassBodyCache instance;

    private ClassBodyCache(){}

    public static ClassBodyCache v() {
        if(instance == null) instance = new ClassBodyCache();
        return instance;
    }

}
