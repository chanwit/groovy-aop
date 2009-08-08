/**
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Chanwit Kaewkasi
 *
 **/
package org.codehaus.groovy.aop.abstraction;

import java.util.Map;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import org.codehaus.groovy.aop.abstraction.pcd.AndPCD;
import org.codehaus.groovy.aop.abstraction.pcd.GetPCD;
import org.codehaus.groovy.aop.abstraction.pcd.PCD;
import org.codehaus.groovy.aop.abstraction.pcd.PCallPCD;
import org.codehaus.groovy.aop.abstraction.pcd.SetPCD;
import org.codehaus.groovy.aop.abstraction.pcd.WithInPCD;

public abstract class Advice extends GroovyObjectSupport {

    public final static int BEFORE          = 1;
    public final static int AFTER           = 2;
    public final static int AROUND          = 3;
    public final static int AFTER_RETURNING = 4;

    private Pointcut pointcut;
    private Closure adviceCode;

    public Advice(Pointcut pointcut, Closure code) {
        super();
        this.pointcut = pointcut;
        this.adviceCode = code;
    }

    public Advice(Class<?> klass, Object[] args) {
        if(args[0] instanceof String) {
            PCallPCD rootPCD = new PCallPCD(new Object[]{klass.getCanonicalName()+"."+(String)args[0]});
            this.pointcut = new Pointcut(rootPCD);
        } else if(args[0] instanceof Map) {
            this.pointcut = null;
            PCD rootPCD=null;
            Map map = (Map)args[0];
            // TODO clean code here
            if(map.containsKey("call")) {
                rootPCD = new PCallPCD(new Object[]{klass.getCanonicalName()+"."+(String)map.get("call")});
            } else if(map.containsKey("get")) {
                rootPCD = new GetPCD(new Object[]{klass.getCanonicalName()+"."+(String)map.get("get")});
            } else if(map.containsKey("set")) {
                rootPCD = new SetPCD(new Object[]{klass.getCanonicalName()+"."+(String)map.get("set")});
            }
            if(map.containsKey("within")) {
                PCD thisPCD = new WithInPCD(new Object[]{klass.getCanonicalName()+"."+(String)map.get("withIn")});
                rootPCD = rootPCD==null? thisPCD: new AndPCD(rootPCD, thisPCD);
            }
            this.pointcut = new Pointcut(rootPCD);
        }  else if(args[0] instanceof Pointcut) {
            this.pointcut = (Pointcut)args[0];
        }  else if(args[0] instanceof PCD){
            this.pointcut = new Pointcut((PCD)args[0]);
        }

        this.adviceCode = (Closure)args[1];
    }

    public Closure getAdviceCode() {
        return adviceCode;
    }

    public void setAdviceCode(Closure code) {
        this.adviceCode = code;
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    public static int kind(String name) {
        if("around".equals(name))
            return Advice.AROUND;
        else if("before".equals(name))
            return Advice.BEFORE;
        else if("after".equals(name))
            return Advice.AFTER;
        else if("afterReturning".equals(name))
            return Advice.AFTER_RETURNING;
        else
            return 0;
    }

}
