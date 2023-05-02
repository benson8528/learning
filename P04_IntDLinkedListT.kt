class IntDLinkedListT(vararg values: Int): IntDLinkedList() {
    override val tail: IntDoublyLinkedNode? get() = _tail
    private var _tail: IntDoublyLinkedNode? = null

    init {
        if (values.isNotEmpty()) {
            head = IntDoublyLinkedNode(values[0])
            _tail = head

            for (i in 1 until values.size) {
                _tail?.next = IntDoublyLinkedNode(values[i], _tail)
                _tail = _tail?.next
            }
            _size = values.size
        }
    }

    override fun add(value: Int) {
        _tail = doAdd(value)
    }

    override fun remove(value: Int) {
        _tail = doRemove(value)
    }

    override fun removeAt(index: Int) {
        val prev = doRemoveAt(index)
        _tail = when (index) {
            lastIndex -> prev
            else -> _tail
        }
    }

    override fun clear() {
        super.clear()
        _tail = null
    }
}