open class IntLinkedList(vararg values: Int) : IntList {

    protected var head: IntNode? = null
    override val size: Int get() = _size
    protected var _size: Int = 0

    init {
        if (values.isNotEmpty()) {
            //<editor-fold desc="Version 2">
            for (index in values.size - 1 downTo 0) {
                this.head = IntNode(values[index], this.head)
            }
            this._size = values.size
            //</editor-fold>

            //<editor-fold desc="Version 1">
//            head = IntNode(values[0])
//            var tail = head
//
//            for (index in 1 until values.size) {
//                tail?.next = IntNode(values[index])
//                tail = tail?.next
//            }
//
//            _size = values.size
            //</editor-fold>
        }
    }

    constructor(l: IntLinkedList) : this() {
        if (l.isNotEmpty) {
            head = IntNode(l.head!!.value)
            var tail = head

            var node = l.head?.next
            while (node != null) {
                tail?.next = IntNode(node.value)

                tail = tail?.next
                node = node.next
            }

            _size = l._size
        }
    }

    protected fun getNode(index: Int): IntNode? {
        var node = head
        for (i in 0 until index) {
            node = node?.next
        }
        return node
    }

    protected open val tail: IntNode? get() = getNode(lastIndex)

    override fun get(index: Int): Int {
        assertValidIndex(index)
        return getNode(index)!!.value
    }


    protected inline fun forEachValue(consume: (Int, Int) -> Unit) {
        // utility function
        var node = head
        repeat(size) { index ->
            consume(index, node!!.value)
            node = node?.next
        }
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

    /**
     * @return 新增的節點
     */
    protected open fun doAdd(value: Int): IntNode {
        val node = IntNode(value)
        if (_size == 0) {
            head = node
        } else {
            tail?.next = node
        }
        _size += 1
        return node
    }

    override fun add(value: Int) {
        doAdd(value)
    }

    override fun insert(index: Int, value: Int) {
//        if (index.isInvalidIndex) {
//            throw ...
//        }
        assertRange(index, 0, _size)
        // 若改變條件的順序，有tail版的insert需被覆寫
        if (index == _size) {
            add(value)
        } else if (index == 0) {
            head = IntNode(value, head)
            _size += 1
        } else {
            val prevNode = getNode(index - 1)
            prevNode?.next = IntNode(value, prevNode?.next)
            _size += 1
        }
    }

    /**
     * 刪除指定位置的值，並回傳前一個節點。
     * 回傳值可用於重設`tail`的相關資訊。
     *
     * @return 被刪除的前一個節點
     */
    protected open fun doRemoveAt(index: Int): IntNode? {
        assertValidIndex(index)
        _size -= 1
        return if (index == 0) {
            val next = head?.next
            head?.next = null
            head = next
//            head
            null
        } else {
            val prev = getNode(index - 1)
            prev?.next = prev?.next?.next
            prev
        }
    }

    override fun removeAt(index: Int) {
        doRemoveAt(index)
    }

    /**
     * @return 回傳 tail (最後一個節點)
     */
    protected open fun doRemove(value: Int): IntNode? {
        while (head != null && head?.value == value) {
            head = head?.next
            _size -= 1
        }
        // 至此，有兩種可能:
        //  1. size == 0: 整個 list 的值都是指定的 value, head = null
        //  2. size != 0: 頭一個值不是指定的 value
        var prev = head
        var node = prev?.next

        while (node != null) {
            if (node.value == value) {
                prev?.next = node.next
                _size -= 1
            } else {
                prev = node
            }
            node = node.next
        }
        return when {
            isEmpty -> null
            else -> prev
        }
    }

    override fun remove(value: Int) {
        doRemove(value)
    }

//    override fun printList() {
//        forEachValue { i, v ->
//            print("$v ")
//        }
//        println()
//    }

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

//    private inline val Int.isValidIndex get() = this in 0 until  this@IntLinkedList.size
//    private inline val Int.isInvalidIndex get() = !isValidIndex

//    override fun assertValues(vararg values: Int) {
//        assertSize(values.size)
//        forEachValue { i, v ->
//            if (values[i] != v) {
//                assertValue(i, v, values[i])
//            }
//        }
//    }

    override fun iterator(): IntIterator {
        return IntLinkedListIterator()
    }

    inner class IntLinkedListIterator : IntIterator {
        private var node = head
        private var index = 0
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
}