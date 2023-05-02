open class IntDLinkedList(vararg values: Int) : IntList {
    protected var head: IntDoublyLinkedNode? = null
    protected open val tail get() = getNode(lastIndex)
    override val size get() = _size
    protected var _size = 0

    init {
        if (values.isNotEmpty()) {
            head = IntDoublyLinkedNode(values[0])
            var _tail = head
            for (i in 1 until values.size) {
                _tail?.next = IntDoublyLinkedNode(values[i], _tail)
                _tail = _tail?.next
            }
            _size = values.size
        }
    }

    protected inline fun forEachValue(consume: (Int, Int) -> Unit) {
        var node = head
        repeat(size) { index ->
            consume(index, node!!.value)
            node = node?.next
        }
    }

    protected fun getNode(index: Int): IntDoublyLinkedNode? {
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

    protected open fun doAdd(value: Int): IntDoublyLinkedNode? {
        if (isEmpty) {
            head = IntDoublyLinkedNode(value)
            _size += 1
            return head
        } else {
            val _tail = tail
            _tail?.next = IntDoublyLinkedNode(value, _tail)
            _size += 1
            return _tail?.next
        }
    }

    override fun add(value: Int) {
        doAdd(value)
    }

    override fun insert(index: Int, value: Int) {
        assertRange(index, 0, _size)
        if (index == size) {
            add(value)
        } else if (index == 0) {
            val node = IntDoublyLinkedNode(value, next = head)
            head?.prev = node
            head = node
            _size += 1
        } else {
            val prev = getNode(index - 1)
            val node = IntDoublyLinkedNode(value, prev, prev?.next)
            prev?.next?.prev = node
            prev?.next = node
            _size += 1
        }
    }

    protected open fun doRemove(value: Int): IntDoublyLinkedNode? {
        var prev: IntDoublyLinkedNode? = null
        while (head?.value == value) {
            head?.next?.prev = null
            head = head?.next
            head?.prev?.next = null
            _size -= 1
        }
        prev = head
        var node = head?.next
        while (node != null) {
            if (node.value == value) {
                node.next?.prev = node.prev
                node.prev?.next = node.next
                node.prev = null
                _size -= 1
            }
            prev = node
            node = node.next
        }
        return prev
    }

    override fun remove(value: Int) {
        doRemove(value)
    }

    protected open fun doRemoveAt(index: Int): IntDoublyLinkedNode? {
        assertValidIndex(index)
        val node = getNode(index)!!
        if (index == 0) {
            head = head?.next
        }
        node.next?.prev = node.prev
        node.prev?.next = node.next
        _size -= 1
        return node.prev
    }

    override fun removeAt(index: Int) {
        doRemoveAt(index)
    }

    override fun indexOf(value: Int): Int? {
        forEachValue { i, v ->
            if (v == value) {
                return i
            }
        }
        return null
    }

    override fun lastIndexOf(value: Int): Int? {
        var index: Int? = null
        forEachValue { i, v ->
            if (v == value) {
                index = i
            }
        }
        return index
    }

    override fun printList() {
        forEachValue { i, v ->
            print("$v ")
        }
        println()
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


    open fun clear() {
        head = null
        _size = 0
    }

    inner class IntDLinkedListIterator : IntIterator {
        private var index = 0
        private var node = head
        override fun hasNext(): Boolean {
            return index < _size
        }

        override fun next(): Int {
            if (!hasNext()) {
                throw RuntimeException("Iterator has no next item.")
            }
            val value = node!!.value
            node = node?.next
            index++
            return value
        }
    }

    inner class ReverseIntDLinkedListIterator : IntIterator {
        private var index = lastIndex
        private var node = tail
        override fun hasNext(): Boolean {
            return index < 0
        }

        override fun next(): Int {
            if (!hasNext()) {
                throw RuntimeException("Iterator has no next item.")
            }
            val value = node!!.value
            node = node?.prev
            index--
            return value
        }
    }

    fun reverseIterator(): IntIterator {
        return ReverseIntDLinkedListIterator()
    }

    override fun iterator(): IntIterator {
        return IntDLinkedListIterator()
    }

}