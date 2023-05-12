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

interface GraphBuilder {
    fun fromFile(filename: String): Graph {
        return fromStream(FileInputStream(filename))
    }
    fun fromStream(istream: InputStream): Graph
}

abstract class AbstractGraphBuilder<T: Gate, U: InputGateI>: GraphBuilder {
    override fun fromFile(filename: String): Graph {
        return fromStream(FileInputStream(filename))
    }

    protected abstract fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<T>
    protected abstract fun createInputsMap(inputIds: List<Int>) : Map<Int,U>

    protected abstract fun bindGates(gatesMap: Map<Int, T>)

    protected abstract fun createGraph(
        inputsMap: Map<Int, U>,
        outputsMap: Map<Int, T>,
        levels: Array<List<T>>): Graph

    protected abstract fun getAllGates(inputsMap: Map<Int, U>, gatesMap: Map<Int, T>): Map<Int, T>

    override fun fromStream(istream: InputStream): Graph {
        val (inputIds, outputIds, gatesMap) =
            parsingFile(Lexer(istream))

        val inputsMap = createInputsMap(inputIds)
        val allGatesMap = getAllGates(inputsMap, gatesMap)
        val outputsMap = outputIds.associateWith { allGatesMap[it]!! }

        // 2nd pass
        bindGates(allGatesMap)

        // 3rd pass
        val maxLevel = outputIds
            .mapNotNull { gatesMap[it] }
            .maxOf {
                it.level
            }

        val levels = Array<List<T>>(maxLevel + 1) { index ->
            gatesMap.values.filter { gate ->
                gate.level == index
            }
        }

        return createGraph(inputsMap, outputsMap, levels)
    }
}



class DefaultGraphBuilder private constructor(): AbstractGraphBuilder<LogicGate,InputGate>() {
    companion object { // static properties and methods
        val default: GraphBuilder = DefaultGraphBuilder()
    }

    override fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<LogicGate> {
        return DefaultGraphParser.default.parse(lexer)
    }

    override fun createInputsMap(inputIds: List<Int>): Map<Int, InputGate> {
        return inputIds.associateWith { InputGate() }
    }

    override fun bindGates(gatesMap: Map<Int, LogicGate>) {
        for ((_, gate) in gatesMap) {
            gate.bindGates(gatesMap)
        }
    }

    override fun createGraph(
        inputsMap: Map<Int, InputGate>,
        outputsMap: Map<Int, LogicGate>,
        levels: Array<List<LogicGate>>
    ): Graph {
        return DefaultGraph(inputsMap, outputsMap, levels)
    }

    override fun getAllGates(
        inputsMap: Map<Int, InputGate>,
        gatesMap: Map<Int, LogicGate>
    ): Map<Int, LogicGate> = inputsMap + gatesMap
}
