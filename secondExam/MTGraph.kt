package secondExam

import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread

private const val FILE_NAME = "C:\\Users\\A273\\Desktop\\c432.txt"
private const val LIMIT_SIZE = 8_000_000

//private const val FILE_NAME = "C:\\Users\\A273\\Desktop\\c7552.txt"
//private const val LIMIT_SIZE = 1_000_000

private const val MAX_CPU_SIZE = 16
private const val MIN_CPU_SIZE = 1

private const val INIT_DELAY = 3_000L

open class MTGraph(
    private val inputsMap: Map<Int, MTInputGate>,
    private val outputsMap: Map<Int, MTGate>,
    private val levels: Array<out List<MTGate>>,
    protected val threadSize: Int
): Graph {

    val inputGates: List<MTInputGate> by lazy { inputsMap.values.toList() }

    val outputGates: List<MTGate> by lazy { outputsMap.values.toList() }

    fun outputValues(threadIndex: Int): List<Boolean> {
        return outputsMap.values.map { it.outputs[threadIndex] }
    }

    fun setInput(index: Int, inputs: Array<Boolean>) {
        inputGates.forEachIndexed { i, gate ->
            gate.inputs[index] = inputs[i]
        }
    }

    private fun printGraphInformation() {
        println("--- MTGraph with $threadSize threads")
        for (level in levels.indices) {
            println("# of gates in level $level: ${levels[level].size}")
        }
        println("--- ")
    }

    override fun evaluateAll() {
        printGraphInformation()

        val perm = LimitedInputPermutation(inputsMap.size, LIMIT_SIZE)
        println("Evaluating $LIMIT_SIZE samples")

        val queues = Array(threadSize) {
            LinkedList<Array<Boolean>>()
        }

        val perms =
            queues.map { queue ->
                object : Permutation {
                    override fun hasNext(): Boolean = queue.isNotEmpty()
                    override fun next(): Array<Boolean> = queue.removeFirst()
                }
            }

        thread { // Dispatch thread
            println("Dispatching input values ...")
            var index = 0
            while (perm.hasNext()) {
                queues[index++].addLast(perm.next())
                index %= threadSize
            }
            println("Dispatching complete.")
        }

        println("Waiting for initial dispatching ...")
        Thread.sleep(INIT_DELAY)
        println("Start evaluating ...")

        doEvaluate(perms)

    }

    protected open fun doEvaluate(perms: List<Permutation>) {
        (0 until threadSize)
            .map { index ->
                thread {
                    evaluate(index, perms[index])
//                    GateEvaluationTask(perms[index], index).run()
                }
            }
            .forEach { thread ->
                thread.join()
            }
    }

    protected fun evaluate(index: Int, perm: Permutation) {
        var count = 0
        val start = System.currentTimeMillis()
        while (true) {
            val inputs = perm.nextOrNull() ?: break
            count++
            setInput(index, inputs)
            evaluate(index)
        }
        val end = System.currentTimeMillis()
        println("${index}th thread takes $count inputs, runs ${end - start} ms")
    }

    fun evaluate(index: Int) {
//        for (level in levels) {
        for (i in 1 until levels.size) {
            val level = levels[i]
            for (gate in level) {
                gate.evaluate(index)
            }
        }
    }

    // Content moved to `evaluate(Int, Permutation)`
//    inner class GateEvaluationTask(
//        val perm: Permutation,
//        val index: Int
//    ) : Runnable {
//        private var inputCount = 0
//
//        suspend fun execute() {
//            val start = System.currentTimeMillis()
//            while (true) {
//                val inputs = perm.nextOrNull() ?: break
//                inputCount++
//                setInput(index, inputs)
//                evaluate(index)
//            }
//            val end = System.currentTimeMillis()
//            println("${index}th thread takes $inputCount inputs, runs ${end - start} ms")
//        }
//
//        override fun run() = runBlocking {
//            execute()
//        }
//    }
}

class CoroutineMTGraph(
    inputsMap: Map<Int, MTInputGate>,
    outputsMap: Map<Int, MTGate>,
    levels: Array<out List<MTGate>>,
    threadSize: Int
): MTGraph(inputsMap, outputsMap, levels, threadSize) {
//    private suspend fun execute(perms: List<Permutation>) = coroutineScope {
//        repeat(threadSize) { index ->
//            launch(Dispatchers.IO) {
//                println("#$index coroutine run on thread: ${Thread.currentThread().name}")
//                GateEvaluationTask(perms[index], index).execute()
//            }
//        }
//    }

    override fun doEvaluate(perms: List<Permutation>) {
        repeat(threadSize) { index ->
            CoroutineScope(Dispatchers.IO)
                .launch {
                    println("#$index coroutine run on thread: ${Thread.currentThread().name}")
                    evaluate(index, perms[index])
                }
        }

//        runBlocking {
//            execute(perms)
//        }
    }
}

class ThreadPoolMTGraph(
    inputsMap: Map<Int, MTInputGate>,
    outputsMap: Map<Int, MTGate>,
    levels: Array<out List<MTGate>>,
    threadSize: Int
): MTGraph(inputsMap, outputsMap, levels, threadSize) {
    override fun doEvaluate(perms: List<Permutation>) {
        val pool = Executors.newFixedThreadPool(threadSize)
        (0 until threadSize)
            .map { index ->
//                pool.submit(GateEvaluationTask(perms[index], index))
                pool.submit(kotlinx.coroutines.Runnable { evaluate(index, perms[index]) })
            }
            .forEach {
                it.get()
            }
        pool.shutdown()
    }
}

fun main() {
    repeat(1) {
        for (i in MIN_CPU_SIZE .. MAX_CPU_SIZE) {
            println("------------")
            val mtGraph = MTGraphBuilder.getBuilder(i).fromFile(FILE_NAME)
//            val mtGraph = CoroutineMTGraphBuilder.getBuilder(i).fromFile(FILE_NAME)
//            val mtGraph = ThreadPoolMTGraphBuilder.getBuilder(i).fromFile(FILE_NAME)
            val time = measureTime {
                mtGraph.evaluateAll()
            }
            println("$i thread: $time ms")
        }
    }
}