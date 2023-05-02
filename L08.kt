import kotlinx.coroutines.*

// Generic
//  1. Generic class
//  2. Generic function

// Kotlin 內建 let, run, with, also, apply

data class Triple<A, B, C>(
    val first: A,
    val second: B,
    val third: C
)

interface GenericList<T: Shape, R, Q, P> {
    fun add(value: T)
    fun insert(index: Int, value: T)

    fun remove(value: T)
    fun removeAt(index: Int)

    operator fun contains(value: T): Boolean

    operator fun get(index: Int): T

    //...
}



// wild-card
fun <T: Comparable<*>> equals(a: T?, b: T?): Boolean {
    return if (a == null) {
        b == null
    }
    else {
        a == b
    }
}

//fun createList(): GenericList<Circle> {
//    TODO()
//}

//fun <R> measure(block: () -> R): R {
//    val start = System.currentTimeMillis() // '1970-01-01 00:00:00'
//    val result = block()
//    val end = System.currentTimeMillis()
//    println("This block requires ${end - start} milli-seconds to execute")
//
//    return result
//}
suspend fun printHello(count: Int) = coroutineScope {
    println("Hello $count")
    delay(1000)
}

suspend fun taskA() {
    delay(1000)
    println("taskA")
}
fun main() = runBlocking {
    val job = launch {
        repeat(5) {
            printHello(it)
            println("${it + 1}")
        }
    }


    repeat(3) {
        println("--- $it")
        delay(1000)
    }


    job.join()
    println("Complete")
}




//fun secondExam.main() {
////    val list = createList()
//
////    list.add("1")
//
//    val result: String = measure {
//        Thread.sleep(1000)
//
//        "Hello world"
//    }
//
////    val name = Triple("JK", "", "Rolling")
//    var name: Triple<String, Int, Double>? = null
//    var name2: Triple<String, String, Int>? = null
//
////    name2 = name
//
//    var general: Triple<String, *, *>? = name2
//
//
//}
