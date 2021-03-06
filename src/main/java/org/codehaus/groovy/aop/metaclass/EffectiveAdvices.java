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
package org.codehaus.groovy.aop.metaclass;

import groovy.lang.*;

import org.codehaus.groovy.runtime.metaclass.ClosureMetaClass;

import java.util.ArrayList;

import org.codehaus.groovy.aop.abstraction.Advice;
import org.codehaus.groovy.aop.abstraction.advice.AfterAdvice;
import org.codehaus.groovy.aop.abstraction.advice.AroundAdvice;
import org.codehaus.groovy.aop.abstraction.advice.BeforeAdvice;
import org.codehaus.groovy.aop.abstraction.advice.TypeAdvice;

public class EffectiveAdvices {

    private ArrayList<Closure> effBeforeAdvices      = new ArrayList<Closure>();
    private ArrayList<Closure> effAfterAdvices       = new ArrayList<Closure>();
    private ArrayList<Closure> effAroundAdvices      = new ArrayList<Closure>();
    private ArrayList<Closure> effAfterReturnAdvices = new ArrayList<Closure>();
    private ArrayList<Closure> effTypeAdvices        = new ArrayList<Closure>();

    private boolean empty = true;

    public boolean isEmpty() {
        return this.empty;
    }

    private static Closure[] convertToArrayAndResetMetaClass(ArrayList<Closure> source) {
        if(source.size() == 0) return null;
        Closure[] result = (Closure[])source.toArray(new Closure[source.size()]);
        for(int i=0; i< result.length; i++) {
            MetaClass mc = new ClosureMetaClass(GroovySystem.getMetaClassRegistry(), result[i].getClass());
            result[i].setMetaClass(mc);
            mc.initialize();
        }
        return result;
    }

    public Closure[] getBeforeClosureArray() {
        return convertToArrayAndResetMetaClass(effBeforeAdvices);
    }

    public Closure[] getAfterClosureArray() {
        return convertToArrayAndResetMetaClass(effAfterAdvices);
    }

    public Closure[] getAroundClosureArray() {
        return convertToArrayAndResetMetaClass(effAroundAdvices);
    }

    public Closure[] getAfterReturnClosureArray() {
        return convertToArrayAndResetMetaClass(effAfterReturnAdvices);
    }

    public Closure[] getTypeAdviceClosureArray() {
        return convertToArrayAndResetMetaClass(effTypeAdvices);
    }

    public ArrayList<Closure> get(int place) {
        switch(place) {
            case Advice.BEFORE: return effBeforeAdvices;
            case Advice.AFTER:  return effAfterAdvices;
            case Advice.AROUND: return effAroundAdvices;
            case Advice.AFTER_RETURNING:
                                return effAfterReturnAdvices;
        }
        return null;
    }

    public void add(Advice advice) {
    	if(advice instanceof TypeAdvice)
    		effTypeAdvices.add(advice.getAdviceCode());
    	else if(advice instanceof BeforeAdvice)
        	effBeforeAdvices.add(advice.getAdviceCode());
        else if(advice instanceof AroundAdvice)
        	effAroundAdvices.add(advice.getAdviceCode());
        else if(advice instanceof AfterAdvice) {
            AfterAdvice af = (AfterAdvice)advice;
            if(af.getReturning())
                effAfterReturnAdvices.add(advice.getAdviceCode());
            else
                effAfterAdvices.add(advice.getAdviceCode());
        }
        empty = false;
    }

    public void addAll(EffectiveAdvices from) {
        if (from.isEmpty()) {
            return;
        }
        this.effTypeAdvices.addAll(from.effTypeAdvices);
        this.effBeforeAdvices.addAll(from.effBeforeAdvices);
        this.effAroundAdvices.addAll(from.effAroundAdvices);
        this.effAfterAdvices.addAll(from.effAfterAdvices);
        this.effAfterReturnAdvices.addAll(from.effAfterReturnAdvices);
        empty = false;
    }

	public boolean containsTypeAdvice() {
		return effTypeAdvices.size() != 0;
	}

}
