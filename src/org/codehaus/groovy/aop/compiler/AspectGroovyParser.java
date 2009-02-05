// $ANTLR 3.1.1 C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g 2009-02-05 23:12:23

package org.codehaus.groovy.aop.compiler;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.debug.*;
import java.io.IOException;
import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.HashMap;
public class AspectGroovyParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Identifier", "Block", "Letter", "JavaIDDigit", "WS", "COMMENT", "LINE_COMMENT", "'aspect'", "'public'", "'private'", "'protected'", "'static'", "'import'", "';'", "'package'", "'@'", "'('", "')'"
    };
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

    public static final String[] ruleNames = new String[] {
        "invalidRule", "aspectDecl", "packageDecl", "annotation", "importDecl", 
        "compilationUnit", "modifier", "modifiers", "annotations"
    };
     
        public int ruleLevel = 0;
        public int getRuleLevel() { return ruleLevel; }
        public void incRuleLevel() { ruleLevel++; }
        public void decRuleLevel() { ruleLevel--; }
        public AspectGroovyParser(TokenStream input) {
            this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
        }
        public AspectGroovyParser(TokenStream input, int port, RecognizerSharedState state) {
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
    public AspectGroovyParser(TokenStream input, DebugEventListener dbg) {
        super(input, dbg, new RecognizerSharedState());

    }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }

    protected StringTemplateGroup templateLib =
      new StringTemplateGroup("AspectGroovyParserTemplates", AngleBracketTemplateLexer.class);

    public void setTemplateLib(StringTemplateGroup templateLib) {
      this.templateLib = templateLib;
    }
    public StringTemplateGroup getTemplateLib() {
      return templateLib;
    }
    /** allows convenient multi-value initialization:
     *  "new STAttrMap().put(...).put(...)"
     */
    public static class STAttrMap extends HashMap {
      public STAttrMap put(String attrName, Object value) {
        super.put(attrName, value);
        return this;
      }
      public STAttrMap put(String attrName, int value) {
        super.put(attrName, new Integer(value));
        return this;
      }
    }

    public String[] getTokenNames() { return AspectGroovyParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g"; }


    protected static class compilationUnit_scope {
        StringBuffer sb;
    }
    protected Stack compilationUnit_stack = new Stack();

    public static class compilationUnit_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "compilationUnit"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:15:1: compilationUnit : ( (a= annotations )? ) ( (p= packageDecl )? ) ( (i= importDecl )* ) aspectDecl -> {new StringTemplate($compilationUnit::sb.toString());};
    public final AspectGroovyParser.compilationUnit_return compilationUnit() throws RecognitionException {
        compilationUnit_stack.push(new compilationUnit_scope());
        AspectGroovyParser.compilationUnit_return retval = new AspectGroovyParser.compilationUnit_return();
        retval.start = input.LT(1);

        AspectGroovyParser.annotations_return a = null;

        AspectGroovyParser.packageDecl_return p = null;

        AspectGroovyParser.importDecl_return i = null;



          ((compilationUnit_scope)compilationUnit_stack.peek()).sb = new StringBuffer();

        try { dbg.enterRule(getGrammarFileName(), "compilationUnit");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(15, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:22:3: ( ( (a= annotations )? ) ( (p= packageDecl )? ) ( (i= importDecl )* ) aspectDecl -> {new StringTemplate($compilationUnit::sb.toString());})
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:22:5: ( (a= annotations )? ) ( (p= packageDecl )? ) ( (i= importDecl )* ) aspectDecl
            {
            dbg.location(22,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:22:5: ( (a= annotations )? )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:22:6: (a= annotations )?
            {
            dbg.location(22,7);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:22:7: (a= annotations )?
            int alt1=2;
            try { dbg.enterSubRule(1);
            try { dbg.enterDecision(1);

            int LA1_0 = input.LA(1);

            if ( (LA1_0==19) ) {
                alt1=1;
            }
            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:22:7: a= annotations
                    {
                    dbg.location(22,7);
                    pushFollow(FOLLOW_annotations_in_compilationUnit49);
                    a=annotations();

                    state._fsp--;


                    }
                    break;

            }
            } finally {dbg.exitSubRule(1);}


            }

            dbg.location(23,5);
            if(a != null) ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append((a!=null?input.toString(a.start,a.stop):null)+"\n");
            dbg.location(24,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:24:5: ( (p= packageDecl )? )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:24:6: (p= packageDecl )?
            {
            dbg.location(24,7);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:24:7: (p= packageDecl )?
            int alt2=2;
            try { dbg.enterSubRule(2);
            try { dbg.enterDecision(2);

            int LA2_0 = input.LA(1);

            if ( (LA2_0==18) ) {
                alt2=1;
            }
            } finally {dbg.exitDecision(2);}

            switch (alt2) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:24:7: p= packageDecl
                    {
                    dbg.location(24,7);
                    pushFollow(FOLLOW_packageDecl_in_compilationUnit66);
                    p=packageDecl();

                    state._fsp--;


                    }
                    break;

            }
            } finally {dbg.exitSubRule(2);}


            }

            dbg.location(25,5);
            if(p != null) ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append((p!=null?input.toString(p.start,p.stop):null)+"\n");
            dbg.location(26,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:26:5: ( (i= importDecl )* )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:26:6: (i= importDecl )*
            {
            dbg.location(26,7);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:26:7: (i= importDecl )*
            try { dbg.enterSubRule(3);

            loop3:
            do {
                int alt3=2;
                try { dbg.enterDecision(3);

                int LA3_0 = input.LA(1);

                if ( (LA3_0==16) ) {
                    alt3=1;
                }


                } finally {dbg.exitDecision(3);}

                switch (alt3) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:26:7: i= importDecl
            	    {
            	    dbg.location(26,7);
            	    pushFollow(FOLLOW_importDecl_in_compilationUnit83);
            	    i=importDecl();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);
            } finally {dbg.exitSubRule(3);}


            }

            dbg.location(27,5);
            if(i != null) ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append((i!=null?input.toString(i.start,i.stop):null)+"\n");
            dbg.location(28,5);
            pushFollow(FOLLOW_aspectDecl_in_compilationUnit97);
            aspectDecl();

            state._fsp--;



            // TEMPLATE REWRITE
            // 29:5: -> {new StringTemplate($compilationUnit::sb.toString());}
            {
                retval.st = new StringTemplate(((compilationUnit_scope)compilationUnit_stack.peek()).sb.toString());;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            compilationUnit_stack.pop();
        }
        dbg.location(30, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "compilationUnit");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "compilationUnit"

    public static class modifiers_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "modifiers"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:32:1: modifiers : ( modifier )+ ;
    public final AspectGroovyParser.modifiers_return modifiers() throws RecognitionException {
        AspectGroovyParser.modifiers_return retval = new AspectGroovyParser.modifiers_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "modifiers");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(32, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:33:3: ( ( modifier )+ )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:33:5: ( modifier )+
            {
            dbg.location(33,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:33:5: ( modifier )+
            int cnt4=0;
            try { dbg.enterSubRule(4);

            loop4:
            do {
                int alt4=2;
                try { dbg.enterDecision(4);

                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=12 && LA4_0<=15)) ) {
                    alt4=1;
                }


                } finally {dbg.exitDecision(4);}

                switch (alt4) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:33:5: modifier
            	    {
            	    dbg.location(33,5);
            	    pushFollow(FOLLOW_modifier_in_modifiers118);
            	    modifier();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        dbg.recognitionException(eee);

                        throw eee;
                }
                cnt4++;
            } while (true);
            } finally {dbg.exitSubRule(4);}


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(34, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "modifiers");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "modifiers"

    public static class aspectDecl_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "aspectDecl"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:36:1: aspectDecl : ( (m= modifiers )? ) 'aspect' Identifier Block ;
    public final AspectGroovyParser.aspectDecl_return aspectDecl() throws RecognitionException {
        AspectGroovyParser.aspectDecl_return retval = new AspectGroovyParser.aspectDecl_return();
        retval.start = input.LT(1);

        Token Identifier1=null;
        Token Block2=null;
        AspectGroovyParser.modifiers_return m = null;


        try { dbg.enterRule(getGrammarFileName(), "aspectDecl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(36, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:37:3: ( ( (m= modifiers )? ) 'aspect' Identifier Block )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:37:5: ( (m= modifiers )? ) 'aspect' Identifier Block
            {
            dbg.location(37,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:37:5: ( (m= modifiers )? )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:37:6: (m= modifiers )?
            {
            dbg.location(37,7);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:37:7: (m= modifiers )?
            int alt5=2;
            try { dbg.enterSubRule(5);
            try { dbg.enterDecision(5);

            int LA5_0 = input.LA(1);

            if ( ((LA5_0>=12 && LA5_0<=15)) ) {
                alt5=1;
            }
            } finally {dbg.exitDecision(5);}

            switch (alt5) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:37:7: m= modifiers
                    {
                    dbg.location(37,7);
                    pushFollow(FOLLOW_modifiers_in_aspectDecl135);
                    m=modifiers();

                    state._fsp--;


                    }
                    break;

            }
            } finally {dbg.exitSubRule(5);}


            }

            dbg.location(37,20);
            match(input,11,FOLLOW_11_in_aspectDecl139); 
            dbg.location(37,29);
            Identifier1=(Token)match(input,Identifier,FOLLOW_Identifier_in_aspectDecl141); 
            dbg.location(37,40);
            Block2=(Token)match(input,Block,FOLLOW_Block_in_aspectDecl143); 
            dbg.location(38,5);
            if(m != null) ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append((m!=null?input.toString(m.start,m.stop):null));
            dbg.location(39,5);
            ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append(" class ");
            dbg.location(40,5);
            ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append((Identifier1!=null?Identifier1.getText():null)+" ");
            dbg.location(41,5);
            ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append(" { def aspect=");
            dbg.location(42,5);
            ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append((Block2!=null?Block2.getText():null));
            dbg.location(43,5);
            ((compilationUnit_scope)compilationUnit_stack.peek()).sb.append("}\n");

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(44, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "aspectDecl");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "aspectDecl"

    public static class modifier_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "modifier"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:46:1: modifier : ( 'public' | 'private' | 'protected' | 'static' );
    public final AspectGroovyParser.modifier_return modifier() throws RecognitionException {
        AspectGroovyParser.modifier_return retval = new AspectGroovyParser.modifier_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "modifier");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(46, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:47:3: ( 'public' | 'private' | 'protected' | 'static' )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:
            {
            dbg.location(47,3);
            if ( (input.LA(1)>=12 && input.LA(1)<=15) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
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
        dbg.location(51, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "modifier");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "modifier"

    public static class importDecl_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "importDecl"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:53:1: importDecl : 'import' Identifier ( ';' )? ;
    public final AspectGroovyParser.importDecl_return importDecl() throws RecognitionException {
        AspectGroovyParser.importDecl_return retval = new AspectGroovyParser.importDecl_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "importDecl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(53, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:54:3: ( 'import' Identifier ( ';' )? )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:54:5: 'import' Identifier ( ';' )?
            {
            dbg.location(54,5);
            match(input,16,FOLLOW_16_in_importDecl223); 
            dbg.location(54,14);
            match(input,Identifier,FOLLOW_Identifier_in_importDecl225); 
            dbg.location(54,25);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:54:25: ( ';' )?
            int alt6=2;
            try { dbg.enterSubRule(6);
            try { dbg.enterDecision(6);

            int LA6_0 = input.LA(1);

            if ( (LA6_0==17) ) {
                alt6=1;
            }
            } finally {dbg.exitDecision(6);}

            switch (alt6) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:54:26: ';'
                    {
                    dbg.location(54,26);
                    match(input,17,FOLLOW_17_in_importDecl228); 

                    }
                    break;

            }
            } finally {dbg.exitSubRule(6);}


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(55, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "importDecl");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "importDecl"

    public static class packageDecl_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "packageDecl"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:57:1: packageDecl : 'package' Identifier ( ';' )? ;
    public final AspectGroovyParser.packageDecl_return packageDecl() throws RecognitionException {
        AspectGroovyParser.packageDecl_return retval = new AspectGroovyParser.packageDecl_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "packageDecl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(57, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:58:3: ( 'package' Identifier ( ';' )? )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:58:5: 'package' Identifier ( ';' )?
            {
            dbg.location(58,5);
            match(input,18,FOLLOW_18_in_packageDecl243); 
            dbg.location(58,15);
            match(input,Identifier,FOLLOW_Identifier_in_packageDecl245); 
            dbg.location(58,26);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:58:26: ( ';' )?
            int alt7=2;
            try { dbg.enterSubRule(7);
            try { dbg.enterDecision(7);

            int LA7_0 = input.LA(1);

            if ( (LA7_0==17) ) {
                alt7=1;
            }
            } finally {dbg.exitDecision(7);}

            switch (alt7) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:58:27: ';'
                    {
                    dbg.location(58,27);
                    match(input,17,FOLLOW_17_in_packageDecl248); 

                    }
                    break;

            }
            } finally {dbg.exitSubRule(7);}


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(59, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "packageDecl");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "packageDecl"

    public static class annotations_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "annotations"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:61:1: annotations : ( annotation )+ ;
    public final AspectGroovyParser.annotations_return annotations() throws RecognitionException {
        AspectGroovyParser.annotations_return retval = new AspectGroovyParser.annotations_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "annotations");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(61, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:62:3: ( ( annotation )+ )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:62:5: ( annotation )+
            {
            dbg.location(62,5);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:62:5: ( annotation )+
            int cnt8=0;
            try { dbg.enterSubRule(8);

            loop8:
            do {
                int alt8=2;
                try { dbg.enterDecision(8);

                int LA8_0 = input.LA(1);

                if ( (LA8_0==19) ) {
                    alt8=1;
                }


                } finally {dbg.exitDecision(8);}

                switch (alt8) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:62:5: annotation
            	    {
            	    dbg.location(62,5);
            	    pushFollow(FOLLOW_annotation_in_annotations263);
            	    annotation();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        dbg.recognitionException(eee);

                        throw eee;
                }
                cnt8++;
            } while (true);
            } finally {dbg.exitSubRule(8);}


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(63, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "annotations");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "annotations"

    public static class annotation_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "annotation"
    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:65:1: annotation : '@' Identifier ( '(' Identifier ')' )? ;
    public final AspectGroovyParser.annotation_return annotation() throws RecognitionException {
        AspectGroovyParser.annotation_return retval = new AspectGroovyParser.annotation_return();
        retval.start = input.LT(1);

        try { dbg.enterRule(getGrammarFileName(), "annotation");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(65, 1);

        try {
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:66:3: ( '@' Identifier ( '(' Identifier ')' )? )
            dbg.enterAlt(1);

            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:66:5: '@' Identifier ( '(' Identifier ')' )?
            {
            dbg.location(66,5);
            match(input,19,FOLLOW_19_in_annotation277); 
            dbg.location(66,9);
            match(input,Identifier,FOLLOW_Identifier_in_annotation279); 
            dbg.location(66,20);
            // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:66:20: ( '(' Identifier ')' )?
            int alt9=2;
            try { dbg.enterSubRule(9);
            try { dbg.enterDecision(9);

            int LA9_0 = input.LA(1);

            if ( (LA9_0==20) ) {
                alt9=1;
            }
            } finally {dbg.exitDecision(9);}

            switch (alt9) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\groovy-ck1\\groovy-aop\\src\\AspectGroovy.g:66:21: '(' Identifier ')'
                    {
                    dbg.location(66,21);
                    match(input,20,FOLLOW_20_in_annotation282); 
                    dbg.location(66,25);
                    match(input,Identifier,FOLLOW_Identifier_in_annotation284); 
                    dbg.location(66,36);
                    match(input,21,FOLLOW_21_in_annotation286); 

                    }
                    break;

            }
            } finally {dbg.exitSubRule(9);}


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        dbg.location(67, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "annotation");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "annotation"

    // Delegated rules


 

    public static final BitSet FOLLOW_annotations_in_compilationUnit49 = new BitSet(new long[]{0x000000000005F800L});
    public static final BitSet FOLLOW_packageDecl_in_compilationUnit66 = new BitSet(new long[]{0x000000000005F800L});
    public static final BitSet FOLLOW_importDecl_in_compilationUnit83 = new BitSet(new long[]{0x000000000005F800L});
    public static final BitSet FOLLOW_aspectDecl_in_compilationUnit97 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_modifiers118 = new BitSet(new long[]{0x000000000000F002L});
    public static final BitSet FOLLOW_modifiers_in_aspectDecl135 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_aspectDecl139 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_aspectDecl141 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_Block_in_aspectDecl143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_modifier0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_importDecl223 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_importDecl225 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_importDecl228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_packageDecl243 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_packageDecl245 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_packageDecl248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_annotations263 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_annotation277 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_annotation279 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_20_in_annotation282 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_annotation284 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_annotation286 = new BitSet(new long[]{0x0000000000000002L});

}