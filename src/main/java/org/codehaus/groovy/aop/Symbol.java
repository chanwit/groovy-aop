/**
 * Copyright 2009 the original author or authors.
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

public class Symbol {

	private String string;
	private int index = -1;
	private Class<?> type = null;

	public Symbol(String string) {
		this.string = string;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public void rightShift(Object object) {
		this.type = (Class<?>)object;
	}

	public Object propertyMissing(String propName) {
		this.string = this.string + "." + propName;
		return this;
	}

	@Override
	public String toString() {
		return this.string;
	}

	public String getName() {
		return this.string;
	}

}
