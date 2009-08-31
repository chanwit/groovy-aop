package org.codehaus.groovy.gjit.soot.binarytree
/* The Computer Language Benchmarks Game
http://shootout.alioth.debian.org/
contributed by Jochen Hinrichsen
modified by Marko Kocic
*/

class BinaryTree {

    def left, right, item

    static bottomUpTree(item, depth) {
       if (depth>0) {
           new BinaryTree(left:  bottomUpTree(2*item-1, depth-1),
                          right: bottomUpTree(2*item,   depth-1),
                          item:  item)
       } else {
           new BinaryTree(item:  item)
       }
    }

    def itemCheck(){
       // if necessary deallocate here
       if (left)
           item + left.itemCheck() - right.itemCheck()
       else
           item
    }

    static void main(args) {
        def n = (args.length == 0) ? 10 : args[0].toInteger()
        def minDepth = 4
        def maxDepth = [minDepth + 2, n].max()
        def stretchDepth = maxDepth + 1

        def check = (BinaryTree.bottomUpTree(0,stretchDepth)).itemCheck()
        println "stretch tree of depth ${stretchDepth}\t check: ${check}"

        def longLivedTree = BinaryTree.bottomUpTree(0, maxDepth)

        def depth = minDepth
        while (depth<=maxDepth) {
            def iterations = 1 << (maxDepth - depth + minDepth)
            check = 0
            for (i in 1..iterations) {
                check += (BinaryTree.bottomUpTree( i, depth)).itemCheck()
                check += (BinaryTree.bottomUpTree(-i, depth)).itemCheck()
            }

            println "${iterations*2}\t trees of depth ${depth}\t check: ${check}"
            depth+=2
        }

        println "long lived tree of depth ${maxDepth}\t check: ${longLivedTree.itemCheck()}"
    }
}
