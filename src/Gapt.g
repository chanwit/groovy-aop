grammar Gapt;

options {k=2; backtrack=true;}

@lexer::header {
package org.codehaus.groovy.aop.pattern.impl;
}

@parser::header {
package org.codehaus.groovy.aop.pattern.impl;

import org.codehaus.groovy.aop.pattern.Pattern;
import org.codehaus.groovy.aop.pattern.MethodPattern;
import org.codehaus.groovy.aop.pattern.TypePattern;
}

pattern returns [Pattern pt]
@init {
  pt = new Pattern();
}
	:
		(mep=methodPattern     { pt.setMethodPattern(mep); } )       // only method		
		
	|	(rtp=returnTypePattern { pt.setReturnTypePattern(rtp); })
		(mep=methodPattern     { pt.setMethodPattern(mep); } )
				
	|	(mod=modifiers         { pt.setModifiers(mod); } )
		(rtp=returnTypePattern { pt.setReturnTypePattern(rtp); })
		(mep=methodPattern     { pt.setMethodPattern(mep); } )
	;


methodPattern returns [MethodPattern mep]
@init {
  mep = new MethodPattern();
  List<String> names = new ArrayList<String>();
  boolean subClass = false;
  boolean mayBeProperty = true;
}
	:
	(       
		(i=Identifier{names.add($i.text);})
	        ('.' i=Identifier{names.add($i.text);})* 
	        ('+' { subClass = true; })? 
	        '.' 
	        (i=Identifier{names.add($i.text);}) 
	        ('(' (a=argTypes {mep.setArgTypePatterns(a);} )? ')' {mayBeProperty=false;})?
	|
		(i=Identifier{names.add($i.text);})
	        ('.' i=Identifier{names.add($i.text);})* 
	        ('(' (a=argTypes {mep.setArgTypePatterns(a);} )? ')' {mayBeProperty=false;})?	        
	)        
{
  String s[] = names.toArray(new String[names.size()]);
  TypePattern t = new TypePattern(s);  
  t.setSubClass(subClass);
  mep.setTypePattern(t);  
  mep.setNamePattern(s[s.length-1]);
  mep.setMayBeProperty(mayBeProperty);
}	        
	;	

argTypes returns [List<TypePattern> pts]
@init{
  pts = new ArrayList<TypePattern>();
}
	:	     c=classPattern  {pts.add(c); } 
	        (',' c=classPattern  {pts.add(c); } )*
	;	

returnTypePattern returns [TypePattern tp]
	:	c=classPattern
{
  tp = c;
}	
	;

classPattern returns [TypePattern tp]
	:	
	(	t=primitive
	|	t=qualifiedName ('+' { t.setSubClass(true); } )?
	)	
{
  tp = t;
}	
	;
	
qualifiedName returns [TypePattern tp]
@init{
  tp = new TypePattern();
}
	:	(c=className        { tp.setClassPattern($c.text);   })
	;

className
	:	Identifier ('.' Identifier)*
	;

modifiers returns [List<String> ms]
@init{
  ms = new ArrayList<String>();
}
	:	(m=modifier {ms.add($m.text); })*
	;

modifier
	:
		'*' 
	|	'public'
	|	'private'
	|	'protected'
	;
	
primitive returns [TypePattern tp]
@init {
  tp = new TypePattern();
}
	:	t=(
		'*' 
	|	'boolean'
	|	'byte'
	|	'char'
	|	'double'
	|	'float'
	|	'int'
	|	'long'
	|	'short'
	|	'void'
	|	'..')
{
  tp.setPrimitive(true);
  tp.setClassPattern($t.text);
}	
	;

Identifier 
	: Letter ( Letter | JavaIDDigit)*
	;
		
fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff' | '*'

    ;

fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

WS  :  ' ' { $channel=HIDDEN; }
    ;    
    
