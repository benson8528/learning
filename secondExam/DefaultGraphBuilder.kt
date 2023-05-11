package secondExam

import Lexer
import expect
import probe
import probeDigits
import probeEOF
import readAlphabet
import readDigits
import readTill
import skipSpCrLfTab
import skipSpace
import java.io.FileInputStream
import java.io.InputStream

// GraphParser
//  +- DefaultGraphParser
//  +- MTGraphParser

abstract class GraphParser<T : Gate> {

    private fun Lexer.expectComment() {
        this.expect("#")
        this.readTill("\r\n")
    }

    private fun Lexer.expectIO(ioName: String): Int {
        this.expect(ioName)
        this.skipSpace()
        this.expect("(")
        this.skipSpace()
        val id = this.readDigits()
        this.skipSpace()
        this.expect(")")

        return id
    }

    private fun <T> Lexer.expectGate(
        build: (List<Int>) -> T
    ): T {
        this.skipSpace()
        this.expect("(")
        this.skipSpace()
        val inputIds = mutableListOf<Int>()
        while (!this.probe(")")) {
            inputIds.add(this.readDigits())
            this.skipSpace()
            if (this.probe(",")) {
                this.expect(",")
            }
            this.skipSpace()
        }
        this.expect(")")
        return build(inputIds)
    }

    abstract fun createGate(type: String, inputIds: List<Int>): T

    private fun Lexer.expectGateWithId(): Pair<Int, T> {
        val id = this.readDigits()
        this.skipSpace()
        this.expect("=")
        this.skipSpace()

        val type = this.readAlphabet()
        val gate = this.expectGate {
            this@GraphParser.createGate(type, it)
        }

        return Pair(id, gate)
    }

    fun parse(lexer: Lexer): ParsingResult<T> {
        val inputIds = mutableListOf<Int>()
        val outputIds = mutableListOf<Int>()
        val gates = mutableMapOf<Int, T>()
        val inputString = "INPUT"
        val outputString = "OUTPUT"

        lexer.skipSpCrLfTab()

        while (!lexer.probeEOF()) {

            if (lexer.probe("#")) {
                lexer.expectComment()
            }
            else if (lexer.probe(inputString)) {
                val id = lexer.expectIO(inputString)
                inputIds.add(id)
            }
            else if (lexer.probe(outputString)) {
                val id = lexer.expectIO(outputString)
                outputIds.add(id)
            }
            else if (lexer.probeDigits()) {
                val (id, gate) = lexer.expectGateWithId()
                gates[id] = gate
            }
            else {
                error("Invalid input format")
            }

            lexer.skipSpCrLfTab()
        }
        return ParsingResult<T>(inputIds, outputIds, gates)
    }

    data class ParsingResult<T: Gate>(
        val inputIds: List<Int>,
        val outputIds: List<Int>,
        val gates: Map<Int,T>
    )
}


class DefaultGraphParser private constructor(): GraphParser<LogicGate>() {
    companion object {
        val default = DefaultGraphParser()
    }

    override fun createGate(type: String, inputIds: List<Int>): LogicGate {
        return stGateFactories[type]?.create(inputIds)
            ?: error("CANNOT CREATE GATE '$type'")
    }
}

// Design pattern
//  - Singleton
//  - Factory

interface GraphBuilderI {
    fun fromFile(filename: String): GraphI {
        return fromStream(FileInputStream(filename))
    }
    fun fromStream(istream: InputStream): GraphI
}

abstract class GraphBuilder<T: Gate, U: InputGateI>: GraphBuilderI {
    override fun fromFile(filename: String): GraphI {
        return fromStream(FileInputStream(filename))
    }

    protected abstract fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<T>
    protected abstract fun createInputsMap(inputIds: List<Int>) : Map<Int,U>

    protected abstract fun bindGates(gatesMap: Map<Int, T>)

    protected abstract fun createGraph(
        inputsMap: Map<Int, U>,
        outputsMap: Map<Int, T>,
        levels: Array<List<T>>): GraphI

    protected abstract fun getAllGates(inputsMap: Map<Int, U>, gatesMap: Map<Int, T>): Map<Int, T>

    override fun fromStream(istream: InputStream): GraphI {
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



class DefaultGraphBuilder private constructor(): GraphBuilder<LogicGate,InputGate>() {
    companion object { // static properties and methods
        val default: GraphBuilderI = DefaultGraphBuilder()
    }

    override fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<LogicGate> {
        return DefaultGraphParser.default.parse(lexer)
    }

    override fun createInputsMap(inputIds: List<Int>): Map<Int, InputGate> {
        return inputIds.associateWith { InputGate() }
    }

    override fun bindGates(gatesMap: Map<Int, LogicGate>) {
        for ((_, gate) in gatesMap) {
            gate.bindInputs(gatesMap)
        }
    }

    override fun createGraph(
        inputsMap: Map<Int, InputGate>,
        outputsMap: Map<Int, LogicGate>,
        levels: Array<List<LogicGate>>
    ): GraphI {
        return Graph(inputsMap, outputsMap, levels)
    }

    override fun getAllGates(
        inputsMap: Map<Int, InputGate>,
        gatesMap: Map<Int, LogicGate>
    ): Map<Int, LogicGate> = inputsMap + gatesMap
}
