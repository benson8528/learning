open class IntCircuitLinkedList(vararg values: Int): IntLinkedList() {
    init {
        if (values.isNotEmpty()) {
            head = IntNode(values[0]).circuit()
            var _tail = head
            for (i in 1 until values.size) {
                val node = IntNode(values[i], head)
                _tail?.next = node
                _tail = node
            }
            _size = values.size
        }
    }
    override fun doAdd(value: Int): IntNode {
        val tail = super.doAdd(value)
        tail.next = head
        return tail
    }

    override fun insert(index: Int, value: Int) {
        assertRange(index,0, _size)
        if (index == _size) {
            add(value)
        }
        else if (index == 0) {
            head = IntNode(value, head)
            _size += 1
            tail!!.next = head
        }
        else {
            val prevNode = getNode(index - 1)
            prevNode?.next = IntNode(value, prevNode?.next)
            _size += 1
        }
    }

    /**
     * 刪除指定位置的值，並回傳前一個節點。
     * 回傳值可用於重設`tail`的相關資訊。
     *
     * (此為循環鏈結串列，head的前一個節點為tail)
     * @return 被刪除的前一個節點
     */
    override fun doRemoveAt(index: Int): IntNode? {
        if (index == 0) {
            return if (_size == 1) {
                _size = 0
                head = null

                null
            }
            else {
                val _tail = tail
                head = head!!.next
                _tail!!.next = head
                _size -= 1

                _tail
            }
        }
        else {
            val prev = getNode(index - 1)!!
            prev.next = prev.next!!.next
            _size -= 1
            return prev
        }
    }

    override fun doRemove(value: Int): IntNode? {
        while (_size > 0 && head?.value == value) {
            head = head?.next
            _size -= 1
        }
        if (_size == 0) {
            head = null
            return null
        }

        tail!!.next = head

        var prev = head!!
        var node = prev.next!!
        while (node != head) {
            if (node.value == value) {
                prev.next = node.next
                _size -= 1
            }
            else {
                prev = prev.next!!
            }
            node = node.next!!
        }
        return node
    }
}