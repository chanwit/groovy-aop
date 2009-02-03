// $ANTLR 3.0.1 C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g 2008-04-10 03:54:49

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
public class GaptParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Identifier", "Letter", "JavaIDDigit", "WS", "'.'", "'+'", "'('", "')'", "','", "'*'", "'public'", "'private'", "'protected'", "'boolean'", "'byte'", "'char'", "'double'", "'float'", "'int'", "'long'", "'short'", "'void'", "'..'", "'*.'"
    };
    public static final int WS=7;
    public static final int JavaIDDigit=6;
    public static final int EOF=-1;
    public static final int Letter=5;
    public static final int Identifier=4;

        public GaptParser(TokenStream input) {
            super(input);
            ruleMemo = new HashMap[40+1];
         }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g"; }



    // $ANTLR start pattern
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );
    public final Pattern pattern() throws RecognitionException {
        Pattern pt = null;

        MethodPattern mep = null;

        TypePattern rtp = null;

        List<String> mod = null;


        
          pt = new Pattern();

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:21:2: ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) )
            int alt1=3;
            switch ( input.LA(1) ) {
            case Identifier:
                {
                switch ( input.LA(2) ) {
                case 8:
                    {
                    int LA1_5 = input.LA(3);

                    if ( (synpred1()) ) {
                        alt1=1;
                    }
                    else if ( (synpred2()) ) {
                        alt1=2;
                    }
                    else if ( (true) ) {
                        alt1=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return pt;}
                        NoViableAltException nvae =
                            new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 5, input);

                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                case 10:
                    {
                    alt1=1;
                    }
                    break;
                case 9:
                    {
                    int LA1_8 = input.LA(3);

                    if ( (synpred1()) ) {
                        alt1=1;
                    }
                    else if ( (synpred2()) ) {
                        alt1=2;
                    }
                    else if ( (true) ) {
                        alt1=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return pt;}
                        NoViableAltException nvae =
                            new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 8, input);

                        throw nvae;
                    }
                    }
                    break;
                case Identifier:
                    {
                    int LA1_9 = input.LA(3);

                    if ( (synpred2()) ) {
                        alt1=2;
                    }
                    else if ( (true) ) {
                        alt1=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return pt;}
                        NoViableAltException nvae =
                            new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 9, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return pt;}
                    NoViableAltException nvae =
                        new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 1, input);

                    throw nvae;
                }

                }
                break;
            case 13:
                {
                int LA1_2 = input.LA(2);

                if ( (LA1_2==Identifier) ) {
                    int LA1_10 = input.LA(3);

                    if ( (synpred2()) ) {
                        alt1=2;
                    }
                    else if ( (true) ) {
                        alt1=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return pt;}
                        NoViableAltException nvae =
                            new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 10, input);

                        throw nvae;
                    }
                }
                else if ( ((LA1_2>=13 && LA1_2<=26)) ) {
                    alt1=3;
                }
                else {
                    if (backtracking>0) {failed=true; return pt;}
                    NoViableAltException nvae =
                        new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 2, input);

                    throw nvae;
                }
                }
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
                {
                int LA1_3 = input.LA(2);

                if ( (LA1_3==Identifier) ) {
                    int LA1_14 = input.LA(3);

                    if ( (synpred2()) ) {
                        alt1=2;
                    }
                    else if ( (true) ) {
                        alt1=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return pt;}
                        NoViableAltException nvae =
                            new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 14, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return pt;}
                    NoViableAltException nvae =
                        new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 3, input);

                    throw nvae;
                }
                }
                break;
            case 14:
            case 15:
            case 16:
                {
                alt1=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return pt;}
                NoViableAltException nvae =
                    new NoViableAltException("17:1: pattern returns [Pattern pt] : ( (mep= methodPattern ) | (rtp= returnTypePattern ) (mep= methodPattern ) | (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern ) );", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:3: (mep= methodPattern )
                    {
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:3: (mep= methodPattern )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:4: mep= methodPattern
                    {
                    pushFollow(FOLLOW_methodPattern_in_pattern56);
                    mep=methodPattern();
                    _fsp--;
                    if (failed) return pt;
                    if ( backtracking==0 ) {
                       pt.setMethodPattern(mep); 
                    }

                    }


                    }
                    break;
                case 2 :
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:4: (rtp= returnTypePattern ) (mep= methodPattern )
                    {
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:4: (rtp= returnTypePattern )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:5: rtp= returnTypePattern
                    {
                    pushFollow(FOLLOW_returnTypePattern_in_pattern82);
                    rtp=returnTypePattern();
                    _fsp--;
                    if (failed) return pt;
                    if ( backtracking==0 ) {
                       pt.setReturnTypePattern(rtp); 
                    }

                    }

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:25:3: (mep= methodPattern )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:25:4: mep= methodPattern
                    {
                    pushFollow(FOLLOW_methodPattern_in_pattern92);
                    mep=methodPattern();
                    _fsp--;
                    if (failed) return pt;
                    if ( backtracking==0 ) {
                       pt.setMethodPattern(mep); 
                    }

                    }


                    }
                    break;
                case 3 :
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:27:4: (mod= modifiers ) (rtp= returnTypePattern ) (mep= methodPattern )
                    {
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:27:4: (mod= modifiers )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:27:5: mod= modifiers
                    {
                    pushFollow(FOLLOW_modifiers_in_pattern113);
                    mod=modifiers();
                    _fsp--;
                    if (failed) return pt;
                    if ( backtracking==0 ) {
                       pt.setModifiers(mod); 
                    }

                    }

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:28:3: (rtp= returnTypePattern )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:28:4: rtp= returnTypePattern
                    {
                    pushFollow(FOLLOW_returnTypePattern_in_pattern132);
                    rtp=returnTypePattern();
                    _fsp--;
                    if (failed) return pt;
                    if ( backtracking==0 ) {
                       pt.setReturnTypePattern(rtp); 
                    }

                    }

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:29:3: (mep= methodPattern )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:29:4: mep= methodPattern
                    {
                    pushFollow(FOLLOW_methodPattern_in_pattern142);
                    mep=methodPattern();
                    _fsp--;
                    if (failed) return pt;
                    if ( backtracking==0 ) {
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
        return pt;
    }
    // $ANTLR end pattern


    // $ANTLR start methodPattern
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:33:1: methodPattern returns [MethodPattern mep] : ( '*.' | Identifier '.' )=> ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? ) ;
    public final MethodPattern methodPattern() throws RecognitionException {
        MethodPattern mep = null;

        Token i=null;
        List<TypePattern> a = null;


        
          mep = new MethodPattern();
          List<String> names = new ArrayList<String>();
          boolean subClass = false;
          boolean mayBeProperty = true;

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:40:2: ( ( '*.' | Identifier '.' )=> ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? ) )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:41:2: ( '*.' | Identifier '.' )=> ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? )
            {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:42:2: ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==Identifier) ) {
                switch ( input.LA(2) ) {
                case 8:
                    {
                    int LA9_2 = input.LA(3);

                    if ( (synpred9()) ) {
                        alt9=1;
                    }
                    else if ( (true) ) {
                        alt9=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return mep;}
                        NoViableAltException nvae =
                            new NoViableAltException("42:2: ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? )", 9, 2, input);

                        throw nvae;
                    }
                    }
                    break;
                case 9:
                    {
                    alt9=1;
                    }
                    break;
                case EOF:
                case 10:
                    {
                    alt9=2;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return mep;}
                    NoViableAltException nvae =
                        new NoViableAltException("42:2: ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? )", 9, 1, input);

                    throw nvae;
                }

            }
            else {
                if (backtracking>0) {failed=true; return mep;}
                NoViableAltException nvae =
                    new NoViableAltException("42:2: ( (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )? | (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )? )", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:43:3: (i= Identifier ) ( '.' i= Identifier )* ( '+' )? '.' (i= Identifier ) ( '(' (a= argTypes )? ')' )?
                    {
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:43:3: (i= Identifier )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:43:4: i= Identifier
                    {
                    i=(Token)input.LT(1);
                    match(input,Identifier,FOLLOW_Identifier_in_methodPattern200); if (failed) return mep;
                    if ( backtracking==0 ) {
                      names.add(i.getText());
                    }

                    }

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:44:10: ( '.' i= Identifier )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0==8) ) {
                            int LA2_2 = input.LA(2);

                            if ( (LA2_2==Identifier) ) {
                                int LA2_3 = input.LA(3);

                                if ( (synpred5()) ) {
                                    alt2=1;
                                }


                            }


                        }


                        switch (alt2) {
                    	case 1 :
                    	    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:44:11: '.' i= Identifier
                    	    {
                    	    match(input,8,FOLLOW_8_in_methodPattern214); if (failed) return mep;
                    	    i=(Token)input.LT(1);
                    	    match(input,Identifier,FOLLOW_Identifier_in_methodPattern218); if (failed) return mep;
                    	    if ( backtracking==0 ) {
                    	      names.add(i.getText());
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:45:10: ( '+' )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==9) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:45:11: '+'
                            {
                            match(input,9,FOLLOW_9_in_methodPattern234); if (failed) return mep;
                            if ( backtracking==0 ) {
                               subClass = true; 
                            }

                            }
                            break;

                    }

                    match(input,8,FOLLOW_8_in_methodPattern250); if (failed) return mep;
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:47:10: (i= Identifier )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:47:11: i= Identifier
                    {
                    i=(Token)input.LT(1);
                    match(input,Identifier,FOLLOW_Identifier_in_methodPattern265); if (failed) return mep;
                    if ( backtracking==0 ) {
                      names.add(i.getText());
                    }

                    }

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:10: ( '(' (a= argTypes )? ')' )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==10) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:11: '(' (a= argTypes )? ')'
                            {
                            match(input,10,FOLLOW_10_in_methodPattern280); if (failed) return mep;
                            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:15: (a= argTypes )?
                            int alt4=2;
                            int LA4_0 = input.LA(1);

                            if ( (LA4_0==Identifier||LA4_0==13||(LA4_0>=17 && LA4_0<=26)) ) {
                                alt4=1;
                            }
                            switch (alt4) {
                                case 1 :
                                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:16: a= argTypes
                                    {
                                    pushFollow(FOLLOW_argTypes_in_methodPattern285);
                                    a=argTypes();
                                    _fsp--;
                                    if (failed) return mep;
                                    if ( backtracking==0 ) {
                                      mep.setArgTypePatterns(a);
                                    }

                                    }
                                    break;

                            }

                            match(input,11,FOLLOW_11_in_methodPattern292); if (failed) return mep;
                            if ( backtracking==0 ) {
                              mayBeProperty=false;
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:50:3: (i= Identifier ) ( '.' i= Identifier )* ( '(' (a= argTypes )? ')' )?
                    {
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:50:3: (i= Identifier )
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:50:4: i= Identifier
                    {
                    i=(Token)input.LT(1);
                    match(input,Identifier,FOLLOW_Identifier_in_methodPattern306); if (failed) return mep;
                    if ( backtracking==0 ) {
                      names.add(i.getText());
                    }

                    }

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:51:10: ( '.' i= Identifier )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==8) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:51:11: '.' i= Identifier
                    	    {
                    	    match(input,8,FOLLOW_8_in_methodPattern320); if (failed) return mep;
                    	    i=(Token)input.LT(1);
                    	    match(input,Identifier,FOLLOW_Identifier_in_methodPattern324); if (failed) return mep;
                    	    if ( backtracking==0 ) {
                    	      names.add(i.getText());
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:52:10: ( '(' (a= argTypes )? ')' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==10) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:52:11: '(' (a= argTypes )? ')'
                            {
                            match(input,10,FOLLOW_10_in_methodPattern340); if (failed) return mep;
                            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:52:15: (a= argTypes )?
                            int alt7=2;
                            int LA7_0 = input.LA(1);

                            if ( (LA7_0==Identifier||LA7_0==13||(LA7_0>=17 && LA7_0<=26)) ) {
                                alt7=1;
                            }
                            switch (alt7) {
                                case 1 :
                                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:52:16: a= argTypes
                                    {
                                    pushFollow(FOLLOW_argTypes_in_methodPattern345);
                                    a=argTypes();
                                    _fsp--;
                                    if (failed) return mep;
                                    if ( backtracking==0 ) {
                                      mep.setArgTypePatterns(a);
                                    }

                                    }
                                    break;

                            }

                            match(input,11,FOLLOW_11_in_methodPattern352); if (failed) return mep;
                            if ( backtracking==0 ) {
                              mayBeProperty=false;
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            if ( backtracking==0 ) {
              
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
        return mep;
    }
    // $ANTLR end methodPattern


    // $ANTLR start argTypes
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:64:1: argTypes returns [List<TypePattern> pts] : c= classPattern ( ',' c= classPattern )* ;
    public final List<TypePattern> argTypes() throws RecognitionException {
        List<TypePattern> pts = null;

        TypePattern c = null;


        
          pts = new ArrayList<TypePattern>();

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:68:2: (c= classPattern ( ',' c= classPattern )* )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:68:9: c= classPattern ( ',' c= classPattern )*
            {
            pushFollow(FOLLOW_classPattern_in_argTypes414);
            c=classPattern();
            _fsp--;
            if (failed) return pts;
            if ( backtracking==0 ) {
              pts.add(c); 
            }
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:69:10: ( ',' c= classPattern )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==12) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:69:11: ',' c= classPattern
            	    {
            	    match(input,12,FOLLOW_12_in_argTypes430); if (failed) return pts;
            	    pushFollow(FOLLOW_classPattern_in_argTypes434);
            	    c=classPattern();
            	    _fsp--;
            	    if (failed) return pts;
            	    if ( backtracking==0 ) {
            	      pts.add(c); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return pts;
    }
    // $ANTLR end argTypes


    // $ANTLR start returnTypePattern
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:72:1: returnTypePattern returns [TypePattern tp] : c= classPattern ;
    public final TypePattern returnTypePattern() throws RecognitionException {
        TypePattern tp = null;

        TypePattern c = null;


        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:73:2: (c= classPattern )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:73:4: c= classPattern
            {
            pushFollow(FOLLOW_classPattern_in_returnTypePattern458);
            c=classPattern();
            _fsp--;
            if (failed) return tp;
            if ( backtracking==0 ) {
              
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
        return tp;
    }
    // $ANTLR end returnTypePattern


    // $ANTLR start classPattern
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:79:1: classPattern returns [TypePattern tp] : (t= primitive | t= qualifiedName ( '+' )? ) ;
    public final TypePattern classPattern() throws RecognitionException {
        TypePattern tp = null;

        TypePattern t = null;


        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:80:2: ( (t= primitive | t= qualifiedName ( '+' )? ) )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:81:2: (t= primitive | t= qualifiedName ( '+' )? )
            {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:81:2: (t= primitive | t= qualifiedName ( '+' )? )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==13||(LA12_0>=17 && LA12_0<=26)) ) {
                alt12=1;
            }
            else if ( (LA12_0==Identifier) ) {
                alt12=2;
            }
            else {
                if (backtracking>0) {failed=true; return tp;}
                NoViableAltException nvae =
                    new NoViableAltException("81:2: (t= primitive | t= qualifiedName ( '+' )? )", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:81:4: t= primitive
                    {
                    pushFollow(FOLLOW_primitive_in_classPattern482);
                    t=primitive();
                    _fsp--;
                    if (failed) return tp;

                    }
                    break;
                case 2 :
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:82:4: t= qualifiedName ( '+' )?
                    {
                    pushFollow(FOLLOW_qualifiedName_in_classPattern489);
                    t=qualifiedName();
                    _fsp--;
                    if (failed) return tp;
                    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:82:20: ( '+' )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==9) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:82:21: '+'
                            {
                            match(input,9,FOLLOW_9_in_classPattern492); if (failed) return tp;
                            if ( backtracking==0 ) {
                               t.setSubClass(true); 
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            if ( backtracking==0 ) {
              
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
        return tp;
    }
    // $ANTLR end classPattern


    // $ANTLR start qualifiedName
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:89:1: qualifiedName returns [TypePattern tp] : (c= className ) ;
    public final TypePattern qualifiedName() throws RecognitionException {
        TypePattern tp = null;

        className_return c = null;


        
          tp = new TypePattern();

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:93:2: ( (c= className ) )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:93:4: (c= className )
            {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:93:4: (c= className )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:93:5: c= className
            {
            pushFollow(FOLLOW_className_in_qualifiedName527);
            c=className();
            _fsp--;
            if (failed) return tp;
            if ( backtracking==0 ) {
               tp.setClassPattern(input.toString(c.start,c.stop));   
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
        return tp;
    }
    // $ANTLR end qualifiedName

    public static class className_return extends ParserRuleReturnScope {
    };

    // $ANTLR start className
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:96:1: className : Identifier ( '.' Identifier )* ;
    public final className_return className() throws RecognitionException {
        className_return retval = new className_return();
        retval.start = input.LT(1);

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:97:2: ( Identifier ( '.' Identifier )* )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:97:4: Identifier ( '.' Identifier )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_className548); if (failed) return retval;
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:97:15: ( '.' Identifier )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==8) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:97:16: '.' Identifier
            	    {
            	    match(input,8,FOLLOW_8_in_className551); if (failed) return retval;
            	    match(input,Identifier,FOLLOW_Identifier_in_className553); if (failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end className


    // $ANTLR start modifiers
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:100:1: modifiers returns [List<String> ms] : (m= modifier )* ;
    public final List<String> modifiers() throws RecognitionException {
        List<String> ms = null;

        modifier_return m = null;


        
          ms = new ArrayList<String>();

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:104:2: ( (m= modifier )* )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:104:4: (m= modifier )*
            {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:104:4: (m= modifier )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==13) ) {
                    int LA14_1 = input.LA(2);

                    if ( ((LA14_1>=13 && LA14_1<=26)) ) {
                        alt14=1;
                    }
                    else if ( (LA14_1==Identifier) ) {
                        int LA14_6 = input.LA(3);

                        if ( (synpred17()) ) {
                            alt14=1;
                        }


                    }


                }
                else if ( ((LA14_0>=14 && LA14_0<=16)) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:104:5: m= modifier
            	    {
            	    pushFollow(FOLLOW_modifier_in_modifiers577);
            	    m=modifier();
            	    _fsp--;
            	    if (failed) return ms;
            	    if ( backtracking==0 ) {
            	      ms.add(input.toString(m.start,m.stop)); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ms;
    }
    // $ANTLR end modifiers

    public static class modifier_return extends ParserRuleReturnScope {
    };

    // $ANTLR start modifier
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:107:1: modifier : ( '*' | 'public' | 'private' | 'protected' );
    public final modifier_return modifier() throws RecognitionException {
        modifier_return retval = new modifier_return();
        retval.start = input.LT(1);

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:108:2: ( '*' | 'public' | 'private' | 'protected' )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:
            {
            if ( (input.LA(1)>=13 && input.LA(1)<=16) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return retval;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_modifier0);    throw mse;
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
        return retval;
    }
    // $ANTLR end modifier


    // $ANTLR start primitive
    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:115:1: primitive returns [TypePattern tp] : t= ( '*' | 'boolean' | 'byte' | 'char' | 'double' | 'float' | 'int' | 'long' | 'short' | 'void' | '..' ) ;
    public final TypePattern primitive() throws RecognitionException {
        TypePattern tp = null;

        Token t=null;

        
          tp = new TypePattern();

        try {
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:119:2: (t= ( '*' | 'boolean' | 'byte' | 'char' | 'double' | 'float' | 'int' | 'long' | 'short' | 'void' | '..' ) )
            // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:119:4: t= ( '*' | 'boolean' | 'byte' | 'char' | 'double' | 'float' | 'int' | 'long' | 'short' | 'void' | '..' )
            {
            t=(Token)input.LT(1);
            if ( input.LA(1)==13||(input.LA(1)>=17 && input.LA(1)<=26) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return tp;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_primitive633);    throw mse;
            }

            if ( backtracking==0 ) {
              
                tp.setPrimitive(true);
                tp.setClassPattern(t.getText());

            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return tp;
    }
    // $ANTLR end primitive

    // $ANTLR start synpred1
    public final void synpred1_fragment() throws RecognitionException {   
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:3: ( ( methodPattern ) )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:3: ( methodPattern )
        {
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:3: ( methodPattern )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:22:4: methodPattern
        {
        pushFollow(FOLLOW_methodPattern_in_synpred156);
        methodPattern();
        _fsp--;
        if (failed) return ;

        }


        }
    }
    // $ANTLR end synpred1

    // $ANTLR start synpred2
    public final void synpred2_fragment() throws RecognitionException {   
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:4: ( ( returnTypePattern ) ( methodPattern ) )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:4: ( returnTypePattern ) ( methodPattern )
        {
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:4: ( returnTypePattern )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:24:5: returnTypePattern
        {
        pushFollow(FOLLOW_returnTypePattern_in_synpred282);
        returnTypePattern();
        _fsp--;
        if (failed) return ;

        }

        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:25:3: ( methodPattern )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:25:4: methodPattern
        {
        pushFollow(FOLLOW_methodPattern_in_synpred292);
        methodPattern();
        _fsp--;
        if (failed) return ;

        }


        }
    }
    // $ANTLR end synpred2

    // $ANTLR start synpred5
    public final void synpred5_fragment() throws RecognitionException {   
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:44:11: ( '.' Identifier )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:44:11: '.' Identifier
        {
        match(input,8,FOLLOW_8_in_synpred5214); if (failed) return ;
        match(input,Identifier,FOLLOW_Identifier_in_synpred5218); if (failed) return ;

        }
    }
    // $ANTLR end synpred5

    // $ANTLR start synpred9
    public final void synpred9_fragment() throws RecognitionException {   
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:43:3: ( ( Identifier ) ( '.' Identifier )* ( '+' )? '.' ( Identifier ) ( '(' ( argTypes )? ')' )? )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:43:3: ( Identifier ) ( '.' Identifier )* ( '+' )? '.' ( Identifier ) ( '(' ( argTypes )? ')' )?
        {
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:43:3: ( Identifier )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:43:4: Identifier
        {
        match(input,Identifier,FOLLOW_Identifier_in_synpred9200); if (failed) return ;

        }

        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:44:10: ( '.' Identifier )*
        loop17:
        do {
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==8) ) {
                int LA17_2 = input.LA(2);

                if ( (LA17_2==Identifier) ) {
                    int LA17_3 = input.LA(3);

                    if ( (synpred5()) ) {
                        alt17=1;
                    }


                }


            }


            switch (alt17) {
        	case 1 :
        	    // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:44:11: '.' Identifier
        	    {
        	    match(input,8,FOLLOW_8_in_synpred9214); if (failed) return ;
        	    match(input,Identifier,FOLLOW_Identifier_in_synpred9218); if (failed) return ;

        	    }
        	    break;

        	default :
        	    break loop17;
            }
        } while (true);

        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:45:10: ( '+' )?
        int alt18=2;
        int LA18_0 = input.LA(1);

        if ( (LA18_0==9) ) {
            alt18=1;
        }
        switch (alt18) {
            case 1 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:45:11: '+'
                {
                match(input,9,FOLLOW_9_in_synpred9234); if (failed) return ;

                }
                break;

        }

        match(input,8,FOLLOW_8_in_synpred9250); if (failed) return ;
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:47:10: ( Identifier )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:47:11: Identifier
        {
        match(input,Identifier,FOLLOW_Identifier_in_synpred9265); if (failed) return ;

        }

        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:10: ( '(' ( argTypes )? ')' )?
        int alt20=2;
        int LA20_0 = input.LA(1);

        if ( (LA20_0==10) ) {
            alt20=1;
        }
        switch (alt20) {
            case 1 :
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:11: '(' ( argTypes )? ')'
                {
                match(input,10,FOLLOW_10_in_synpred9280); if (failed) return ;
                // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:15: ( argTypes )?
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==Identifier||LA19_0==13||(LA19_0>=17 && LA19_0<=26)) ) {
                    alt19=1;
                }
                switch (alt19) {
                    case 1 :
                        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:48:16: argTypes
                        {
                        pushFollow(FOLLOW_argTypes_in_synpred9285);
                        argTypes();
                        _fsp--;
                        if (failed) return ;

                        }
                        break;

                }

                match(input,11,FOLLOW_11_in_synpred9292); if (failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred9

    // $ANTLR start synpred17
    public final void synpred17_fragment() throws RecognitionException {   
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:104:5: ( modifier )
        // C:\\workspace.grails\\groovy-aop\\src\\org\\codehaus\\groovy\\aop\\pattern\\impl\\Gapt.g:104:5: modifier
        {
        pushFollow(FOLLOW_modifier_in_synpred17577);
        modifier();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred17

    public final boolean synpred9() {
        backtracking++;
        int start = input.mark();
        try {
            synpred9_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred5() {
        backtracking++;
        int start = input.mark();
        try {
            synpred5_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred17() {
        backtracking++;
        int start = input.mark();
        try {
            synpred17_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred1() {
        backtracking++;
        int start = input.mark();
        try {
            synpred1_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred2() {
        backtracking++;
        int start = input.mark();
        try {
            synpred2_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_methodPattern_in_pattern56 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnTypePattern_in_pattern82 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_methodPattern_in_pattern92 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_pattern113 = new BitSet(new long[]{0x0000000007FE2010L});
    public static final BitSet FOLLOW_returnTypePattern_in_pattern132 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_methodPattern_in_pattern142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern200 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_8_in_methodPattern214 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern218 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_9_in_methodPattern234 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_8_in_methodPattern250 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern265 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_10_in_methodPattern280 = new BitSet(new long[]{0x0000000007FE2810L});
    public static final BitSet FOLLOW_argTypes_in_methodPattern285 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_methodPattern292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern306 = new BitSet(new long[]{0x0000000000000502L});
    public static final BitSet FOLLOW_8_in_methodPattern320 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_methodPattern324 = new BitSet(new long[]{0x0000000000000502L});
    public static final BitSet FOLLOW_10_in_methodPattern340 = new BitSet(new long[]{0x0000000007FE2810L});
    public static final BitSet FOLLOW_argTypes_in_methodPattern345 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_methodPattern352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classPattern_in_argTypes414 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_12_in_argTypes430 = new BitSet(new long[]{0x0000000007FE2010L});
    public static final BitSet FOLLOW_classPattern_in_argTypes434 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_classPattern_in_returnTypePattern458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitive_in_classPattern482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_classPattern489 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_9_in_classPattern492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_className_in_qualifiedName527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_className548 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_8_in_className551 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_className553 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_modifier_in_modifiers577 = new BitSet(new long[]{0x000000000001E002L});
    public static final BitSet FOLLOW_set_in_modifier0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_primitive633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodPattern_in_synpred156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnTypePattern_in_synpred282 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_methodPattern_in_synpred292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_8_in_synpred5214 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred5218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_synpred9200 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_8_in_synpred9214 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred9218 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_9_in_synpred9234 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_8_in_synpred9250 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred9265 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_10_in_synpred9280 = new BitSet(new long[]{0x0000000007FE2810L});
    public static final BitSet FOLLOW_argTypes_in_synpred9285 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_synpred9292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred17577 = new BitSet(new long[]{0x0000000000000002L});

}