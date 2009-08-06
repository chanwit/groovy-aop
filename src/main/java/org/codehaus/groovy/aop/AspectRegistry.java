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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;

public class AspectRegistry {

    private static AspectRegistry _instance=null;

    private ConcurrentMap<Class<?>,Aspect> classAspects = new ConcurrentHashMap<Class<?>, Aspect>();

//	TODO not included in 0.2 release
//	private HashMap<Object, Aspect> instanceAspects = new HashMap<Object, Aspect>();

    private AspectRegistry(){}

    public static AspectRegistry v() {
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
        AdviceCacheL2.v().removeByAspect(aspect);
    }

//	TODO not included in 0.2 release
//	public void add(Object instance, Aspect aspect) {
//		if(instanceAspects.containsKey(instance)) {
//			instanceAspects.remove(instance);
//		}
//		instanceAspects.put(instance, aspect);
//		AdviceCacheL2.v().removeByAspect(aspect);
//	}

    public Collection<Aspect> getClassAspects() {
        return new ArrayList<Aspect>(classAspects.values());
    }

//	TODO not included in 0.2 release
//	public Collection<Aspect> getInstanceAspects() {
//		return instanceAspects.values();
//	}

//	TODO not included in 0.2 release
//	public HashMap<Object, Aspect> getInstanceAspectMap() {
//		return instanceAspects;
//	}

    public Aspect get(Class<?> aspectOwner) {
        return classAspects.get(aspectOwner);
    }

    public void remove(Class<?> aspectOwner) {
        Aspect aspect = classAspects.get(aspectOwner);
        classAspects.remove(aspectOwner);
        AdviceCacheL2.v().removeByAspect(aspect);
    }

    public String toString() {
        Set<Class<?>> cc = classAspects.keySet();
        StringBuffer sb = new StringBuffer();
        sb.append("===\n");
        for (Class<?> class1 : cc) {
            sb.append(class1.getName() + "\n");
        }
        sb.append("===\n");
        return sb.toString();
    }

}
