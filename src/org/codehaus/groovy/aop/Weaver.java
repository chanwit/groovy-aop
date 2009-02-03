/**
 * Copyright 2007-2008 the original author or authors.
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

package org.codehaus.groovy.aop;

import groovy.lang.Closure;

import java.lang.reflect.Method;

import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.metaclass.AspectMetaclassCreationHandle;

public class Weaver {
	
	public static void install(Class<?> aspectWrapper) throws Throwable {
		if(!AspectMetaclassCreationHandle.isEnabled()) {
			throw new AspectMetaClassNotEnabledException();
		}
		if(containAspect(aspectWrapper)) {
			Aspect aspect = new Aspect(aspectWrapper);
			AspectRegistry.v().add(aspectWrapper, aspect);
			buildAspect(aspectWrapper);
		}
	}

	public static void uninstall(Class<?> aspectWrapper) {
		AspectRegistry.v().remove(aspectWrapper);
	}
		
	private static void buildAspect(Class<?> wrapper) throws Throwable {
		Method method = wrapper.getDeclaredMethod("getAspect", new Class[]{});
		Closure c = (Closure)method.invoke(wrapper, new Object[]{});
		c.call();
	}	
	
	private static boolean containAspect(Class<?> theClass) {
		try {
			theClass.getDeclaredMethod("getAspect", new Class[]{});
			return true;
		} catch (Exception e) {
			return false;
		}
	}		
}
