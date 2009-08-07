/**
 * Copyright 2007-2009 the original author or authors.
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
package org.codehaus.groovy.aop.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.metaclass.EffectiveAdvices;

public class AdviceCacheL1 {

    private static AdviceCacheL1 _instance=null;
    private ConcurrentMap<Joinpoint,EffectiveAdvices> _inner = new ConcurrentHashMap<Joinpoint, EffectiveAdvices>();

    private AdviceCacheL1(){}

    public static AdviceCacheL1 instance() {
        if(_instance == null) {
            _instance = new AdviceCacheL1();
        }
        return _instance;
    }

    public static void reset() {
        _instance = new AdviceCacheL1();
    }

    public boolean contains(Joinpoint jp) {
        return _inner.containsKey(jp);
    }

    public EffectiveAdvices get(Joinpoint jp) {
        return _inner.get(jp);
    }

    public void put(Joinpoint jp, EffectiveAdvices advices) {
        if(advices.isEmpty()) {
            _inner.remove(jp);
        } else {
            EffectiveAdvices results = new EffectiveAdvices();
            results.addAll(advices);
            _inner.put(jp, results);
        }
    }

    public void remove(Joinpoint jp) {
        _inner.remove(jp);
    }

}
