import java.util.LinkedList


class ThreadPool(val size: Int) : AbstractThreadPool(size) {
    var stopped = true
    private val taskQueue = LinkedList<Runnable>()
    private val lock = Object()
    private val threads = Array(size) {
        MyThread()
    }

//    init {
//        for (thread in threads) {
//            thread.start()
//        }
//    }
    internal fun addTask(task: Runnable) {
        taskQueue.add(task)
    }

    fun start() {
        stopped = false
        for (thread in threads) {
            thread.start()
        }
    }

    inner class MyThread() : Thread() {

//        override fun start() {
//            super.start()
//            stopped = false
//        }
        override fun run() {
            while (true) {
                val task =
                    synchronized(taskQueue) {
                        taskQueue.removeFirstOrNull()
                    }

                if (task != null) {
                    task.run()
                }
                else {
                    if (stopped) { break }

                    synchronized(lock) {
//                        printCurrentQueue("RC locked")
//                        println("$currentThread waits for next job")
                        lock.wait()
                    }
                }
            }
        }
    }
    private fun printCurrentQueue(message: String) {
        println("$currentThread $message; $taskQueue")
    }

    fun <R> async(task: () -> R): Future<R> {
        val futureTask = FutureTask(task)
        if (!stopped) {
            synchronized(taskQueue) {
                taskQueue.add(futureTask)
            }
            synchronized(lock) {
                lock.notify()
            }
        }
        return futureTask
    }

    override fun launch(task: () -> Unit): Future<Any?> {
        val futureTask = FutureTask<Any?>(task)
        if (!stopped) {
            // StringBuilder - not thread-safe
            // StringBuffer - thread-safe
            synchronized(taskQueue) {
                taskQueue.add(futureTask) // not thread-safe
//                printCurrentQueue("Queue locked")
            }
            synchronized(lock) {
//                printCurrentQueue("RC locked")
                lock.notify()
            }
        }
        return futureTask
    }

    override fun stop() {
        stopped = true
        synchronized(lock) {
            lock.notifyAll()
        }
    }

    fun assertThread() {
        val threadList: Array<String>
    }



}

private class FutureTask<R>(
    val task: () -> R

): Future<R>, Runnable {
    private var completed = false
    private var result: R? = null
    private val lock = Object()
    override fun join() {
        if (!completed) {
            synchronized(lock) {
                lock.wait()
            }
        }
    }

    override fun await(): R {
        if (!completed) {
            synchronized(lock) {
                lock.wait()
            }
        }
        return result!!
    }

    override fun run() {
        result = task()
        completed = true
        synchronized(lock) {
            lock.notify()
        }
    }
}

//private fun <T, P, R> LinkedList<T>.println(p: P): R {
//
//}

private fun <E> LinkedList<E>.removeFirstOrNull(): E? {
    if (isNotEmpty()) {
        return removeFirst()
    }
    return null
}

private inline val currentThread get() = "[${Thread.currentThread().name}] "

data class TestTask(
    private var done: Boolean = false
): Runnable {
    val isExecuted: Boolean get() = done

    override fun run() {
        done = true
    }
}

fun testThreadPool() {

    val pool = ThreadPool(10)
//    pool.start()

    val tasks = Array<TestTask>(10) {
        TestTask()
    }
    for (task in tasks) {
        pool.addTask(task)
    }

    pool.start()

    for (task in tasks) {
        if (!task.isExecuted) {
            throw RuntimeException("Task didn't executed")
        }
    }

    pool.stop()
}

fun main() {

//    testThreadPool()
//    println("PASS")
//    var task: Runnable? = TestTask()
//    task?.run() ?: {
//        println(">>> ")
//    }

    val pool = ThreadPool(10)
    pool.start()
//
//    pool.async { Thread.sleep(2000); println("${currentThread}a") }
//    pool.async { println("${currentThread}b"); Thread.sleep(5000) }
//    pool.async { println("${currentThread}c"); Thread.sleep(5000) }
//    pool.async { println("${currentThread}d") }
//    pool.async { println("${currentThread}e") }
//    pool.async { println("${currentThread}f") }
//    pool.stop()
//    pool.async { println("${currentThread}g") }
//    pool.async { println("${currentThread}h") }
//    pool.async { println("${currentThread}i") }

//    pool.stop()
    // join
//    val l = IntArrayList()


    val future1 = pool.async { Thread.sleep(2000);println("1"); 1}
    val future2 = pool.async { Thread.sleep(1000);println(2); 2 }
//    val future3 = pool.async { print("123");Thread.sleep(1000) }


    println(future1.await() + future2.await())

//    future2.join()
//    future2?.join()
//    future3?.join()
//
//    pool.stop()
    print("done")
    pool.stop()
}
