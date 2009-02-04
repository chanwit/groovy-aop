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
package org.codehaus.groovy.aop.commons.cli2.option;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.aop.commons.cli2.Argument;
import org.codehaus.groovy.aop.commons.cli2.Option;
import org.codehaus.groovy.aop.commons.cli2.OptionException;
import org.codehaus.groovy.aop.commons.cli2.WriteableCommandLine;
import org.codehaus.groovy.aop.commons.cli2.resource.ResourceConstants;
import org.codehaus.groovy.aop.commons.cli2.resource.ResourceHelper;

/**
 * An Argument implementation that allows a variable size Argument to precede a
 * fixed size argument.  The canonical example of it's use is in the unix
 * <code>cp</code> command where a number of source can be specified with
 * exactly one destination specfied at the end.
 */
public class SourceDestArgument
    extends ArgumentImpl {
    private final Argument source;
    private final Argument dest;

    /**
     * Creates a SourceDestArgument using defaults where possible.
     *
     * @param source the variable size Argument
     * @param dest the fixed size Argument
     */
    public SourceDestArgument(final Argument source,
                              final Argument dest) {
        this(source, dest, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
             DEFAULT_CONSUME_REMAINING, null);
    }

    /**
     * Creates a SourceDestArgument using the specified parameters.
     *
     * @param source the variable size Argument
     * @param dest the fixed size Argument
     * @param initialSeparator the inistial separator to use
     * @param subsequentSeparator the subsequent separator to use
     * @param consumeRemaining the token triggering consume remaining behaviour
     * @param defaultValues the default values for the SourceDestArgument
     */
    public SourceDestArgument(final Argument source,
                              final Argument dest,
                              final char initialSeparator,
                              final char subsequentSeparator,
                              final String consumeRemaining,
                              final List defaultValues) {
        super("SourceDestArgument", null, sum(source.getMinimum(), dest.getMinimum()),
              sum(source.getMaximum(), dest.getMaximum()), initialSeparator, subsequentSeparator,
              null, consumeRemaining, defaultValues, 0);

        this.source = source;
        this.dest = dest;

        if (dest.getMinimum() != dest.getMaximum()) {
            throw new IllegalArgumentException(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.SOURCE_DEST_MUST_ENFORCE_VALUES));
        }
    }

    private static int sum(final int a,
                           final int b) {
        return Math.max(a, Math.max(b, a + b));
    }

    public void appendUsage(final StringBuffer buffer,
                            final Set helpSettings,
                            final Comparator comp) {
        final int length = buffer.length();

        source.appendUsage(buffer, helpSettings, comp);

        if (buffer.length() != length) {
            buffer.append(' ');
        }

        dest.appendUsage(buffer, helpSettings, comp);
    }

    public List helpLines(int depth,
                          Set helpSettings,
                          Comparator comp) {
        final List helpLines = new ArrayList();
        helpLines.addAll(source.helpLines(depth, helpSettings, comp));
        helpLines.addAll(dest.helpLines(depth, helpSettings, comp));

        return helpLines;
    }

    public void validate(WriteableCommandLine commandLine,
                         Option option)
        throws OptionException {
        final List values = commandLine.getValues(option);

        final int limit = values.size() - dest.getMinimum();
        int count = 0;

        final Iterator i = values.iterator();

        while (count++ < limit) {
            commandLine.addValue(source, i.next());
        }

        while (i.hasNext()) {
            commandLine.addValue(dest, i.next());
        }

        source.validate(commandLine, source);
        dest.validate(commandLine, dest);
    }

    public boolean canProcess(final WriteableCommandLine commandLine,
                              final String arg) {
        return source.canProcess(commandLine, arg) || dest.canProcess(commandLine, arg);
    }
}
