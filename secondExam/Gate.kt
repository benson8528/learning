package secondExam

import kotlin.math.max

interface Gate {
    val output: Boolean
    val level: Int

    fun evaluate(): Boolean
//    fun bindInputs(gatesMap: Map<Int, Gate>)
}

interface InputGateI: Gate {
    var input: Boolean

    override val output: Boolean get() = input
    override val level: Int get() = 0

    // To avoid multi-inheritance, this implementation is temporarily commented.
    // This problem will be resolved
    //      when the inheritance between InputGate and LogicGate is removed.
//    override fun evaluate(): Boolean = output
}

// LogicGate
//  +- UnaryGate
//      +- NotGate
//      +- BuffGate
//  +- BinaryGate
//      +- XorGate
//  +- MultiGate
//      +- AndGate
//      ...

abstract class LogicGate: Gate {
    private var _output: Boolean = false

    override val output: Boolean get() = _output

    // 2nd pass
    abstract fun bindGates(gatesMap: Map<Int, Gate>)

    protected fun errorGateNotFound(gateId: Int): Nothing {
        error("Missing gate #$gateId")
    }

    protected abstract fun doEvaluate(): Boolean
    final override fun evaluate(): Boolean {
        return doEvaluate().also {
            _output = it
        }
    }
}

class InputGate(
    override var input: Boolean = false
): LogicGate(), InputGateI {
    override val output: Boolean get() = input
    override val level: Int get() = 0

    override fun bindGates(gatesMap: Map<Int, Gate>) { }

    override fun doEvaluate(): Boolean = input
}

//<editor-fold desc="Unary gates">
abstract class UnaryGate(private val inputId: Int): Gate, LogicGate() {
    protected lateinit var input: Gate

    override val level: Int by lazy {
        input.level + 1
    }

    override fun bindGates(gatesMap: Map<Int, Gate>) {
        input = gatesMap[inputId] ?: errorGateNotFound(inputId)
    }
}

class NotGate(inputId: Int): UnaryGate(inputId) {
    override fun doEvaluate(): Boolean = !input.output
}

class BuffGate(inputId: Int): UnaryGate(inputId) {
    override fun doEvaluate(): Boolean = input.output
}
//</editor-fold>

//<editor-fold desc="Binary gates">
abstract class BinaryGate(
    private val inputId1: Int,
    private val inputId2: Int
): Gate, LogicGate() {
    protected lateinit var input1: Gate
    protected lateinit var input2: Gate

    override val level: Int by lazy {
        max(input1.level, input2.level) + 1
    }

    override fun bindGates(gatesMap: Map<Int, Gate>) {
        input1 = gatesMap[inputId1] ?: errorGateNotFound(inputId1)
        input2 = gatesMap[inputId2] ?: errorGateNotFound(inputId2)
    }
}

class XorGate(
    inputId1: Int,
    inputId2: Int
): BinaryGate(inputId1, inputId2) {
    override fun doEvaluate(): Boolean = input1.output != input2.output
}

class XnorGate(
    inputId1: Int,
    inputId2: Int
): BinaryGate(inputId1, inputId2) {
    override fun doEvaluate(): Boolean {
        return input1.output == input2.output
    }
}
//</editor-fold>

//<editor-fold desc="Tuple gates">
abstract class MultiGate(private val inputIds: List<Int>): Gate, LogicGate() {
    protected lateinit var inputs: List<Gate>

    override val level: Int by lazy {
        inputs.maxOf { it.level } + 1
    }

    override fun bindGates(gatesMap: Map<Int, Gate>) {
        inputs = inputIds.map { gatesMap[it] ?: errorGateNotFound(it) }
    }
}

class OrGate(inputIds: List<Int>): MultiGate(inputIds) {
    override fun doEvaluate(): Boolean {
        return inputs.any { it.output }
    }
}
class AndGate(inputIds: List<Int>): MultiGate(inputIds) {
    override fun doEvaluate(): Boolean {
        return inputs.all { it.output }
    }
}
class NorGate(inputIds: List<Int>): MultiGate(inputIds) {
    override fun doEvaluate(): Boolean {
        return !inputs.any { it.output }
    }
}
class NandGate(inputIds: List<Int>): MultiGate(inputIds) {
    override fun doEvaluate(): Boolean {
        return !inputs.all { it.output }
    }
}
//</editor-fold>
