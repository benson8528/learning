class IntTailedDLinkedList {
     var size = 0
         private set
    private var head: IntDoublyLinkedNode? = null
    private var tail: IntDoublyLinkedNode? = null


    private val isEmpty: Boolean get() = size == 0

//    fun getSize(): Int {
//        return size
//    }



    fun add(value: Int) {
        val node = IntDoublyLinkedNode(value, tail)
        if (isEmpty) {
            head = node
            tail = node
        }
        else {
            tail!!.next = node
            tail = node
        }
        size += 1
    }

    fun remove(value: Int) {
        while (head?.value == value) {
            head = head?.next

            head?.prev?.next = null
            head?.prev = null
            size -= 1
        }

        var node = head?.next

        while (node != null) {
            if (node.value == value) {
                node.prev?.next = node.next
                node.next?.prev = node.prev
                if (node == tail) {
                    tail = node.prev
                }
                size -= 1
            }
            node = node.next
        }

    }

    private fun getNode(index: Int): IntDoublyLinkedNode? {

        return if (index > size / 2) {
            var node = tail
            for (i in 0 until size - 1 - index) {
                node = node?.prev
            }
            node
        }
        else {
            var node = head
            for (i in 0 until index) {
                node = node?.next
            }
            node
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
    fun printListReversely() {
        var node = tail
        for (i in 0 until size) {
            print("${node?.value} ")
            node = node?.prev
        }
        println()
    }

    fun get(index: Int): Int {
        if (index >= size || index < 0){
            throw RuntimeException("Invalid index: $index, which should be between 0 and ${size - 1}")
        }
        return getNode(index)!!.value
    }

    fun insert(index: Int, value: Int) {
        if (index > size || index < 0) {
            throw RuntimeException("Invalid index: $index, which should be between 0 and $size")
        }
        val node = IntDoublyLinkedNode(value)

        if (index == 0) {
            head?.prev = node
            node.next = head
            head = node
        }
        else {
            val prev = getNode(index - 1)
            node.next = prev?.next
            node.next?.prev = node
            node.prev = prev
            prev?.next = node
        }

        if (index == size) {
            tail = node
        }
        size += 1
    }

    private val lastIndex: Int get() = size - 1

    fun removeAt(index: Int) {
        if (index >= size || index < 0) {
            throw RuntimeException("Invalid index: $index, which should be between 0 and ${size - 1}")
        }
        val node = getNode(index)
        if (index == 0) {
            head = head?.next
        }
        node?.next?.prev = node?.prev
        node?.prev?.next = node?.next
        if (index == lastIndex) {
            tail = node?.prev
        }
        size -= 1
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
        for (i in size - 1 downTo 0 ) {
            if (node?.value == value) {
                return i
            }
            node = node?.prev
        }
        return null
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
        head = null
        tail = null
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
//contains(value: Int): Boolean