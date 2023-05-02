// 1. Property in a class (getter/setter)
//      1.1 Backing field
// 2. vararg: various arguments
// 3. Function call by assigning parameter's name
// 4. Error handling 例外處理 (throw, try, catch, finally)
//      4.1 throw: 丟出錯誤
//      4.2 try-catch(-finally): 接錯誤

private const val BUFFER_SIZE = 5

abstract class AbstractIntList: IntList {
    private val pairs: List<Pair<Int, List<Int>>> = emptyList()

    override fun equals(other: Any?): Boolean {
        val l = (other as? IntList) ?: return false

        if (l.size != size) { return false }

        val iterA = iterator()
        val iterB = l.iterator()
        repeat(size) {
            if (iterA.next() != iterB.next()) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return size
    }
}

class IntArrayList(vararg values: Int) : AbstractIntList() {
    private var _size: Int = 0

    override val size: Int get() = _size

    private inline val isFull: Boolean get() = (size == array.size)

    private var array: Array<Int>

    init {
        _size = values.size
        array = Array(values.size + BUFFER_SIZE) {
            when {
                it < values.size -> values[it]
                else -> 0
            }
        }
    }

    override fun add(value: Int) {
        if (isFull) {
            expandArray()
        }
        array[size] = value
        _size += 1
    }

    override fun insert(index: Int, value: Int) {
        assertRange(index, 0, size)
        if (isFull) {
            expandArray()
        }
        for (i in size downTo index + 1) {
            array[i] = array[i - 1]
        }
        array[index] = value
        _size += 1
    }

    // this.size <= array.size
    // If this.size == array.size, then it's full.

    override fun remove(value: Int) {
        //TODO: 不要產生新陣列
        var count = 0
        for (i in 0 until _size) {
            if (array[i] == value) {
                count++
            } else {
                array[i - count] = array[i]
            }
        }
        _size -= count
    }

    override fun removeAt(index: Int) {
        assertValidIndex(index)
        for (i in index until lastIndex) {
            array[i] = array[i + 1]
        }
        _size -= 1
    }

    override fun get(index: Int): Int {
        assertValidIndex(index)
        return array[index]
    }

    override fun equals(other: Any?): Boolean {
        val list = (other as? IntArrayList) ?: return false
        val iterA = this.iterator()
        val iterB = list.iterator()

        while (iterA.hasNext()) {
            if (iterA.next() != iterB.next()) {
                return false
            }
        }
        return true
    }


    fun sort() {
        // Bubble sort
        for (i in size - 1 downTo 0) {
            var swapped = false

            // Count the number of elements that's already in correct ordering.
//            var count = 0
            for (j in 0 until i) {
                if (array[j] > array[j + 1]) {
                    array.swap(j, j + 1)
                    swapped = true
                }
            }
            if (!swapped) { break }
        }
    }

    private fun Array<Int>.swap(i: Int, j: Int) {
        require(i in indices)
        require(j in indices)

        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }

    fun binarySearch(value: Int): Boolean {
        if (isEmpty) { return false }
        if (get(0) == value || get(lastIndex) == value) { return true }

        //
        var startIndex = 0
        var endIndex = lastIndex
        var midIndex = endIndex / 2
        while (startIndex != endIndex) {
            when {
                value > array[midIndex] -> {
                    startIndex = midIndex
                    midIndex = (midIndex + endIndex) / 2
                }
                value < array[midIndex] -> {
                    endIndex = midIndex
                    midIndex = (midIndex + startIndex) / 2
                }
                else -> return true
            }
        }
        return false
    }

    fun clear() {
        _size = 0
        array = Array(BUFFER_SIZE) { 0 }
    }

    override fun iterator(): IntIterator {
        return IntArrayListIterator()
    }

    // nested class 巢狀類別
    private class A

    // inner class
    inner class IntArrayListIterator : IntIterator {
        private var nextIndex = 0

        override fun hasNext(): Boolean {
            return nextIndex < this@IntArrayList.size
        }

        override fun next(): Int {
            if (!hasNext()) {
                throw RuntimeException("There's no next element")
            }
            return array[nextIndex++]
        }
    }


    private fun expandArray() {
        array = Array(array.size + BUFFER_SIZE) {
            when {
                it < array.size -> array[it]
                else -> 0
            }
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

//indexOf(value: Int): Int?
//lastIndexOf(value: Int): Int?

private fun testBinarySearch(size: Int) {
//    val random = Random(System.currentTimeMillis())
    val l = IntArrayList()
    repeat(size) {
        l.add(it)
    }

    l.printList()

    for (i in 0 until size) {
        val check = l.get(i)
        if (l.contains(check)) {
            print("$check ")
            require(l.binarySearch(check))
        }
        else {
            require(!l.binarySearch(check))
        }
    }
    println()

}

fun main() {
//    val l = IntArrayList(1, 3, 4, 5, 2)
//
//    l.sort()
//    l.printList()


    testBinarySearch(10)
    testBinarySearch(11)

//    testBinarySearch(50)


//    l.printList()
//
//    val iter = l.iterator()
//    val iter2 = l.iterator()
//
//    while (iter.hasNext()) {
//        println(iter.next())
//    }
//    println("----")
//    while (iter2.hasNext()) {
//        println(iter2.next())
}
//    val arr = IntArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9)
//
//    arr.binarySearch(4)

//    val l = IntCircuitLinkedListT()
//    l.insert(0, 2)
//    l.assertValues(2)
//    l.insert(0, 1)
//    l.assertValues(1, 2)
//    l.insert(l.size, 3)
//    l.assertValues(1, 2, 3)
//    l.insert(2, 4)
//    l.assertValues(1, 2, 4, 3)
//    // 從零開始 insert、insert 頭尾、中間
//
//    l.removeAt(0)
//    l.assertValues(2, 4, 3)
//    l.removeAt(l.size - 1)
//    l.assertValues(2, 4)
//    l.removeAt(0)
//    l.assertValues(4)
//    l.removeAt(0)
//    l.assertValues()
//    //removeAt 頭尾
//    repeat(3) { l.add(3) }
//    repeat(4) { l.add(it) }
//    l.removeAt(3)
//    l.assertValues(3, 3, 3, 1, 2, 3)
////    l.assertValuesReversely(3, 3, 3, 1, 2, 3)
//    //removeAt 中間
//
//    l.insert(4, 3)
//    l.insert(4, 3)
//    if (!l.contains(3)) {
//        print("l.contains1 ERROR")
//    }
//    l.printList()
//    l.remove(3)
//    l.printList()
//    l.assertValues(1, 2)
//    //remove 頭尾、中間
//    if (!(l.contains(1) && l.contains(2)) || l.contains(3)) {
//        print("l.contains2 ERROR")
//    }
//    //contains
//    l.clear()
//
//    repeat(5) {
//        l.add(2)
//    }
//    l.remove(2)
//    l.assertValues()
//    l.add(5)
//    l.assertValues(5)
//    //remove 全部
//
//
//    l.clear()
//    repeat(4) {
//        l.add(it + 1)
//    }
//    repeat(3) {
//        l.add(3 - it)
//    }
//
//    if (l.lastIndexOf(2) != 5 || l.indexOf(2) != 1) {
//        println("${l.lastIndexOf(2)}, ${l.indexOf(2)}, wrong")
//    }
//    println("PASS")

//    l.removeAt(0)

//    try {
//        l.removeAt(0)
//
//        println("Successfully remove the first element")
//
////        println("DONE")
//    }
//    catch (xpt: RuntimeException) {
//        println("Error while remove the first element")
//
////        xpt.printStackTrace(System.out)
//        xpt.printStackTrace()
//
////        println("DONE")
//    }
//    finally {
//        println("DONE")
//    }

