class IntCircuitDLinkedList(vararg values: Int): IntDLinkedList() {
    init {
        if (values.isNotEmpty()) {
            head = IntDoublyLinkedNode(values[0]).circuit()
            var _tail = head
            for (i in 1 until values.size) {
                _tail?.next = IntDoublyLinkedNode(values[i], _tail, head)
                _tail = _tail?.next
            }
            head?.prev = _tail
            _size = values.size
        }
    }

    override fun add(value: Int) {
        val node = doAdd(value)
        node?.next = head
        head?.prev = node
    }

    override fun insert(index: Int, value: Int) {
        assertRange(index, 0, _size)
        if (index == size) {
            add(value)
        } else if (index == 0) {
            val _tail = tail
            val node = IntDoublyLinkedNode(value, _tail, head)
            head?.prev = node
            _tail?.next = node
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

    override fun doRemoveAt(index: Int): IntDoublyLinkedNode? {
        if (index == 0) {
            if (_size == 1) {
                _size = 0
                head = null
                return null
            }
            val _tail = tail
            head = head!!.next
            head?.prev = _tail
            _tail?.next = head
            _size -= 1
            return _tail
        } else {
            val prev = getNode(index - 1)!!
            prev.next = prev.next!!.next
            prev.next?.prev = prev
            _size -= 1
            return prev
        }
    }

    override fun doRemove(value: Int): IntDoublyLinkedNode? {
        while (_size > 0 && head?.value == value) {
            head = head?.next
            _size -= 1
        }
        if (_size == 0) {
            head = null
            return null
        }
        val _tail = tail
        _tail!!.next = head
        head!!.prev = _tail

        var node = head!!.next!!
        while (node != head) {
            if (node.value == value) {
                node.prev!!.next = node.next
                node.next!!.prev = node.prev
                _size -= 1
            }
            node = node.next!!
        }
        return head!!.prev
    }
}