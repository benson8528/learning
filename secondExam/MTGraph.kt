package secondExam

import Lexer

private const val FILE_NAME = "C:\\Users\\A273\\Desktop\\c432.txt"

class MTGraphBuilder(private val threadSize: Int): GraphBuilder<MTGate, MTInputGate>() {
    override fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<MTGate> {
        return MTGraphParser(threadSize).parse(lexer)
    }

    override fun createInputsMap(inputIds: List<Int>): Map<Int, MTInputGate> {
        return inputIds.associateWith { MTInputGate(threadSize) }
    }

    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputIds: List<Int>,
        gates: Map<Int, MTGate>,
        levels: Array<List<MTGate>>
    ): GraphI {
        return MTGraph(inputsMap, outputIds, gates, levels, threadSize)
    }

    override fun bindInput(inputsMap: Map<Int, MTInputGate>, gates: Map<Int, MTGate>) {
        val allGatesMap = inputsMap + gates
        for (gate in gates) {
            (gate.value as MTLogicGate).bindInputs(allGatesMap)
        }
    }
}

class MTGraph(
    private val inputsMap: Map<Int, MTInputGate>,
    outputIds: List<Int>,
    private val gates: Map<Int, MTGate>,
    private val levels: Array<List<MTGate>>,
    private val threadSize: Int
): GraphI {

    val inputsGate: List<MTInputGate> by lazy {
        inputsMap.values.toList()
    }

    val outputsMap: Map<Int, MTGate> =
        outputIds.associateWith { inputsMap[it] ?: gates[it] ?: error("missing gate") }

    fun outputValue(threadIndex: Int): List<Boolean> {
        return outputsMap.values.map { it.outputs[threadIndex] }
    }


//    val inputValues: Array<Array<Boolean>>
//        get() {
//            return Array(inputsGate.size) {
//                inputsGate[it].input
//            }
//        }

//    private val perm = InputPermutation(inputsMap.size)
    private val perm = InputPermutation(13)
    private val threads = Array<GateEvaluateThread>(threadSize) {
        GateEvaluateThread(it)
    }
    private var count = 1
    private var timestamp = 0L
    private fun getInputCount() {
        if (count % 1_000_000 == 0) {
            val old = timestamp
            timestamp = System.currentTimeMillis()
            println("${(timestamp - old) / 1000} seconds")
        }
        count++
    }

    override fun evaluateAll() {
        timestamp = System.currentTimeMillis()
        startTime = System.currentTimeMillis()
        for (thread in threads) {
            thread.start()
        }
        for (thread in threads) {
            thread.join()
        }
//        println("${(System.currentTimeMillis() - startTime) / 1_000} second")
    }
    private var startTime = 0L
    private var completeThread = 0
    fun getInputComplete() {
        completeThread++
        if (completeThread == threadSize) {
            println("${(System.currentTimeMillis() - startTime) / 1_000} second")
        }
    }

    inner class GateEvaluateThread(val index: Int): Thread() {
        private val lock = Object()
        private var completed = false
        var inputCount = 0

        override fun run() {

            while (true) {
                val gateInputs =
//                val gateInputs = synchronized(perm) {
                    when {
                        perm.hasNext() -> {
//                            getInputCount()
                            perm.next()
                            inputCount++
                        }
                        else -> null
                    }
                        ?:break
//                } ?: break
                sleep(5)
//                setInput(index ,gateInputs)
//                evaluate(index)
            }
//            println("thread: $index get $inputCount inputs")
        }



        fun evaluate(threadIndex: Int) {
//            println(threadIndex)
            for (level in levels) {
                for (gate in level) {
                    gate.evaluate(threadIndex)
                }
            }
//            println(outputValue(threadIndex))
        }

        fun setInput(threadIndex: Int,inputs: Array<Boolean>) {
            inputsGate.forEachIndexed { i, mtInputGate ->
                mtInputGate.input[threadIndex] = inputs[i]
            }
        }
    }
}

fun main() {
//    val mtGraph = MTGraphBuilder(20).fromFile(FILE_NAME)
//    val stGraph = DefaultGraphBuilder.default.fromFile(FILE_NAME)
//    mtGraph.evaluateAll()

    for (i in 20 downTo 2) {
        val mtGraph = MTGraphBuilder(i).fromFile(FILE_NAME)
        val time = measureTime {
            mtGraph.evaluateAll()
        }
        println("$i thread: ${time} ms")
    }
}

