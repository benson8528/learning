package secondExam

import kotlin.math.max

interface MTGate: Gate {
    val outputs: Array<Boolean>
    override val output: Boolean get() = outputs[0]

    fun getOutput(index: Int): Boolean

    fun evaluate(index: Int): Boolean
    override fun evaluate(): Boolean = evaluate(0)

}

abstract class MTLogicGate(threadSize: Int): MTGate {
    override val outputs: Array<Boolean>
        get() = _outputs
    protected val _outputs: Array<Boolean> = Array(threadSize) { false }


    abstract fun bindInputs(gatesMap: Map<Int,MTGate>)

    protected fun error(gateId: Int): Nothing {
        error("Missing gate #$gateId")
    }

    override fun getOutput(index: Int): Boolean {
        require(index < outputs.size)
        return outputs[index]
    }
    abstract fun doEvaluate(index: Int): Boolean
    override fun evaluate(index: Int): Boolean {
        return doEvaluate(index).also { _outputs[index] = it }
    }
}

abstract class MTUnaryGate(private val inputId: Int, threadSize: Int): MTGate, MTLogicGate(threadSize) {
    lateinit var input: MTGate

    override fun bindInputs(gatesMap: Map<Int, MTGate>) {
        input = gatesMap[inputId] ?: error(inputId)
    }


    override val level get() = input.level + 1

}
abstract class MTBinaryGate(
    private val inputId1: Int,
    private val inputId2: Int,
    threadSize: Int
): MTGate, MTLogicGate(threadSize) {
    lateinit var input1: MTGate
    lateinit var input2: MTGate

    override val level
        get() = max(input1.level, input2.level)

    override fun bindInputs(gatesMap: Map<Int, MTGate>) {
        input1 = gatesMap[inputId1] ?: error(inputId1)
        input2 = gatesMap[inputId2] ?: error(inputId2)
    }
}
abstract class MTMultiGate(private val inputIds: List<Int>, threadSize: Int): MTGate, MTLogicGate(threadSize) {
    lateinit var inputs: List<MTGate>

    override val level: Int
        get() = inputs.maxOf { it.level } + 1

    override fun bindInputs(gatesMap: Map<Int, MTGate>) {
        inputs = inputIds.map { gatesMap[it] ?: error(it) }
    }
}

class MTNotGate(inputId: Int, threadSize: Int): MTUnaryGate(inputId, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return !input.outputs[index]
    }
}

class MTBuffGate(inputId: Int, threadSize: Int): MTUnaryGate(inputId, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return input.outputs[index]
    }
}

class MTXorGate(inputId1: Int, inputId2: Int, threadSize: Int): MTBinaryGate(inputId1, inputId2, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return input1.outputs[index] != input2.outputs[index]
    }
}

class MTXnorGate(inputId1: Int, inputId2: Int, threadSize: Int): MTBinaryGate(inputId1, inputId2, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return input1.outputs[index] == input2.outputs[index]
    }
}

class MTAndGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds ,threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return inputs.all { it.outputs[index] }
    }
}
class MTNandGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return !inputs.all { it.outputs[index] }
    }
}

class MTOrGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return inputs.any { it.outputs[index] }
    }
}
class MTNorGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return !inputs.any { it.outputs[index] }
    }
}

class MTInputGate(threadSize: Int):MTLogicGate(threadSize), InputGateI {

    override val outputs: Array<Boolean>
        get() = input

    var input = Array(threadSize) { false }
    override val level: Int get() = 0

    override fun doEvaluate(index: Int): Boolean {
        return input[index]
    }

    override fun bindInputs(gatesMap: Map<Int, MTGate>) { }
}
