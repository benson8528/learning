package secondExam

import java.util.LinkedList

private const val FILE_NAME = "C:\\Users\\A273\\Desktop\\c17.txt"
//private const val FILE_NAME = "C:\\Users\\A273\\Desktop\\c432.txt"

interface GraphI {
    fun evaluateAll()
}

class Graph(
    private val inputsMap: Map<Int, InputGate>,
    private val outputIds: List<Int>,
    private val gates: Map<Int, Gate>,
    private val levels: Array<List<Gate>>
): GraphI {
    val inputGates: List<InputGate> by lazy { inputsMap.values.toList() }

    val inputIds get() = inputsMap.keys
    fun getInputGate(id: Int) = inputsMap[id]

    val outputGates: List<Gate> by lazy { outputsMap.values.toList() }
    private val allGatesMap = gates + inputsMap
    private val outputsMap = outputIds.associateWith { allGatesMap[it]!!}
    fun getOutputGate(id: Int) = outputsMap[id]

    var inputValues: Array<Boolean>
        get() {
            return Array(inputGates.size) {
                inputGates[it].input
            }
        }
        set(value) {
            value.forEachIndexed { index, inputValue ->
                inputGates[index].input = inputValue
            }
        }

    val outputValues get() = outputsMap.values.map { it.output }

    fun evaluate() {
        for (level in levels) {
            for (gate in level) {
                gate.evaluate()
            }
        }
    }

    private fun gatesNumOfLevels() {
        for (level in levels.indices) {
            println("level no.$level : ${levels[level].size} gates")
        }
    }
    override fun evaluateAll() {

        val inputIds = inputsMap.keys
        val perm = InputPermutation(inputIds.size)

        var count = 0
        var timestamp = System.currentTimeMillis()

        while (perm.hasNext()) {
            inputValues = perm.next()
            evaluate()

            count++
            if (count % 1_000_000 == 0) {
                val old = timestamp
                timestamp = System.currentTimeMillis()
                println("${(timestamp - old) / 1000} seconds")
            }
        }
    }
}

fun main() {

    val c17 = DefaultGraphBuilder.default.fromFile(FILE_NAME)
    val map = mapOf<Int, Boolean>(
        Pair(1, true),
        Pair(2, false),
        Pair(3, true),
        Pair(6, false),
        Pair(7, true),
    )

    val results = measure {
        c17.evaluateAll()
    }
}

inline fun <T> measure(block: () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    val end = System.currentTimeMillis()

    println("Total execution time (in ms): ${(end - start) }")

    return result
}

inline fun  measureTime(block: () -> Any): Long {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    return end - start
}