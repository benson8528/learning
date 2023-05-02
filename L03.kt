// -----------
// class: 自訂資料型態
// OOP 物件導向
//  1. encapsulation 封裝
//      1.1 限制存取

//  2. inheritance 繼承
//  3. polymorphism 多型
//      3.0 dynamic binding 動態繫結

// -----------
// 建構子 -
//  * 一個裝在 class 裡的特殊 function
//  * 目的: 生成物件

// -----------
// Function pointer (函數指標)

fun main() {

    val l = IntLinkedList()
    val func: (Int) -> Int = {
        it + 1
    }

    val min: (Int, Int) -> Int = { a, b ->
        if (a > b) {
            b
        } else {
            a
        }
    }

//    printResult(
//        { a, b ->
//            if (a > b) {
//                b
//            }
//            else {
//                a
//            }
//        }
//    )

    printResult(3, 4) { a, b ->
        a + b
    }

    val array = Array(5) {
        it + 1
    }

//    val node = IntNode(null)
}

//fun Array2(size: Int, func: (Int) -> Int) {
//
//}

class Array2(
    size: Int,
    func: (Int) -> Int
){

}

fun printResult(a: Int, b: Int, compare: (Int, Int) -> Int) {
    println(compare(a, b))
}

class DateTime {
    var year: Int = 1970
    var month: Int = 1
    var day: Int = 1
    var hour: Int = 0
    var minute: Int = 0
    var second = 0

//    fun addDay(amount: Int): DateTime {
//
//    }
}

interface IntList {
//    var size: Int // getter + setter
    val size: Int // getter

//    val isEmpty: Boolean get() = size == 0
    val lastIndex get() = size -1

    fun get(index: Int): Int

    fun add(value: Int)
    fun insert(index: Int, value: Int)
    fun remove(value: Int)
    fun removeAt(index: Int)

    override fun equals(other: Any?): Boolean

    operator fun contains(value: Int): Boolean {
        return indexOf(value) != null
    }

    fun indexOf(value: Int): Int? {
        val iter = iterator()
        for (i in 0 until size) {
            if (iter.next() == value) {
                return i
            }
        }
        return null
    }

    fun lastIndexOf(value: Int): Int? {
        val iter = iterator()
        var index: Int? = null
        for (i in 0 until size) {
            if (iter.next() == value) {
                index = i
            }
        }
        return index
    }

    fun printList() {
        val iter = iterator()
        while (iter.hasNext()) {
            print("${iter.next()} ")
        }
        println()
    }

    val Int.isValidIndex: Boolean get() = this in 0 until size

    fun assertValues(vararg values: Int) {
        if (values.size != size) {
            throw RuntimeException("size mis-match")
        }
        val iter = iterator()
        for (i in 0 until size) {
            val num = iter.next()
            if (num != values[i]) {
                throw RuntimeException("${i}th value mis-match $num != ${values[i]}")
            }
        }
    }

    fun assertRange(index: Int, from: Int, to: Int) {
        if (index !in from .. to) {
            throw RuntimeException("Invalid index: $from, which should be between 0 and $to")
        }
    }

    fun assertValidIndex(index: Int) {
//        if (!index.isValidIndex) {
//            throw ...
//        }

        assertRange(index, 0, lastIndex)
    }

    fun iterator(): IntIterator
}

//<editor-fold desc="語意相同">
//fun IntList.isEmpty(): Boolean {
//    return this.size == 0
//}

//fun isEmpty(`this`: IntList): Boolean {
//    return `this`.size == 0
//}
//</editor-fold>

val IntList.isEmpty: Boolean get() {
    return this.size == 0
}

inline val IntList.isNotEmpty: Boolean get() = !isEmpty

//class IntArrayList: IntList
//class LinkedList: IntList
//class LinkedListT: LinkedList()

class IntNode( // primary constructor
    val value: Int,
    var next: IntNode? = null
) {
    fun circuit(): IntNode {
        this.next = this
        return this
    }
}

/*

IntList
    +- AbstractIntList
        +- IntArrayList
        +- IntLinkedList
            +- IntLinkedListT
            +- IntCircuitLinkedList
        +- IntDLinkedList
            +- IntDLinkedListT
            +- IntCircuitDLinkedList
        +- IntCircuitLinkedListT

 */
