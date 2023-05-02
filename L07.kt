import java.util.LinkedList
import java.util.Objects

// Multi-threading
//  1. Runnable and Thread
//  2. synchronized
//  3. wait and notify/notifyAll

// Kotlin
//  class `Any`

// Java
//  class `Object`

/*

public class L07Kt {
    public static class C {
    }

    public static void secondExam.main() {
        println("Hello world");
    }
}

 */

//class Runnable_0: Runnable {
//    override fun run() {
//        println("[${Thread.currentThread().name}] Hi")
//    }
//}

class MyThread: Thread() {
    override fun run() {
        println("[${Thread.currentThread().name}] Hi")
    }
}

private val lockA = Object()
private val lockB = Object()

private val queue = LinkedList<Runnable>()

class CustomizedThread: Thread() {
    // stop the thread gracefully
    var stopped: Boolean = true

    override fun start() {
        super.start()

        stopped = false
    }

    override fun run() {
        while (!stopped) {
            if (queue.isNotEmpty()) {
                val task = queue.removeFirst()
                task.run()
            }
            else {
                //<editor-fold desc="Method 1">
//                yield()
                //</editor-fold>

                //<editor-fold desc="Method 2">
                synchronized(lockA) {
                    lockA.wait() // 無止盡的等
//                    lockA.wait(1_000) // 只等1秒鐘
                }
                //</editor-fold>
            }

        }
    }
}

private inline val currentThread get() = "[${Thread.currentThread().name}] "

class TaskA: Runnable {
    override fun run() {
        synchronized(lockA) {
            // Here, means `lockA` is acquired.

            println("$currentThread Wait 5 seconds and try to acquire `lockB`")
            Thread.sleep(5_000)


            // Step 1.
            // Step 2.
            // Step 3.
            synchronized(lockB) {
                println("$currentThread `lockB` acquired")
            }
        }
    }
}

class TaskB: Runnable {
    override fun run() {
        synchronized(lockB) {

            println("$currentThread Wait 5 seconds and try to acquire `lockA`")
            Thread.sleep(5_000)

            synchronized(lockA) {
                println("$currentThread `lockA` acquired")
            }
        }
    }
}

fun main() {
//    val task = object: Runnable {
//        override fun run() {
//            println("[${Thread.currentThread().name}] Hi")
//        }
//    }
//    val task = Runnable_0()

//    val thread = Thread(task)
//    thread.start()

//    val thread = MyThread()
//    thread.start()
//
//    println("[${Thread.currentThread().name}] Hello world")
//    println("[${Thread.currentThread().name}] Hi")


    queue.add(Runnable {println("add1")})

    val thread = CustomizedThread()

    thread.start()

    queue.add(Runnable {println("add2")})

    print("dsdadsa")

    queue.add(Runnable {println("add3")})

    synchronized(lockA) {
        lockA.notify()
    }

//    deadlockDemo()
}

private fun deadlockDemo() {
    val threadA = Thread(TaskA())
    val threadB = Thread(TaskB())

    threadA.start()
    threadB.start()

    queue.add(Runnable { /* ... */ })
    synchronized(lockA) {
        lockA.notify()
        lockA.notifyAll()
    }

    println("DONE")
}

abstract class AbstractThreadPool(val capacity: Int) {
    abstract fun launch(task: ()-> Unit): Future<Any?>
    abstract fun stop()

}

interface Future<T> {
    fun join()
    fun await():T
}

