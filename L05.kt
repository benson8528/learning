
interface IntIterator {
    fun hasNext(): Boolean
    fun next(): Int
}

fun Shape.printMessage() {
    println("This is a shape")
}

//fun printMessage(`this`: Shape) {
//
//}

fun Circle.printMessage() {
    println("This is a circle")
}

fun binarySearch(array: Array<Int>, value: Int): Boolean {
    TODO()
}

abstract class BinarySearchTree {
    abstract fun add(value: Int)
    abstract fun remove(value: Int)
    abstract fun contains(value: Int): Boolean

    abstract fun println()
    abstract fun reversePrintln()

    abstract override fun equals(other: Any?): Boolean

    abstract fun iterator(): IntIterator
    abstract fun reverseIterator(): IntIterator
}

fun main() {
    val l = IntArrayList()

    val iter = l.iterator()
    while (iter.hasNext()) {
        iter.next()
    }

    Name()

    val circle = Circle(3.0)
    circle.printMessage()

    val shape: Shape = Circle(3.0)
    shape.printMessage()
}
