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
package org.codehaus.groovy.aop.commons.cli2.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.aop.commons.cli2.DisplaySetting;
import org.codehaus.groovy.aop.commons.cli2.Group;
import org.codehaus.groovy.aop.commons.cli2.HelpLine;
import org.codehaus.groovy.aop.commons.cli2.Option;
import org.codehaus.groovy.aop.commons.cli2.OptionException;
import org.codehaus.groovy.aop.commons.cli2.resource.ResourceConstants;
import org.codehaus.groovy.aop.commons.cli2.resource.ResourceHelper;

/**
 * Presents on screen help based on the application's Options
 */
public class HelpFormatter {
    /**
     * The default screen width
     */
    public static final int DEFAULT_FULL_WIDTH = 80;

    /**
     * The default screen furniture left of screen
     */
    public static final String DEFAULT_GUTTER_LEFT = "";

    /**
     * The default screen furniture right of screen
     */
    public static final String DEFAULT_GUTTER_CENTER = "    ";

    /**
     * The default screen furniture between columns
     */
    public static final String DEFAULT_GUTTER_RIGHT = "";

    /**
     * The default DisplaySettings used to select the elements to display in the
     * displayed line of full usage information.
     *
     * @see DisplaySetting
     */
    public static final Set DEFAULT_FULL_USAGE_SETTINGS;

    /**
     * The default DisplaySettings used to select the elements of usage per help
     * line in the main body of help
     *
     * @see DisplaySetting
     */
    public static final Set DEFAULT_LINE_USAGE_SETTINGS;

    /**
     * The default DisplaySettings used to select the help lines in the main
     * body of help
     */
    public static final Set DEFAULT_DISPLAY_USAGE_SETTINGS;

    static {
        final Set fullUsage = new HashSet(DisplaySetting.ALL);
        fullUsage.remove(DisplaySetting.DISPLAY_ALIASES);
        fullUsage.remove(DisplaySetting.DISPLAY_GROUP_NAME);
        fullUsage.remove(DisplaySetting.DISPLAY_OPTIONAL_CHILD_GROUP);
        DEFAULT_FULL_USAGE_SETTINGS = Collections.unmodifiableSet(fullUsage);

        final Set lineUsage = new HashSet();
        lineUsage.add(DisplaySetting.DISPLAY_ALIASES);
        lineUsage.add(DisplaySetting.DISPLAY_GROUP_NAME);
        lineUsage.add(DisplaySetting.DISPLAY_PARENT_ARGUMENT);
        DEFAULT_LINE_USAGE_SETTINGS = Collections.unmodifiableSet(lineUsage);

        final Set displayUsage = new HashSet(DisplaySetting.ALL);
        displayUsage.remove(DisplaySetting.DISPLAY_PARENT_ARGUMENT);
        DEFAULT_DISPLAY_USAGE_SETTINGS = Collections.unmodifiableSet(displayUsage);
    }

    private Set fullUsageSettings = new HashSet(DEFAULT_FULL_USAGE_SETTINGS);
    private Set lineUsageSettings = new HashSet(DEFAULT_LINE_USAGE_SETTINGS);
    private Set displaySettings = new HashSet(DEFAULT_DISPLAY_USAGE_SETTINGS);
    private OptionException exception = null;
    private Group group;
    private Comparator comparator = null;
    private String divider = null;
    private String header = null;
    private String footer = null;
    private String shellCommand = "";
    private PrintWriter out = new PrintWriter(System.out);

    //or should this default to .err?
    private final String gutterLeft;
    private final String gutterCenter;
    private final String gutterRight;
    private final int pageWidth;

    /**
     * Creates a new HelpFormatter using the defaults
     */
    public HelpFormatter() {
        this(DEFAULT_GUTTER_LEFT, DEFAULT_GUTTER_CENTER, DEFAULT_GUTTER_RIGHT, DEFAULT_FULL_WIDTH);
    }

    /**
     * Creates a new HelpFormatter using the specified parameters
     * @param gutterLeft the string marking left of screen
     * @param gutterCenter the string marking center of screen
     * @param gutterRight the string marking right of screen
     * @param fullWidth the width of the screen
     */
    public HelpFormatter(final String gutterLeft,
                         final String gutterCenter,
                         final String gutterRight,
                         final int fullWidth) {
        // default the left gutter to empty string
        this.gutterLeft = (gutterLeft == null) ? DEFAULT_GUTTER_LEFT : gutterLeft;

        // default the center gutter to a single space
        this.gutterCenter = (gutterCenter == null) ? DEFAULT_GUTTER_CENTER : gutterCenter;

        // default the right gutter to empty string
        this.gutterRight = (gutterRight == null) ? DEFAULT_GUTTER_RIGHT : gutterRight;

        // calculate the available page width
        this.pageWidth = fullWidth - this.gutterLeft.length() - this.gutterRight.length();

        // check available page width is valid
        int availableWidth = fullWidth - pageWidth + this.gutterCenter.length();

        if (availableWidth < 2) {
            throw new IllegalArgumentException(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.HELPFORMATTER_GUTTER_TOO_LONG));
        }
    }

    /**
     * Prints the Option help.
     */
    public void print() {
        printHeader();
        printException();
        printUsage();
        printHelp();
        printFooter();
        out.flush();
    }

    /**
     * Prints any error message.
     */
    public void printException() {
        if (exception != null) {
            printDivider();
            printWrapped(exception.getMessage());
        }
    }

    /**
     * Prints detailed help per option.
     */
    public void printHelp() {
        printDivider();

        final Option option;

        if ((exception != null) && (exception.getOption() != null)) {
            option = exception.getOption();
        } else {
            option = group;
        }

        // grab the HelpLines to display
        final List helpLines = option.helpLines(0, displaySettings, comparator);

        // calculate the maximum width of the usage strings
        int usageWidth = 0;

        for (final Iterator i = helpLines.iterator(); i.hasNext();) {
            final HelpLine helpLine = (HelpLine) i.next();
            final String usage = helpLine.usage(lineUsageSettings, comparator);
            usageWidth = Math.max(usageWidth, usage.length());
        }

        // build a blank string to pad wrapped descriptions
        final StringBuffer blankBuffer = new StringBuffer();

        for (int i = 0; i < usageWidth; i++) {
            blankBuffer.append(' ');
        }

        // determine the width available for descriptions
        final int descriptionWidth = Math.max(1, pageWidth - gutterCenter.length() - usageWidth);

        // display each HelpLine
        for (final Iterator i = helpLines.iterator(); i.hasNext();) {
            // grab the HelpLine
            final HelpLine helpLine = (HelpLine) i.next();

            // wrap the description
            final List descList = wrap(helpLine.getDescription(), descriptionWidth);
            final Iterator descriptionIterator = descList.iterator();

            // display usage + first line of description
            printGutterLeft();
            pad(helpLine.usage(lineUsageSettings, comparator), usageWidth, out);
            out.print(gutterCenter);
            pad((String) descriptionIterator.next(), descriptionWidth, out);
            printGutterRight();
            out.println();

            // display padding + remaining lines of description
            while (descriptionIterator.hasNext()) {
                printGutterLeft();

                //pad(helpLine.getUsage(),usageWidth,out);
                out.print(blankBuffer);
                out.print(gutterCenter);
                pad((String) descriptionIterator.next(), descriptionWidth, out);
                printGutterRight();
                out.println();
            }
        }

        printDivider();
    }

    /**
     * Prints a single line of usage information (wrapping if necessary)
     */
    public void printUsage() {
        printDivider();

        final StringBuffer buffer = new StringBuffer("Usage:\n");
        buffer.append(shellCommand).append(' ');
        group.appendUsage(buffer, fullUsageSettings, comparator, " ");
        printWrapped(buffer.toString());
    }

    /**
     * Prints a header string if necessary
     */
    public void printHeader() {
        if (header != null) {
            printDivider();
            printWrapped(header);
        }
    }

    /**
     * Prints a footer string if necessary
     */
    public void printFooter() {
        if (footer != null) {
            printWrapped(footer);
            printDivider();
        }
    }

    /**
     * Prints a string wrapped if necessary
     * @param text the string to wrap
     */
    public void printWrapped(final String text) {
        for (final Iterator i = wrap(text, pageWidth).iterator(); i.hasNext();) {
            printGutterLeft();
            pad((String) i.next(), pageWidth, out);
            printGutterRight();
            out.println();
        }

        out.flush();
    }

    /**
     * Prints the left gutter string
     */
    public void printGutterLeft() {
        if (gutterLeft != null) {
            out.print(gutterLeft);
        }
    }

    /**
     * Prints the right gutter string
     */
    public void printGutterRight() {
        if (gutterRight != null) {
            out.print(gutterRight);
        }
    }

    /**
     * Prints the divider text
     */
    public void printDivider() {
        if (divider != null) {
            out.println(divider);
        }
    }

    protected static void pad(final String text,
                              final int width,
                              final PrintWriter writer) {
        final int left;

        // write the text and record how many characters written
        if (text == null) {
            left = 0;
        } else {
            writer.write(text);
            left = text.length();
        }

        // pad remainder with spaces
        for (int i = left; i < width; ++i) {
            writer.write(' ');
        }
    }

    protected static List wrap(final String text,
                               final int width) {
        // check for valid width
        if (width < 1) {
            throw new IllegalArgumentException(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.HELPFORMATTER_WIDTH_TOO_NARROW,
                                                                                             new Object[] {
                                                                                                 new Integer(width)
                                                                                             }));
        }

        // handle degenerate case
        if (text == null) {
            return Collections.singletonList("");
        }

        final List lines = new ArrayList();
        final char[] chars = text.toCharArray();
        int left = 0;

        // for each character in the string
        while (left < chars.length) {
            // sync left and right indeces
            int right = left;

            // move right until we run out of characters, width or find a newline
            while ((right < chars.length) && (chars[right] != '\n') &&
                       (right < (left + width + 1))) {
                right++;
            }

            // if a newline was found
            if ((right < chars.length) && (chars[right] == '\n')) {
                // record the substring
                final String line = new String(chars, left, right - left);
                lines.add(line);

                // move to the end of the substring
                left = right + 1;

                if (left == chars.length) {
                    lines.add("");
                }

                // restart the loop
                continue;
            }

            // move to the next ideal wrap point
            right = (left + width) - 1;

            // if we have run out of characters
            if (chars.length <= right) {
                // record the substring
                final String line = new String(chars, left, chars.length - left);
                lines.add(line);

                // abort the loop
                break;
            }

            // back track the substring end until a space is found
            while ((right >= left) && (chars[right] != ' ')) {
                right--;
            }

            // if a space was found
            if (right >= left) {
                // record the substring to space
                final String line = new String(chars, left, right - left);
                lines.add(line);

                // absorb all the spaces before next substring
                while ((right < chars.length) && (chars[right] == ' ')) {
                    right++;
                }

                left = right;

                // restart the loop
                continue;
            }

            // move to the wrap position irrespective of spaces
            right = Math.min(left + width, chars.length);

            // record the substring
            final String line = new String(chars, left, right - left);
            lines.add(line);

            // absorb any the spaces before next substring
            while ((right < chars.length) && (chars[right] == ' ')) {
                right++;
            }

            left = right;
        }

        return lines;
    }

    /**
     * The Comparator to use when sorting Options
     * @param comparator Comparator to use when sorting Options
     */
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
     * The DisplaySettings used to select the help lines in the main body of
     * help
     *
     * @param displaySettings the settings to use
     * @see DisplaySetting
     */
    public void setDisplaySettings(Set displaySettings) {
        this.displaySettings = displaySettings;
    }

    /**
     * Sets the string to use as a divider between sections of help
     * @param divider the dividing string
     */
    public void setDivider(String divider) {
        this.divider = divider;
    }

    /**
     * Sets the exception to document
     * @param exception the exception that occured
     */
    public void setException(OptionException exception) {
        this.exception = exception;
    }

    /**
     * Sets the footer text of the help screen
     * @param footer the footer text
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * The DisplaySettings used to select the elements to display in the
     * displayed line of full usage information.
     * @see DisplaySetting
     * @param fullUsageSettings
     */
    public void setFullUsageSettings(Set fullUsageSettings) {
        this.fullUsageSettings = fullUsageSettings;
    }

    /**
     * Sets the Group of Options to document
     * @param group the options to document
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Sets the footer text of the help screen
     * @param header the footer text
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Sets the DisplaySettings used to select elements in the per helpline
     * usage strings.
     * @see DisplaySetting
     * @param lineUsageSettings the DisplaySettings to use
     */
    public void setLineUsageSettings(Set lineUsageSettings) {
        this.lineUsageSettings = lineUsageSettings;
    }

    /**
     * Sets the command string used to invoke the application
     * @param shellCommand the invokation command
     */
    public void setShellCommand(String shellCommand) {
        this.shellCommand = shellCommand;
    }

    /**
     * @return the Comparator used to sort the Group
     */
    public Comparator getComparator() {
        return comparator;
    }

    /**
     * @return the DisplaySettings used to select HelpLines
     */
    public Set getDisplaySettings() {
        return displaySettings;
    }

    /**
     * @return the String used as a horizontal section divider
     */
    public String getDivider() {
        return divider;
    }

    /**
     * @return the Exception being documented by this HelpFormatter
     */
    public OptionException getException() {
        return exception;
    }

    /**
     * @return the help screen footer text
     */
    public String getFooter() {
        return footer;
    }

    /**
     * @return the DisplaySettings used in the full usage string
     */
    public Set getFullUsageSettings() {
        return fullUsageSettings;
    }

    /**
     * @return the group documented by this HelpFormatter
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @return the String used as the central gutter
     */
    public String getGutterCenter() {
        return gutterCenter;
    }

    /**
     * @return the String used as the left gutter
     */
    public String getGutterLeft() {
        return gutterLeft;
    }

    /**
     * @return the String used as the right gutter
     */
    public String getGutterRight() {
        return gutterRight;
    }

    /**
     * @return the help screen header text
     */
    public String getHeader() {
        return header;
    }

    /**
     * @return the DisplaySettings used in the per help line usage strings
     */
    public Set getLineUsageSettings() {
        return lineUsageSettings;
    }

    /**
     * @return the width of the screen in characters
     */
    public int getPageWidth() {
        return pageWidth;
    }

    /**
     * @return the command used to execute the application
     */
    public String getShellCommand() {
        return shellCommand;
    }

    /**
     * @param out the PrintWriter to write to
     */
    public void setPrintWriter(PrintWriter out) {
        this.out = out;
    }

    /**
     * @return the PrintWriter that will be written to
     */
    public PrintWriter getPrintWriter() {
        return out;
    }
}
