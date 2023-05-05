package secondExam

import Lexer
import expect
import probe
import probeEOF
import readAlphabet
import readTill
import java.io.FileInputStream
import java.io.InputStream

// GraphParser
//  +- DefaultGraphParser
//  +- MTGraphParser

abstract class GraphParser<T : Gate> {
    companion object {
        val default = DefaultGraphParser()
    }

    protected fun expectComment(lexer: Lexer) {
        lexer.expect("#")
        lexer.readTill("\r\n")
    }

    protected fun expectInput(lexer: Lexer): Int {
        lexer.expect("INPUT")
        lexer.skipSpace()
        lexer.expect("(")
        lexer.skipSpace()
        val id = lexer.readDigits()
        lexer.skipSpace()
        lexer.expect(")")

        return id
    }

    protected fun expectOutput(lexer: Lexer): Int {
        lexer.expect("OUTPUT")
        lexer.skipSpace()
        lexer.expect("(")
        lexer.skipSpace()
        val id = lexer.readDigits()
        lexer.skipSpace()
        lexer.expect(")")

        return id
    }

    protected fun <T> expectGate(
        lexer: Lexer,
//        type: String,
        build: (List<Int>) -> T
    ): T {
//        lexer.expect(type)
        lexer.skipSpace()
        lexer.expect("(")
        lexer.skipSpace()
        val inputIds = mutableListOf<Int>()
        while (!lexer.probe(")")) {
            inputIds.add(lexer.readDigits())
            lexer.skipSpace()
            if (lexer.probe(",")) {
                lexer.expect(",")
            }
            lexer.skipSpace()
        }
        lexer.expect(")")
        return build(inputIds)
    }

//    abstract val gateFactories: Map<String, GateFactory<*>>

    abstract fun createGate(type: String, inputIds: List<Int>): T

    protected fun expectGate(lexer: Lexer): Pair<Int, T> {
        val id = lexer.readDigits()
        lexer.skipSpace()
        lexer.expect("=")
        lexer.skipSpace()

        val type = lexer.readAlphabet()
        val gate = expectGate(lexer) {
            createGate(type, it)
        }

        return Pair(id, gate)

//        val gate =
//            gateFactories.keys
//                .firstOrNull {
//                    lexer.probe(it)
//                }
//                ?.let { key ->
//                    expectGate(lexer, key) { inputIds ->
//                        gateFactories[key]?.create(inputIds)
//                            ?: error("Factory of gate [$key] does not exist")
//                    }
//                }
//                ?: error("Invalid input format")
//        return Pair(id, gate)
    }




    fun parse(lexer: Lexer): ParsingResult<T> {
        val inputIds = mutableListOf<Int>()
        val outputIds = mutableListOf<Int>()
        val gates = mutableMapOf<Int, T>()

        lexer.skipSpCrLfTab()
        while (!lexer.probeEOF()) {
//            println("Peek: ${lexer.peek()?.let { Char(it) }}")

            if (lexer.probe("#")) {
                expectComment(lexer)
            }
            else if (lexer.probe("INPUT")) {
                val id = expectInput(lexer)
                inputIds.add(id)
            }
            else if (lexer.probe("OUTPUT")) {
                val id = expectOutput(lexer)
                outputIds.add(id)
            }
            else if (lexer.probeDigits()) {
                val (id, gate) = expectGate(lexer)
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
        val gates: Map<Int, T>
    )
}


class DefaultGraphParser : GraphParser<Gate>() {
    override fun createGate(type: String, inputIds: List<Int>): Gate {
        return gateFactories[type]?.create(inputIds)!!
    }

//    override val gateFactories: Map<String, GateFactory<*>>
    private val gateFactories: Map<String, GateFactory<*>>
        get() = stGateFactories
}

class MTGraphParser(private val threadSize: Int): GraphParser<MTGate>() {
    override fun createGate(type: String, inputIds: List<Int>): MTGate {
        return gateFactories[type]?.create(inputIds, threadSize)!!
    }

//    override val gateFactories: Map<String, MTGateFactory<*>>
    private val gateFactories: Map<String, MTGateFactory<*>>
        get() = MTGateFactories

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

//class LeveledGraphBuilder: GraphBuilderI {
//    override fun fromStream(istream: InputStream, threadSize: Int): Graph {
//        TODO("Not yet implemented")
//    }
//}

abstract class GraphBuilder<T: Gate, U: InputGateI>: GraphBuilderI {
    override fun fromFile(filename: String): GraphI {
        return fromStream(FileInputStream(filename))
    }

    abstract fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<T>
    abstract fun createInputsMap(inputIds: List<Int>) : Map<Int, U>
    abstract fun bindInput(inputsMap: Map<Int, U>, gates: Map<Int, T>)

    override fun fromStream(istream: InputStream): GraphI {
        val (inputIds, outputIds, gates) =
            parsingFile(Lexer(istream))



        val inputsMap = createInputsMap(inputIds)

        val allGatesMap = gates + inputsMap




        val allGates = allGatesMap.values.toList()

        // 2nd pass
        bindInput(inputsMap, gates)
//        for (gate in gates) {
//            gate.value.bindInputs(allGatesMap)
//        }

        // 3rd pass
        val maxLevel = outputIds
            .mapNotNull { gates[it] }
            .maxOf {
                it.level
            }

        val levels = Array<List<T>>(maxLevel + 1) { index ->
            gates.values.filter { gate ->
                gate.level == index
            }
        }

//        val inputsMap = inputIds.associateWith { allGatesMap[it]!! as InputGate }
//        val outputsMap = outputIds.associateWith { allGatesMap[it]!!}


        return createGraph(inputsMap, outputIds, gates, levels)
    }
    abstract fun createGraph(
        inputsMap: Map<Int, U>,
        outputIds: List<Int>,
        gates: Map<Int, T>,
        levels: Array<List<T>>): GraphI
}



class DefaultGraphBuilder private constructor(): GraphBuilder<Gate,InputGate>() {
    companion object {
        val default: GraphBuilderI = DefaultGraphBuilder()
//        val default: GraphBuilderI = LeveledGraphBuilder()

    }

    override fun parsingFile(lexer: Lexer): GraphParser.ParsingResult<Gate> {
        return GraphParser.default.parse(lexer)
    }

    override fun createInputsMap(inputIds: List<Int>): Map<Int, InputGate> {
        return inputIds.associateWith { InputGate() }
    }

    override fun createGraph(
        inputsMap: Map<Int, InputGate>,
        outputIds: List<Int>,
        gates: Map<Int, Gate>,
        levels: Array<List<Gate>>
    ): GraphI {
        return Graph(inputsMap, outputIds, gates, levels)
    }

    override fun bindInput(inputsMap: Map<Int, InputGate>, gates: Map<Int, Gate>) {
        val allGatesMap = inputsMap + gates
        for (gate in gates) {
            (gate.value as LogicGate).bindInputs(allGatesMap)
        }
    }


}


