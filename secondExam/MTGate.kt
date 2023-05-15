package secondExam

import kotlin.math.max

interface MTGate: Gate {
    val outputs: Array<Boolean>
    override val output: Boolean get() = outputs[0]

    fun getOutput(index: Int): Boolean

    fun evaluate(index: Int): Boolean
    override fun evaluate(): Boolean = evaluate(0)
}

interface MTInputGateI: MTGate, InputGateI {
    val inputs: Array<Boolean>
    override var input: Boolean
        get() = inputs[0]
        set(value) {
            inputs[0] = value
        }

    override val outputs: Array<Boolean> get() = inputs
    override val output: Boolean get() = outputs[0]

    override fun getOutput(index: Int): Boolean = inputs[index]

    override fun evaluate(index: Int): Boolean = inputs[index]
    override val level: Int get() = 0
}

class MTInputGate(threadSize: Int): MTLogicGate(threadSize), InputGateI {
    var inputs = Array(threadSize) { false }
    override var input: Boolean = inputs[0]

    override val outputs: Array<Boolean> get() = inputs
    override val output: Boolean get() = outputs[0]

    override val level: Int get() = 0

    override fun doEvaluate(index: Int): Boolean {
        return inputs[index]
    }

    override fun bindGates(gatesMap: Map<Int, MTGate>) { }
}

abstract class MTLogicGate(threadSize: Int): MTGate {
    private val _outputs: Array<Boolean> = Array(threadSize) { false }
    override val outputs: Array<Boolean> get() = _outputs

    // 2nd pass
    abstract fun bindGates(gatesMap: Map<Int, MTGate>)

    protected fun errorGateNotFound(gateId: Int): Nothing {
        error("Missing gate #$gateId")
    }

    override fun getOutput(index: Int): Boolean {
        require(index < outputs.size)
        return outputs[index]
    }

    protected abstract fun doEvaluate(index: Int): Boolean
    override fun evaluate(index: Int): Boolean {
        return doEvaluate(index).also { _outputs[index] = it }
    }
}

//<editor-fold desc="Unary gates">
abstract class MTUnaryGate(
    private val inputId: Int,
    threadSize: Int
): MTLogicGate(threadSize) {
    protected lateinit var input: MTGate

    override fun bindGates(gatesMap: Map<Int, MTGate>) {
        input = gatesMap[inputId] ?: errorGateNotFound(inputId)
    }

    override val level get() = input.level + 1
}

class MTNotGate(
    inputId: Int,
    threadSize: Int
): MTUnaryGate(inputId, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return !input.outputs[index]
    }
}

class MTBuffGate(
    inputId: Int,
    threadSize: Int
): MTUnaryGate(inputId, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        return input.outputs[index]
    }
}
//</editor-fold>

//<editor-fold desc="Binary gates">
abstract class MTBinaryGate(
    private val inputId1: Int,
    private val inputId2: Int,
    threadSize: Int
): MTLogicGate(threadSize) {
    lateinit var input1: MTGate
    lateinit var input2: MTGate

    override val level
        get() = max(input1.level, input2.level)

    override fun bindGates(gatesMap: Map<Int, MTGate>) {
        input1 = gatesMap[inputId1] ?: errorGateNotFound(inputId1)
        input2 = gatesMap[inputId2] ?: errorGateNotFound(inputId2)
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
//</editor-fold>

//<editor-fold desc="Tuple gates">
abstract class MTMultiGate(
    private val inputIds: List<Int>,
    threadSize: Int
): MTLogicGate(threadSize) {
    lateinit var inputs: List<MTGate>

    override val level: Int
        get() = inputs.maxOf { it.level } + 1

    override fun bindGates(gatesMap: Map<Int, MTGate>) {
        inputs = inputIds.map { gatesMap[it] ?: errorGateNotFound(it) }
    }
}

class MTAndGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds ,threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        var i = 0
        while (i < inputs.size) {
            if (!inputs[i].outputs[index]) {
                return false
            }
            i++
        }
        return true
//        return inputs.all { it.outputs[index] }
    }
}

//fun <T> Iterable<T>.all2(predicate: (T) -> Boolean): Boolean {
//    val iter = iterator()
//    while (iter.hasNext()) {
//        if (!predicate(iter.next())) {
//            return false
//        }
//    }
//    return true
//}

class MTNandGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        var i = 0
        while (i < inputs.size) {
            if (!inputs[i].outputs[index]) {
                return true
            }
            i++
        }
        return false
//        return !inputs.all { it.outputs[index] }
    }
}

class MTOrGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        var i = 0
        while (i < inputs.size) {
            if (inputs[i].outputs[index]) {
                return true
            }
            i++
        }
        return false
//        return inputs.any { it.outputs[index] }
    }
}

class MTNorGate(inputIds: List<Int>, threadSize: Int): MTMultiGate(inputIds, threadSize) {
    override fun doEvaluate(index: Int): Boolean {
        var i = 0
        while (i < inputs.size) {
            if (inputs[i].outputs[index]) {
                return false
            }
            i++
        }
        return true
//        return !inputs.any { it.outputs[index] }
    }
}
//</editor-fold>


