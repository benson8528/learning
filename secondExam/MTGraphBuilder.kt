package secondExam

import Lexer

class MTGraphParser(private val threadSize: Int): GraphParser<MTGate>() {
    override fun createGate(type: String, inputIds: List<Int>): MTGate {
        return mtGateFactories[type]?.create(inputIds, threadSize)!!
    }
}

open class MTGraphBuilder(protected val threadSize: Int): GraphBuilder<MTGate, MTInputGate>() {
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

class CoroutineMTGraphBuilder(threadSize: Int) : MTGraphBuilder(threadSize) {
    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputIds: List<Int>,
        gates: Map<Int, MTGate>,
        levels: Array<List<MTGate>>
    ): GraphI {
        return CoroutineMTGraph(inputsMap, outputIds, gates, levels, threadSize)
    }
}

class ThreadPoolMTGraphBuilder(threadSize: Int): MTGraphBuilder(threadSize) {
    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputIds: List<Int>,
        gates: Map<Int, MTGate>,
        levels: Array<List<MTGate>>
    ): GraphI {
        return ThreadPoolMTGraph(inputsMap, outputIds, gates, levels, threadSize)
    }
}

