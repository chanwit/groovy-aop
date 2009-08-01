package org.codehaus.groovy.aop.compiler;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;
import java.security.CodeSource;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;

import org.codehaus.groovy.aop.compiler.AspectGroovyParser.compilationUnit_return;

public class AspectCompilationUnit extends CompilationUnit {

    public AspectCompilationUnit() {
        super();
    }

    public AspectCompilationUnit(CompilerConfiguration config, CodeSource codeSource,
            GroovyClassLoader gcl) {
        super(config, codeSource, gcl);
    }

    public AspectCompilationUnit(CompilerConfiguration config) {
        super(config);
    }

    public AspectCompilationUnit(GroovyClassLoader gcl) {
        super(gcl);
    }

    public void addSources(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            File file = new File(paths[i]);
            if (file.getName().endsWith(".ga") ||
                file.getName().endsWith("Aspect.groovy")) {
                addSource(translateAspect(file));
            } else {
                addSource(file);
            }
        }
    }

    public void addSources(File[] files) {
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".ga") ||
                files[i].getName().endsWith("Aspect.groovy")) {
                addSource(translateAspect(files[i]));
            } else {
                addSource(files[i]);
            }
        }
    }

    // TODO: add encoding support
    private SourceUnit translateAspect(File file) {
        AspectGroovyLexer lex = null;
        try {
            lex = new AspectGroovyLexer(new ANTLRFileStream(file.getPath()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        CommonTokenStream tokens = new CommonTokenStream(lex);
        AspectGroovyParser g = new AspectGroovyParser(tokens);
        try {
            compilationUnit_return r = g.compilationUnit();
            SourceUnit unit = SourceUnit.create(file.getPath(), r.st.toString());
            return unit;
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
