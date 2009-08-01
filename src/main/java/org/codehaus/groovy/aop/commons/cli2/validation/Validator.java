/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.aop.commons.cli2.validation;

import java.util.List;

/**
 * The validation interface for validating argument values(s).
 *
 * A validator can replace the argument string value with a
 * specific class instance e.g. the {@link UrlValidator} replaces
 * the string value with a {@link java.net.URL} instance.
 *
 * @author Rob Oxspring
 * @author John Keyes
 */
public interface Validator {

    /**
     * Validate the specified values (List of Strings).
     *
     * @param values The values to validate.
     *
     * @throws InvalidArgumentException If any of the
     * specified values are not valid.
     */
    void validate(final List values) throws InvalidArgumentException;

}
