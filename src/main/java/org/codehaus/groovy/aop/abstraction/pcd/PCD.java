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
package org.codehaus.groovy.aop.abstraction.pcd;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public interface PCD {
	
	public static int AND = 1;
	public static int OR = 2;
	public static int NOT = 3;
	
	Object and(Object target);
	Object or(Object target);
	Object negate();
	
	boolean matches(Joinpoint jp);

}