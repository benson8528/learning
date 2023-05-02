package secondExam

import kotlin.math.max

interface Gate {
    val output: Boolean

    val level: Int

    fun evaluate(): Boolean
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
    protected var _output: Boolean = false

    override val output: Boolean get() = _output!!

//    fun resetOutput() {
//        _output = null
//    }

    abstract fun bindInputs(gatesMap: Map<Int, Gate>)

    protected fun error(gateId: Int): Nothing {
        error("Missing gate #$gateId")
    }

    protected abstract fun doEvaluate(): Boolean

    override fun evaluate(): Boolean {
        return doEvaluate().also {
            _output = it
        }
//        return _output ?: doEvaluate().also { _output = it }
    }

//    protected inline fun ev(cond: () -> Boolean):Boolean {
//        if (_output != null ){
//            return _output!!
//        }
//        val result = cond()
//        _output = result
//        return result
//    }
}

abstract class UnaryGate(private val inputId: Int): Gate, LogicGate() {
    protected lateinit var input: Gate

    override val level: Int by lazy {
        input.level + 1
    }

    override fun bindInputs(gatesMap: Map<Int, Gate>) {
        input = gatesMap[inputId] ?: error(inputId)
    }

}

abstract class BinaryGate(
    private val inputId1: Int,
    private val inputId2: Int
): Gate, LogicGate() {
//    constructor(private val inputIds: List<Int>)

    protected lateinit var input1: Gate
    protected lateinit var input2: Gate

    override val level: Int by lazy {
        max(input1.level, input2.level) + 1
    }

    override fun bindInputs(gatesMap: Map<Int, Gate>) {
        input1 = gatesMap[inputId1] ?: error(inputId1)
        input2 = gatesMap[inputId2] ?: error(inputId2)
    }
}

abstract class MultiGate(private val inputIds: List<Int>): Gate, LogicGate() {
    protected lateinit var inputs: List<Gate>

    override val level: Int by lazy {
        inputs.maxOf { it.level } + 1
    }

    override fun bindInputs(gatesMap: Map<Int, Gate>) {
        inputs = inputIds.map { gatesMap[it] ?: error(it) }
    }
}

class NotGate(inputId: Int): UnaryGate(inputId) {
    override fun doEvaluate(): Boolean {
        return !input.output
    }
}

class BuffGate(inputId: Int): UnaryGate(inputId) {
    override fun doEvaluate(): Boolean {
        return input.output
    }

}

class InputGate(var input: Boolean = false): LogicGate() {
    override val output: Boolean get() = input

    override val level: Int get() = 0

    override fun bindInputs(gatesMap: Map<Int, Gate>) { }

    override fun doEvaluate(): Boolean {
        return input
    }
}

class XorGate(
    inputId1: Int,
    inputId2: Int
): BinaryGate(inputId1, inputId2) {
    override fun doEvaluate(): Boolean {
        return input1.output != input2.output
    }
}

class XnorGate(
    inputId1: Int,
    inputId2: Int
): BinaryGate(inputId1, inputId2) {
    override fun doEvaluate(): Boolean {
        return input1.output == input2.output
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
