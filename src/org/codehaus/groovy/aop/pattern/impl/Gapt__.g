lexer grammar Gapt;
@header {
package org.codehaus.groovy.aop.pattern.impl;
}

T8 : '.' ;
T9 : '+' ;
T10 : '(' ;
T11 : ')' ;
T12 : ',' ;
T13 : '*' ;
T14 : 'public' ;
T15 : 'private' ;
T16 : 'protected' ;
T17 : 'boolean' ;
T18 : 'byte' ;
T19 : 'char' ;
T20 : 'double' ;
T21 : 'float' ;
T22 : 'int' ;
T23 : 'long' ;
T24 : 'short' ;
T25 : 'void' ;
T26 : '..' ;
T27 : '*.' ;

// $ANTLR src "C:\workspace.grails\groovy-aop\src\org\codehaus\groovy\aop\pattern\impl\Gapt.g" 137
Identifier 
	: Letter ( Letter | JavaIDDigit)*
	;
		
// $ANTLR src "C:\workspace.grails\groovy-aop\src\org\codehaus\groovy\aop\pattern\impl\Gapt.g" 141
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

// $ANTLR src "C:\workspace.grails\groovy-aop\src\org\codehaus\groovy\aop\pattern\impl\Gapt.g" 159
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

// $ANTLR src "C:\workspace.grails\groovy-aop\src\org\codehaus\groovy\aop\pattern\impl\Gapt.g" 178
WS  :  ' ' { $channel=HIDDEN; }
    ;    
    
