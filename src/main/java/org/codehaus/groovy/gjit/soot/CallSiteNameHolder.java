package org.codehaus.groovy.gjit.soot;

import java.util.concurrent.ConcurrentHashMap;

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

}
