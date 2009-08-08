package groovy

/** 
 * Tests Closures in Groovy
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision: 4996 $
 */
class ClosureAsParamTest extends GroovyTestCase {

    void testSimpleBlockCall() {
        assertClosure({owner-> println(owner) })
    }
  
    def assertClosure(Closure block) {
        assert block != null
        block.call("hello!")
    }
}
