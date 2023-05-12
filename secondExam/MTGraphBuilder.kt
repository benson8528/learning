package secondExam

import Lexer

class MTGraphParser(private val threadSize: Int): GraphParser<MTLogicGate>() {
    override fun createGate(type: String, inputIds: List<Int>): MTLogicGate {
        return mtGateFactories[type]?.create(inputIds, threadSize)!!
    }
}

open class MTGraphBuilder protected constructor(protected val threadSize: Int)
: AbstractGraphBuilder<MTLogicGate, MTInputGate>() {
    companion object {
        private val builders = mutableMapOf<Int,MTGraphBuilder>()
        fun getBuilder(threadSize: Int): MTGraphBuilder {
            return builders[threadSize] ?: MTGraphBuilder(threadSize).also { builders[threadSize] = it }
        }
    }

    override fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<MTLogicGate> {
        return MTGraphParser(threadSize).parse(lexer)
    }

    override fun createInputsMap(inputIds: List<Int>): Map<Int, MTInputGate> {
        return inputIds.associateWith { MTInputGate(threadSize) }
    }

    override fun getAllGates(
        inputsMap: Map<Int, MTInputGate>,
        gatesMap: Map<Int, MTLogicGate>
    ): Map<Int, MTLogicGate> = inputsMap + gatesMap

    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputsMap: Map<Int, MTLogicGate>,
        levels: Array<List<MTLogicGate>>
    ): Graph {
        return MTGraph(inputsMap, outputsMap, levels, threadSize)
    }

    override fun bindGates(gatesMap: Map<Int, MTLogicGate>) {
        for ((_,gate) in gatesMap) {
            gate.bindInputs(gatesMap)
        }
    }
}

class CoroutineMTGraphBuilder private constructor(threadSize: Int) : MTGraphBuilder(threadSize) {
    companion object {
        private val builders = mutableMapOf<Int, CoroutineMTGraphBuilder>()
        fun getBuilder(threadSize: Int): CoroutineMTGraphBuilder {
            return builders[threadSize]
                ?: CoroutineMTGraphBuilder(threadSize).also { builders[threadSize] = it }
        }
    }

    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputsMap: Map<Int, MTLogicGate>,
        levels: Array<List<MTLogicGate>>
    ): Graph {
        return CoroutineMTGraph(inputsMap, outputsMap, levels, threadSize)
    }
}

class ThreadPoolMTGraphBuilder private constructor(threadSize: Int): MTGraphBuilder(threadSize) {
    companion object {
        private val builders = mutableMapOf<Int,ThreadPoolMTGraphBuilder>()
        fun getBuilder(threadSize: Int): ThreadPoolMTGraphBuilder {
            return builders[threadSize]
                ?:ThreadPoolMTGraphBuilder(threadSize).also { builders[threadSize] = it }
        }
    }

    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputsMap: Map<Int, MTLogicGate>,
        levels: Array<List<MTLogicGate>>
    ): Graph {
        return ThreadPoolMTGraph(inputsMap, outputsMap, levels, threadSize)
    }
}

