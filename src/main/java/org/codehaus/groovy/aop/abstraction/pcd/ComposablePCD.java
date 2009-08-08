package org.codehaus.groovy.aop.abstraction.pcd;

public abstract class ComposablePCD implements PCD {

    public Object and(Object right) {
    	return new AndPCD(this, (PCD)right);
    }

    public Object negate() {
        return new NotPCD((PCD)this);
    }

    public Object or(Object target) {
        return new OrPCD(this, (PCD)target);
    }

}
