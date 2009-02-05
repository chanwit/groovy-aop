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

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaClassRegistry;

public class AspectBuilderMetaClass extends MetaClassImpl {

    public AspectBuilderMetaClass(MetaClassRegistry registry, Class<?> wrapper) {
        super(registry, wrapper);
    }
/*
    @Override
    public Object invokeMissingMethod(Object object, String methodName, Object[] originalArguments) {
        Object result=null;
        try {
            result = AspectBuilder.buildAspect(object, methodName, originalArguments);
        } catch (AspectCreationFailedException e) {
            return super.invokeMissingMethod(object, methodName, originalArguments);
        }
        if(result==null) {
            return super.invokeMissingMethod(object, methodName, originalArguments);
        } else {
            return result;
        }
    }
*/

//	@Override
//	public Object invokeMethod(Class sender, Object object, String methodName, Object[] originalArguments, boolean isCallToSuper, boolean fromInsideClass) {
//		return super.invokeMethod(sender, object, methodName, originalArguments, isCallToSuper, fromInsideClass);
//	}


}
