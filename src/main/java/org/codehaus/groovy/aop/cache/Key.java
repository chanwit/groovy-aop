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

import org.codehaus.groovy.aop.abstraction.Aspect;
import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class Key {

	private final Joinpoint jp;
	private final Aspect aspect;

	public Key(Joinpoint jp, Aspect aspect) {
		this.jp = jp;
		this.aspect = aspect;
	}

	@Override
	public int hashCode() {
		final int PRIME = 91;
		int result = 1;
		result = PRIME * result + ((aspect == null) ? 0 : aspect.hashCode());
		result = PRIME * result + ((jp == null) ? 0 : jp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Key other = (Key) obj;
		if (aspect == null) {
			if (other.aspect != null)
				return false;
		} else if (!aspect.equals(other.aspect))
			return false;
		if (jp == null) {
			if (other.jp != null)
				return false;
		} else if (!jp.equals(other.jp))
			return false;
		return true;
	}

	public Aspect getAspect() {
		return aspect;
	}

	public Joinpoint getJp() {
		return jp;
	}

}
