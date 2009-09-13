package org.codehaus.groovy.aop.metaclass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.codehaus.groovy.aop.AspectRegistry;
import org.codehaus.groovy.aop.Symbol;
import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.abstraction.Joinpoint;
import org.codehaus.groovy.aop.abstraction.Pointcut;
import org.codehaus.groovy.aop.cache.AdviceCacheL1;
import org.codehaus.groovy.aop.cache.AdviceCacheL2;

public class Matcher {

    final private AdviceCacheL1 l1Cache;
    final private AdviceCacheL2 l2Cache;
    final private AspectRegistry registry;

    public Matcher(AspectRegistry registry, AdviceCacheL1 cache, AdviceCacheL2 cache2) {
        super();
        this.registry = registry;
        l1Cache = cache;
        l2Cache = cache2;

        // always clear cache
        l1Cache.reset();
        l2Cache.reset();
    }

    //
    // TODO: matchPerInstance
    //

    public void matchPerClass(EffectiveAdvices effAdvices, Joinpoint jp) {
        //
        // If there is the jp as a key in L1, retrieve it
        //
        if (l1Cache.contains(jp)) {
            effAdvices.addAll(l1Cache.get(jp));
            return;
        }

        //
        // if there is no jp in L1:
        //  - pick aspects from the registry
        //
        Collection<Aspect> aspects = registry.getClassAspects();

        //
        // - matchPCD
        // - store them in L2
        //
        for (Iterator<Aspect> iter = aspects.iterator(); iter.hasNext();) {
            Aspect aspect = iter.next();
            ArrayList<Advice> advices = aspect.getAdvices();
            for (Iterator<Advice> i = advices.iterator(); i.hasNext();) {
                Advice advice = i.next();
                Pointcut pc = advice.getPointcut();
                Symbol[] old = jp.getBinding();
                if (pc != null && pc.matches(jp)) {
                    l2Cache.put(jp, aspect, advice);
                } else {
                    jp.setBinding(old);
                }
            }
        }

        //
        // build an L1 entry from a set of L2 entries
        // store to L1 using jp as its key
        //
        EffectiveAdvices result = l2Cache.getByJoinpoint(jp);
        l1Cache.put(jp, result);

        //
        // then all matched advice is going to be the result
        //
        effAdvices.addAll(result);
    }

}
