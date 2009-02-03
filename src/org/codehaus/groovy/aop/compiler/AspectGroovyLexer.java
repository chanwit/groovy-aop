// $ANTLR 3.1.1 C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g 2009-02-04 01:00:20

package org.codehaus.groovy.aop.compiler;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class AspectGroovyLexer extends Lexer {
    public static final int LINE_COMMENT=10;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int Identifier=4;
    public static final int Block=5;
    public static final int T__19=19;
    public static final int T__16=16;
    public static final int WS=8;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int JavaIDDigit=7;
    public static final int COMMENT=9;
    public static final int Letter=6;

    // delegates
    // delegators

    public AspectGroovyLexer() {;} 
    public AspectGroovyLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public AspectGroovyLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g"; }

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:7:7: ( 'aspect' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:7:9: 'aspect'
            {
            match("aspect"); 


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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:8:7: ( 'public' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:8:9: 'public'
            {
            match("public"); 


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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:9:7: ( 'private' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:9:9: 'private'
            {
            match("private"); 


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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:10:7: ( 'protected' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:10:9: 'protected'
            {
            match("protected"); 


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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:11:7: ( 'static' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:11:9: 'static'
            {
            match("static"); 


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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:12:7: ( 'import' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:12:9: 'import'
            {
            match("import"); 


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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:13:7: ( ';' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:13:9: ';'
            {
            match(';'); 

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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:14:7: ( 'package' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:14:9: 'package'
            {
            match("package"); 


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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:15:7: ( '@' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:15:9: '@'
            {
            match('@'); 

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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:16:7: ( '(' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:16:9: '('
            {
            match('('); 

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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:17:7: ( ')' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:17:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "Identifier"
    public final void mIdentifier() throws RecognitionException {
        try {
            int _type = Identifier;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:70:5: ( Letter ( Letter | JavaIDDigit | '*' | '.' | ',' | '=' | '\\'' | '\\\"' )* )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:70:11: Letter ( Letter | JavaIDDigit | '*' | '.' | ',' | '=' | '\\'' | '\\\"' )*
            {
            mLetter(); 
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:70:18: ( Letter | JavaIDDigit | '*' | '.' | ',' | '=' | '\\'' | '\\\"' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\"'||LA1_0=='$'||LA1_0=='\''||LA1_0=='*'||LA1_0==','||LA1_0=='.'||(LA1_0>='0' && LA1_0<='9')||LA1_0=='='||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')||(LA1_0>='\u00C0' && LA1_0<='\u00D6')||(LA1_0>='\u00D8' && LA1_0<='\u00F6')||(LA1_0>='\u00F8' && LA1_0<='\u1FFF')||(LA1_0>='\u3040' && LA1_0<='\u318F')||(LA1_0>='\u3300' && LA1_0<='\u337F')||(LA1_0>='\u3400' && LA1_0<='\u3D2D')||(LA1_0>='\u4E00' && LA1_0<='\u9FFF')||(LA1_0>='\uF900' && LA1_0<='\uFAFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:
            	    {
            	    if ( input.LA(1)=='\"'||input.LA(1)=='$'||input.LA(1)=='\''||input.LA(1)=='*'||input.LA(1)==','||input.LA(1)=='.'||(input.LA(1)>='0' && input.LA(1)<='9')||input.LA(1)=='='||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:75:5: ( '\\u0024' | '\\u0041' .. '\\u005a' | '\\u005f' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:
            {
            if ( input.LA(1)=='$'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:92:5: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:
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
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:109:5: ( ( ' ' | '\\t' | '\\u000C' | '\\r' | '\\n' ) )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:109:8: ( ' ' | '\\t' | '\\u000C' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:114:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:114:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:114:14: ( options {greedy=false; } : . )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='*') ) {
                    int LA2_1 = input.LA(2);

                    if ( (LA2_1=='/') ) {
                        alt2=2;
                    }
                    else if ( ((LA2_1>='\u0000' && LA2_1<='.')||(LA2_1>='0' && LA2_1<='\uFFFF')) ) {
                        alt2=1;
                    }


                }
                else if ( ((LA2_0>='\u0000' && LA2_0<=')')||(LA2_0>='+' && LA2_0<='\uFFFF')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:114:42: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match("*/"); 

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "LINE_COMMENT"
    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:119:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:119:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:119:12: (~ ( '\\n' | '\\r' ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\uFFFF')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:119:12: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:119:26: ( '\\r' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\r') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:119:26: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LINE_COMMENT"

    // $ANTLR start "Block"
    public final void mBlock() throws RecognitionException {
        try {
            int _type = Block;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:123:7: ( '{' ( options {greedy=true; } : . )* '}' )
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:123:9: '{' ( options {greedy=true; } : . )* '}'
            {
            match('{'); 
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:123:13: ( options {greedy=true; } : . )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='}') ) {
                    int LA5_1 = input.LA(2);

                    if ( ((LA5_1>='\u0000' && LA5_1<='\uFFFF')) ) {
                        alt5=1;
                    }


                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='|')||(LA5_0>='~' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:123:39: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Block"

    public void mTokens() throws RecognitionException {
        // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:8: ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | Identifier | WS | COMMENT | LINE_COMMENT | Block )
        int alt6=16;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:10: T__11
                {
                mT__11(); 

                }
                break;
            case 2 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:16: T__12
                {
                mT__12(); 

                }
                break;
            case 3 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:22: T__13
                {
                mT__13(); 

                }
                break;
            case 4 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:28: T__14
                {
                mT__14(); 

                }
                break;
            case 5 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:34: T__15
                {
                mT__15(); 

                }
                break;
            case 6 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:40: T__16
                {
                mT__16(); 

                }
                break;
            case 7 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:46: T__17
                {
                mT__17(); 

                }
                break;
            case 8 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:52: T__18
                {
                mT__18(); 

                }
                break;
            case 9 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:58: T__19
                {
                mT__19(); 

                }
                break;
            case 10 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:64: T__20
                {
                mT__20(); 

                }
                break;
            case 11 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:70: T__21
                {
                mT__21(); 

                }
                break;
            case 12 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:76: Identifier
                {
                mIdentifier(); 

                }
                break;
            case 13 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:87: WS
                {
                mWS(); 

                }
                break;
            case 14 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:90: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 15 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:98: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;
            case 16 :
                // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:1:111: Block
                {
                mBlock(); 

                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA6_eotS =
        "\1\uffff\4\11\10\uffff\6\11\2\uffff\25\11\1\61\1\62\3\11\1\66\1"+
        "\67\2\uffff\1\70\1\11\1\72\3\uffff\1\11\1\uffff\1\74\1\uffff";
    static final String DFA6_eofS =
        "\75\uffff";
    static final String DFA6_minS =
        "\1\11\1\163\1\141\1\164\1\155\6\uffff\1\52\1\uffff\1\160\1\142"+
        "\1\151\1\143\1\141\1\160\2\uffff\1\145\1\154\1\166\1\164\1\153\1"+
        "\164\1\157\1\143\1\151\1\141\1\145\1\141\1\151\1\162\1\164\1\143"+
        "\1\164\1\143\1\147\1\143\1\164\2\42\1\145\1\164\1\145\2\42\2\uffff"+
        "\1\42\1\145\1\42\3\uffff\1\144\1\uffff\1\42\1\uffff";
    static final String DFA6_maxS =
        "\1\ufaff\1\163\1\165\1\164\1\155\6\uffff\1\57\1\uffff\1\160\1\142"+
        "\1\157\1\143\1\141\1\160\2\uffff\1\145\1\154\1\166\1\164\1\153\1"+
        "\164\1\157\1\143\1\151\1\141\1\145\1\141\1\151\1\162\1\164\1\143"+
        "\1\164\1\143\1\147\1\143\1\164\2\ufaff\1\145\1\164\1\145\2\ufaff"+
        "\2\uffff\1\ufaff\1\145\1\ufaff\3\uffff\1\144\1\uffff\1\ufaff\1\uffff";
    static final String DFA6_acceptS =
        "\5\uffff\1\7\1\11\1\12\1\13\1\14\1\15\1\uffff\1\20\6\uffff\1\16"+
        "\1\17\34\uffff\1\1\1\2\3\uffff\1\5\1\6\1\3\1\uffff\1\10\1\uffff"+
        "\1\4";
    static final String DFA6_specialS =
        "\75\uffff}>";
    static final String[] DFA6_transitionS = {
            "\2\12\1\uffff\2\12\22\uffff\1\12\3\uffff\1\11\3\uffff\1\7\1"+
            "\10\5\uffff\1\13\13\uffff\1\5\4\uffff\1\6\32\11\4\uffff\1\11"+
            "\1\uffff\1\1\7\11\1\4\6\11\1\2\2\11\1\3\7\11\1\14\104\uffff"+
            "\27\11\1\uffff\37\11\1\uffff\u1f08\11\u1040\uffff\u0150\11\u0170"+
            "\uffff\u0080\11\u0080\uffff\u092e\11\u10d2\uffff\u5200\11\u5900"+
            "\uffff\u0200\11",
            "\1\15",
            "\1\20\20\uffff\1\17\2\uffff\1\16",
            "\1\21",
            "\1\22",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\23\4\uffff\1\24",
            "",
            "\1\25",
            "\1\26",
            "\1\27\5\uffff\1\30",
            "\1\31",
            "\1\32",
            "\1\33",
            "",
            "",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "\1\40",
            "\1\41",
            "\1\42",
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
            "\1\11\1\uffff\1\11\2\uffff\1\11\2\uffff\1\11\1\uffff\1\11"+
            "\1\uffff\1\11\1\uffff\12\11\3\uffff\1\11\3\uffff\32\11\4\uffff"+
            "\1\11\1\uffff\32\11\105\uffff\27\11\1\uffff\37\11\1\uffff\u1f08"+
            "\11\u1040\uffff\u0150\11\u0170\uffff\u0080\11\u0080\uffff\u092e"+
            "\11\u10d2\uffff\u5200\11\u5900\uffff\u0200\11",
            "\1\11\1\uffff\1\11\2\uffff\1\11\2\uffff\1\11\1\uffff\1\11"+
            "\1\uffff\1\11\1\uffff\12\11\3\uffff\1\11\3\uffff\32\11\4\uffff"+
            "\1\11\1\uffff\32\11\105\uffff\27\11\1\uffff\37\11\1\uffff\u1f08"+
            "\11\u1040\uffff\u0150\11\u0170\uffff\u0080\11\u0080\uffff\u092e"+
            "\11\u10d2\uffff\u5200\11\u5900\uffff\u0200\11",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\11\1\uffff\1\11\2\uffff\1\11\2\uffff\1\11\1\uffff\1\11"+
            "\1\uffff\1\11\1\uffff\12\11\3\uffff\1\11\3\uffff\32\11\4\uffff"+
            "\1\11\1\uffff\32\11\105\uffff\27\11\1\uffff\37\11\1\uffff\u1f08"+
            "\11\u1040\uffff\u0150\11\u0170\uffff\u0080\11\u0080\uffff\u092e"+
            "\11\u10d2\uffff\u5200\11\u5900\uffff\u0200\11",
            "\1\11\1\uffff\1\11\2\uffff\1\11\2\uffff\1\11\1\uffff\1\11"+
            "\1\uffff\1\11\1\uffff\12\11\3\uffff\1\11\3\uffff\32\11\4\uffff"+
            "\1\11\1\uffff\32\11\105\uffff\27\11\1\uffff\37\11\1\uffff\u1f08"+
            "\11\u1040\uffff\u0150\11\u0170\uffff\u0080\11\u0080\uffff\u092e"+
            "\11\u10d2\uffff\u5200\11\u5900\uffff\u0200\11",
            "",
            "",
            "\1\11\1\uffff\1\11\2\uffff\1\11\2\uffff\1\11\1\uffff\1\11"+
            "\1\uffff\1\11\1\uffff\12\11\3\uffff\1\11\3\uffff\32\11\4\uffff"+
            "\1\11\1\uffff\32\11\105\uffff\27\11\1\uffff\37\11\1\uffff\u1f08"+
            "\11\u1040\uffff\u0150\11\u0170\uffff\u0080\11\u0080\uffff\u092e"+
            "\11\u10d2\uffff\u5200\11\u5900\uffff\u0200\11",
            "\1\71",
            "\1\11\1\uffff\1\11\2\uffff\1\11\2\uffff\1\11\1\uffff\1\11"+
            "\1\uffff\1\11\1\uffff\12\11\3\uffff\1\11\3\uffff\32\11\4\uffff"+
            "\1\11\1\uffff\32\11\105\uffff\27\11\1\uffff\37\11\1\uffff\u1f08"+
            "\11\u1040\uffff\u0150\11\u0170\uffff\u0080\11\u0080\uffff\u092e"+
            "\11\u10d2\uffff\u5200\11\u5900\uffff\u0200\11",
            "",
            "",
            "",
            "\1\73",
            "",
            "\1\11\1\uffff\1\11\2\uffff\1\11\2\uffff\1\11\1\uffff\1\11"+
            "\1\uffff\1\11\1\uffff\12\11\3\uffff\1\11\3\uffff\32\11\4\uffff"+
            "\1\11\1\uffff\32\11\105\uffff\27\11\1\uffff\37\11\1\uffff\u1f08"+
            "\11\u1040\uffff\u0150\11\u0170\uffff\u0080\11\u0080\uffff\u092e"+
            "\11\u10d2\uffff\u5200\11\u5900\uffff\u0200\11",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | Identifier | WS | COMMENT | LINE_COMMENT | Block );";
        }
    }
 

}