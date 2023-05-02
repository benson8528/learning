package secondExam

import Lexer
import expect
import probe
import probeEOF
import readTill
import java.io.FileInputStream
import java.io.InputStream

abstract class GraphParser {
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

    protected fun <T: LogicGate> expectGate(
        lexer: Lexer,
        type: String,
        build: (List<Int>) -> T
    ): T {
        lexer.expect(type)
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

    protected fun expectGate(lexer: Lexer): Pair<Int, LogicGate> {
        val id = lexer.readDigits()
        lexer.skipSpace()
        lexer.expect("=")
        lexer.skipSpace()

        val gate =
            gateFactories.keys
                .firstOrNull {
                    lexer.probe(it)
                }
                ?.let { key ->
                    expectGate(lexer, key) { inputIds ->
                        gateFactories[key]?.create(inputIds)
                            ?: error("Factory of gate [$key] does not exist")
                    }
                }
                ?: error("Invalid input format")
        return Pair(id, gate)
    }




    fun parse(lexer: Lexer): ParsingResult {
        val inputIds = mutableListOf<Int>()
        val outputIds = mutableListOf<Int>()
        val gates = mutableMapOf<Int, LogicGate>()

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
        return ParsingResult(inputIds, outputIds, gates)
    }

    data class ParsingResult(
        val inputIds: List<Int>,
        val outputIds: List<Int>,
        val gates: Map<Int, LogicGate>
    )
}


class DefaultGraphParser : GraphParser(){

    protected fun <T: LogicGate> expectGate(
        lexer: Lexer,
        type: String,
        build: (List<Int>) -> T
    ): T {
        lexer.expect(type)
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

    private fun expectGate(lexer: Lexer): Pair<Int, LogicGate> {
        val id = lexer.readDigits()
        lexer.skipSpace()
        lexer.expect("=")
        lexer.skipSpace()

        val gate =
            gateFactories.keys
                .firstOrNull {
                    lexer.probe(it)
                }
                ?.let { key ->
                    expectGate(lexer, key) { inputIds ->
                        gateFactories[key]?.create(inputIds)
                            ?: error("Factory of gate [$key] does not exist")
                    }
                }
                ?: error("Invalid input format")
        return Pair(id, gate)
    }




    fun parse(lexer: Lexer): ParsingResult {
        val inputIds = mutableListOf<Int>()
        val outputIds = mutableListOf<Int>()
        val gates = mutableMapOf<Int, LogicGate>()

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

        return ParsingResult(inputIds, outputIds, gates)
    }


//    data class ParsingResult(
//        val inputIds: List<Int>,
//        val outputIds: List<Int>,
//        val gates: Map<Int, LogicGate>
//    )
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





class GraphBuilder private constructor(): GraphBuilderI {
    companion object {
        val default: GraphBuilderI = GraphBuilder()
//        val default: GraphBuilderI = LeveledGraphBuilder()
//        val default: GraphBuilderI = MTGraphBuilder()
    }

    override fun fromFile(filename: String): Graph {
        return fromStream(FileInputStream(filename))
    }

    override fun fromStream(istream: InputStream): Graph {
        val (inputIds, outputIds, gates) =
            GraphParser.default.parse(Lexer(istream))

        val allGatesMap = gates + inputIds.associateWith { InputGate() }
        val allGates: List<LogicGate> = allGatesMap.values.toList()

        // 2nd pass
        for (gate in gates) {
            gate.value.bindInputs(allGatesMap)
        }

        // 3rd pass
        val maxLevel = outputIds
            .mapNotNull { allGatesMap[it] }
            .maxOf {
                it.level
            }

        val levels = Array<List<LogicGate>>(maxLevel + 1) { index ->
            allGates.filter { gate ->
                gate.level == index
            }
        }

        val inputsMap = inputIds.associateWith { allGatesMap[it]!! as InputGate }
        val outputsMap = outputIds.associateWith { allGatesMap[it]!! as LogicGate }
        return Graph(inputsMap, outputsMap, gates, levels)
    }
}


