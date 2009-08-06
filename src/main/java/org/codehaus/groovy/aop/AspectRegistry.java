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

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;

public class AspectRegistry {

    private static AspectRegistry _instance=null;
    private ConcurrentMap<Class<?>,Aspect> classAspects = new ConcurrentHashMap<Class<?>, Aspect>();

    private AspectRegistry(){}

    public static AspectRegistry instance() {
        if(_instance == null) {
            _instance = new AspectRegistry();
        }
        return _instance;
    }

    public static void reset() {
        _instance = new AspectRegistry();
    }

    public void add(Class<?> aspectOwner, Aspect aspect) {
        if(classAspects.containsKey(aspectOwner)) {
            classAspects.remove(aspectOwner);
        }
        classAspects.put(aspectOwner, aspect);
        AdviceCacheL2.instance().removeByAspect(aspect);
    }

    public Collection<Aspect> getClassAspects() {
        return new ArrayList<Aspect>(classAspects.values());
    }

    public Aspect get(Class<?> aspectOwner) {
        return classAspects.get(aspectOwner);
    }

    public void remove(Class<?> aspectOwner) {
        Aspect aspect = classAspects.get(aspectOwner);
        classAspects.remove(aspectOwner);
        AdviceCacheL2.instance().removeByAspect(aspect);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("===\n");
        for (Class<?> clazz : classAspects.keySet()) {
            sb.append(clazz.getName() + "\n");
        }
        sb.append("===\n");
        return sb.toString();
    }

}
