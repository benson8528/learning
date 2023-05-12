package secondExam

abstract class GraphParser<T : Gate> {
    companion object {
        private const val INPUT_NAME = "INPUT"
        private const val OUTPUT_NAME = "OUTPUT"
    }

    private fun Lexer.expectComment() {
        this.expect("#")
        this.readTill("\r\n")
    }

    private enum class IOType(val type: String) {
        INPUT(INPUT_NAME),
        OUTPUT(OUTPUT_NAME)
    }

    /**
     * Read identifier for `INPUT` or `OUTPUT`
     */
    private fun Lexer.expectId(type: IOType): Int {
        this.expect(type.type)
        this.skipSpace()
        this.expect("(")
        this.skipSpace()
        val id = this.readDigits()
        this.skipSpace()
        this.expect(")")

        return id
    }

    private fun Lexer.expectInputIds(): List<Int> {
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
        return inputIds
    }

    abstract fun createGate(type: String, inputIds: List<Int>): T

    private fun Lexer.expectGateDef(): Pair<Int, T> {
        val id = this.readDigits()
        this.skipSpace()
        this.expect("=")
        this.skipSpace()

        val type = this.readAlphabets()
        val gate = createGate(type, expectInputIds())

        return Pair(id, gate)
    }

    fun parse(lexer: Lexer): ParsingResult<T> {
        val inputIds = mutableListOf<Int>()
        val outputIds = mutableListOf<Int>()
        val gates = mutableMapOf<Int, T>()

        lexer.skipSpCrLfTab()

        while (!lexer.probeEOF()) {
            if (lexer.probe("#")) {
                lexer.expectComment()
            }
            else if (lexer.probe(INPUT_NAME)) {
                val id = lexer.expectId(IOType.INPUT)
                inputIds.add(id)
            }
            else if (lexer.probe(OUTPUT_NAME)) {
                val id = lexer.expectId(IOType.OUTPUT)
                outputIds.add(id)
            }
            else if (lexer.probeDigits()) {
                val (id, gate) = lexer.expectGateDef()
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

class DefaultGraphParser private constructor(): GraphParser<LogicGate>() {
    companion object {
        val default = DefaultGraphParser()
    }

    override fun createGate(type: String, inputIds: List<Int>): LogicGate {
        return DefaultGateFactories[type]?.create(inputIds)
            ?: error("CANNOT CREATE GATE '$type'")
    }
}