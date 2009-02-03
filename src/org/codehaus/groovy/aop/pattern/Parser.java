/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Description
 *
 * @author chanwit
 */

package org.codehaus.groovy.aop.pattern;

import org.antlr.runtime.*;
import org.codehaus.groovy.aop.pattern.impl.GaptParser;
import org.codehaus.groovy.aop.pattern.impl.GaptLexer;

public class Parser {
    
    private String input;

    public Parser(String input) {
        this.input = input;
    }

    public Pattern parse() throws Exception {
        final Exception[] ex = new Exception[]{null};
        ANTLRStringStream s = new ANTLRStringStream(input);
        GaptLexer lexer = new GaptLexer(s);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GaptParser parser = new GaptParser(tokens) {
            public void reportError(RecognitionException e) {
                super.reportError(e);
                ex[0] = e;
            }
        };
        Pattern result = parser.pattern();
        if(ex[0] !=null) {
            throw new Exception(ex[0].getMessage());
        } else {
            return result;
        }
    }
}
