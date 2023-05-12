package secondExam

open class MTGraphBuilder protected constructor(
    protected val threadSize: Int
): AbstractGraphBuilder<MTGraph, MTLogicGate, MTInputGate>() {
    companion object {
        private val builders = mutableMapOf<Int,MTGraphBuilder>()

        fun getBuilder(threadSize: Int): MTGraphBuilder =
            builders.getOrPut(threadSize) {
                MTGraphBuilder(threadSize)
            }
    }

    override val parser: GraphParser<MTLogicGate> get() = MTGraphParser.getParser(threadSize)
    override fun createInputGate(): MTInputGate = MTInputGate(threadSize)

    override fun compose(
        inputsMap: Map<Int, MTInputGate>,
        gatesMap: Map<Int, MTLogicGate>
    ): Map<Int, MTLogicGate> = inputsMap + gatesMap

    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputsMap: Map<Int, MTLogicGate>,
        levels: Array<List<MTLogicGate>>
    ): MTGraph {
        return MTGraph(inputsMap, outputsMap, levels, threadSize)
    }

    override fun MTLogicGate.bind(gatesMap: Map<Int, MTLogicGate>) {
        this.bindGates(gatesMap)
    }

    // Move to super class
//    override fun bindGates(gatesMap: Map<Int, MTLogicGate>) {
//        for ((_,gate) in gatesMap) {
//            gate.bindInputs(gatesMap)
//        }
//    }
}

class CoroutineMTGraphBuilder private constructor(
    threadSize: Int
): MTGraphBuilder(threadSize) {
    companion object {
        private val builders = mutableMapOf<Int, CoroutineMTGraphBuilder>()

        fun getBuilder(threadSize: Int): CoroutineMTGraphBuilder =
            builders.getOrPut(threadSize) {
                CoroutineMTGraphBuilder(threadSize)
            }
    }

    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputsMap: Map<Int, MTLogicGate>,
        levels: Array<List<MTLogicGate>>
    ): CoroutineMTGraph {
        return CoroutineMTGraph(inputsMap, outputsMap, levels, threadSize)
    }
}

class ThreadPoolMTGraphBuilder private constructor(
    threadSize: Int
): MTGraphBuilder(threadSize) {
    companion object {
        private val builders = mutableMapOf<Int,ThreadPoolMTGraphBuilder>()

        fun getBuilder(threadSize: Int): ThreadPoolMTGraphBuilder =
            builders.getOrPut(threadSize) {
                ThreadPoolMTGraphBuilder(threadSize)
            }
    }

    override fun createGraph(
        inputsMap: Map<Int, MTInputGate>,
        outputsMap: Map<Int, MTLogicGate>,
        levels: Array<List<MTLogicGate>>
    ): ThreadPoolMTGraph {
        return ThreadPoolMTGraph(inputsMap, outputsMap, levels, threadSize)
    }
}

