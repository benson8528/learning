package secondExam

import Lexer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
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
    outputIds: List<Int>,
    private val gates: Map<Int, MTGate>,
    private val levels: Array<List<MTGate>>,
    protected val threadSize: Int
): GraphI {

    val inputsGate: List<MTInputGate> by lazy {
        inputsMap.values.toList()
    }

    val outputsMap: Map<Int, MTGate> =
        outputIds.associateWith { inputsMap[it] ?: gates[it] ?: error("missing gate") }

    fun outputValue(threadIndex: Int): List<Boolean> {
        return outputsMap.values.map { it.outputs[threadIndex] }
    }


    override fun evaluateAll() {
        val perm = LimitedInputPermutation(inputsMap.size, LIMIT_SIZE)
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
            var index = 0
            while (perm.hasNext()) {
                queues[index++].addLast(perm.next())
                index %= threadSize
            }
        }

        Thread.sleep(INIT_DELAY)


        doEvaluate(perms)

    }

    protected open fun doEvaluate(perms: List<Permutation>) {
        (0 until threadSize)
            .map {index ->
                thread{ GateEvaluationTask(perms[index], index).run() }
            }
            .forEach { thread ->
                thread.join()
            }
    }


    fun evaluate(index: Int) {
        for (level in levels) {
            for (gate in level) {
                gate.evaluate(index)
            }
        }
    }

    fun setInput(index: Int, inputs: Array<Boolean>) {
        inputsGate.forEachIndexed { i, mtInputGate ->
            mtInputGate.inputs[index] = inputs[i]
        }
    }

    inner class GateEvaluationTask(
        val perm: Permutation,
        val index: Int
    ): Runnable {
        private var inputCount = 0

        suspend fun execute() {
            val start = System.currentTimeMillis()
            while (true) {
                val gateInputs = perm.nextOrNull() ?: break
                inputCount++
                setInput(index, gateInputs)
                evaluate(index)
            }
            val end = System.currentTimeMillis()
            println("${index}th thread takes $inputCount inputs, runs ${end - start} ms")
        }

        override fun run() = runBlocking {
            execute()
        }
    }
}

class CoroutineMTGraph(
    inputsMap: Map<Int, MTInputGate>,
    outputIds: List<Int>,
    gates: Map<Int, MTGate>,
    levels: Array<List<MTGate>>,
    threadSize: Int
): MTGraph(inputsMap, outputIds, gates, levels, threadSize) {
    private suspend fun execute(perms: List<Permutation>) = coroutineScope {
        repeat(threadSize) { index ->
            launch(Dispatchers.IO) {
                println("#$index coroutine run on thread: ${Thread.currentThread().name}")
                GateEvaluationTask(perms[index], index).execute()
            }
        }
    }

    override fun doEvaluate(perms: List<Permutation>) {
        runBlocking {
            execute(perms)
        }
    }
}

class ThreadPoolMTGraph(
    inputsMap: Map<Int, MTInputGate>,
    outputIds: List<Int>,
    gates: Map<Int, MTGate>,
    levels: Array<List<MTGate>>,
    threadSize: Int
): MTGraph(inputsMap, outputIds, gates, levels, threadSize) {
    override fun doEvaluate(perms: List<Permutation>) {
        val threadPool = Executors.newFixedThreadPool(threadSize)
        (0 until threadSize)
            .map { index ->
                threadPool.submit(GateEvaluationTask(perms[index], index))
            }
            .forEach {
                it.get()
            }
        threadPool.shutdown()
    }
}

fun main() {
    repeat(1) {
        for (i in MIN_CPU_SIZE .. MAX_CPU_SIZE) {
            println("------------")
//            val mtGraph = MTGraphBuilder(i).fromFile(FILE_NAME)
//            val mtGraph = CoroutineMTGraphBuilder(i).fromFile(FILE_NAME)
            val mtGraph = ThreadPoolMTGraphBuilder(i).fromFile(FILE_NAME)
            val time = measureTime {
                mtGraph.evaluateAll()
            }
            println("$i thread: ${time} ms")
        }
    }
}