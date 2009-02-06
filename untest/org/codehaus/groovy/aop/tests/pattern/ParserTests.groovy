package org.codehaus.groovy.aop.tests.pattern

import org.codehaus.groovy.aop.pattern.Parser

class ParserTests extends GroovyTestCase {

/*
    void test_01_Parser() {
        def p  = new Parser('public')
        try {
            def pt = p.parse()
        } catch(e) {
            assert e != null
        }
    }

    void test_02_SingleSignature_No_Argument() {
        def p = new Parser('public void Test.test()')
        def pt = p.parse()
        assert pt.anyReturnType == false
        assert pt.modifiers.'public' == true
        assert pt.returnTypePattern.primitive == true
        assert pt.returnTypePattern.classPattern == 'void'
        assert pt.methodPattern.typePattern.classPattern == 'Test'
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == false
        assert pt.methodPattern.mayBeProperty == false  // because 'void' + parenthesis test'()'
    }

    void test_03_SingleSignature_Any_Argument_1() {
        def p = new Parser('public void Test.test')
        def pt = p.parse()
        assert pt.anyReturnType == false
        assert pt.modifiers.'public' == true
        assert pt.returnTypePattern.primitive == true
        assert pt.returnTypePattern.classPattern == 'void'
        assert pt.methodPattern.typePattern.classPattern == 'Test'
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == false
        assert pt.methodPattern.mayBeProperty == false  // because 'void' is specified
    }

    void test_04_SingleSignature_Any_Argument_2() {
        def p = new Parser('public void Test.test(..)')
        def pt = p.parse()
        assert pt.anyReturnType == false
        assert pt.modifiers.'public' == true
        assert pt.returnTypePattern.primitive == true
        assert pt.returnTypePattern.classPattern == 'void'
        assert pt.methodPattern.typePattern.classPattern == 'Test'
        assert pt.methodPattern.typePattern.isSubClass() == false
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == true
        assert pt.methodPattern.mayBeProperty == false  // because 'void' + '(..)'
    }

    void test_05_SingleProperty() {
        def p  = new Parser('Test.test')
        def pt = p.parse() // pattern
        assert pt.anyModifier == true
        assert pt.anyReturnType == true
        assert pt.methodPattern.typePattern.classPattern == 'Test'
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == false
        assert pt.methodPattern.mayBeProperty == true
    }

    void test_06_SingleProperty_with_Def() {
        def p  = new Parser('def Test.test')
        def pt = p.parse() // pattern
        assert pt.anyModifier == true
        assert pt.anyReturnType == false
        assert pt.returnTypePattern.primitive == false
        assert pt.returnTypePattern.classPattern == 'java.lang.Object'
        assert pt.methodPattern.typePattern.classPattern == 'Test'
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == false
        assert pt.methodPattern.mayBeProperty == true
    }

    void test_07_SingleSignature_Any_Argument_with_Subclass() {
        def p = new Parser('public void Test+.test(..)')
        def pt = p.parse()
        assert pt.anyReturnType == false
        assert pt.modifiers.'public' == true
        assert pt.returnTypePattern.primitive == true
        assert pt.returnTypePattern.classPattern == 'void'
        assert pt.methodPattern.typePattern.classPattern == 'Test'
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == true
        assert pt.methodPattern.mayBeProperty == false  // because 'void' + '(..)'
    }

    void test_08_SingleProperty_with_Def_with_Subclass() {
        def p  = new Parser('def Test+.test')
        def pt = p.parse() // pattern
        assert pt.anyModifier == true
        assert pt.anyReturnType == false
        assert pt.returnTypePattern.primitive == false
        assert pt.returnTypePattern.classPattern == 'java.lang.Object'
        assert pt.methodPattern.typePattern.classPattern == 'Test'
        assert pt.methodPattern.typePattern.isSubClass() == true
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == false
        assert pt.methodPattern.mayBeProperty == true
    }

    void test_09_SinglePackageMethod_with_Def() {
        def p  = new Parser('def org.codehaus.groovy.aop.Test.test()')
        def pt = p.parse() // pattern
        assert pt.anyModifier == true
        assert pt.anyReturnType == false
        assert pt.returnTypePattern.primitive == false
        assert pt.returnTypePattern.classPattern == 'java.lang.Object'
        //println pt.methodPattern.typePattern.classPattern
        assert pt.methodPattern.typePattern.classPattern == 'org.codehaus.groovy.aop.Test'
        assert pt.methodPattern.typePattern.isSubClass() == false
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == false
        assert pt.methodPattern.mayBeProperty == false
    }

    void test_10_SinglePackageMethod_with_Def_with_Subclass() {
        def p  = new Parser('def org.codehaus.groovy.aop.Test+.test()')
        def pt = p.parse() // pattern
        assert pt.anyModifier == true
        assert pt.anyReturnType == false
        assert pt.returnTypePattern.primitive == false
        assert pt.returnTypePattern.classPattern == 'java.lang.Object'
        // println pt.methodPattern.typePattern.classPattern
        assert pt.methodPattern.typePattern.classPattern == 'org.codehaus.groovy.aop.Test'
        assert pt.methodPattern.typePattern.isSubClass() == true
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == false
        assert pt.methodPattern.mayBeProperty == false
    }

    void test_11_SinglePackageMethod_with_Def_with_Subclass_withAnyArg() {
        def p  = new Parser('def org.codehaus.groovy.aop.Test+.test(..)')
        def pt = p.parse() // pattern
        assert pt.anyModifier == true
        assert pt.anyReturnType == false
        assert pt.returnTypePattern.primitive == false
        assert pt.returnTypePattern.classPattern == 'java.lang.Object'
        // println pt.methodPattern.typePattern.classPattern
        assert pt.methodPattern.typePattern.classPattern == 'org.codehaus.groovy.aop.Test'
        assert pt.methodPattern.typePattern.isSubClass() == true
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == true
        assert pt.methodPattern.mayBeProperty == false
    }

    void test_12_PublicVoid_WildcardPackage_with_Subclass_withAnyArg() {
        def p  = new Parser('public void o*.Test+.test(..)')
        def pt = p.parse() // pattern
        assert pt.anyModifier == false
        assert pt.modifiers.'public' == true
        assert pt.anyReturnType == false
        assert pt.returnTypePattern.primitive == true
        assert pt.returnTypePattern.classPattern == 'void'
        // println pt.methodPattern.typePattern.classPattern
        assert pt.methodPattern.typePattern.classPattern == 'o*.Test'
        assert pt.methodPattern.typePattern.isSubClass() == true
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == true
        assert pt.methodPattern.mayBeProperty == false
    }

    void test_13_PublicVoid_WildcardPackage_with_Subclass_withAnyArg() {
        def p  = new Parser('*.Test+.test(..)')
        def pt = p.parse() // pattern
        assert pt.anyModifier == true
        assert pt.anyReturnType == true
        // println pt.methodPattern.typePattern.classPattern
        assert pt.methodPattern.typePattern.classPattern == '*.Test'
        assert pt.methodPattern.typePattern.isSubClass() == true
        assert pt.methodPattern.namePattern == 'test'
        assert pt.methodPattern.anyArgument == true
        assert pt.methodPattern.mayBeProperty == false
    }
*/
}