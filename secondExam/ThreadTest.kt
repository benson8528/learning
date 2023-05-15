package secondExam

import java.util.LinkedList
import kotlin.concurrent.thread

private const val MIN_CPU_SIZE = 1
private const val MAX_CPU_SIZE = 8

private const val FACTORIAL_N = 10
private const val FACTORIAL_TIMES = 20_000

private const val PERM_LIMIT = 600_000

fun testCase(numOfThreads: Int, totalTimes: Int) {
    val queues = Array(numOfThreads) {
        LinkedList<Array<Boolean>>()
    }

    val perm =  LimitedInputPermutation(36, PERM_LIMIT)

    thread {
        var index = 0
        while (perm.hasNext()) {
            queues[index++].addLast(perm.next())
            index %= numOfThreads
        }
        println("Finished dispatching")
    }

    Thread.sleep(1000)

    val times = totalTimes / numOfThreads

    println("Start evaluating")
    (0 until numOfThreads)
        .map {
            thread {
                println("Thread#$it starting ...")
                while (queues[it].isNotEmpty()) {
                    queues[it].removeFirst()
                    repeatFactorial(FACTORIAL_N, times)
                }
            }
        }
        .forEach {
            it.join()
        }

    println("DONE")
}

fun repeatFactorial(num: Int, times: Int) {
    repeat(times) {
        factorial(num)
    }
}
fun factorial(n: Int): Int {
    return when (n) {
        1,2 -> n
        else -> n * factorial(n - 1)
    }
}




fun main() {
    for (i in MIN_CPU_SIZE .. MAX_CPU_SIZE) {
        println("------------------------")
        val time = measureTime {
            testCase(i, FACTORIAL_TIMES)
        }
        println("It takes $time ms for $i threads to finish")
    }
}