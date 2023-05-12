package secondExam

private const val C17_FILENAME = "C:\\Users\\A273\\Desktop\\c17.txt"
private const val C432_FILE_NAME = "C:\\Users\\A273\\Desktop\\c432.txt"

interface Graph {
    fun evaluateAll()
}

class DefaultGraph(
    private val inputsMap: Map<Int, InputGate>,
    private val outputsMap: Map<Int, Gate>,
    private val levels: Array<out List<Gate>>
): Graph {
    val inputGates: List<InputGate> by lazy { inputsMap.values.toList() }

    val inputIds get() = inputsMap.keys
    fun getInputGate(id: Int) = inputsMap[id]

    val outputGates: List<Gate> by lazy { outputsMap.values.toList() }
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
        // To avoid evaluating InputGates
//        for (level in levels) {

        for (i in 1 until levels.size) {
            val level = levels[i]
            for (gate in level) {
                gate.evaluate()
            }
        }
    }

    private fun printGraphInformation() {
        println("--- DefaultGraph")
        for (level in levels.indices) {
            println("# of gates in level $level: ${levels[level].size}")
        }
        println("--- ")
    }

    override fun evaluateAll() {
        printGraphInformation()

        val perm = InputPermutation(inputsMap.size)

        var count = 0
        var timestamp = System.currentTimeMillis()

        while (perm.hasNext()) {
            inputValues = perm.next()
            evaluate()

            count++
            if (count % 1_000_000 == 0) {
                val old = timestamp
                timestamp = System.currentTimeMillis()
                println("Running 1_000_000 samples takes ${timestamp - old} ms")
            }
        }
    }
}

private fun test() {
    val c17 = DefaultGraphBuilder.default.fromFile(C17_FILENAME)
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

fun main() {
//    test()
//    val graph = DefaultGraphBuilder.default.fromFile(C17_FILENAME)
    val graph = DefaultGraphBuilder.default.fromFile(C432_FILE_NAME)
    measure {
        graph.evaluateAll()
    }
}

inline fun <T> measure(block: () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    val end = System.currentTimeMillis()

    println("Total execution time (in ms): ${(end - start) }")

    return result
}

//inline fun measureTime(block: () -> Any): Long {
inline fun measureTime(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    return end - start
}