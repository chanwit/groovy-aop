package org.codehaus.groovy.gjit.asm;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConstantHolder extends ConcurrentHashMap<String, ConstantHolder.ConstantPack>{

    private static final long serialVersionUID = 1L;

    private static ConstantHolder instance;

    private ConstantHolder(){}

    public static ConstantHolder v() {
        if(instance==null) instance = new ConstantHolder();
        return instance;
    }

    public static class ConstantPack extends HashMap<String, Object>  {

        private static final long serialVersionUID = 1L;

    }

}
