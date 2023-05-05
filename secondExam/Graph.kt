package secondExam

import java.util.LinkedList

private const val FILE_NAME = "C:\\Users\\A273\\Desktop\\c432.txt"

interface GraphI {
    fun evaluateAll()
}

class Graph(
    private val inputsMap: Map<Int, InputGate>,
//    private val outputsMap: Map<Int, Gate>,
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

//    val outputIds get() = outputsMap.keys
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
    val levelTime = Array<Long>(levels.size) { 0 }
    fun evaluate() {
//        for (level in levels) {
//            for (gate in level) {
//                gate.evaluate()
//            }
//        }
        for (level in levels.indices) {
            val time = measureTime {
                for (gate in levels[level]) {
                    gate.evaluate()
                }
            }
            levelTime[level] += time
        }

    }

    fun setInput(inputs: Map<Int, Boolean>) {
        inputsMap.forEach {
            it.value.input = inputs[it.key]!!

        }
    }

    override fun evaluateAll() {

//        for (level in levels.indices) {
//            println("level no.$level : ${levels[level].size} gates")
//        }

        val inputIds = inputsMap.keys
        val perm = InputPermutation(inputIds.size)
//        val results = mutableListOf<List<Boolean>>()
        val results = LinkedList<List<Boolean>>()

        var count = 0
        var timestamp = System.currentTimeMillis()

        while (perm.hasNext()) {
            inputValues = perm.next()
//            println(gateInput.values)
            evaluate()
//            println(outputValues)
//            results.add(outputValues)

            count++
            if (count % 1_000_000 == 0) {
                val old = timestamp
                timestamp = System.currentTimeMillis()
                println("${(timestamp - old) / 1000} seconds")
            }
        }
//        levelTime.forEach {
//            print("$it ")
//            println()
//        }
//        return results
    }

}



class InputIterator(private val inputCount: Int) : Iterator<List<Boolean>> {
    private var currentInput = 0

    override fun hasNext(): Boolean {
        return currentInput < (1 shl inputCount)
    }

    override fun next(): List<Boolean> {
        val inputList = (0 until inputCount).map { j ->
            ((currentInput shr j) and 1) == 1
        }
        currentInput++
        return inputList
    }
}

fun String.subStringBetween(first: String, second: String): String {
    return this.substringAfter(first).substringBefore(second)
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
//    for (result in results) {
//        println(result)
//    }

//    val iter = InputIterator(5)
//    while (iter.hasNext()){
//        println(iter.next())
//    }
}

inline fun <T> measure(block: () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    val end = System.currentTimeMillis()

    println("Total execution time (in ms): ${(end - start) }")

    return result
}

inline fun  measureTime(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()

//    println("Total execution time (in ms): ${(end - start) }")

    return end - start
}