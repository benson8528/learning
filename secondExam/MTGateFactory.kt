package secondExam

interface MTGateFactory<T: MTLogicGate> {
    fun create(inputIds: List<Int>, threadSize: Int): T
}

class MTAndGateFactory private constructor(): MTGateFactory<MTAndGate> {
    companion object {
        val default = MTAndGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTAndGate {
        return MTAndGate(inputIds, threadSize)
    }
}

class MTNandGateFactory private constructor(): MTGateFactory<MTNandGate> {
    companion object {
        val default = MTNandGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTNandGate {
        return MTNandGate(inputIds, threadSize)
    }
}

class MTOrGateFactory private constructor(): MTGateFactory<MTOrGate> {
    companion object {
        val default = MTOrGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTOrGate {
        return MTOrGate(inputIds, threadSize)
    }
}

class MTNorGateFactory private constructor(): MTGateFactory<MTNorGate> {
    companion object {
        val default = MTNorGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTNorGate {
        return MTNorGate(inputIds, threadSize)
    }
}

class MTXorGateFactory private constructor(): MTGateFactory<MTXorGate> {
    companion object {
        val default = MTXorGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTXorGate {
        require(inputIds.size == 2)
        val inputId1 = inputIds[0]
        val inputId2 = inputIds[1]
        return MTXorGate(inputId1, inputId2, threadSize)
    }
}

class MTXnorGateFactory private constructor(): MTGateFactory<MTXnorGate> {
    companion object {
        val default = MTXnorGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTXnorGate {
        require(inputIds.size == 2)
        val inputId1 = inputIds[0]
        val inputId2 = inputIds[1]
        return MTXnorGate(inputId1, inputId2, threadSize)
    }
}

class MTNotGateFactory private constructor(): MTGateFactory<MTNotGate> {
    companion object {
        val default = MTNotGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTNotGate {
        require(inputIds.size == 1)
        val inputId1 = inputIds.single()
        return MTNotGate(inputId1, threadSize)
    }
}

class MTBuffGateFactory private constructor(): MTGateFactory<MTBuffGate> {
    companion object {
        val default = MTBuffGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTBuffGate {
        require(inputIds.size == 1)
        val inputId1 = inputIds.single()
        return MTBuffGate(inputId1, threadSize)
    }
}

val MTGateFactories: Map<String, MTGateFactory<*>> = mapOf(
    "AND" to MTAndGateFactory.default,
    "NAND" to MTNandGateFactory.default,
    "OR" to MTOrGateFactory.default,
    "NOR" to MTNorGateFactory.default,
    "XOR" to MTXorGateFactory.default,
    "XNOR" to MTXnorGateFactory.default,
    "NOT" to MTNotGateFactory.default,
    "BUFF" to MTBuffGateFactory.default
)
