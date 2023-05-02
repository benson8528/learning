package secondExam

import Lexer
import java.io.InputStream
private const val FILE_NAME = "C:\\Users\\A273\\Desktop\\c17.txt"
class MTGraphBuilder: GraphBuilderI {
    override fun fromStream(istream: InputStream): GraphI {
        return fromStream(istream,3)
    }

    fun fromStream(istream: InputStream, threadSize: Int): GraphI {
        val (inputIds, outputIds, gates) = DefaultGraphParser.default.parse(Lexer(istream))

        val allGatesMap = inputIds.associateWith { MTInputGate(threadSize) } + gates
        val allGates = allGatesMap.values.toList()

        for (gate in gates) {
            gate.value.bindInputs(allGatesMap)
        }

        val maxlevel = outputIds
            .mapNotNull { gates[it] }
            .maxOf { it.level } 

        val levels = Array<List<Gate>>(maxlevel + 1) {index ->
            allGates.filter { gate ->
                gate.level == index
            }
        }

        val inputsMap = inputIds.associateWith { allGatesMap[it]!! as MTInputGate }
        val outputsMap = outputIds.associateWith { allGatesMap[it]!! as MTGate }
        return MTGraph(inputsMap, outputsMap,gates, levels, threadSize)
    }
}

class MTGraph(
    private val inputsMap: Map<Int, MTInputGate>,
    private val outputsMap: Map<Int, MTGate>,
    private val gates: Map<Int, Gate>,
    private val levels: Array<List<Gate>>,
    threadSize: Int
): GraphI {
    val inputsGate: List<MTInputGate> by lazy {
        inputsMap.values.toList()
    }

//    val inputValues: Array<Array<Boolean>>
//        get() {
//            return Array(inputsGate.size) {
//                inputsGate[it].input
//            }
//        }

    val perm = InputPermutation(inputsMap.size)
    val threads = Array<Thread>(threadSize) {
        GateEvaluateThread(it)
    }

    override fun evaluateAll() {
        for (thread in threads) {
            thread.start()
        }
    }



    inner class GateEvaluateThread(val index: Int): Thread() {
        override fun run() {
            while (true) {
                val gateInputs = synchronized(perm) {
                    when {
                        perm.hasNext() -> perm.next()
                        else -> null
                    }
                } ?: break
                setInput(index ,gateInputs)
                evaluate(index)
            }
        }

        fun evaluate(threadIndex: Int) {
            println("$threadIndex evaluate")
            for (level in levels) {
                for (gate in level) {
                    require(gate is MTGate)
                    gate.evaluate(threadIndex)
                    println(gate.outputs)
                }
            }
        }

        fun setInput(threadIndex: Int,inputs: Array<Boolean>) {
            inputsGate.forEachIndexed { i, mtInputGate ->
                mtInputGate.input[threadIndex] = inputs[i]
            }
        }
    }
}

fun main() {
    val c17 = GraphBuilder.default.fromFile(FILE_NAME)
    c17.evaluateAll()
}

