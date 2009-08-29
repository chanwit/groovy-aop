package org.codehaus.groovy.aop.magic;

public class TestMagic {

    /*private*/ TestMagic() { }
    int add(int i, int j) {
        Object ix = Integer.valueOf(i);
        return (Integer)ix;
    }

}
