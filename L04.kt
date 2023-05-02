import kotlin.math.PI

// OOP (object-oriented programming): Reuse code
// Encapsulation: 封裝
// Inheritance: 繼承
//  - Abstract 抽象: 只有宣告，沒有實作
//  - interface 介面
//  * 一個類別只能有一個父類別，但可以實作多個介面(interface)
// Polymorphism: 多型
//  - Dynamic binding: 動態繫結

// 父類別
open class A {
    protected val size = 0

    open fun funA() {
        println("funA in class A")
    }
}

// 子類別
class B: A() {
    val desc: String = ""

    override fun funA() {
        println("funA in class B")
    }

    fun funB() {


    }
}

interface ShapeI {
    val area: Double

    fun printArea() {
        println("This shape has area: $area")
    }
}

interface ShapeJ {
    fun printArea() {
        println("This shape has no area")
    }
}

abstract class Shape: ShapeI, ShapeJ, Comparable<Shape> {
    abstract override val area: Double

//    abstract override fun printArea()

    override fun compareTo(other: Shape): Int {
        if (area > other.area) { return 1 }
        if (area < other.area) { return -1 }
        return 0
    }

    override fun printArea() {
        super<ShapeI>.printArea()
        super<ShapeJ>.printArea()
        println("This shape has area: $area")
    }
}
class Circle(val radius: Double): Shape() {
    override val area: Double
        get() = radius * radius * PI

    override fun equals(other: Any?): Boolean {
        val b = (other as? Circle) ?: return false
        return radius == b.radius
    }

    override fun printArea() {
        println("This circle has area: $area")
    }

    override fun toString(): String =
        "This is a circle with radius: $radius"

}
class Rectangle(val width: Double, val height: Double): Shape() {
    override val area: Double
        get() = width * height

    override fun printArea() {
        println("This rectangle has area: $area")
    }
}
//class Triangle: Shape()

fun main() {
    val a = A()
//    a.size
    a.funA()


    val b = B()
//    b.size
    b.funA()
//    b.funB()

    val q: A = B()
    q.funA()

    val s: Shape = Circle(3.0)
    s.printArea()
}
