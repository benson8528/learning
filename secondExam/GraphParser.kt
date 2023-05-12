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