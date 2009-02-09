/**
 * Copyright 2007 the original author or authors.
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
package org.codehaus.groovy.aop.abstraction.advice;

import groovy.lang.Closure;

import java.util.Map;

import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.Pointcut;
import org.codehaus.groovy.aop.abstraction.pcd.PCD;

public class AfterAdvice extends Advice {

    private Boolean returning=false;

    public AfterAdvice(Object[] args) {
        super(null, args);
        if(args[0] instanceof Map) {
            Object pc = ((Map)args[0]).get("returning");
            if(pc!=null) {
                if(pc instanceof Pointcut)
                    this.setPointcut((Pointcut)pc);
                else if(pc instanceof PCD) {
                    Pointcut p = new Pointcut((PCD)pc);
                    this.setPointcut(p);
                }
                this.returning = true;
            }
        }
    }

    public AfterAdvice(Pointcut pointcut, Closure code) {
        super(pointcut, code);
    }

    public AfterAdvice(Class<?> class1, Object[] originalArguments) {
        super(class1, originalArguments);
    }

    public Boolean getReturning() {
        return returning;
    }

    public void setReturning(Boolean returning) {
        this.returning = returning;
    }

}
