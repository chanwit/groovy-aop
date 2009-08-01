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
package org.codehaus.groovy.aop.abstraction;

import java.util.ArrayList;

public class Aspect {
	
	private ArrayList<Pointcut> pointcuts = new ArrayList<Pointcut>();
	private ArrayList<Advice> advices = new ArrayList<Advice>();
	private Object owner;
	
	public Aspect(Object owner) {
		this.owner = owner;
	}

	public void add(Advice advice) {
		advices.add(advice);		
	}
	
	public void add(Pointcut pointcut) {
		pointcuts.add(pointcut);
	}

	public ArrayList<Advice> getAdvices() {
		return advices;
	}

	public ArrayList<Pointcut> getPointcuts() {
		return pointcuts;
	}

	public Object getOwner() {
		return owner;
	}

}
