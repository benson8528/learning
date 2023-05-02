class IntCircuitNode(
    val value: Int,
    next: IntCircuitNode? = null /* 參數的預設值 */
) {
    val next: IntCircuitNode
    init {
//        this.next =
//            if (next == null) {
//                this
//            }
//            else {
//                next
//            }

        this.next = next ?: this
    }
}

class IntCircuitLinkedListOld {
    var size = 0
        private set
    private var head: IntNode? = null

//    fun getSize(): Int {
//        return size
//    }

//    private val tail: IntNode? get() {
//        return getLastNode()
//    }
    private val tail: IntNode? get() = getLastNode()

    // Not the same thing
//    private val tail: IntNode? = getLastNode()

    fun add(value: Int) {
        val node = IntNode(value).circuit()
        if (size == 0) {
            head = node
        }
        else {
            node.next = head

            tail?.next = node
//            val last = tail!!
//            last.next = node
        }
        size += 1
    }

    // l.add(0)
    // l.remove(0)

    fun remove(value: Int) {
        if (size == 0) {
            return
        }

        //<editor-fold desc="Version 2">
//        while (size > 0 && head?.value == value) {
//            head = head?.next
//            size -= 1
//        }
////        tail?.next = head
//
//        if (size == 0) {
//            head = null
//        }
//        else {
//            var prev = head!! // 前一個值不為value的node
//            var node = head?.next
//
//            while (node != head) {
//                if (node?.value == value) {
//                    prev.next = node.next
//                    size -= 1
//                }
//                else {
//                    prev = node!!
//                }
//                node = node.next
//            }
//            // 不寫這行的前提: 在跑這個 else 前，list 有正確的循環
//            prev.next = head
//        }
//
//        tail?.next = head
        //</editor-fold>

        //<editor-fold desc="Version 1">
//        var node = head
//
//        while (node?.value == value ) {
//            val last = getLastNode()!!
//            head = node.next
//            node = head
//            last.next = head
//            if (size == 1 && head!!.value == value) {
//                head = null
//                size = 0
//                return
//            }
//            size -= 1
//        }
//
//
//        var prev = head!!
//        node = prev.next
//        while (node!! != head) {
//            if (node.value == value) {
//                prev.next = node.next
//                size -= 1
//            }
//            else {
//                prev = node
//            }
//            node = node.next
//        }
        //</editor-fold>

        while (size > 0 && head!!.value == value) {
            head = head!!.next
            size -= 1
        }
        if (size == 0) {
            head = null
            return
        }

        tail!!.next = head
        var prev = head!!
        var node = prev.next
        while (node != head) {
            if (node!!.value != value) {
                prev = prev.next!!
            }
            else {
                prev.next = node.next
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
            throw RuntimeException("Invalid index, which should be between 0 and $size")
        }

        //<editor-fold desc="Version 2">
        if (size == index) {
            this.add(value)
        }
        else {
            if (index == 0) {
                val tail = getLastNode()!!
                head = IntNode(value, head)
                tail.next = head
            }
            else {
                val prev = getNode(index - 1)
                prev?.next = IntNode(value, prev?.next)
            }

            size += 1
        }
        //</editor-fold>

        //<editor-fold desc="Version 1">
//        val node = IntNode(value).circuit()
//
//        if (size == 0) {
//            head = node
//            size = 1
//            return
//        }
//        if (index == 0 || index == size) {// insert 於頭或尾
//            val last = getLastNode()
//            last?.next = node
//            node.next = head
//            if (index == 0) {
//                head = node
//            }
//        }
//        else {
//            val prev = getNode(index - 1)
//            node.next =prev?.next
//            prev?.next = node
//        }
//        size += 1
        //</editor-fold>
    }

    fun removeAt(index: Int) {
        if (index >=size || index < 0) {
            return
        }
        if (index == 0) {
            if (size == 1) {
                //TODO: Buggy
                size = 0
                head = null
                return
            }
            head = head!!.next
            tail!!.next = head
        }
        else {
            val prev = getNode(index - 1)!!
            prev.next = prev.next!!.next
        }
        size -= 1
    }

    fun contains(value: Int): Boolean {
        return indexOf(value) != null

//        var node = head
//        for (i in 0 until size) {
//            if (node?.value == value) {
//                return true
//            }
//            node = node?.next
//        }
//        return false
    }

//    interface AAA {
//        fun f(a: Int, b: Int)
//    }
//
//    private fun forEachValue(a: AAA) {
//        var node = head
//        repeat(size) { index ->
//            a.f(index, node!!.value)
//            node = node?.next
//        }
//    }

    private inline fun forEachValue(consume: (Int, Int) -> Unit) {
        var node = head
        repeat(size) { index ->
            consume(index, node!!.value)
            node = node?.next
        }
    }

    fun printList() {
        forEachValue { _, value ->
            print(value)
        }

        println()

//        var node = head
//        for (i in 0 until size) {
//            print("${node?.value} ")
//            node = node?.next
//        }
//        println()
    }

    private fun getLastNode() = getNode(size - 1)

    private fun getNode(index: Int): IntNode? {
        var node = head
        for (i in 0 until index) {
            node = node?.next
        }
        return node
    }

    fun indexOf(value: Int): Int? {
        var node = head
        repeat(size) { index ->
            if (node?.value == value) {
                return index
            }
            node = node?.next
        }
        return null
    }

    fun lastIndexOf(value: Int): Int? {
        var index: Int? = null

        forEachValue { i, v ->
            if (value == v) {
                index = i
            }
        }

        return index


//        var index: Int? = null
//        var node = head
//        for (i in 0 until size) {
//            if (node?.value == value) {
//                index = i
//            }
//            node = node?.next
//        }
//        return index
    }

    fun assertValues(vararg values: Int) {
        if (size != values.size) {
            throw RuntimeException("size mis-match")
        }
        forEachValue { i, v ->
            if (v != values[i]) {
                throw RuntimeException("${i}th value mis-match ${v} != ${values[i]}")
            }
        }

//        var node = head
//        repeat(size) {
//            if (node?.value != values[it]) {
//                throw RuntimeException("${it}th value mis-match ${node?.value} != ${values[it]}")
//            }
//            node = node?.next
//        }
    }

    fun clear() {
        head = null
        size = 0
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