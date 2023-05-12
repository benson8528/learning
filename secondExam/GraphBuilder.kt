package secondExam

import Lexer
import java.io.FileInputStream
import java.io.InputStream

// GraphParser
//  +- DefaultGraphParser
//  +- MTGraphParser

// Design pattern
//  - Singleton
//  - Factory

interface GraphBuilder<G: Graph> {
    fun fromFile(filename: String): G {
        return fromStream(FileInputStream(filename))
    }
    fun fromStream(istream: InputStream): G
}

abstract class AbstractGraphBuilder<
        G: Graph,
        T: Gate,
        U: InputGateI
>: GraphBuilder<G> {
    // Move to super class (interface)
//    override fun fromFile(filename: String): G {
//        return fromStream(FileInputStream(filename))
//    }

    protected abstract val parser: GraphParser<T>
    protected abstract fun createInputGate(): U
    protected abstract fun T.bind(gatesMap: Map<Int, T>)

    // Inlined
//    protected fun bindGates(gatesMap: Map<Int, T>) {
//        for ((_, gate) in gatesMap) {
//            gate.bind(gatesMap)
//        }
//    }

    protected abstract fun createGraph(
        inputsMap: Map<Int, U>,
        outputsMap: Map<Int, T>,
        levels: Array<List<T>>
    ): G

    protected abstract fun compose(
        inputsMap: Map<Int, U>,
        gatesMap: Map<Int, T>
    ): Map<Int, T>

    override fun fromStream(istream: InputStream): G {
        val (inputIds, outputIds, gatesMap) =
            parser.parse(Lexer(istream))

        val inputsMap = inputIds.associateWith { createInputGate() }
        val allGatesMap = compose(inputsMap, gatesMap)
        val outputsMap = outputIds.associateWith { allGatesMap[it]!! }

        // 2nd pass
        for ((_, gate) in allGatesMap) {
            gate.bind(allGatesMap)
        }

        // 3rd pass
        val maxLevel = outputIds
            .mapNotNull { gatesMap[it] }
            .maxOf {
                it.level
            }

        val levels = Array(maxLevel + 1) { index ->
            gatesMap.values.filter { gate ->
                gate.level == index
            }
        }

        return createGraph(inputsMap, outputsMap, levels)
    }
}

class DefaultGraphBuilder private constructor(
): AbstractGraphBuilder<DefaultGraph, LogicGate,InputGate>() {
    companion object { // static properties and methods
        val default = DefaultGraphBuilder()
    }

    override val parser: GraphParser<LogicGate> get() = DefaultGraphParser.default

    override fun createInputGate(): InputGate = InputGate()

    override fun LogicGate.bind(gatesMap: Map<Int, LogicGate>) {
        this.bindGates(gatesMap)
    }

    // Move to super class
//    override fun bindGates(gatesMap: Map<Int, LogicGate>) {
//        for ((_, gate) in gatesMap) {
//            gate.bindGates(gatesMap)
//        }
//    }

    override fun createGraph(
        inputsMap: Map<Int, InputGate>,
        outputsMap: Map<Int, LogicGate>,
        levels: Array<List<LogicGate>>
    ): DefaultGraph {
        return DefaultGraph(inputsMap, outputsMap, levels)
    }

    override fun compose(
        inputsMap: Map<Int, InputGate>,
        gatesMap: Map<Int, LogicGate>
    ): Map<Int, LogicGate> = inputsMap + gatesMap
}
