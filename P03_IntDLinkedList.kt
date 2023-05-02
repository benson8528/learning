class IntDoublyLinkedNode(
    var value: Int,
    var prev: IntDoublyLinkedNode? = null,
    var next: IntDoublyLinkedNode? = null,


    ) {
    fun circuit(): IntDoublyLinkedNode {
        this.next = this
        this.prev = this
        return this
    }
}

class IntDLinkedListOld(
    vararg values: Int
){
    init {
        for (i in values) {
            repeat(values.size) {
                add(i)
            }
        }
    }
    var size: Int = 0
        private set
    private var head: IntDoublyLinkedNode? = null

    private val tail: IntDoublyLinkedNode? get() { return getLastNode() }

    fun assertValues(vararg values: Int) {
        if (size != values.size) {
            throw RuntimeException("size mis-match")
        }
        var node = head
        repeat(size) {
            if (node?.value != values[it]) {
                throw RuntimeException("${it}th value mis-match ${node?.value} != ${values[it]}")
            }
            node = node?.next
        }
    }
    fun assertValuesReversely(vararg values: Int) {
        if (size != values.size) {
            throw RuntimeException("size mis-match")
        }
        var node = tail
        for (i in size - 1 downTo 0) {
            if (values[i] != node?.value) {
                throw RuntimeException("${i}th value mis-match ${node?.value} != ${values[i]}")
            }
            node = node.prev
        }
    }

//    fun getSize(): Int {
//        return size
//    }

    fun clear() {
        head = null
        size = 0
    }

    fun add(value: Int) {
        if (size == 0) {
            val node = IntDoublyLinkedNode(value)
            head = node
        }
        else {
            val node = IntDoublyLinkedNode(value, tail)
            tail?.next = node
        }
        size += 1
    }

    fun remove(value: Int) {
        //<editor-fold desc="Version 2">
//        while (head?.value == value) {
//            head = head?.next
//
//            head?.prev?.next = null // For GC (garbage collecting)
//            head?.prev = null
//            size -= 1
//        }
//
//        var node = head?.next
//        while (node != null) {
//            if (node.value != value) {
//                node = node.next
//            }
//            else {
//                val next = node.next

//                node.prev?.next = node.next
//                node.next?.prev = node.prev
//
//                node.next = null
//                node.prev = null
//                node = next
//
//                size -= 1
//            }
//        }
        //</editor-fold>

//        var node = head
//        while (node != null && node.value == value) {
//            node.next?.prev = null
//            head = node.next
//            node = node.next
//            size -= 1
//        }
//
//        if (size == 0) {
//            return
//        }
//        var prev = head!!
//        node = prev.next
//
//        while (node != null) {
//            if (node.value == value) {
//                node.next?.prev = prev
//                prev.next = node.next
//                size -= 1
//            }
//            else {
//                prev = node
//            }
//            node = node.next
//        }

        while (head?.value == value) {
            head?.next?.prev = null
            head = head?.next
            head?.prev?.next = null
            size -= 1
        }

        var node = head?.next
        while (node != null) {
            if (node.value == value) {
                node.next?.prev = node.prev
                node.prev?.next = node.next
                node.prev = null
                size -= 1
            }
            node = node.next
        }
    }

    fun get(index: Int): Int {
        if (index >= size || index < 0) {
            throw RuntimeException("Invalid index, which should be between 0 and ${size - 1}")
        }
        return getNode(index)!!.value
    }

    fun insert(index: Int, value: Int) {
        if (index > size || index < 0) {
            throw RuntimeException("Invalid index, which should be between 0 and ${size}")
        }
        if (index == 0) {
            val node = IntDoublyLinkedNode(value,next = head)
            head?.prev = node
            head = node
        }
        else {
            val prev = getNode(index - 1)

            val node = IntDoublyLinkedNode(value, prev = prev, next = prev?.next)
            prev?.next = node
            node.next?.prev = node

//            node.next = prev?.next
//            node.next?.prev = node
//            node.prev = prev
//            prev?.next = node

        }
        size += 1
    }

    fun removeAt(index: Int) {
        if (index >= size || index < 0) {
            throw RuntimeException("Invalid index, which should be between 0 and ${size - 1}")
        }
        val node = getNode(index)
        if (index == 0) {
            //TODO: Buggy
            head = head?.next
        }
        node?.next?.prev = node?.prev
        node?.prev?.next = node?.next
        size -= 1

    }

    fun printList() {
        var node = head
        for (i in 0 until size) {
            print("${node!!.value} ")
            node = node.next
        }
        println()
    }

    fun printListReversely() {
        var node = tail
        repeat(size) {
            print("${node?.value} ")
            node = node?.prev
        }
        println()
    }

    fun contains(value: Int): Boolean {
        var node = head
        for (i in 0 until size) {
            if (node?.value == value) {
                return true
            }
            node = node?.next
        }
        return false
    }

    fun indexOf(value: Int): Int? {
        var node = head
        repeat(size) {
            if (node?.value == value) {
                return it
            }
            node = node?.next
        }
        return null
    }

    fun lastIndexOf(value: Int): Int? {
        var node = tail
        for (i in size - 1 downTo  0) {
            if (node?.value == value) {
                return i
            }
            node = node?.prev
        }
        return null
    }

    fun reverseIterator(): IntIterator = TODO()

    private fun getNode(index: Int): IntDoublyLinkedNode? {
        var node = head
        for (i in 0 until index) {
            node = node?.next
        }
        return node
    }
    private fun getLastNode(): IntDoublyLinkedNode? {
        return getNode(size - 1)
    }
}

//getSize(): Int v
//add(value: Int) v
//remove(value: Int) v
//get(index: Int): Int v
//insert(index: Int, value: Int) v
//removeAt(index: Int) v
//printList() v
//contains(value: Int): Boolean v