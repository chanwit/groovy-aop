// $ANTLR 3.1.1 C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g 2009-02-04 01:49:46

package org.codehaus.groovy.aop.pattern.impl;

import org.codehaus.groovy.aop.pattern.Pattern;
import org.codehaus.groovy.aop.pattern.MethodPattern;
import org.codehaus.groovy.aop.pattern.TypePattern;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.runtime.debug.*;
import java.io.IOException;
public class GaptParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Identifier", "Letter", "JavaIDDigit", "WS", "'.'", "'+'", "'('", "')'", "','", "'*'", "'public'", "'private'", "'protected'", "'boolean'", "'byte'", "'char'", "'double'", "'float'", "'int'", "'long'", "'short'", "'void'", "'..'"
    };
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

    public static final String[] ruleNames = new String[] {
        "invalidRule", "synpred22_Gapt", "pattern", "synpred12_Gapt", "synpred5_Gapt", 
        "synpred9_Gapt", "synpred24_Gapt", "synpred14_Gapt", "synpred23_Gapt", 
        "synpred7_Gapt", "synpred10_Gapt", "modifier", "synpred26_Gapt", 
        "synpred6_Gapt", "synpred8_Gapt", "modifiers", "synpred11_Gapt", 
        "synpred16_Gapt", "synpred2_Gapt", "primitive", "synpred25_Gapt", 
        "synpred19_Gapt", "synpred28_Gapt", "synpred1_Gapt", "synpred27_Gapt", 
        "synpred20_Gapt", "methodPattern", "synpred18_Gapt", "synpred3_Gapt", 
        "synpred17_Gapt", "returnTypePattern", "synpred21_Gapt", "synpred13_Gapt", 
        "argTypes", "synpred4_Gapt", "qualifiedName", "classPattern", "className", 
        "synpred15_Gapt"
    };
     
        public int ruleLevel = 0;
        public int getRuleLevel() { return ruleLevel; }
        public void incRuleLevel() { ruleLevel++; }
        public void decRuleLevel() { ruleLevel--; }
        public GaptParser(TokenStream input) {
            this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
        }
        public GaptParser(TokenStream input, int port, RecognizerSharedState state) {
            super(input, state);
            DebugEventSocketProxy proxy =
                new DebugEventSocketProxy(this, port, null);
            setDebugListener(proxy);
            try {
                proxy.handshake();
            }
            catch (IOException ioe) {
                reportError(ioe);
            }
        }
    public GaptParser(TokenStream input, DebugEventListener dbg) {
        super(input, dbg, new RecognizerSharedState());

    }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }


    public String[] getTokenNames() { return GaptParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g"; }



    // $ANTLR start "pattern"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );
    public final Pattern pattern() throws RecognitionException {
        Pattern pt = null;

        MethodPattern mep = null;

        TypePattern rtp = null;

        List<String> mod = null;



          pt = new Pattern();

        try { dbg.enterRule(getGrammarFileName(), "pattern");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(17, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:21:5: ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) )
            int alt1=3;
            try { dbg.enterDecision(1);

            try {
                isCyclicDecision = true;
                alt1 = dfa1.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:9: (mep= methodPattern )
                    {
                    dbg.location(22,9);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:9: (mep= methodPattern )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:10: mep= methodPattern
                    {
                    dbg.location(22,13);
                    pushFollow(FOLLOW_methodPattern_in_pattern65);
                    mep=methodPattern();

                    state._fsp--;
                    if (state.failed) return pt;
                    dbg.location(22,32);
                    if ( state.backtracking==0 ) {
                       pt.setMethodPattern(mep); 
                    }

                    }


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:7: (rtp= returnTypePattern ) (mep= methodPattern )
                    {
                    dbg.location(24,7);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:7: (rtp= returnTypePattern )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:8: rtp= returnTypePattern
                    {
                    dbg.location(24,11);
                    pushFollow(FOLLOW_returnTypePattern_in_pattern92);
                    rtp=returnTypePattern();

                    state._fsp--;
                    if (state.failed) return pt;
                    dbg.location(24,30);
                    if ( state.backtracking==0 ) {
                       pt.setReturnTypePattern(rtp); 
                    }

                    }

                    dbg.location(25,9);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:25:9: (mep= methodPattern )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:25:10: mep= methodPattern
                    {
                    dbg.location(25,13);
                    pushFollow(FOLLOW_methodPattern_in_pattern108);
                    mep=methodPattern();

                    state._fsp--;
                    if (state.failed) return pt;
                    dbg.location(25,32);
                    if ( state.backtracking==0 ) {
                       pt.setMethodPattern(mep); 
                    }

                    }


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:27:7: (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern )
                    {
                    dbg.location(27,7);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:27:7: (mod= modifiers )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:27:8: mod= modifiers
                    {
                    dbg.location(27,11);
                    pushFollow(FOLLOW_modifiers_in_pattern128);
                    mod=modifiers();

                    state._fsp--;
                    if (state.failed) return pt;
                    dbg.location(27,30);
                    if ( state.backtracking==0 ) {
                       pt.setModifiers(mod); 
                    }

                    }

                    dbg.location(28,9);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:28:9: (rtp= returnTypePattern )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:28:10: rtp= returnTypePattern
                    {
                    dbg.location(28,13);
                    pushFollow(FOLLOW_returnTypePattern_in_pattern153);
                    rtp=returnTypePattern();

                    state._fsp--;
                    if (state.failed) return pt;
                    dbg.location(28,32);
                    if ( state.backtracking==0 ) {
                       pt.setReturnTypePattern(rtp); 
                    }

                    }

                    dbg.location(29,9);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:29:9: (mep= methodPattern )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:29:10: mep= methodPattern
                    {
                    dbg.location(29,13);
                    pushFollow(FOLLOW_methodPattern_in_pattern169);
                    mep=methodPattern();

                    state._fsp--;
                    if (state.failed) return pt;
                    dbg.location(29,32);
                    if ( state.backtracking==0 ) {
                       pt.setMethodPattern(mep); 
                    }

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(30, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "pattern");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return pt;
    }
    // $ANTLR end "pattern"


    // $ANTLR start "methodPattern"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:33:1: methodPattern returns [MethodPattern mep] : ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? ) ;
    public final MethodPattern methodPattern() throws RecognitionException {
        MethodPattern mep = null;

        Token i=null;
        List<TypePattern> a = null;



          mep = new MethodPattern();
          List<String> names = new ArrayList<String>();
          boolean subClass = false;
          boolean mayBeProperty = true;

        try { dbg.enterRule(getGrammarFileName(), "methodPattern");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(33, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:40:5: ( ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? ) )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:41:5: ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? )
            {
            dbg.location(41,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:41:5: ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? )
            int alt9=2;
            try { dbg.enterSubRule(9);
            try { dbg.enterDecision(9);

            int LA9_0 = input.LA(1);

            if ( (LA9_0==Identifier) ) {
                switch ( input.LA(2) ) {
                case 8:
                    {
                    int LA9_2 = input.LA(3);

                    if ( (synpred7_Gapt()) ) {
                        alt9=1;
                    }
                    else if ( (true) ) {
                        alt9=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return mep;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 2, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                case 10:
                    {
                    alt9=2;
                    }
                    break;
                case 9:
                    {
                    alt9=1;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return mep;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return mep;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(9);}

            switch (alt9) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:42:9: (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )?
                    {
                    dbg.location(42,9);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:42:9: (i= Identifier )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:42:10: i= Identifier
                    {
                    dbg.location(42,11);
                    i=(Token)match(input,Identifier,FOLLOW_Identifier_in_methodPattern221); if (state.failed) return mep;
                    dbg.location(42,22);
                    if ( state.backtracking==0 ) {
                      names.add((i!=null?i.getText():null));
                    }

                    }

                    dbg.location(43,13);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:43:13: ( '.' i= Identifier )*
                    try { dbg.enterSubRule(2);

                    loop2:
                    do {
                        int alt2=2;
                        try { dbg.enterDecision(2);

                        int LA2_0 = input.LA(1);

                        if ( (LA2_0==8) ) {
                            int LA2_2 = input.LA(2);

                            if ( (LA2_2==Identifier) ) {
                                int LA2_3 = input.LA(3);

                                if ( (synpred3_Gapt()) ) {
                                    alt2=1;
                                }


                            }


                        }


                        } finally {dbg.exitDecision(2);}

                        switch (alt2) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:43:14: '.' i= Identifier
                    	    {
                    	    dbg.location(43,14);
                    	    match(input,8,FOLLOW_8_in_methodPattern238); if (state.failed) return mep;
                    	    dbg.location(43,19);
                    	    i=(Token)match(input,Identifier,FOLLOW_Identifier_in_methodPattern242); if (state.failed) return mep;
                    	    dbg.location(43,30);
                    	    if ( state.backtracking==0 ) {
                    	      names.add((i!=null?i.getText():null));
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(2);}

                    dbg.location(44,13);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:44:13: ( '+' )?
                    int alt3=2;
                    try { dbg.enterSubRule(3);
                    try { dbg.enterDecision(3);

                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==9) ) {
                        alt3=1;
                    }
                    } finally {dbg.exitDecision(3);}

                    switch (alt3) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:44:14: '+'
                            {
                            dbg.location(44,14);
                            match(input,9,FOLLOW_9_in_methodPattern260); if (state.failed) return mep;
                            dbg.location(44,18);
                            if ( state.backtracking==0 ) {
                               subClass = true; 
                            }

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(3);}

                    dbg.location(45,13);
                    match(input,8,FOLLOW_8_in_methodPattern278); if (state.failed) return mep;
                    dbg.location(46,13);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:46:13: (i= Identifier )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:46:14: i= Identifier
                    {
                    dbg.location(46,15);
                    i=(Token)match(input,Identifier,FOLLOW_Identifier_in_methodPattern295); if (state.failed) return mep;
                    dbg.location(46,26);
                    if ( state.backtracking==0 ) {
                      names.add((i!=null?i.getText():null));
                    }

                    }

                    dbg.location(47,13);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:13: ( '(' (a= argTypes )? ')' )?
                    int alt5=2;
                    try { dbg.enterSubRule(5);
                    try { dbg.enterDecision(5);

                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==10) ) {
                        alt5=1;
                    }
                    } finally {dbg.exitDecision(5);}

                    switch (alt5) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:14: '(' (a= argTypes )? ')'
                            {
                            dbg.location(47,14);
                            match(input,10,FOLLOW_10_in_methodPattern312); if (state.failed) return mep;
                            dbg.location(47,18);
                            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:18: (a= argTypes )?
                            int alt4=2;
                            try { dbg.enterSubRule(4);
                            try { dbg.enterDecision(4);

                            int LA4_0 = input.LA(1);

                            if ( (LA4_0==Identifier||LA4_0==13||(LA4_0>=17 && LA4_0<=26)) ) {
                                alt4=1;
                            }
                            } finally {dbg.exitDecision(4);}

                            switch (alt4) {
                                case 1 :
                                    dbg.enterAlt(1);

                                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:19: a= argTypes
                                    {
                                    dbg.location(47,20);
                                    pushFollow(FOLLOW_argTypes_in_methodPattern317);
                                    a=argTypes();

                                    state._fsp--;
                                    if (state.failed) return mep;
                                    dbg.location(47,30);
                                    if ( state.backtracking==0 ) {
                                      mep.setArgTypePatterns(a);
                                    }

                                    }
                                    break;

                            }
                            } finally {dbg.exitSubRule(4);}

                            dbg.location(47,62);
                            match(input,11,FOLLOW_11_in_methodPattern324); if (state.failed) return mep;
                            dbg.location(47,66);
                            if ( state.backtracking==0 ) {
                              mayBeProperty=false;
                            }

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(5);}


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:49:9: (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )?
                    {
                    dbg.location(49,9);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:49:9: (i= Identifier )
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:49:10: i= Identifier
                    {
                    dbg.location(49,11);
                    i=(Token)match(input,Identifier,FOLLOW_Identifier_in_methodPattern347); if (state.failed) return mep;
                    dbg.location(49,22);
                    if ( state.backtracking==0 ) {
                      names.add((i!=null?i.getText():null));
                    }

                    }

                    dbg.location(50,13);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:50:13: ( '.' i= Identifier )*
                    try { dbg.enterSubRule(6);

                    loop6:
                    do {
                        int alt6=2;
                        try { dbg.enterDecision(6);

                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==8) ) {
                            alt6=1;
                        }


                        } finally {dbg.exitDecision(6);}

                        switch (alt6) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:50:14: '.' i= Identifier
                    	    {
                    	    dbg.location(50,14);
                    	    match(input,8,FOLLOW_8_in_methodPattern364); if (state.failed) return mep;
                    	    dbg.location(50,19);
                    	    i=(Token)match(input,Identifier,FOLLOW_Identifier_in_methodPattern368); if (state.failed) return mep;
                    	    dbg.location(50,30);
                    	    if ( state.backtracking==0 ) {
                    	      names.add((i!=null?i.getText():null));
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(6);}

                    dbg.location(51,13);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:51:13: ( '(' (a= argTypes )? ')' )?
                    int alt8=2;
                    try { dbg.enterSubRule(8);
                    try { dbg.enterDecision(8);

                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==10) ) {
                        alt8=1;
                    }
                    } finally {dbg.exitDecision(8);}

                    switch (alt8) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:51:14: '(' (a= argTypes )? ')'
                            {
                            dbg.location(51,14);
                            match(input,10,FOLLOW_10_in_methodPattern386); if (state.failed) return mep;
                            dbg.location(51,18);
                            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:51:18: (a= argTypes )?
                            int alt7=2;
                            try { dbg.enterSubRule(7);
                            try { dbg.enterDecision(7);

                            int LA7_0 = input.LA(1);

                            if ( (LA7_0==Identifier||LA7_0==13||(LA7_0>=17 && LA7_0<=26)) ) {
                                alt7=1;
                            }
                            } finally {dbg.exitDecision(7);}

                            switch (alt7) {
                                case 1 :
                                    dbg.enterAlt(1);

                                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:51:19: a= argTypes
                                    {
                                    dbg.location(51,20);
                                    pushFollow(FOLLOW_argTypes_in_methodPattern391);
                                    a=argTypes();

                                    state._fsp--;
                                    if (state.failed) return mep;
                                    dbg.location(51,30);
                                    if ( state.backtracking==0 ) {
                                      mep.setArgTypePatterns(a);
                                    }

                                    }
                                    break;

                            }
                            } finally {dbg.exitSubRule(7);}

                            dbg.location(51,62);
                            match(input,11,FOLLOW_11_in_methodPattern398); if (state.failed) return mep;
                            dbg.location(51,66);
                            if ( state.backtracking==0 ) {
                              mayBeProperty=false;
                            }

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(8);}


                    }
                    break;

            }
            } finally {dbg.exitSubRule(9);}

            dbg.location(53,1);
            if ( state.backtracking==0 ) {

                String s[] = names.toArray(new String[names.size()]);
                TypePattern t = new TypePattern(s);
                t.setSubClass(subClass);
                mep.setTypePattern(t);
                mep.setNamePattern(s[s.length-1]);
                mep.setMayBeProperty(mayBeProperty);

            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(61, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "methodPattern");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return mep;
    }
    // $ANTLR end "methodPattern"


    // $ANTLR start "argTypes"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:63:1: argTypes returns [List<TypePattern> pts] : c= classPattern ( ',' c= classPattern )* ;
    public final List<TypePattern> argTypes() throws RecognitionException {
        List<TypePattern> pts = null;

        TypePattern c = null;



          pts = new ArrayList<TypePattern>();

        try { dbg.enterRule(getGrammarFileName(), "argTypes");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(63, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:67:5: (c= classPattern ( ',' c= classPattern )* )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:67:12: c= classPattern ( ',' c= classPattern )*
            {
            dbg.location(67,13);
            pushFollow(FOLLOW_classPattern_in_argTypes442);
            c=classPattern();

            state._fsp--;
            if (state.failed) return pts;
            dbg.location(67,28);
            if ( state.backtracking==0 ) {
              pts.add(c); 
            }
            dbg.location(68,13);
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:68:13: ( ',' c= classPattern )*
            try { dbg.enterSubRule(10);

            loop10:
            do {
                int alt10=2;
                try { dbg.enterDecision(10);

                int LA10_0 = input.LA(1);

                if ( (LA10_0==12) ) {
                    alt10=1;
                }


                } finally {dbg.exitDecision(10);}

                switch (alt10) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:68:14: ',' c= classPattern
            	    {
            	    dbg.location(68,14);
            	    match(input,12,FOLLOW_12_in_argTypes460); if (state.failed) return pts;
            	    dbg.location(68,19);
            	    pushFollow(FOLLOW_classPattern_in_argTypes464);
            	    c=classPattern();

            	    state._fsp--;
            	    if (state.failed) return pts;
            	    dbg.location(68,34);
            	    if ( state.backtracking==0 ) {
            	      pts.add(c); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);
            } finally {dbg.exitSubRule(10);}


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(69, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "argTypes");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return pts;
    }
    // $ANTLR end "argTypes"


    // $ANTLR start "returnTypePattern"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:71:1: returnTypePattern returns [TypePattern tp] : c= classPattern ;
    public final TypePattern returnTypePattern() throws RecognitionException {
        TypePattern tp = null;

        TypePattern c = null;


        try { dbg.enterRule(getGrammarFileName(), "returnTypePattern");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(71, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:72:5: (c= classPattern )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:72:7: c= classPattern
            {
            dbg.location(72,8);
            pushFollow(FOLLOW_classPattern_in_returnTypePattern493);
            c=classPattern();

            state._fsp--;
            if (state.failed) return tp;
            dbg.location(73,1);
            if ( state.backtracking==0 ) {

                tp = c;

            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(76, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "returnTypePattern");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return tp;
    }
    // $ANTLR end "returnTypePattern"


    // $ANTLR start "classPattern"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:78:1: classPattern returns [TypePattern tp] : (t= primitive | t= qualifiedName ( '+' )? ) ;
    public final TypePattern classPattern() throws RecognitionException {
        TypePattern tp = null;

        TypePattern t = null;


        try { dbg.enterRule(getGrammarFileName(), "classPattern");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(78, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:79:5: ( (t= primitive | t= qualifiedName ( '+' )? ) )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:80:5: (t= primitive | t= qualifiedName ( '+' )? )
            {
            dbg.location(80,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:80:5: (t= primitive | t= qualifiedName ( '+' )? )
            int alt12=2;
            try { dbg.enterSubRule(12);
            try { dbg.enterDecision(12);

            int LA12_0 = input.LA(1);

            if ( (LA12_0==13||(LA12_0>=17 && LA12_0<=26)) ) {
                alt12=1;
            }
            else if ( (LA12_0==Identifier) ) {
                alt12=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return tp;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(12);}

            switch (alt12) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:80:7: t= primitive
                    {
                    dbg.location(80,8);
                    pushFollow(FOLLOW_primitive_in_classPattern524);
                    t=primitive();

                    state._fsp--;
                    if (state.failed) return tp;

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:81:7: t= qualifiedName ( '+' )?
                    {
                    dbg.location(81,8);
                    pushFollow(FOLLOW_qualifiedName_in_classPattern534);
                    t=qualifiedName();

                    state._fsp--;
                    if (state.failed) return tp;
                    dbg.location(81,23);
                    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:81:23: ( '+' )?
                    int alt11=2;
                    try { dbg.enterSubRule(11);
                    try { dbg.enterDecision(11);

                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==9) ) {
                        alt11=1;
                    }
                    } finally {dbg.exitDecision(11);}

                    switch (alt11) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:81:24: '+'
                            {
                            dbg.location(81,24);
                            match(input,9,FOLLOW_9_in_classPattern537); if (state.failed) return tp;
                            dbg.location(81,28);
                            if ( state.backtracking==0 ) {
                               t.setSubClass(true); 
                            }

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(11);}


                    }
                    break;

            }
            } finally {dbg.exitSubRule(12);}

            dbg.location(83,1);
            if ( state.backtracking==0 ) {

                tp = t;

            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(86, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "classPattern");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return tp;
    }
    // $ANTLR end "classPattern"


    // $ANTLR start "qualifiedName"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:88:1: qualifiedName returns [TypePattern tp] : (c= className ) ;
    public final TypePattern qualifiedName() throws RecognitionException {
        TypePattern tp = null;

        GaptParser.className_return c = null;



          tp = new TypePattern();

        try { dbg.enterRule(getGrammarFileName(), "qualifiedName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(88, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:92:5: ( (c= className ) )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:92:7: (c= className )
            {
            dbg.location(92,7);
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:92:7: (c= className )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:92:8: c= className
            {
            dbg.location(92,9);
            pushFollow(FOLLOW_className_in_qualifiedName578);
            c=className();

            state._fsp--;
            if (state.failed) return tp;
            dbg.location(92,27);
            if ( state.backtracking==0 ) {
               tp.setClassPattern((c!=null?input.toString(c.start,c.stop):null));   
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(93, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "qualifiedName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return tp;
    }
    // $ANTLR end "qualifiedName"

    public static class className_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "className"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:95:1: className : Identifier ( '.' Identifier )* ;
    public final GaptParser.className_return className() throws RecognitionException {
        GaptParser.className_return retval = new GaptParser.className_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "className");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(95, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:96:5: ( Identifier ( '.' Identifier )* )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:96:7: Identifier ( '.' Identifier )*
            {
            dbg.location(96,7);
            match(input,Identifier,FOLLOW_Identifier_in_className605); if (state.failed) return retval;
            dbg.location(96,18);
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:96:18: ( '.' Identifier )*
            try { dbg.enterSubRule(13);

            loop13:
            do {
                int alt13=2;
                try { dbg.enterDecision(13);

                int LA13_0 = input.LA(1);

                if ( (LA13_0==8) ) {
                    alt13=1;
                }


                } finally {dbg.exitDecision(13);}

                switch (alt13) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:96:19: '.' Identifier
            	    {
            	    dbg.location(96,19);
            	    match(input,8,FOLLOW_8_in_className608); if (state.failed) return retval;
            	    dbg.location(96,23);
            	    match(input,Identifier,FOLLOW_Identifier_in_className610); if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);
            } finally {dbg.exitSubRule(13);}


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(97, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "className");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "className"


    // $ANTLR start "modifiers"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:99:1: modifiers returns [List<String> ms] : (m= modifier )* ;
    public final List<String> modifiers() throws RecognitionException {
        List<String> ms = null;

        GaptParser.modifier_return m = null;



          ms = new ArrayList<String>();

        try { dbg.enterRule(getGrammarFileName(), "modifiers");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(99, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:103:5: ( (m= modifier )* )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:103:7: (m= modifier )*
            {
            dbg.location(103,7);
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:103:7: (m= modifier )*
            try { dbg.enterSubRule(14);

            loop14:
            do {
                int alt14=2;
                try { dbg.enterDecision(14);

                int LA14_0 = input.LA(1);

                if ( (LA14_0==13) ) {
                    int LA14_1 = input.LA(2);

                    if ( ((LA14_1>=13 && LA14_1<=26)) ) {
                        alt14=1;
                    }
                    else if ( (LA14_1==Identifier) ) {
                        int LA14_6 = input.LA(3);

                        if ( (synpred15_Gapt()) ) {
                            alt14=1;
                        }


                    }


                }
                else if ( ((LA14_0>=14 && LA14_0<=16)) ) {
                    alt14=1;
                }


                } finally {dbg.exitDecision(14);}

                switch (alt14) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:103:8: m= modifier
            	    {
            	    dbg.location(103,9);
            	    pushFollow(FOLLOW_modifier_in_modifiers640);
            	    m=modifier();

            	    state._fsp--;
            	    if (state.failed) return ms;
            	    dbg.location(103,19);
            	    if ( state.backtracking==0 ) {
            	      ms.add((m!=null?input.toString(m.start,m.stop):null)); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);
            } finally {dbg.exitSubRule(14);}


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(104, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "modifiers");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return ms;
    }
    // $ANTLR end "modifiers"

    public static class modifier_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "modifier"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:106:1: modifier : ( '*' | 'public' | 'private' | 'protected' );
    public final GaptParser.modifier_return modifier() throws RecognitionException {
        GaptParser.modifier_return retval = new GaptParser.modifier_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "modifier");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(106, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:107:5: ( '*' | 'public' | 'private' | 'protected' )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:
            {
            dbg.location(107,5);
            if ( (input.LA(1)>=13 && input.LA(1)<=16) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(112, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "modifier");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "modifier"


    // $ANTLR start "primitive"
    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:114:1: primitive returns [TypePattern tp] : t= ( '*' | 'boolean' | 'byte' | 'char' | 'double' | 'float' | 'int' | 'long' | 'short' | 'void' | '..' ) ;
    public final TypePattern primitive() throws RecognitionException {
        TypePattern tp = null;

        Token t=null;


          tp = new TypePattern();

        try { dbg.enterRule(getGrammarFileName(), "primitive");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(114, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:118:5: (t= ( '*' | 'boolean' | 'byte' | 'char' | 'double' | 'float' | 'int' | 'long' | 'short' | 'void' | '..' ) )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:118:7: t= ( '*' | 'boolean' | 'byte' | 'char' | 'double' | 'float' | 'int' | 'long' | 'short' | 'void' | '..' )
            {
            dbg.location(118,8);
            t=(Token)input.LT(1);
            if ( input.LA(1)==13||(input.LA(1)>=17 && input.LA(1)<=26) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return tp;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }

            dbg.location(130,1);
            if ( state.backtracking==0 ) {

                tp.setPrimitive(true);
                tp.setClassPattern((t!=null?t.getText():null));

            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(134, 5);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "primitive");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return tp;
    }
    // $ANTLR end "primitive"

    // $ANTLR start synpred1_Gapt
    public final void synpred1_Gapt_fragment() throws RecognitionException {   
        MethodPattern mep = null;


        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:9: ( (mep= methodPattern ) )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:9: (mep= methodPattern )
        {
        dbg.location(22,9);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:9: (mep= methodPattern )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:22:10: mep= methodPattern
        {
        dbg.location(22,13);
        pushFollow(FOLLOW_methodPattern_in_synpred1_Gapt65);
        mep=methodPattern();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred1_Gapt

    // $ANTLR start synpred2_Gapt
    public final void synpred2_Gapt_fragment() throws RecognitionException {   
        TypePattern rtp = null;

        MethodPattern mep = null;


        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:7: ( (rtp= returnTypePattern ) (mep= methodPattern ) )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:7: (rtp= returnTypePattern ) (mep= methodPattern )
        {
        dbg.location(24,7);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:7: (rtp= returnTypePattern )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:24:8: rtp= returnTypePattern
        {
        dbg.location(24,11);
        pushFollow(FOLLOW_returnTypePattern_in_synpred2_Gapt92);
        rtp=returnTypePattern();

        state._fsp--;
        if (state.failed) return ;

        }

        dbg.location(25,9);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:25:9: (mep= methodPattern )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:25:10: mep= methodPattern
        {
        dbg.location(25,13);
        pushFollow(FOLLOW_methodPattern_in_synpred2_Gapt108);
        mep=methodPattern();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred2_Gapt

    // $ANTLR start synpred3_Gapt
    public final void synpred3_Gapt_fragment() throws RecognitionException {   
        Token i=null;

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:43:14: ( '.' i= Identifier )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:43:14: '.' i= Identifier
        {
        dbg.location(43,14);
        match(input,8,FOLLOW_8_in_synpred3_Gapt238); if (state.failed) return ;
        dbg.location(43,19);
        i=(Token)match(input,Identifier,FOLLOW_Identifier_in_synpred3_Gapt242); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred3_Gapt

    // $ANTLR start synpred7_Gapt
    public final void synpred7_Gapt_fragment() throws RecognitionException {   
        Token i=null;
        List<TypePattern> a = null;


        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:42:9: ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:42:9: (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )?
        {
        dbg.location(42,9);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:42:9: (i= Identifier )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:42:10: i= Identifier
        {
        dbg.location(42,11);
        i=(Token)match(input,Identifier,FOLLOW_Identifier_in_synpred7_Gapt221); if (state.failed) return ;

        }

        dbg.location(43,13);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:43:13: ( '.' i= Identifier )*
        try { dbg.enterSubRule(16);

        loop16:
        do {
            int alt16=2;
            try { dbg.enterDecision(16);

            int LA16_0 = input.LA(1);

            if ( (LA16_0==8) ) {
                int LA16_2 = input.LA(2);

                if ( (LA16_2==Identifier) ) {
                    int LA16_3 = input.LA(3);

                    if ( (synpred3_Gapt()) ) {
                        alt16=1;
                    }


                }


            }


            } finally {dbg.exitDecision(16);}

            switch (alt16) {
        	case 1 :
        	    dbg.enterAlt(1);

        	    // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:43:14: '.' i= Identifier
        	    {
        	    dbg.location(43,14);
        	    match(input,8,FOLLOW_8_in_synpred7_Gapt238); if (state.failed) return ;
        	    dbg.location(43,19);
        	    i=(Token)match(input,Identifier,FOLLOW_Identifier_in_synpred7_Gapt242); if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop16;
            }
        } while (true);
        } finally {dbg.exitSubRule(16);}

        dbg.location(44,13);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:44:13: ( '+' )?
        int alt17=2;
        try { dbg.enterSubRule(17);
        try { dbg.enterDecision(17);

        int LA17_0 = input.LA(1);

        if ( (LA17_0==9) ) {
            alt17=1;
        }
        } finally {dbg.exitDecision(17);}

        switch (alt17) {
            case 1 :
                dbg.enterAlt(1);

                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:44:14: '+'
                {
                dbg.location(44,14);
                match(input,9,FOLLOW_9_in_synpred7_Gapt260); if (state.failed) return ;

                }
                break;

        }
        } finally {dbg.exitSubRule(17);}

        dbg.location(45,13);
        match(input,8,FOLLOW_8_in_synpred7_Gapt278); if (state.failed) return ;
        dbg.location(46,13);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:46:13: (i= Identifier )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:46:14: i= Identifier
        {
        dbg.location(46,15);
        i=(Token)match(input,Identifier,FOLLOW_Identifier_in_synpred7_Gapt295); if (state.failed) return ;

        }

        dbg.location(47,13);
        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:13: ( '(' (a= argTypes )? ')' )?
        int alt19=2;
        try { dbg.enterSubRule(19);
        try { dbg.enterDecision(19);

        int LA19_0 = input.LA(1);

        if ( (LA19_0==10) ) {
            alt19=1;
        }
        } finally {dbg.exitDecision(19);}

        switch (alt19) {
            case 1 :
                dbg.enterAlt(1);

                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:14: '(' (a= argTypes )? ')'
                {
                dbg.location(47,14);
                match(input,10,FOLLOW_10_in_synpred7_Gapt312); if (state.failed) return ;
                dbg.location(47,18);
                // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:18: (a= argTypes )?
                int alt18=2;
                try { dbg.enterSubRule(18);
                try { dbg.enterDecision(18);

                int LA18_0 = input.LA(1);

                if ( (LA18_0==Identifier||LA18_0==13||(LA18_0>=17 && LA18_0<=26)) ) {
                    alt18=1;
                }
                } finally {dbg.exitDecision(18);}

                switch (alt18) {
                    case 1 :
                        dbg.enterAlt(1);

                        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:47:19: a= argTypes
                        {
                        dbg.location(47,20);
                        pushFollow(FOLLOW_argTypes_in_synpred7_Gapt317);
                        a=argTypes();

                        state._fsp--;
                        if (state.failed) return ;

                        }
                        break;

                }
                } finally {dbg.exitSubRule(18);}

                dbg.location(47,62);
                match(input,11,FOLLOW_11_in_synpred7_Gapt324); if (state.failed) return ;

                }
                break;

        }
        } finally {dbg.exitSubRule(19);}


        }
    }
    // $ANTLR end synpred7_Gapt

    // $ANTLR start synpred15_Gapt
    public final void synpred15_Gapt_fragment() throws RecognitionException {   
        GaptParser.modifier_return m = null;


        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:103:8: (m= modifier )
        dbg.enterAlt(1);

        // C:\\groovy-ck1\\groovy-aop\\src\\Gapt.g:103:8: m= modifier
        {
        dbg.location(103,9);
        pushFollow(FOLLOW_modifier_in_synpred15_Gapt640);
        m=modifier();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred15_Gapt

    // Delegated rules

    public final boolean synpred2_Gapt() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred2_Gapt_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_Gapt() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred7_Gapt_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred15_Gapt() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred15_Gapt_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_Gapt() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred3_Gapt_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_Gapt() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred1_Gapt_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA1 dfa1 = new DFA1(this);
    static final String DFA1_eotS =
        "\20\uffff";
    static final String DFA1_eofS =
        "\1\uffff\1\6\16\uffff";
    static final String DFA1_minS =
        "\4\4\1\uffff\1\0\2\uffff\3\0\3\uffff\1\0\1\uffff";
    static final String DFA1_maxS =
        "\1\32\1\12\1\32\1\4\1\uffff\1\0\2\uffff\3\0\3\uffff\1\0\1\uffff";
    static final String DFA1_acceptS =
        "\4\uffff\1\3\1\uffff\1\1\10\uffff\1\2";
    static final String DFA1_specialS =
        "\5\uffff\1\0\2\uffff\1\1\1\2\1\3\3\uffff\1\4\1\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\1\10\uffff\1\2\3\4\12\3",
            "\1\11\3\uffff\1\5\1\10\1\6",
            "\1\12\10\uffff\16\4",
            "\1\16",
            "",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA1_5 = input.LA(1);

                         
                        int index1_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Gapt()) ) {s = 6;}

                        else if ( (synpred2_Gapt()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index1_5);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA1_8 = input.LA(1);

                         
                        int index1_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Gapt()) ) {s = 6;}

                        else if ( (synpred2_Gapt()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index1_8);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA1_9 = input.LA(1);

                         
                        int index1_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_Gapt()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index1_9);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA1_10 = input.LA(1);

                         
                        int index1_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_Gapt()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index1_10);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA1_14 = input.LA(1);

                         
                        int index1_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_Gapt()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index1_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 1, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_methodPattern_in_pattern65 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnTypePattern_in_pattern92 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_methodPattern_in_pattern108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_pattern128 = new BitSet(new long[]{0x0000000007FE2010L});
    public static final BitSet FOLLOW_returnTypePattern_in_pattern153 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_methodPattern_in_pattern169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern221 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_8_in_methodPattern238 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern242 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_9_in_methodPattern260 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_8_in_methodPattern278 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern295 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_10_in_methodPattern312 = new BitSet(new long[]{0x0000000007FE2810L});
    public static final BitSet FOLLOW_argTypes_in_methodPattern317 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_methodPattern324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern347 = new BitSet(new long[]{0x0000000000000502L});
    public static final BitSet FOLLOW_8_in_methodPattern364 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern368 = new BitSet(new long[]{0x0000000000000502L});
    public static final BitSet FOLLOW_10_in_methodPattern386 = new BitSet(new long[]{0x0000000007FE2810L});
    public static final BitSet FOLLOW_argTypes_in_methodPattern391 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_methodPattern398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classPattern_in_argTypes442 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_12_in_argTypes460 = new BitSet(new long[]{0x0000000007FE2010L});
    public static final BitSet FOLLOW_classPattern_in_argTypes464 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_classPattern_in_returnTypePattern493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitive_in_classPattern524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_classPattern534 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_9_in_classPattern537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_className_in_qualifiedName578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_className605 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_8_in_className608 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_className610 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_modifier_in_modifiers640 = new BitSet(new long[]{0x000000000001E002L});
    public static final BitSet FOLLOW_set_in_modifier0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_primitive721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodPattern_in_synpred1_Gapt65 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnTypePattern_in_synpred2_Gapt92 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_methodPattern_in_synpred2_Gapt108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_8_in_synpred3_Gapt238 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred3_Gapt242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_synpred7_Gapt221 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_8_in_synpred7_Gapt238 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred7_Gapt242 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_9_in_synpred7_Gapt260 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_8_in_synpred7_Gapt278 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred7_Gapt295 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_10_in_synpred7_Gapt312 = new BitSet(new long[]{0x0000000007FE2810L});
    public static final BitSet FOLLOW_argTypes_in_synpred7_Gapt317 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_synpred7_Gapt324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred15_Gapt640 = new BitSet(new long[]{0x0000000000000002L});

}