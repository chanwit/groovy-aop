/**
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
package org.codehaus.groovy.aop.commons.cli2.commandline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.aop.commons.cli2.CommandLine;
import org.codehaus.groovy.aop.commons.cli2.Option;
import org.codehaus.groovy.aop.commons.cli2.option.PropertyOption;

/**
 * Manages a queue of default CommandLines. This CommandLine implementation is
 * backed by a queue of CommandLine instances which are queried in turn until a
 * suitable result is found.
 *
 * CommandLine instances can either be added to the back of the queue or can be
 * pushed in at a specific position.
 *
 * @see #appendCommandLine(CommandLine)
 * @see #insertCommandLine(int, CommandLine)
 */
public class DefaultingCommandLine extends CommandLineImpl {

    /**
     * The list of default CommandLine instances
     */
    private final List commandLines = new ArrayList();

    /**
     * Adds a CommandLine instance to the back of the queue. The supplied
     * CommandLine will be used as defaults when all other CommandLines produce
     * no result
     *
     * @param commandLine
     *            the default values to use if all CommandLines
     */
    public void appendCommandLine(final CommandLine commandLine) {
        commandLines.add(commandLine);
    }

    /**
     * Adds a CommandLine instance to a specified position in the queue.
     *
     * @param index ths position at which to insert
     * @param commandLine the CommandLine to insert
     */
    public void insertCommandLine(
        final int index,
        final CommandLine commandLine) {
        commandLines.add(index, commandLine);
    }

    /**
     * Builds an iterator over the build in CommandLines.
     *
     * @return an unmodifiable iterator
     */
    public Iterator commandLines(){
        return Collections.unmodifiableList(commandLines).iterator();
    }

    public Option getOption(String trigger) {
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            final Option actual = commandLine.getOption(trigger);
            if (actual != null) {
                return actual;
            }
        }
        return null;
    }

    public List getOptions() {
        final List options = new ArrayList();

        final List temp = new ArrayList();
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            temp.clear();
            temp.addAll(commandLine.getOptions());
            temp.removeAll(options);
            options.addAll(temp);
        }

        return Collections.unmodifiableList(options);
    }

    public Set getOptionTriggers() {
        final Set all = new HashSet();
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            all.addAll(commandLine.getOptionTriggers());
        }

        return Collections.unmodifiableSet(all);
    }

    public boolean hasOption(Option option) {
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            if (commandLine.hasOption(option)) {
                return true;
            }
        }
        return false;
    }

    public List getValues(Option option, List defaultValues) {
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            final List actual = commandLine.getValues(option);
            if (actual != null && !actual.isEmpty()) {
                return actual;
            }
        }
        if(defaultValues==null){
            return Collections.EMPTY_LIST;
        }
        else{
            return defaultValues;
        }
    }

    public Boolean getSwitch(Option option, Boolean defaultValue) {
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            final Boolean actual = commandLine.getSwitch(option);
            if (actual != null) {
                return actual;
            }
        }
        return defaultValue;
    }

    public String getProperty(final String property) {
        return getProperty(new PropertyOption(), property);
    }

    public String getProperty(final Option option, String property, String defaultValue) {
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            final String actual = commandLine.getProperty(option, property);
            if (actual != null) {
                return actual;
            }
        }
        return defaultValue;
    }

    public Set getProperties(final Option option) {
        final Set all = new HashSet();
        for (final Iterator i = commandLines.iterator(); i.hasNext();) {
            final CommandLine commandLine = (CommandLine)i.next();
            all.addAll(commandLine.getProperties(option));
        }
        return Collections.unmodifiableSet(all);
    }

    public Set getProperties() {
        return getProperties(new PropertyOption());
    }
}
