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

import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.Pointcut;

public class BeforeAdvice extends Advice {

	public BeforeAdvice(Object[] args) {
		super(null, args);
	}

	public BeforeAdvice(Pointcut pointcut, Closure code) {
		super(pointcut, code);
	}

	public BeforeAdvice(Class<?> class1, Object[] args) {
		super(class1, args);
	}
	

}
