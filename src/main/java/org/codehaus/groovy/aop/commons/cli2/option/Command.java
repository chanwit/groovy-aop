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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.codehaus.groovy.aop.commons.cli2.Argument;
import org.codehaus.groovy.aop.commons.cli2.DisplaySetting;
import org.codehaus.groovy.aop.commons.cli2.Group;
import org.codehaus.groovy.aop.commons.cli2.OptionException;
import org.codehaus.groovy.aop.commons.cli2.WriteableCommandLine;
import org.codehaus.groovy.aop.commons.cli2.resource.ResourceConstants;
import org.codehaus.groovy.aop.commons.cli2.resource.ResourceHelper;

/**
 * Represents a cvs "update" style command line option.
 *
 * Like all Parents, Commands can have child options and can be part of
 * Arguments
 */
public class Command
    extends ParentImpl {
    /** The display name for the command */
    private final String preferredName;

    /** The aliases for this command */
    private final Set aliases;

    /** All the names for this command */
    private final Set triggers;

    /**
     * Creates a new Command instance.
     *
     * @param preferredName
     *            The name normally used to refer to the Command
     * @param description
     *            A description of the Command
     * @param aliases
     *            Alternative names for the Command
     * @param required
     *            Whether the Command is required
     * @param argument
     *            An Argument that the command takes
     * @param children
     *            The Group of child options for this Command
     * @param id
     *            A unique id for the Command
     *
     * @see ParentImpl#ParentImpl(Argument, Group, String, int, boolean)
     */
    public Command(final String preferredName,
                   final String description,
                   final Set aliases,
                   final boolean required,
                   final Argument argument,
                   final Group children,
                   final int id) {
        super(argument, children, description, id, required);

        // check the preferred name is valid
        if ((preferredName == null) || (preferredName.length() < 1)) {
            throw new IllegalArgumentException(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.COMMAND_PREFERRED_NAME_TOO_SHORT));
        }

        this.preferredName = preferredName;

        // gracefully and defensively handle aliases
        this.aliases =
            (aliases == null) ? Collections.EMPTY_SET
                              : Collections.unmodifiableSet(new HashSet(aliases));

        // populate the triggers Set
        final Set newTriggers = new HashSet();
        newTriggers.add(preferredName);
        newTriggers.addAll(this.aliases);
        this.triggers = Collections.unmodifiableSet(newTriggers);
    }

    public void processParent(final WriteableCommandLine commandLine,
                              final ListIterator arguments)
        throws OptionException {
        // grab the argument to process
        final String arg = (String) arguments.next();

        // if we can process it
        if (canProcess(commandLine, arg)) {
            // then note the option
            commandLine.addOption(this);

            // normalise the argument list
            arguments.set(preferredName);
        } else {
            throw new OptionException(this, ResourceConstants.UNEXPECTED_TOKEN, arg);
        }
    }

    public Set getTriggers() {
        return triggers;
    }

    public void validate(WriteableCommandLine commandLine)
        throws OptionException {
        if (isRequired() && !commandLine.hasOption(this)) {
            throw new OptionException(this, ResourceConstants.OPTION_MISSING_REQUIRED,
                                      getPreferredName());
        }

        super.validate(commandLine);
    }

    public void appendUsage(final StringBuffer buffer,
                            final Set helpSettings,
                            final Comparator comp) {
        // do we display optionality
        final boolean optional =
            !isRequired() && helpSettings.contains(DisplaySetting.DISPLAY_OPTIONAL);
        final boolean displayAliases = helpSettings.contains(DisplaySetting.DISPLAY_ALIASES);

        if (optional) {
            buffer.append('[');
        }

        buffer.append(preferredName);

        if (displayAliases && !aliases.isEmpty()) {
            buffer.append(" (");

            final List list = new ArrayList(aliases);
            Collections.sort(list);

            for (final Iterator i = list.iterator(); i.hasNext();) {
                final String alias = (String) i.next();
                buffer.append(alias);

                if (i.hasNext()) {
                    buffer.append(',');
                }
            }

            buffer.append(')');
        }

        super.appendUsage(buffer, helpSettings, comp);

        if (optional) {
            buffer.append(']');
        }
    }

    public String getPreferredName() {
        return preferredName;
    }
}
