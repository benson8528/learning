package secondExam

import kotlin.concurrent.thread

private const val MIN_CPU_SIZE = 1
private const val MAX_CPU_SIZE = 32
private const val FACTORIAL_N = 50
private const val FACTORIAL_TIMES = 200_000_000


private fun testCase(numOfThreads: Int, times: Int) {
    (0 until numOfThreads)
        .map {
            thread {
                repeatFactorial(FACTORIAL_N, times / numOfThreads)
            }
        }
        .forEach {
            it.join()
        }
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
        println("----------")
        val time = measureTime {
            testCase(i, FACTORIAL_TIMES)
        }
        println("$i threads : $time ms")
    }
}