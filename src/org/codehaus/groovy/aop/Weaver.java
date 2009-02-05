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
import org.codehaus.groovy.aop.builder.AspectBuilder;
import org.codehaus.groovy.aop.metaclass.AspectMetaclassCreationHandle;

public class Weaver {

    /**
     *	@param aspectOwner A class that contains static aspect { ... }
     *
     * **/
    public static Aspect install(Class<?> aspectOwner) throws Throwable {
        if(!AspectMetaclassCreationHandle.isEnabled()) {
            throw new AspectMetaClassNotEnabledException();
        }
        if(containAspect(aspectOwner)) {
            Aspect aspect = new Aspect(aspectOwner);
            AspectRegistry.v().add(aspectOwner, aspect);
            buildAspect(aspect, aspectOwner);
            return aspect;
        } else {
            return null;
        }
    }

    public static void uninstall(Class<?> aspectOwner) {
        AspectRegistry.v().remove(aspectOwner);
    }

    private static void buildAspect(Aspect aspect, Class<?> aspectOwner) throws Throwable {
        Method method = aspectOwner.getDeclaredMethod("getAspect", new Class[]{});
        Closure c = (Closure)method.invoke(aspectOwner, new Object[]{});
        c.setDelegate(new AspectBuilder(aspect));
        c.setResolveStrategy(Closure.DELEGATE_ONLY); // or delegate only?
        c.call();
    }

    private static boolean containAspect(Class<?> theOwnerClass) {
        try {
            theOwnerClass.getDeclaredMethod("getAspect", new Class[]{});
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
