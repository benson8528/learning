class IntCircuitLinkedList_tail {
    var size = 0
        private set
    private var tail: IntNode? = null

    private val head: IntNode? get() = tail?.next

//    fun getSize(): Int {
//        return size
//    }

    fun dd() {

    }
    fun add(value: Int) {
        val node: IntNode
        if (size == 0) {
            node = IntNode(value).circuit()
        }
        else {
            node = IntNode(value,tail!!.next)
            tail!!.next = node
        }
        tail = node
        size += 1
    }

    fun remove(value: Int) {
        if (size == 0) {
            return
        }
        var node = tail!!.next
        while (size > 0 && node!!.value == value ) {
            node = node.next
            size -= 1
        }

        if (size == 0) {
            tail = null
            return
        }

        tail!!.next = node
        var prev = node!!
        node = node.next
        while (node != tail!!.next) {
            if (node!!.value == value) {
                prev.next = node.next
                size -= 1
            }
            else {
                prev = node
            }
            node = node.next
        }
        tail = prev
    }

    fun get(index: Int): Int {
        if (index > size || index < 0) {
            throw RuntimeException("Invalid index, which should be between 0 and ${size - 1}")
        }
        return getNode(index)!!.value
    }

    fun insert(index: Int, value: Int) {
        if (index > size || index < 0) {
            throw RuntimeException("Invalid index, which should be between 0 and $size")
        }
        if (index == size) {
            add(value)
        }
        else {
            if (index == 0) {
                val node = IntNode(value,tail!!.next!!)
                tail!!.next = node
            }
            else {
                val prev = getNode(index - 1)
                prev!!.next = IntNode(value,prev.next)
            }
            size += 1
        }
    }

    private val lastIndex get() = size - 1

    fun removeAt(index: Int) {
        if (index >=size || index < 0) {
            return
        }
        if (index == 0) {
            if (size == 1) {
                size = 0
                tail = null
                return
            }
            tail!!.next = tail!!.next!!.next
        }
        else {
            val prev = getNode(index - 1)!!
            prev.next = prev.next!!.next
            if (index == size) {
                tail = prev
            }
        }
        size -= 1
    }

    fun contains(value: Int): Boolean {
        var node = tail
        for (i in 0 until size) {
            if (node?.value == value) {
                return true
            }
            node = node?.next
        }
        return false
    }

    fun printList() {
        var node = tail?.next
        for (i in 0 until size) {
            print("${node?.value} ")
            node = node?.next
        }
        println()
    }

    private fun getNode(index: Int): IntNode? {
        var node = tail?.next
        for (i in 0 until index) {
            node = node?.next
        }
        return node
    }

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

    fun clear() {
        size = 0
        tail = null
    }

    fun indexOf(value: Int): Int? {
        var node = tail?.next
        repeat(size) {
            if (node?.value == value) {
                return it
            }
            node = node?.next
        }
        return null
    }

    fun lastIndexOf(value: Int): Int? {
        var index: Int? = null
        var node = tail?.next
        repeat(size) {
            if (node?.value == value) {
                index = it
            }
            node = node?.next
        }
        return index
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