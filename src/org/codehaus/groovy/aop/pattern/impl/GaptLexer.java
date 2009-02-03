// $ANTLR 3.0.1 C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g 2008-04-10 03:54:50

package org.codehaus.groovy.aop.pattern.impl;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GaptLexer extends Lexer {
    public static final int T27=27;
    public static final int T8=8;
    public static final int T26=26;
    public static final int T9=9;
    public static final int T25=25;
    public static final int Tokens=28;
    public static final int T24=24;
    public static final int EOF=-1;
    public static final int T23=23;
    public static final int T22=22;
    public static final int T21=21;
    public static final int Identifier=4;
    public static final int T20=20;
    public static final int WS=7;
    public static final int JavaIDDigit=6;
    public static final int T10=10;
    public static final int T11=11;
    public static final int T12=12;
    public static final int T13=13;
    public static final int T14=14;
    public static final int T15=15;
    public static final int T16=16;
    public static final int T17=17;
    public static final int Letter=5;
    public static final int T18=18;
    public static final int T19=19;
    public GaptLexer() {;} 
    public GaptLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g"; }

    // $ANTLR start T8
    public final void mT8() throws RecognitionException {
        try {
            int _type = T8;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:6:4: ( '.' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:6:6: '.'
            {
            match('.'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T8

    // $ANTLR start T9
    public final void mT9() throws RecognitionException {
        try {
            int _type = T9;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:7:4: ( '+' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:7:6: '+'
            {
            match('+'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T9

    // $ANTLR start T10
    public final void mT10() throws RecognitionException {
        try {
            int _type = T10;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:8:5: ( '(' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:8:7: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T10

    // $ANTLR start T11
    public final void mT11() throws RecognitionException {
        try {
            int _type = T11;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:9:5: ( ')' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:9:7: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T11

    // $ANTLR start T12
    public final void mT12() throws RecognitionException {
        try {
            int _type = T12;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:10:5: ( ',' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:10:7: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T12

    // $ANTLR start T13
    public final void mT13() throws RecognitionException {
        try {
            int _type = T13;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:11:5: ( '*' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:11:7: '*'
            {
            match('*'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T13

    // $ANTLR start T14
    public final void mT14() throws RecognitionException {
        try {
            int _type = T14;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:12:5: ( 'public' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:12:7: 'public'
            {
            match("public"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T14

    // $ANTLR start T15
    public final void mT15() throws RecognitionException {
        try {
            int _type = T15;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:13:5: ( 'private' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:13:7: 'private'
            {
            match("private"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T15

    // $ANTLR start T16
    public final void mT16() throws RecognitionException {
        try {
            int _type = T16;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:14:5: ( 'protected' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:14:7: 'protected'
            {
            match("protected"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T16

    // $ANTLR start T17
    public final void mT17() throws RecognitionException {
        try {
            int _type = T17;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:15:5: ( 'boolean' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:15:7: 'boolean'
            {
            match("boolean"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T17

    // $ANTLR start T18
    public final void mT18() throws RecognitionException {
        try {
            int _type = T18;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:16:5: ( 'byte' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:16:7: 'byte'
            {
            match("byte"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T18

    // $ANTLR start T19
    public final void mT19() throws RecognitionException {
        try {
            int _type = T19;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:17:5: ( 'char' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:17:7: 'char'
            {
            match("char"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T19

    // $ANTLR start T20
    public final void mT20() throws RecognitionException {
        try {
            int _type = T20;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:18:5: ( 'double' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:18:7: 'double'
            {
            match("double"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T20

    // $ANTLR start T21
    public final void mT21() throws RecognitionException {
        try {
            int _type = T21;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:19:5: ( 'float' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:19:7: 'float'
            {
            match("float"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T21

    // $ANTLR start T22
    public final void mT22() throws RecognitionException {
        try {
            int _type = T22;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:20:5: ( 'int' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:20:7: 'int'
            {
            match("int"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T22

    // $ANTLR start T23
    public final void mT23() throws RecognitionException {
        try {
            int _type = T23;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:21:5: ( 'long' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:21:7: 'long'
            {
            match("long"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T23

    // $ANTLR start T24
    public final void mT24() throws RecognitionException {
        try {
            int _type = T24;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:5: ( 'short' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:7: 'short'
            {
            match("short"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T24

    // $ANTLR start T25
    public final void mT25() throws RecognitionException {
        try {
            int _type = T25;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:23:5: ( 'void' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:23:7: 'void'
            {
            match("void"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T25

    // $ANTLR start T26
    public final void mT26() throws RecognitionException {
        try {
            int _type = T26;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:5: ( '..' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:7: '..'
            {
            match(".."); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T26

    // $ANTLR start T27
    public final void mT27() throws RecognitionException {
        try {
            int _type = T27;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:25:5: ( '*.' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:25:7: '*.'
            {
            match("*."); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T27

    // $ANTLR start Identifier
    public final void mIdentifier() throws RecognitionException {
        try {
            int _type = Identifier;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:138:2: ( Letter ( Letter | JavaIDDigit )* )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:138:4: Letter ( Letter | JavaIDDigit )*
            {
            mLetter(); 
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:138:11: ( Letter | JavaIDDigit )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='$'||LA1_0=='*'||(LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')||(LA1_0>='\u00C0' && LA1_0<='\u00D6')||(LA1_0>='\u00D8' && LA1_0<='\u00F6')||(LA1_0>='\u00F8' && LA1_0<='\u1FFF')||(LA1_0>='\u3040' && LA1_0<='\u318F')||(LA1_0>='\u3300' && LA1_0<='\u337F')||(LA1_0>='\u3400' && LA1_0<='\u3D2D')||(LA1_0>='\u4E00' && LA1_0<='\u9FFF')||(LA1_0>='\uF900' && LA1_0<='\uFAFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:
            	    {
            	    if ( input.LA(1)=='$'||input.LA(1)=='*'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Identifier

    // $ANTLR start Letter
    public final void mLetter() throws RecognitionException {
        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:143:5: ( '\\u0024' | '\\u0041' .. '\\u005a' | '\\u005f' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' | '*' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:
            {
            if ( input.LA(1)=='$'||input.LA(1)=='*'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end Letter

    // $ANTLR start JavaIDDigit
    public final void mJavaIDDigit() throws RecognitionException {
        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:161:5: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09EF')||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A6F')||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u1040' && input.LA(1)<='\u1049') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end JavaIDDigit

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:178:5: ( ' ' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:178:8: ' '
            {
            match(' '); 
             channel=HIDDEN; 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    public void mTokens() throws RecognitionException {
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:8: ( T8 | T9 | T10 | T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | Identifier | WS )
        int alt2=22;
        int LA2_0 = input.LA(1);

        if ( (LA2_0=='.') ) {
            int LA2_1 = input.LA(2);

            if ( (LA2_1=='.') ) {
                alt2=19;
            }
            else {
                alt2=1;}
        }
        else if ( (LA2_0=='+') ) {
            alt2=2;
        }
        else if ( (LA2_0=='(') ) {
            alt2=3;
        }
        else if ( (LA2_0==')') ) {
            alt2=4;
        }
        else if ( (LA2_0==',') ) {
            alt2=5;
        }
        else if ( (LA2_0=='*') ) {
            int LA2_6 = input.LA(2);

            if ( (LA2_6=='.') ) {
                alt2=20;
            }
            else if ( (LA2_6=='$'||LA2_6=='*'||(LA2_6>='0' && LA2_6<='9')||(LA2_6>='A' && LA2_6<='Z')||LA2_6=='_'||(LA2_6>='a' && LA2_6<='z')||(LA2_6>='\u00C0' && LA2_6<='\u00D6')||(LA2_6>='\u00D8' && LA2_6<='\u00F6')||(LA2_6>='\u00F8' && LA2_6<='\u1FFF')||(LA2_6>='\u3040' && LA2_6<='\u318F')||(LA2_6>='\u3300' && LA2_6<='\u337F')||(LA2_6>='\u3400' && LA2_6<='\u3D2D')||(LA2_6>='\u4E00' && LA2_6<='\u9FFF')||(LA2_6>='\uF900' && LA2_6<='\uFAFF')) ) {
                alt2=21;
            }
            else {
                alt2=6;}
        }
        else if ( (LA2_0=='p') ) {
            switch ( input.LA(2) ) {
            case 'r':
                {
                switch ( input.LA(3) ) {
                case 'i':
                    {
                    int LA2_33 = input.LA(4);

                    if ( (LA2_33=='v') ) {
                        int LA2_45 = input.LA(5);

                        if ( (LA2_45=='a') ) {
                            int LA2_57 = input.LA(6);

                            if ( (LA2_57=='t') ) {
                                int LA2_68 = input.LA(7);

                                if ( (LA2_68=='e') ) {
                                    int LA2_75 = input.LA(8);

                                    if ( (LA2_75=='$'||LA2_75=='*'||(LA2_75>='0' && LA2_75<='9')||(LA2_75>='A' && LA2_75<='Z')||LA2_75=='_'||(LA2_75>='a' && LA2_75<='z')||(LA2_75>='\u00C0' && LA2_75<='\u00D6')||(LA2_75>='\u00D8' && LA2_75<='\u00F6')||(LA2_75>='\u00F8' && LA2_75<='\u1FFF')||(LA2_75>='\u3040' && LA2_75<='\u318F')||(LA2_75>='\u3300' && LA2_75<='\u337F')||(LA2_75>='\u3400' && LA2_75<='\u3D2D')||(LA2_75>='\u4E00' && LA2_75<='\u9FFF')||(LA2_75>='\uF900' && LA2_75<='\uFAFF')) ) {
                                        alt2=21;
                                    }
                                    else {
                                        alt2=8;}
                                }
                                else {
                                    alt2=21;}
                            }
                            else {
                                alt2=21;}
                        }
                        else {
                            alt2=21;}
                    }
                    else {
                        alt2=21;}
                    }
                    break;
                case 'o':
                    {
                    int LA2_34 = input.LA(4);

                    if ( (LA2_34=='t') ) {
                        int LA2_46 = input.LA(5);

                        if ( (LA2_46=='e') ) {
                            int LA2_58 = input.LA(6);

                            if ( (LA2_58=='c') ) {
                                int LA2_69 = input.LA(7);

                                if ( (LA2_69=='t') ) {
                                    int LA2_76 = input.LA(8);

                                    if ( (LA2_76=='e') ) {
                                        int LA2_81 = input.LA(9);

                                        if ( (LA2_81=='d') ) {
                                            int LA2_83 = input.LA(10);

                                            if ( (LA2_83=='$'||LA2_83=='*'||(LA2_83>='0' && LA2_83<='9')||(LA2_83>='A' && LA2_83<='Z')||LA2_83=='_'||(LA2_83>='a' && LA2_83<='z')||(LA2_83>='\u00C0' && LA2_83<='\u00D6')||(LA2_83>='\u00D8' && LA2_83<='\u00F6')||(LA2_83>='\u00F8' && LA2_83<='\u1FFF')||(LA2_83>='\u3040' && LA2_83<='\u318F')||(LA2_83>='\u3300' && LA2_83<='\u337F')||(LA2_83>='\u3400' && LA2_83<='\u3D2D')||(LA2_83>='\u4E00' && LA2_83<='\u9FFF')||(LA2_83>='\uF900' && LA2_83<='\uFAFF')) ) {
                                                alt2=21;
                                            }
                                            else {
                                                alt2=9;}
                                        }
                                        else {
                                            alt2=21;}
                                    }
                                    else {
                                        alt2=21;}
                                }
                                else {
                                    alt2=21;}
                            }
                            else {
                                alt2=21;}
                        }
                        else {
                            alt2=21;}
                    }
                    else {
                        alt2=21;}
                    }
                    break;
                default:
                    alt2=21;}

                }
                break;
            case 'u':
                {
                int LA2_23 = input.LA(3);

                if ( (LA2_23=='b') ) {
                    int LA2_35 = input.LA(4);

                    if ( (LA2_35=='l') ) {
                        int LA2_47 = input.LA(5);

                        if ( (LA2_47=='i') ) {
                            int LA2_59 = input.LA(6);

                            if ( (LA2_59=='c') ) {
                                int LA2_70 = input.LA(7);

                                if ( (LA2_70=='$'||LA2_70=='*'||(LA2_70>='0' && LA2_70<='9')||(LA2_70>='A' && LA2_70<='Z')||LA2_70=='_'||(LA2_70>='a' && LA2_70<='z')||(LA2_70>='\u00C0' && LA2_70<='\u00D6')||(LA2_70>='\u00D8' && LA2_70<='\u00F6')||(LA2_70>='\u00F8' && LA2_70<='\u1FFF')||(LA2_70>='\u3040' && LA2_70<='\u318F')||(LA2_70>='\u3300' && LA2_70<='\u337F')||(LA2_70>='\u3400' && LA2_70<='\u3D2D')||(LA2_70>='\u4E00' && LA2_70<='\u9FFF')||(LA2_70>='\uF900' && LA2_70<='\uFAFF')) ) {
                                    alt2=21;
                                }
                                else {
                                    alt2=7;}
                            }
                            else {
                                alt2=21;}
                        }
                        else {
                            alt2=21;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
                }
                break;
            default:
                alt2=21;}

        }
        else if ( (LA2_0=='b') ) {
            switch ( input.LA(2) ) {
            case 'o':
                {
                int LA2_24 = input.LA(3);

                if ( (LA2_24=='o') ) {
                    int LA2_36 = input.LA(4);

                    if ( (LA2_36=='l') ) {
                        int LA2_48 = input.LA(5);

                        if ( (LA2_48=='e') ) {
                            int LA2_60 = input.LA(6);

                            if ( (LA2_60=='a') ) {
                                int LA2_71 = input.LA(7);

                                if ( (LA2_71=='n') ) {
                                    int LA2_78 = input.LA(8);

                                    if ( (LA2_78=='$'||LA2_78=='*'||(LA2_78>='0' && LA2_78<='9')||(LA2_78>='A' && LA2_78<='Z')||LA2_78=='_'||(LA2_78>='a' && LA2_78<='z')||(LA2_78>='\u00C0' && LA2_78<='\u00D6')||(LA2_78>='\u00D8' && LA2_78<='\u00F6')||(LA2_78>='\u00F8' && LA2_78<='\u1FFF')||(LA2_78>='\u3040' && LA2_78<='\u318F')||(LA2_78>='\u3300' && LA2_78<='\u337F')||(LA2_78>='\u3400' && LA2_78<='\u3D2D')||(LA2_78>='\u4E00' && LA2_78<='\u9FFF')||(LA2_78>='\uF900' && LA2_78<='\uFAFF')) ) {
                                        alt2=21;
                                    }
                                    else {
                                        alt2=10;}
                                }
                                else {
                                    alt2=21;}
                            }
                            else {
                                alt2=21;}
                        }
                        else {
                            alt2=21;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
                }
                break;
            case 'y':
                {
                int LA2_25 = input.LA(3);

                if ( (LA2_25=='t') ) {
                    int LA2_37 = input.LA(4);

                    if ( (LA2_37=='e') ) {
                        int LA2_49 = input.LA(5);

                        if ( (LA2_49=='$'||LA2_49=='*'||(LA2_49>='0' && LA2_49<='9')||(LA2_49>='A' && LA2_49<='Z')||LA2_49=='_'||(LA2_49>='a' && LA2_49<='z')||(LA2_49>='\u00C0' && LA2_49<='\u00D6')||(LA2_49>='\u00D8' && LA2_49<='\u00F6')||(LA2_49>='\u00F8' && LA2_49<='\u1FFF')||(LA2_49>='\u3040' && LA2_49<='\u318F')||(LA2_49>='\u3300' && LA2_49<='\u337F')||(LA2_49>='\u3400' && LA2_49<='\u3D2D')||(LA2_49>='\u4E00' && LA2_49<='\u9FFF')||(LA2_49>='\uF900' && LA2_49<='\uFAFF')) ) {
                            alt2=21;
                        }
                        else {
                            alt2=11;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
                }
                break;
            default:
                alt2=21;}

        }
        else if ( (LA2_0=='c') ) {
            int LA2_9 = input.LA(2);

            if ( (LA2_9=='h') ) {
                int LA2_26 = input.LA(3);

                if ( (LA2_26=='a') ) {
                    int LA2_38 = input.LA(4);

                    if ( (LA2_38=='r') ) {
                        int LA2_50 = input.LA(5);

                        if ( (LA2_50=='$'||LA2_50=='*'||(LA2_50>='0' && LA2_50<='9')||(LA2_50>='A' && LA2_50<='Z')||LA2_50=='_'||(LA2_50>='a' && LA2_50<='z')||(LA2_50>='\u00C0' && LA2_50<='\u00D6')||(LA2_50>='\u00D8' && LA2_50<='\u00F6')||(LA2_50>='\u00F8' && LA2_50<='\u1FFF')||(LA2_50>='\u3040' && LA2_50<='\u318F')||(LA2_50>='\u3300' && LA2_50<='\u337F')||(LA2_50>='\u3400' && LA2_50<='\u3D2D')||(LA2_50>='\u4E00' && LA2_50<='\u9FFF')||(LA2_50>='\uF900' && LA2_50<='\uFAFF')) ) {
                            alt2=21;
                        }
                        else {
                            alt2=12;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
            }
            else {
                alt2=21;}
        }
        else if ( (LA2_0=='d') ) {
            int LA2_10 = input.LA(2);

            if ( (LA2_10=='o') ) {
                int LA2_27 = input.LA(3);

                if ( (LA2_27=='u') ) {
                    int LA2_39 = input.LA(4);

                    if ( (LA2_39=='b') ) {
                        int LA2_51 = input.LA(5);

                        if ( (LA2_51=='l') ) {
                            int LA2_63 = input.LA(6);

                            if ( (LA2_63=='e') ) {
                                int LA2_72 = input.LA(7);

                                if ( (LA2_72=='$'||LA2_72=='*'||(LA2_72>='0' && LA2_72<='9')||(LA2_72>='A' && LA2_72<='Z')||LA2_72=='_'||(LA2_72>='a' && LA2_72<='z')||(LA2_72>='\u00C0' && LA2_72<='\u00D6')||(LA2_72>='\u00D8' && LA2_72<='\u00F6')||(LA2_72>='\u00F8' && LA2_72<='\u1FFF')||(LA2_72>='\u3040' && LA2_72<='\u318F')||(LA2_72>='\u3300' && LA2_72<='\u337F')||(LA2_72>='\u3400' && LA2_72<='\u3D2D')||(LA2_72>='\u4E00' && LA2_72<='\u9FFF')||(LA2_72>='\uF900' && LA2_72<='\uFAFF')) ) {
                                    alt2=21;
                                }
                                else {
                                    alt2=13;}
                            }
                            else {
                                alt2=21;}
                        }
                        else {
                            alt2=21;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
            }
            else {
                alt2=21;}
        }
        else if ( (LA2_0=='f') ) {
            int LA2_11 = input.LA(2);

            if ( (LA2_11=='l') ) {
                int LA2_28 = input.LA(3);

                if ( (LA2_28=='o') ) {
                    int LA2_40 = input.LA(4);

                    if ( (LA2_40=='a') ) {
                        int LA2_52 = input.LA(5);

                        if ( (LA2_52=='t') ) {
                            int LA2_64 = input.LA(6);

                            if ( (LA2_64=='$'||LA2_64=='*'||(LA2_64>='0' && LA2_64<='9')||(LA2_64>='A' && LA2_64<='Z')||LA2_64=='_'||(LA2_64>='a' && LA2_64<='z')||(LA2_64>='\u00C0' && LA2_64<='\u00D6')||(LA2_64>='\u00D8' && LA2_64<='\u00F6')||(LA2_64>='\u00F8' && LA2_64<='\u1FFF')||(LA2_64>='\u3040' && LA2_64<='\u318F')||(LA2_64>='\u3300' && LA2_64<='\u337F')||(LA2_64>='\u3400' && LA2_64<='\u3D2D')||(LA2_64>='\u4E00' && LA2_64<='\u9FFF')||(LA2_64>='\uF900' && LA2_64<='\uFAFF')) ) {
                                alt2=21;
                            }
                            else {
                                alt2=14;}
                        }
                        else {
                            alt2=21;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
            }
            else {
                alt2=21;}
        }
        else if ( (LA2_0=='i') ) {
            int LA2_12 = input.LA(2);

            if ( (LA2_12=='n') ) {
                int LA2_29 = input.LA(3);

                if ( (LA2_29=='t') ) {
                    int LA2_41 = input.LA(4);

                    if ( (LA2_41=='$'||LA2_41=='*'||(LA2_41>='0' && LA2_41<='9')||(LA2_41>='A' && LA2_41<='Z')||LA2_41=='_'||(LA2_41>='a' && LA2_41<='z')||(LA2_41>='\u00C0' && LA2_41<='\u00D6')||(LA2_41>='\u00D8' && LA2_41<='\u00F6')||(LA2_41>='\u00F8' && LA2_41<='\u1FFF')||(LA2_41>='\u3040' && LA2_41<='\u318F')||(LA2_41>='\u3300' && LA2_41<='\u337F')||(LA2_41>='\u3400' && LA2_41<='\u3D2D')||(LA2_41>='\u4E00' && LA2_41<='\u9FFF')||(LA2_41>='\uF900' && LA2_41<='\uFAFF')) ) {
                        alt2=21;
                    }
                    else {
                        alt2=15;}
                }
                else {
                    alt2=21;}
            }
            else {
                alt2=21;}
        }
        else if ( (LA2_0=='l') ) {
            int LA2_13 = input.LA(2);

            if ( (LA2_13=='o') ) {
                int LA2_30 = input.LA(3);

                if ( (LA2_30=='n') ) {
                    int LA2_42 = input.LA(4);

                    if ( (LA2_42=='g') ) {
                        int LA2_54 = input.LA(5);

                        if ( (LA2_54=='$'||LA2_54=='*'||(LA2_54>='0' && LA2_54<='9')||(LA2_54>='A' && LA2_54<='Z')||LA2_54=='_'||(LA2_54>='a' && LA2_54<='z')||(LA2_54>='\u00C0' && LA2_54<='\u00D6')||(LA2_54>='\u00D8' && LA2_54<='\u00F6')||(LA2_54>='\u00F8' && LA2_54<='\u1FFF')||(LA2_54>='\u3040' && LA2_54<='\u318F')||(LA2_54>='\u3300' && LA2_54<='\u337F')||(LA2_54>='\u3400' && LA2_54<='\u3D2D')||(LA2_54>='\u4E00' && LA2_54<='\u9FFF')||(LA2_54>='\uF900' && LA2_54<='\uFAFF')) ) {
                            alt2=21;
                        }
                        else {
                            alt2=16;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
            }
            else {
                alt2=21;}
        }
        else if ( (LA2_0=='s') ) {
            int LA2_14 = input.LA(2);

            if ( (LA2_14=='h') ) {
                int LA2_31 = input.LA(3);

                if ( (LA2_31=='o') ) {
                    int LA2_43 = input.LA(4);

                    if ( (LA2_43=='r') ) {
                        int LA2_55 = input.LA(5);

                        if ( (LA2_55=='t') ) {
                            int LA2_66 = input.LA(6);

                            if ( (LA2_66=='$'||LA2_66=='*'||(LA2_66>='0' && LA2_66<='9')||(LA2_66>='A' && LA2_66<='Z')||LA2_66=='_'||(LA2_66>='a' && LA2_66<='z')||(LA2_66>='\u00C0' && LA2_66<='\u00D6')||(LA2_66>='\u00D8' && LA2_66<='\u00F6')||(LA2_66>='\u00F8' && LA2_66<='\u1FFF')||(LA2_66>='\u3040' && LA2_66<='\u318F')||(LA2_66>='\u3300' && LA2_66<='\u337F')||(LA2_66>='\u3400' && LA2_66<='\u3D2D')||(LA2_66>='\u4E00' && LA2_66<='\u9FFF')||(LA2_66>='\uF900' && LA2_66<='\uFAFF')) ) {
                                alt2=21;
                            }
                            else {
                                alt2=17;}
                        }
                        else {
                            alt2=21;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
            }
            else {
                alt2=21;}
        }
        else if ( (LA2_0=='v') ) {
            int LA2_15 = input.LA(2);

            if ( (LA2_15=='o') ) {
                int LA2_32 = input.LA(3);

                if ( (LA2_32=='i') ) {
                    int LA2_44 = input.LA(4);

                    if ( (LA2_44=='d') ) {
                        int LA2_56 = input.LA(5);

                        if ( (LA2_56=='$'||LA2_56=='*'||(LA2_56>='0' && LA2_56<='9')||(LA2_56>='A' && LA2_56<='Z')||LA2_56=='_'||(LA2_56>='a' && LA2_56<='z')||(LA2_56>='\u00C0' && LA2_56<='\u00D6')||(LA2_56>='\u00D8' && LA2_56<='\u00F6')||(LA2_56>='\u00F8' && LA2_56<='\u1FFF')||(LA2_56>='\u3040' && LA2_56<='\u318F')||(LA2_56>='\u3300' && LA2_56<='\u337F')||(LA2_56>='\u3400' && LA2_56<='\u3D2D')||(LA2_56>='\u4E00' && LA2_56<='\u9FFF')||(LA2_56>='\uF900' && LA2_56<='\uFAFF')) ) {
                            alt2=21;
                        }
                        else {
                            alt2=18;}
                    }
                    else {
                        alt2=21;}
                }
                else {
                    alt2=21;}
            }
            else {
                alt2=21;}
        }
        else if ( (LA2_0=='$'||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||LA2_0=='a'||LA2_0=='e'||(LA2_0>='g' && LA2_0<='h')||(LA2_0>='j' && LA2_0<='k')||(LA2_0>='m' && LA2_0<='o')||(LA2_0>='q' && LA2_0<='r')||(LA2_0>='t' && LA2_0<='u')||(LA2_0>='w' && LA2_0<='z')||(LA2_0>='\u00C0' && LA2_0<='\u00D6')||(LA2_0>='\u00D8' && LA2_0<='\u00F6')||(LA2_0>='\u00F8' && LA2_0<='\u1FFF')||(LA2_0>='\u3040' && LA2_0<='\u318F')||(LA2_0>='\u3300' && LA2_0<='\u337F')||(LA2_0>='\u3400' && LA2_0<='\u3D2D')||(LA2_0>='\u4E00' && LA2_0<='\u9FFF')||(LA2_0>='\uF900' && LA2_0<='\uFAFF')) ) {
            alt2=21;
        }
        else if ( (LA2_0==' ') ) {
            alt2=22;
        }
        else {
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( T8 | T9 | T10 | T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | Identifier | WS );", 2, 0, input);

            throw nvae;
        }
        switch (alt2) {
            case 1 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:10: T8
                {
                mT8(); 

                }
                break;
            case 2 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:13: T9
                {
                mT9(); 

                }
                break;
            case 3 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:16: T10
                {
                mT10(); 

                }
                break;
            case 4 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:20: T11
                {
                mT11(); 

                }
                break;
            case 5 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:24: T12
                {
                mT12(); 

                }
                break;
            case 6 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:28: T13
                {
                mT13(); 

                }
                break;
            case 7 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:32: T14
                {
                mT14(); 

                }
                break;
            case 8 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:36: T15
                {
                mT15(); 

                }
                break;
            case 9 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:40: T16
                {
                mT16(); 

                }
                break;
            case 10 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:44: T17
                {
                mT17(); 

                }
                break;
            case 11 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:48: T18
                {
                mT18(); 

                }
                break;
            case 12 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:52: T19
                {
                mT19(); 

                }
                break;
            case 13 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:56: T20
                {
                mT20(); 

                }
                break;
            case 14 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:60: T21
                {
                mT21(); 

                }
                break;
            case 15 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:64: T22
                {
                mT22(); 

                }
                break;
            case 16 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:68: T23
                {
                mT23(); 

                }
                break;
            case 17 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:72: T24
                {
                mT24(); 

                }
                break;
            case 18 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:76: T25
                {
                mT25(); 

                }
                break;
            case 19 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:80: T26
                {
                mT26(); 

                }
                break;
            case 20 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:84: T27
                {
                mT27(); 

                }
                break;
            case 21 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:88: Identifier
                {
                mIdentifier(); 

                }
                break;
            case 22 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:1:99: WS
                {
                mWS(); 

                }
                break;

        }

    }


 

}