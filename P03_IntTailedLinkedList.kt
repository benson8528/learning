class IntTailedLinkedList {
    private var head: IntNode? = null
    private var tail: IntNode? = null
    var size: Int = 0
         private set

//    fun getSize(): Int {
//        return size
//    }

    fun add(value: Int) {
        val node = IntNode(value)
        tail?.next = node
        tail = node
        if (isEmpty) {
            head = node
        }
        size += 1

//        val node = IntNode(value)
//        if (size == 0) {
//            head = node
//        }
//        else {
//            tail!!.next = node
//        }
//        tail = node
//        size += 1
    }

    fun remove(value: Int) {
        while (head?.value == value) {
            head = head?.next
            size -= 1
        }
        if (isEmpty) {
            tail = null
            return
        }

        var prev = head!! // 前一個值不為value的node
        var node = prev.next

        while (node != null) {
            if (node.value == value) {
                prev.next = node.next
                size -= 1
            }
            else {
                prev = node
            }
            node = node.next
        }
        // Here, 需刪除的node都已刪除
        tail = prev
    }
    private fun getNode(index: Int): IntNode? {
        var node = head
        repeat(index) {
//        for (i in 0 until index) {
            node = node?.next
        }
        return node
    }

    fun get(index: Int): Int{
        if (index >= size || index < 0) {
            throw RuntimeException("Invalid index: $index, which should be between 0 and ${size - 1}")
        }
        return getNode(index)!!.value
    }

//    private val isEmpty: Boolean get() {
//        return size == 0
//    }
    val isEmpty get() = size == 0

    fun insert(index: Int, value: Int) {
        if (index > size || index < 0) {
            throw RuntimeException("Invalid index: $index, which should be between 0 and $size")
        }

        // 有順序之分時，別這樣寫
//        when (index) {
//            size -> add(value)
//            0 -> {
//                head = IntNode(value, head)
//                size += 1
//            }
//            else -> {
//                val prev = getNode(index - 1)!!
//                prev.next = IntNode(value, prev.next)
//                size += 1
//            }
//        }

        if (index == size) {
            add(value)
        }
        else if (index == 0) {
            head = IntNode(value, head)
            size += 1
        }
        else {
            val prev = getNode(index - 1)!!
            prev.next = IntNode(value, prev.next)
            size += 1
        }


        val node = IntNode(value)
        if (index == 0) {
            node.next = head
            head = node
        }
        else {
            val prev = getNode(index - 1)!!
            node.next = prev.next
            prev.next = node

        }
        if (index == size) {
            tail = node
        }
        size += 1
    }

    fun clear() {
        head = null
        tail = null
        size = 0
    }

    private val lastIndex get() = size - 1

    fun removeAt(index: Int) {
        if (index >= size || size < 0) {
            throw RuntimeException("Invalid index: $index, which should be between 0 and ${size - 1}")
        }
        if (index == 0) {
            if (head == tail) {
                tail = null
            }
            head = head?.next
        }
        else {
            val prev = getNode(index - 1)!!
            prev.next = prev.next?.next
            if (index == lastIndex) {
                tail = prev
            }
        }
        size -= 1
    }

    fun printList() {
        var node = head
        for (i in 0 until size) {
            print("${node?.value} ")
            node = node?.next
        }
        println()
    }

    fun contains(value: Int): Boolean {
        //TODO: Buggy
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
        var lastIndex: Int? = null
        var node = head
        repeat(size) {
            if (node?.value == value) {
                lastIndex = it
            }
            node = node?.next
        }
        return lastIndex
    }

    fun assertValues(vararg values: Int) {
        if (size != values.size) {
            throw RuntimeException("size mis-match")
        }
        var node = head
        repeat(size) { index ->
            if (node?.value != values[index]) {
                throw RuntimeException("${index}th value mis-match ${node?.value} != ${values[index]}")
            }
            node = node?.next
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