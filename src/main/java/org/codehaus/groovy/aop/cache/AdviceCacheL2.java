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
package org.codehaus.groovy.aop.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.metaclass.EffectiveAdvices;

public class AdviceCacheL2 {

    // Cache Model Level 2
    // ===================
    //
    // structure should be
    // E ::=
    // (<jp1, A>, X1),
    // (<jp1, B>, X2),
    // (<jp2, A>, X1);
    //
    // A re-installed
    //
    // E ::=
    // (<jp1, B>, X2);
    //
    // A re-matched at jp1,
    // not re-matching B
    //
    // E ::=
    // (<jp1, A>, X1),
    // (<jp1, B>, X2);

    private static AdviceCacheL2 _instance = null;

    // TODO hashset or arraylist ?
    private ConcurrentMap<Key,HashSet<Advice>> adviceCache     = new ConcurrentHashMap<Key, HashSet<Advice>>();
    private ConcurrentMap<Aspect,HashSet<Key>> idxAspect       = new ConcurrentHashMap<Aspect, HashSet<Key>>();
    private ConcurrentMap<Joinpoint,HashSet<Key>> idxJoinpoint = new ConcurrentHashMap<Joinpoint, HashSet<Key>>();

    private AdviceCacheL2() {
    }

    public static AdviceCacheL2 v() {
        if (_instance == null) {
            _instance = new AdviceCacheL2();
        }
        return _instance;
    }

    public static void reset() {
        _instance = new AdviceCacheL2();
    }

    public void put(Joinpoint jp, Aspect aspect, Advice advice) {
        Key key = new Key(jp, aspect);
        if(!adviceCache.containsKey(key)) {
            adviceCache.put(key, new HashSet<Advice>());
        }
        adviceCache.get(key).add(advice);
        addAspectIndex(key, aspect);
        addJoinpointIndex(key, jp);
    }

    public void removeByAspect(Aspect aspect) {
        Collection<Key> keys = idxAspect.get(aspect);
        if (keys == null)
            return;
        for (Iterator<Key> iter = keys.iterator(); iter.hasNext();) {
            Key key = iter.next();
            adviceCache.remove(key); // remove data
            // update idxJoinpoint index
            Joinpoint jp = key.getJp();
            HashSet<Key> jpKeys = idxJoinpoint.get(jp);
            jpKeys.remove(key);
            // re-build each cache-L1 entry
            AdviceCacheL1.v().put(jp, getByJoinpoint(jpKeys));
        }
        idxAspect.remove(aspect); // update index
    }

    public Collection<Aspect> getCachedAspects() {
        return idxAspect.keySet();
    }

    public EffectiveAdvices getByJoinpoint(Joinpoint jp) {
        Collection<Key> keys = idxJoinpoint.get(jp);
        return getByJoinpoint(keys);
    }

    private EffectiveAdvices getByJoinpoint(Collection<Key> keys) {
        EffectiveAdvices effAdvices = new EffectiveAdvices();
        if (keys != null) {
            for (Iterator iter = keys.iterator(); iter.hasNext();) {
                Key k = (Key) iter.next();
                HashSet<Advice> advices = adviceCache.get(k);
                for (Advice advice : advices) {
                    effAdvices.add(advice);
                }
            }
        }
        return effAdvices;
    }

    private void addAspectIndex(Key key, Aspect aspect) {
        if (idxAspect.containsKey(aspect)) {
            HashSet<Key> keys = idxAspect.get(aspect);
            keys.add(key);
        } else {
            HashSet<Key> keys = new HashSet<Key>();
            idxAspect.put(aspect, keys);
            keys.add(key);
        }
    }

    private void addJoinpointIndex(Key key, Joinpoint jp) {
        if (idxJoinpoint.containsKey(jp)) {
            HashSet<Key> keys = idxJoinpoint.get(jp);
            keys.add(key);
        } else {
            HashSet<Key> keys = new HashSet<Key>();
            idxJoinpoint.put(jp, keys);
            keys.add(key);
        }
    }

}
