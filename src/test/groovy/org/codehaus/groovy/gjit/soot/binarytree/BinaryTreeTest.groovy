package org.codehaus.groovy.gjit.soot.binarytree

import groovy.util.GroovyTestCase
class BinaryTreeTest extends GroovyTestCase {

	void testRunBinaryTree() {
		def aspect = weave(BinaryTreeAspect)
		assert aspect != null
		BinaryTree.main([] as String[])
		unweave(BinaryTreeAspect)
	}

}
