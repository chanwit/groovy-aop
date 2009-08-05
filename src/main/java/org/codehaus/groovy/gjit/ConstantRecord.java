package org.codehaus.groovy.gjit;


import java.util.HashMap;

public class ConstantRecord extends HashMap<String, ConstantPack>{

    private static final long serialVersionUID = 1L;

    private static ConstantRecord instance;

    private ConstantRecord(){}

    public static ConstantRecord v() {
        if(instance==null) instance = new ConstantRecord();
        return instance;
    }

}
