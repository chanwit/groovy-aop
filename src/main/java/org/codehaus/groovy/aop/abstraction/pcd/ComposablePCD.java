package org.codehaus.groovy.aop.abstraction.pcd;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public abstract class ComposablePCD implements PCD {

    @Override
    public void exposeContext(Joinpoint jp) { }

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
