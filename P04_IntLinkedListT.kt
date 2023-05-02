class IntLinkedListT(vararg values: Int): IntLinkedList() {

    private var _tail: IntNode? = null

    override val tail: IntNode? get() = _tail

    init {
        if (values.isNotEmpty()) {
            head = IntNode(values[0])
            _tail = head
            for (i in 1 until values.size) {
                _tail?.next = IntNode(values[i])
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
        if (index == lastIndex) {
            _tail = prev
        }
    }

    override fun clear() {
        super.clear()
        _tail = null
    }
}