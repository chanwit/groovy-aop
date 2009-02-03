package org.codehaus.groovy.aop.compiler;

import java.io.File;

import groovy.lang.GroovyClassLoader;
import groovy.util.CliBuilder;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.option.DefaultOption;
import org.apache.commons.cli2.util.HelpFormatter;
import org.codehaus.groovy.control.CompilerConfiguration;

public class Compiler {

    private static Group options;

    private static DefaultOption help;
    private static DefaultOption dir;
    private static DefaultOption encoding;

    public static CommandLine parse(String[] args) {
        final DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        final ArgumentBuilder abuilder = new ArgumentBuilder();
        final GroupBuilder gbuilder = new GroupBuilder();

        help = obuilder
                .withLongName("help")
                .withShortName("h")
                .withDescription("print this message")
                .create();

        dir = obuilder
                .withLongName("outdir")
                .withShortName("d")
                .withDescription("set output directory")
                .create();

        encoding = obuilder
                .withLongName("encoding")
                .withShortName("e")
                .withDescription("set input file encoding")
                .withArgument(abuilder
                    .withDescription("character set")
                    .withName("character-set")
                    .withMaximum(1)
                    .withMinimum(1)
                    .create()
                )
                .create();

        Option version =
            obuilder
                .withShortName("version")
                .withDescription("print the version information and exit")
                .create();

        options = gbuilder
                .withName("options")
                .withOption(help)
                .withOption(dir)
                .withOption(encoding)
                .withOption(version)
                .create();

        Parser parser = new Parser();
        parser.setGroup(options);
        try {
            CommandLine cl = parser.parse(args);
            return cl;
        } catch (OptionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        CommandLine cl = parse(args);
        CompilerConfiguration config = new CompilerConfiguration();

        if(args.length == 0 || cl.hasOption(help)) {
            displayHelp();
            return;
        }

        if(cl.hasOption("-version")) {
            displayVersion();
            return;
        }

        if(cl.hasOption(encoding)==false) {
            config.setSourceEncoding("UTF8");
        } else {
            config.setSourceEncoding((String)cl.getValue(encoding));
        }

        /* TODO continue here to complete compilation process
            config.setClasspath("c:\\grails\\grails-0.5.6\\lib\\groovy-all-1.1-beta-2-snapshot.jar");
            config.setTargetDirectory("F:\\Projects\\groovy-aop\\bin-groovy\\");
            GroovyClassLoader gcl = new GroovyClassLoader(Compiler.class.getClass().getClassLoader(), config);
            AspectCompilationUnit unit = new AspectCompilationUnit(config, null, gcl);
            File[] files = new File[1];
            files[0] = new File("F:\\Projects\\groovy-aop\\main\\compiler\\org\\groovy\\aop\\compiler\\test\\Testcast.ga");
            unit.addSources(files);
            unit.compile();
        */
    }

    private static void displayVersion() {
        System.out.println("Groovy AOP version 0.5");
    }

    @SuppressWarnings("unchecked")
    private static void displayHelp() {
        HelpFormatter hf = new HelpFormatter();
        hf.setShellCommand("gac");
        hf.setHeader(
            "Groovy AOP Compiler version 0.5\n" +
            "(c) 2007-2009 Chanwit Kaewkasi\n"
        );
        hf.setGroup(options);

        hf.getFullUsageSettings().add(DisplaySetting.DISPLAY_GROUP_NAME);
        hf.getFullUsageSettings().add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        hf.getFullUsageSettings().remove(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        hf.print();
    }

}
