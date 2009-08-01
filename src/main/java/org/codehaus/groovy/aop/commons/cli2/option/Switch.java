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
 * A Parent implementation representing normal switch options.
 * For example: <code>+d|-d</code> or <code>--enable-x|--disable-x</code>.
 */
public class Switch
    extends ParentImpl {
    /** i18n */
    public static final ResourceHelper resources = ResourceHelper.getResourceHelper();

    /**
     * The default prefix for enabled switches
     */
    public static final String DEFAULT_ENABLED_PREFIX = "+";

    /**
     * The default prefix for disabled switches
     */
    public static final String DEFAULT_DISABLED_PREFIX = "-";
    private final String enabledPrefix;
    private final String disabledPrefix;
    private final Set triggers;
    private final String preferredName;
    private final Set aliases;
    private final Set prefixes;
    private final Boolean defaultSwitch;

    /**
     * Creates a new Switch with the specified parameters
     * @param enabledPrefix the prefix used for enabled switches
     * @param disabledPrefix the prefix used for disabled switches
     * @param preferredName the preferred name of the switch
     * @param aliases the aliases by which the Switch is known
     * @param description a description of the Switch
     * @param required whether the Option is strictly required
     * @param argument the Argument belonging to this Parent, or null
     * @param children the Group children belonging to this Parent, ot null
     * @param id the unique identifier for this Option
     * @throws IllegalArgumentException if the preferredName or an alias isn't
     *     prefixed with enabledPrefix or disabledPrefix
     */
    public Switch(final String enabledPrefix,
                  final String disabledPrefix,
                  final String preferredName,
                  final Set aliases,
                  final String description,
                  final boolean required,
                  final Argument argument,
                  final Group children,
                  final int id,
                  final Boolean switchDefault) {
        super(argument, children, description, id, required);

        if (enabledPrefix == null) {
            throw new IllegalArgumentException(resources.getMessage(ResourceConstants.SWITCH_NO_ENABLED_PREFIX));
        }

        if (disabledPrefix == null) {
            throw new IllegalArgumentException(resources.getMessage(ResourceConstants.SWITCH_NO_DISABLED_PREFIX));
        }

        if (enabledPrefix.startsWith(disabledPrefix)) {
            throw new IllegalArgumentException(resources.getMessage(ResourceConstants.SWITCH_ENABLED_STARTS_WITH_DISABLED));
        }

        if (disabledPrefix.startsWith(enabledPrefix)) {
            throw new IllegalArgumentException(resources.getMessage(ResourceConstants.SWITCH_DISABLED_STARTWS_WITH_ENABLED));
        }

        this.enabledPrefix = enabledPrefix;
        this.disabledPrefix = disabledPrefix;
        this.preferredName = preferredName;

        if ((preferredName == null) || (preferredName.length() < 1)) {
            throw new IllegalArgumentException(resources.getMessage(ResourceConstants.SWITCH_PREFERRED_NAME_TOO_SHORT));
        }

        final Set newTriggers = new HashSet();
        newTriggers.add(enabledPrefix + preferredName);
        newTriggers.add(disabledPrefix + preferredName);
        this.triggers = Collections.unmodifiableSet(newTriggers);

        if (aliases == null) {
            this.aliases = Collections.EMPTY_SET;
        } else {
            this.aliases = Collections.unmodifiableSet(new HashSet(aliases));

            for (final Iterator i = aliases.iterator(); i.hasNext();) {
                final String alias = (String) i.next();
                newTriggers.add(enabledPrefix + alias);
                newTriggers.add(disabledPrefix + alias);
            }
        }

        final Set newPrefixes = new HashSet(super.getPrefixes());
        newPrefixes.add(enabledPrefix);
        newPrefixes.add(disabledPrefix);
        this.prefixes = Collections.unmodifiableSet(newPrefixes);

        this.defaultSwitch = switchDefault;

        checkPrefixes(newPrefixes);
    }

    public void processParent(final WriteableCommandLine commandLine,
                              final ListIterator arguments)
        throws OptionException {
        final String arg = (String) arguments.next();

        if (canProcess(commandLine, arg)) {
            if (arg.startsWith(enabledPrefix)) {
                commandLine.addSwitch(this, true);
                arguments.set(enabledPrefix + preferredName);
            }

            if (arg.startsWith(disabledPrefix)) {
                commandLine.addSwitch(this, false);
                arguments.set(disabledPrefix + preferredName);
            }
        } else {
            throw new OptionException(this, ResourceConstants.UNEXPECTED_TOKEN, arg);
        }
    }

    public Set getTriggers() {
        return triggers;
    }

    public Set getPrefixes() {
        return prefixes;
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
        final boolean disabled = helpSettings.contains(DisplaySetting.DISPLAY_SWITCH_DISABLED);
        final boolean enabled =
            !disabled || helpSettings.contains(DisplaySetting.DISPLAY_SWITCH_ENABLED);
        final boolean both = disabled && enabled;

        if (optional) {
            buffer.append('[');
        }

        if (enabled) {
            buffer.append(enabledPrefix).append(preferredName);
        }

        if (both) {
            buffer.append('|');
        }

        if (disabled) {
            buffer.append(disabledPrefix).append(preferredName);
        }

        if (displayAliases && !aliases.isEmpty()) {
            buffer.append(" (");

            final List list = new ArrayList(aliases);
            Collections.sort(list);

            for (final Iterator i = list.iterator(); i.hasNext();) {
                final String alias = (String) i.next();

                if (enabled) {
                    buffer.append(enabledPrefix).append(alias);
                }

                if (both) {
                    buffer.append('|');
                }

                if (disabled) {
                    buffer.append(disabledPrefix).append(alias);
                }

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
        return enabledPrefix + preferredName;
    }

    public void defaults(final WriteableCommandLine commandLine) {
        commandLine.setDefaultSwitch(this, defaultSwitch);
    }
}
