// $ANTLR 3.1.1 C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g 2009-02-05 03:59:37

package org.codehaus.groovy.aop.pattern.impl;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GaptLexer extends Lexer {
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int T__8=8;
    public static final int Identifier=4;
    public static final int T__19=19;
    public static final int WS=7;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int JavaIDDigit=6;
    public static final int Letter=5;

    // delegates
    // delegators

    public GaptLexer() {;} 
    public GaptLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public GaptLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g"; }

    // $ANTLR start "T__8"
    public final void mT__8() throws RecognitionException {
        try {
            int _type = T__8;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:7:6: ( '.' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:7:8: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__8"

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:8:6: ( '+' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:8:8: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:9:7: ( '(' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:9:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:10:7: ( ')' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:10:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:11:7: ( ',' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:11:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:12:7: ( '*' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:12:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:13:7: ( 'public' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:13:9: 'public'
            {
            match("public"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:14:7: ( 'private' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:14:9: 'private'
            {
            match("private"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:15:7: ( 'protected' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:15:9: 'protected'
            {
            match("protected"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:16:7: ( 'boolean' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:16:9: 'boolean'
            {
            match("boolean"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:17:7: ( 'byte' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:17:9: 'byte'
            {
            match("byte"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:18:7: ( 'char' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:18:9: 'char'
            {
            match("char"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:19:7: ( 'double' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:19:9: 'double'
            {
            match("double"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:20:7: ( 'float' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:20:9: 'float'
            {
            match("float"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:21:7: ( 'int' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:21:9: 'int'
            {
            match("int"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:7: ( 'long' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:9: 'long'
            {
            match("long"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:23:7: ( 'short' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:23:9: 'short'
            {
            match("short"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:7: ( 'void' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:9: 'void'
            {
            match("void"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:25:7: ( '..' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:25:9: '..'
            {
            match(".."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "Identifier"
    public final void mIdentifier() throws RecognitionException {
        try {
            int _type = Identifier;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:137:5: ( Letter ( Letter | JavaIDDigit )* )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:137:7: Letter ( Letter | JavaIDDigit )*
            {
            mLetter(); 
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:137:14: ( Letter | JavaIDDigit )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='$'||LA1_0=='*'||(LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')||(LA1_0>='\u00C0' && LA1_0<='\u00D6')||(LA1_0>='\u00D8' && LA1_0<='\u00F6')||(LA1_0>='\u00F8' && LA1_0<='\u1FFF')||(LA1_0>='\u3040' && LA1_0<='\u318F')||(LA1_0>='\u3300' && LA1_0<='\u337F')||(LA1_0>='\u3400' && LA1_0<='\u3D2D')||(LA1_0>='\u4E00' && LA1_0<='\u9FFF')||(LA1_0>='\uF900' && LA1_0<='\uFAFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:
            	    {
            	    if ( input.LA(1)=='$'||input.LA(1)=='*'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Identifier"

    // $ANTLR start "Letter"
    public final void mLetter() throws RecognitionException {
        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:142:5: ( '\\u0024' | '\\u0041' .. '\\u005a' | '\\u005f' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' | '*' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:
            {
            if ( input.LA(1)=='$'||input.LA(1)=='*'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Letter"

    // $ANTLR start "JavaIDDigit"
    public final void mJavaIDDigit() throws RecognitionException {
        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:160:5: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09EF')||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A6F')||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u1040' && input.LA(1)<='\u1049') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "JavaIDDigit"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:177:5: ( ' ' )
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:177:8: ' '
            {
            match(' '); 
             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:8: ( T__8 | T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | Identifier | WS )
        int alt2=21;
        alt2 = dfa2.predict(input);
        switch (alt2) {
            case 1 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:10: T__8
                {
                mT__8(); 

                }
                break;
            case 2 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:15: T__9
                {
                mT__9(); 

                }
                break;
            case 3 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:20: T__10
                {
                mT__10(); 

                }
                break;
            case 4 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:26: T__11
                {
                mT__11(); 

                }
                break;
            case 5 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:32: T__12
                {
                mT__12(); 

                }
                break;
            case 6 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:38: T__13
                {
                mT__13(); 

                }
                break;
            case 7 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:44: T__14
                {
                mT__14(); 

                }
                break;
            case 8 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:50: T__15
                {
                mT__15(); 

                }
                break;
            case 9 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:56: T__16
                {
                mT__16(); 

                }
                break;
            case 10 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:62: T__17
                {
                mT__17(); 

                }
                break;
            case 11 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:68: T__18
                {
                mT__18(); 

                }
                break;
            case 12 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:74: T__19
                {
                mT__19(); 

                }
                break;
            case 13 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:80: T__20
                {
                mT__20(); 

                }
                break;
            case 14 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:86: T__21
                {
                mT__21(); 

                }
                break;
            case 15 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:92: T__22
                {
                mT__22(); 

                }
                break;
            case 16 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:98: T__23
                {
                mT__23(); 

                }
                break;
            case 17 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:104: T__24
                {
                mT__24(); 

                }
                break;
            case 18 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:110: T__25
                {
                mT__25(); 

                }
                break;
            case 19 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:116: T__26
                {
                mT__26(); 

                }
                break;
            case 20 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:122: Identifier
                {
                mIdentifier(); 

                }
                break;
            case 21 :
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:1:133: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\1\uffff\1\23\4\uffff\1\24\11\20\5\uffff\23\20\1\64\7\20\1\74\1"+
        "\75\2\20\1\uffff\1\100\1\20\1\102\4\20\2\uffff\1\20\1\110\1\uffff"+
        "\1\111\1\uffff\1\112\3\20\1\116\3\uffff\1\117\1\20\1\121\2\uffff"+
        "\1\20\1\uffff\1\123\1\uffff";
    static final String DFA2_eofS =
        "\124\uffff";
    static final String DFA2_minS =
        "\1\40\1\56\4\uffff\1\44\1\162\1\157\1\150\1\157\1\154\1\156\1\157"+
        "\1\150\1\157\5\uffff\1\142\1\151\1\157\1\164\1\141\1\165\1\157\1"+
        "\164\1\156\1\157\1\151\1\154\1\166\1\164\1\154\1\145\1\162\1\142"+
        "\1\141\1\44\1\147\1\162\1\144\1\151\1\141\2\145\2\44\1\154\1\164"+
        "\1\uffff\1\44\1\164\1\44\1\143\1\164\1\143\1\141\2\uffff\1\145\1"+
        "\44\1\uffff\1\44\1\uffff\1\44\1\145\1\164\1\156\1\44\3\uffff\1\44"+
        "\1\145\1\44\2\uffff\1\144\1\uffff\1\44\1\uffff";
    static final String DFA2_maxS =
        "\1\ufaff\1\56\4\uffff\1\ufaff\1\165\1\171\1\150\1\157\1\154\1\156"+
        "\1\157\1\150\1\157\5\uffff\1\142\2\157\1\164\1\141\1\165\1\157\1"+
        "\164\1\156\1\157\1\151\1\154\1\166\1\164\1\154\1\145\1\162\1\142"+
        "\1\141\1\ufaff\1\147\1\162\1\144\1\151\1\141\2\145\2\ufaff\1\154"+
        "\1\164\1\uffff\1\ufaff\1\164\1\ufaff\1\143\1\164\1\143\1\141\2\uffff"+
        "\1\145\1\ufaff\1\uffff\1\ufaff\1\uffff\1\ufaff\1\145\1\164\1\156"+
        "\1\ufaff\3\uffff\1\ufaff\1\145\1\ufaff\2\uffff\1\144\1\uffff\1\ufaff"+
        "\1\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\2\1\3\1\4\1\5\12\uffff\1\24\1\25\1\23\1\1\1\6\37\uffff"+
        "\1\17\7\uffff\1\13\1\14\2\uffff\1\20\1\uffff\1\22\5\uffff\1\16\1"+
        "\21\1\7\3\uffff\1\15\1\10\1\uffff\1\12\1\uffff\1\11";
    static final String DFA2_specialS =
        "\124\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\21\3\uffff\1\20\3\uffff\1\3\1\4\1\6\1\2\1\5\1\uffff\1\1"+
            "\22\uffff\32\20\4\uffff\1\20\1\uffff\1\20\1\10\1\11\1\12\1\20"+
            "\1\13\2\20\1\14\2\20\1\15\3\20\1\7\2\20\1\16\2\20\1\17\4\20"+
            "\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20\u1040\uffff"+
            "\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e\20\u10d2\uffff"+
            "\u5200\20\u5900\uffff\u0200\20",
            "\1\22",
            "",
            "",
            "",
            "",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\26\2\uffff\1\25",
            "\1\27\11\uffff\1\30",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "",
            "",
            "",
            "",
            "",
            "\1\40",
            "\1\41\5\uffff\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\76",
            "\1\77",
            "",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\101",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "",
            "",
            "\1\107",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "",
            "",
            "",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "\1\120",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            "",
            "",
            "\1\122",
            "",
            "\1\20\5\uffff\1\20\5\uffff\12\20\7\uffff\32\20\4\uffff\1\20"+
            "\1\uffff\32\20\105\uffff\27\20\1\uffff\37\20\1\uffff\u1f08\20"+
            "\u1040\uffff\u0150\20\u0170\uffff\u0080\20\u0080\uffff\u092e"+
            "\20\u10d2\uffff\u5200\20\u5900\uffff\u0200\20",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__8 | T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | Identifier | WS );";
        }
    }
 

}