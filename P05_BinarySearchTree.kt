import java.util.Queue
import java.util.Stack
import kotlin.random.Random

class IntBinarySearchTree : BinarySearchTree() {
    private var root: IntBinaryNode? = null
    val isEmpty: Boolean get() = (root == null)

    override fun add(value: Int) {
        val node = IntBinaryNode(value)
        findValue(value,
            whenEmpty = { root = node },
            whenNotFound = { parent ->
                if (parent.value < value) {
//                    require(parent.right == null)
                    parent.right = node
                }
                else {
//                    require(parent.left == null)
                    parent.left = node
                }
            }
        )
    }

    private fun IntBinaryNode.findAndRemoveLargest(parent: IntBinaryNode): Int {
        require(parent.left === this)

        return if (this.isLargestInSubtree) {
            parent.left = this.left

            this.value
        }
        else {
            // Find node containing largest value
            var prev = parent
            var largest = this
            while (largest.right != null) {
                prev = largest
                largest = largest.right!!
            }
            prev.right = largest.left

            largest.value
        }

//        var prev = parent
//
//
//        var largest = this
//        while (largest.right != null) {
//            prev = largest
//            largest = largest.right!!
//        }
//
//        if (parent.left!!.value == largest.value) {
//            parent.left = largest.left
//        }
//        else {
//            prev.right = largest.left
//        }
//
//        return  largest.value
        //...
    }

    private fun IntBinaryNode.findAndRemoveSmallest(parent: IntBinaryNode): Int {
        require(parent.right === this)

        return if (this.isSmallestInSubtree) {
            parent.right = this.right

            this.value
        }
        else {
            // Find node containing smallest value
            var prev = parent
            var smallest = this
            while (smallest.left != null) {
                prev = smallest
                smallest = smallest.left!!
            }
            prev.left = smallest.right

            smallest.value
        }


//        var prev = parent
//        var smallest = this
//        while (smallest.left != null) {
//            prev = smallest
//            smallest = smallest.left!!
//        }
//        if (prev.right!!.value == smallest.value) {
//            prev.right = smallest.right
//        }
//        else if (smallest.right == null){
//            prev.left = null
//        }
//        else {
//            prev.left = smallest.right
//        }
//        return smallest.value
    }

    override fun remove(value: Int) {
        findValue(
            value,
            whenFound = { parent, node ->
                //<editor-fold desc="Version 2">
//                node.value =
//                    node.right?.findAndRemoveSmallest(node)
//                    ?: node.left?.findAndRemoveLargest(node)
//                    ?: kotlin.run { // node is leaf
//                        if (parent == null) { // node is `root`
//                            root = null
//                        }
//                        else {
//                            when (node) {
//                                parent.left -> parent.left = null
//                                parent.right -> parent.right = null
//                                else -> error("???")
//                            }
//                        }
//
//                        node.value// No change to `node.value`
//                    }
                //</editor-fold>

                if (node.right != null) {
                    node.value = node.right?.findAndRemoveSmallest(node)!!
                }
                else if (node.left != null) {
                    node.value = node.left?.findAndRemoveLargest(node)!!
                }
                else { // node is leaf
                    if (parent.left == node) {
                        parent.left = null
                    }
                    else if (parent.right == node) {
                        parent.right = null
                    }
                    else {
                        root = null
                    }
                }
            }
        )
    }

    override fun contains(value: Int): Boolean {
        var result: Boolean = false
        findValue(
            value,
            whenFound = { _, _ -> result = true }
        )
        return result
    }

    override fun println() {
        val iter = iterator()
        while (iter.hasNext()) {
            print("${iter.next()} ")
        }
        kotlin.io.println()

//        printValue(root)
//        root?.print()
//        kotlin.io.println()
    }

    private fun IntBinaryNode.print() {
        var node: IntBinaryNode? = this
        val stack = Stack<IntBinaryNode>()
        while (stack.isNotEmpty() || node != null) {
            while (node != null) {
                stack.push(node)
                node = node.left
            }
            node = stack.pop()
            print("${node.value} ")
            node = node.right
        }
    }

    private fun printValue(root: IntBinaryNode?) {
//        if (node != null) {
//            printValue(node.left)
//            print("${node.value} ")
//            printValue(node.right)
//        }

        // These are equivalent
//        if (node == null) {
//            return
//        }
        root ?: return

        var node = root
        val stack = Stack<IntBinaryNode>()
        while (stack.isNotEmpty() || node != null) {
            while (node != null) {
                stack.push(node)
                node = node.left
            }
            node = stack.pop()
            print("${node.value} ")
            node = node.right
        }
    }

    private inner class IntBinarySearchTreeIterator : IntIterator {
        // Stack 能不能在有 next 時保持 non-empty?
        private var nextNodeToPush = root
        private val stack = Stack<IntBinaryNode>()
//        private var output: IntBinaryNode? = null

        override fun hasNext(): Boolean {
            return nextNodeToPush != null || stack.isNotEmpty()

//            return stack.isNotEmpty()
        }

        //<editor-fold desc="Version 2">
//        init {
//            var node = root
//            while (node != null) {
//                stack.push(node)
//                node = node.left
//            }
//        }
        //</editor-fold>

        override fun next(): Int {
            //<editor-fold desc="Version 2">
//            require(hasNext()) {
//                "There's no element remains"
//            }
//
//            val next = stack.pop()
//            var node = next.right
//            while (node != null) {
//                stack.push(node)
//                node = node.left
//            }
//            return next.value
            //</editor-fold>

            if (!hasNext()) {
                throw RuntimeException("Iterator has no item")
            }
            else {
                while (nextNodeToPush != null) {
                    stack.push(nextNodeToPush)
                    nextNodeToPush = nextNodeToPush?.left
                }
                //<editor-fold desc="Version 2">
//                val output = stack.pop()
//                nextNodeToPush = output.right
//                return output.value
                //</editor-fold>

                nextNodeToPush = stack.pop()
                val output = nextNodeToPush
//                output = node
                nextNodeToPush = nextNodeToPush?.right
                return output!!.value
            }
//            return output!!.value
        }
    }

    override fun reversePrintln() {
        val reIter = reverseIterator()
        while (reIter.hasNext()) {
            print("${reIter.next()} ")
        }
        kotlin.io.println()
    }

    override fun equals(other: Any?): Boolean {
        val tree = (other as? IntBinarySearchTree) ?: return false
        val iterB = tree.iterator()
        val iterA = this.iterator()

        while (iterA.hasNext()) {
            if (iterA.next() != iterB.next()) {
                return false
            }
        }
        return true
    }

    override fun iterator(): IntIterator {
        return IntBinarySearchTreeIterator()
    }

    override fun reverseIterator(): IntIterator {
        return IntBinarySearchTreeReverseIterator()
    }


    private inner class IntBinarySearchTreeReverseIterator : IntIterator {
        var node = root
        val stack = Stack<IntBinaryNode>()
        var output: IntBinaryNode? = null
        override fun hasNext(): Boolean {
            return stack.isNotEmpty() || node != null
        }

        override fun next(): Int {
            if (!hasNext()) {
                throw RuntimeException("Iterator has no item")
            }
            else {
                while (node != null) {
                    stack.push(node)
                    node = node?.right
                }
                node = stack.pop()
                output = node
                node = node?.left
            }
            return output!!.value
        }
    }

    /**
     * If found, then `whenFound` is called and the node containing specified value is provided.
     * If not found, then `whenNotFound` is called and the parent node of specified value is provided.
     */
    private inline fun findValue(
        value: Int,
        whenEmpty: () -> Unit = { },
        whenNotFound: (IntBinaryNode) -> Unit = { },
        whenFound: (IntBinaryNode, IntBinaryNode) -> Unit = { _, _ -> }
    ) {
        if (isEmpty) {
            whenEmpty()
            return
        }
        var child = this.root
        var parent = child // should start from `null`, since `root` has no parent.
        while (child?.value != null) {
            if (value < child.value) {
                if (child.left == null) {
                    whenNotFound(child)
                }
                parent = child
                child = child.left
            }
            else if (child.value < value) {
                if (child.right == null) {
                    whenNotFound(child)
                }
                parent = child
                child = child.right
            }
            else {
                whenFound(parent!!, child)
                return
            }
        }
    }

    class IntBinaryNode(
        var value: Int,
        var left: IntBinaryNode? = null,
        var right: IntBinaryNode? = null
    ) {
        inline val isLeaf: Boolean get() = left == null && right == null
        inline val isLargestInSubtree: Boolean get() = right == null
        inline val isSmallestInSubtree: Boolean get() = left == null
    }

}

private fun min(v1: Int, v2: Int?): Int =
    when {
        v2 == null -> v1
        else -> if (v1 < v2) {
            v1
        }
        else {
            v2
        }
    }

private fun max(v1: Int, v2: Int?): Int =
    when {
        v2 == null -> v1
        else -> if (v1 > v2) {
            v1
        }
        else {
            v2
        }
    }


inline val IntBinarySearchTree.isNotEmpty get() = !isEmpty

fun IntBinarySearchTree.add(vararg values: Int) {
    for (value in values) {
        add(value)
    }
}

fun IntArrayList.min(): Int? {
    var min: Int? = null
    for (i in 0 until size) {
        min = min(this.get(i), min)
    }
    return min
}

fun IntArrayList.max(): Int? {
    var max: Int? = null
    for (i in 0 until size) {
        max = max(this.get(i), max)
    }
    return max
}

private fun testRemoveValue(vararg values: Int) {
    for (v in values) {
        val tree = IntBinarySearchTree()
        tree.add(*values)
        tree.remove(v)
//        tree.println()
        for (check in values) {
            if (v != check) {
                require(tree.contains(check))
            }
            else {
                require(!tree.contains(check))
            }

        }
    }
}

private fun testRemove(tree: IntBinarySearchTree) {
    if (tree.isEmpty) {
        return
    }
    testRemoveValue(5, 2, 1, 4, 3, 8, 6, 7, 9)

    testRemoveValue(5, 2, 1, 3, 4, 8, 7, 6, 9)

    testRemoveValue(4, 2, 1, 3, 6, 5, 7)

    testRemoveValue(3, 1, 2, 5, 4, 63, 1, 2, 5, 4, 6)

    testRemoveValue(4, 2, 1, 3, 6, 5)

    testRemoveValue(4, 2, 1, 3, 5, 6)

    testRemoveValue(3, 2, 1, 5, 4, 6)

    testRemoveValue(2, 1, 3)

    testRemoveValue(1)


    val l = IntArrayList()

    if (3 in l) { // l.contains(3)
        // ....
    }

    val iter = tree.iterator()
    while (iter.hasNext()) {
        l.add(iter.next())
    }

    val randomIndex = Random(System.currentTimeMillis()).nextInt(l.size)
    val removedValue = l.get(randomIndex)
    tree.remove(removedValue)

    for (i in 0 until l.size) {
        if (i == randomIndex) {
            require(!tree.contains(removedValue))
        }
        else {
            require(tree.contains(l.get(i)))
        }
    }
}


private fun testContains(tree: IntBinarySearchTree) {
    if (tree.isEmpty) {
        return
    }

    val l = IntArrayList()

    val iter = tree.iterator()
    while (iter.hasNext()) {
        l.add(iter.next())
    }

    val min = l.min() ?: error("Shouldn't be null")
    val max = l.max() ?: error("Shouldn't be null")

    require(!tree.contains(min - 1))
    require(!tree.contains(max + 1))

    for (value in min..max) {
        if (l.contains(value)) {
            require(tree.contains(value))
        }
        else {
            require(!tree.contains(value))
        }
    }

}

private fun randomTest(size: Int) {
    val tree = IntBinarySearchTree()

    val random = Random(System.currentTimeMillis())
    repeat(size) {
        tree.add(random.nextInt(1000000))
    }

    tree.println()

    val iter = tree.iterator()
    while (iter.hasNext()) {
        print("${iter.next()} ")
    }

    println()

    tree.reversePrintln()

    val reIter = tree.reverseIterator()
    while (reIter.hasNext()) {
        print("${reIter.next()} ")
    }

    testContains(tree)
    testRemove(tree)
}

fun main() {


//    randomTest(500)


    val tree1 = IntBinarySearchTree()
    tree1.add(5, 1, 3, 4, 6 , 8)

    val tree2 = IntBinarySearchTree()
    tree2.add(5, 3, 1, 4, 6 , 8)

    println(tree1.equals(tree2))

//    var tree = IntBinarySearchTree()
////    tree.add(20, 15, 10, 18, 13, 11, 12)
//    tree.add(5, 2, 1, 4, 3)
//    tree.println()
//    tree.remove(5)
//    tree.println()
//    tree.remove(13)
//
//    tree.println()
//    if (tree.contains(10)) {
//        println("contains")
//    }
//
//    tree.reversePrintln()
//
//    val iter = tree.reverseIterator()
//    while (iter.hasNext()) {
//        print( "${iter.next()}  ")
//    }

}