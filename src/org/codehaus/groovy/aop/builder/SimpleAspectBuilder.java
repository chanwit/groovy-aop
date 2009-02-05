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
package org.codehaus.groovy.aop.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.abstraction.Pointcut;
import org.codehaus.groovy.aop.abstraction.advice.AfterAdvice;
import org.codehaus.groovy.aop.abstraction.advice.AroundAdvice;
import org.codehaus.groovy.aop.abstraction.advice.BeforeAdvice;
import org.codehaus.groovy.aop.abstraction.pcd.PCallPCD;

public class SimpleAspectBuilder extends GroovyObjectSupport {

    @SuppressWarnings("unused") private boolean isStatic;
    private int adviceKind;
    @SuppressWarnings("unused") private Object object;
    private Class<?> sender;

    public SimpleAspectBuilder(Class<?> sender, Object object, boolean isStatic, String name) {
        super();
        this.isStatic = isStatic;
        this.adviceKind = Advice.kind(name);
        this.sender = sender;
        this.object = object;
    }

    public void setProperty(String propName, Object closure) {
        PCallPCD rootPCD = new PCallPCD(new Object[]{this.sender.getCanonicalName()+"."+propName});
        Pointcut pointcut = new Pointcut(rootPCD);
        Advice advice=null;
        switch(this.adviceKind) {
            case Advice.AROUND: advice = new AroundAdvice(pointcut, (Closure)closure); break;
            case Advice.BEFORE: advice = new BeforeAdvice(pointcut, (Closure)closure); break;
            case Advice.AFTER: advice = new AfterAdvice(pointcut, (Closure)closure); break;
            case Advice.AFTER_RETURNING: 	advice = new AfterAdvice(pointcut, (Closure)closure);
                                            ((AfterAdvice)advice).setReturning(true);
                                            break;
        }
        Aspect aspect = null;
// 		TODO not included in 0.2 release
//		if(this.isStatic) {
//			aspect = new Aspect(this.sender);
//			AspectRegistry.v().add(this.sender, aspect);
//		} else {
//			aspect = new Aspect(this.object);
//			AspectRegistry.v().add(this.object, aspect);
//		}
        aspect = AspectRegistry.v().get(this.sender);
        aspect.add(advice);
    }

}
