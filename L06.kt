// 1. class `Any`
// 2. Methods in class `Any`
//  2.1 `equals`, `==`, `===`
//  2.2 `toString`
// 3. Operator
// 4. data class

data class Book(
    val title: String,
    val author: String,
) {
    constructor(): this("Untitled", "Unknown")

    override fun equals(other: Any?): Boolean {
        val b = (other as? Book) ?: return false
        return title == b.title && author == b.author
    }

//    fun component1() = title
//    fun component2() = author

    override fun toString(): String {
        return super.toString()
    }
}

fun greet(name: String) = "Hello $name"

fun main() {
    // `+`, `-`, `*`, `/`, `==`, `in`, `[]`, `-`, `+`, `++`, `--`, `%`, `..`

//    val a = "Hello world"
//    val b = greet("world")
//    println("a = '$a'")
//    println("b = '$b'")
//    println(a == b)
//    println(a === b)

    val circle = Circle(3.0)
    println(circle)

    val rectangle = Rectangle(3.0, 5.0)
    println(rectangle)

    val book = Book("Harry Potter", "Unknown")

    val p = Point2D(10, 20)
    val p2 = Point2D(10, 20)
    println(p)
    println(p == p2) // `===`

//    val (x, y) = p
}

data
class Point2D(val x: Int, val y: Int)

