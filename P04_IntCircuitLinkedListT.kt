class IntCircuitLinkedListT(vararg values: Int) : IntList {

    private var tail: IntNode? = null

    private inline val head get() = tail?.next

    private var _size = 0

    override val size: Int get() = _size

    init {
        if (values.isNotEmpty()){
            val _head = IntNode(values[0]).circuit()
            tail = _head
            for (i in 1 until values.size) {
                tail?.next = IntNode(values[i], _head)
                tail = tail?.next
            }
            _size = values.size
        }
    }


    private inline fun forEachValue(consume: (Int, Int) -> Unit) {
        var node = head
        for (i in 0 until size) {
            consume(i, node!!.value)
            node = node.next
        }
    }

    private fun getNode(index: Int): IntNode? {
        var node = head
        for (i in 0 until index) {
            node = node?.next
        }
        return node
    }
    override fun get(index: Int): Int {
        assertValidIndex(index)
        return getNode(index)!!.value
    }

    override fun add(value: Int) {
        isEmpty
        val node: IntNode
        if (size == 0) {
            node = IntNode(value).circuit()
        } else {
            node = IntNode(value, tail!!.next)
            tail!!.next = node
        }
        tail = node
        _size += 1
    }

    override fun insert(index: Int, value: Int) {
        assertRange(index, 0, _size)

        if (index == size) {
            add(value)
        } else {
            if (index == 0) {
                val node = IntNode(value, tail!!.next!!)
                tail!!.next = node
            } else {
                val prev = getNode(index - 1)
                prev!!.next = IntNode(value, prev.next)
            }
            _size += 1
        }
    }

    override fun remove(value: Int) {
        var node = tail!!.next
        while (size > 0 && node!!.value == value) {
            node = node.next
            _size -= 1
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
                _size -= 1
            } else {
                prev = node
            }
            node = node.next
        }
        tail = prev
    }

    override fun removeAt(index: Int) {
        assertValidIndex(index)
        if (index == 0) {
            if (size == 1) {
                _size = 0
                tail = null
                return
            }
            tail!!.next = tail!!.next!!.next
        } else {
            val prev = getNode(index - 1)!!
            prev.next = prev.next!!.next
            if (index == size) {
                tail = prev
            }
        }
        _size -= 1
    }

    override fun indexOf(value: Int): Int? {
        forEachValue { i, v ->
            if (value == v) {
                return i
            }
        }
        return null
    }

    override fun lastIndexOf(value: Int): Int? {
        var index: Int? = null
        forEachValue { i, v ->
            if (value == v) {
                index = i
            }
        }
        return index
    }

    override fun equals(other: Any?): Boolean {
        val list = (other as? IntArrayList) ?: return false
        val iterB = list.iterator()
        val iterA = this.iterator()

        while (iterA.hasNext()) {
            if (iterA.next() != iterB.next()) {
                return false
            }
        }
        return true
    }

    override fun printList() {
        forEachValue { _, v ->
            print("$v ")
        }
        println()
    }

    inner class IntCircuitLinkedListTIterator : IntIterator {
        var node = head
        var index = 0
        override fun hasNext(): Boolean {
            return index < _size
        }

        override fun next(): Int {
            val value = node!!.value
            if (!hasNext()) {
                throw RuntimeException("Iterator has no next item.")
            }
            index++
            node = node?.next
            return value
        }
    }

    override fun iterator(): IntIterator {
        return IntCircuitLinkedListTIterator()
    }

    fun clear() {
        tail = null
        _size = 0
    }
}