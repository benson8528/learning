package secondExam

class MTGraphParser private constructor(
    private val threadSize: Int
): GraphParser<MTLogicGate>() {
    companion object {
        private val parsers = mutableMapOf<Int, MTGraphParser>()

        fun getParser(threadSize: Int): MTGraphParser =
            parsers.getOrPut(threadSize) {
                MTGraphParser(threadSize)
            }
    }

    override fun createGate(
        type: String,
        inputIds: List<Int>
    ): MTLogicGate {
        return MTGateFactories[type]?.create(inputIds, threadSize)
            ?: error("CANNOT CREATE GATE '$type'")
    }
}