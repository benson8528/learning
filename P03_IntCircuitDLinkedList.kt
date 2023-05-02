class IntCircuitDLinkedListOld{
    var size = 0
        private set
    private var head: IntDoublyLinkedNode? = null

//    fun getSize(): Int {
//        return size
//    }

    private val tail: IntDoublyLinkedNode? get() = head?.prev

    fun add(value: Int) {
        if (size == 0) {
            val node = IntDoublyLinkedNode(value).circuit()
            head = node
        }
        else {
            val node = IntDoublyLinkedNode(value,tail,head)
            tail!!.next = node
            head!!.prev = node
        }
        size += 1
    }

    fun remove(value: Int) {
        var tailT = tail
        while (size > 0 && head!!.value == value) {
            head = head!!.next
            size -= 1
        }
        if (size == 0) {
            head = null
            return
        }
        head!!.prev = tailT
        tailT!!.next = head

//        // 剩下 head.value 不等於 value 且 size > 0
        var node = head!!.next
        while (node!! != head) {
            if (node.value == value) {
                node.next!!.prev = node.prev
                node.prev!!.next = node.next
                size -= 1
            }
            node = node.next
        }
    }

    fun printList() {
        var node = head
        for (i in 0 until size) {
            print("${node?.value} ")
            node = node?.next
        }
        println()
    }

    private fun getNode(index: Int): IntDoublyLinkedNode? {
        var node = head
        for (i in 0 until index) {
            node = node?.next
        }
        return node
    }

    fun get(index: Int): Int {
        if (index >= size || index < 0) {
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
                val node = IntDoublyLinkedNode(value,tail,head)
                head = node
            }
            else {
                val prev = getNode(index - 1)
                val node = IntDoublyLinkedNode(value, prev, prev!!.next)
                prev.next = node
                node.next!!.prev = node
            }
            size += 1
        }

    }
    fun removeAt(index: Int) {
        if (index >= size || index < 0) {
            throw RuntimeException("Invalid index, which should be between 0 and ${size - 1}")
        }

        val node = getNode(index)!!
        node.next!!.prev = node.prev
        node.prev!!.next = node.next
        size -= 1
        if (index == 0) {
            head = node.next
        }
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
        for (i in size - 1 downTo 0) {
            if (node?.value == value) {
                return i
            }
            node = node?.prev
        }
        return null
    }

    fun clear() {
        head = null
        size = 0
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

}

//getSize(): Int v
//add(value: Int) v
//remove(value: Int) v
//get(index: Int): Int v
//insert(index: Int, value: Int) v
//removeAt(index: Int) v
//printList() v
//contains(value: Int): Boolean v