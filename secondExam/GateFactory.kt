package secondExam

// Factory, Singleton

interface GateFactory<T: Gate> {
    fun create(inputIds: List<Int>): T
}

class AndGateFactory private constructor(): GateFactory<AndGate> {
    companion object {
        val default = AndGateFactory()
    }

    override fun create(inputIds: List<Int>): AndGate {
        return AndGate(inputIds)
    }
}


class NandGateFactory private constructor(): GateFactory<NandGate> {
    companion object {
        val default = NandGateFactory()
    }

    override fun create(inputIds: List<Int>): NandGate {
        return NandGate(inputIds)
    }
}

class OrGateFactory private constructor(): GateFactory<OrGate> {
    companion object {
        val default = OrGateFactory()
    }

    override fun create(inputIds: List<Int>): OrGate {
        return OrGate(inputIds)
    }
}

class NorGateFactory private constructor(): GateFactory<NorGate> {
    companion object {
        val default = NorGateFactory()
    }

    override fun create(inputIds: List<Int>): NorGate {
        return NorGate(inputIds)
    }
}

class XorGateFactory private constructor(): GateFactory<XorGate> {
    companion object {
        val default = XorGateFactory()
    }

    override fun create(inputIds: List<Int>): XorGate {
        require(inputIds.size == 2)
        return XorGate(inputIds[0],inputIds[1])
    }
}

class XnorGateFactory private constructor(): GateFactory<XnorGate> {
    companion object {
        val default = XnorGateFactory()
    }

    override fun create(inputIds: List<Int>): XnorGate {
        require(inputIds.size == 2)
        return XnorGate(inputIds[0],inputIds[1])
    }
}

class NotGateFactory private constructor(): GateFactory<NotGate> {
    companion object {
        val default = NotGateFactory()
    }

    override fun create(inputIds: List<Int>): NotGate {
        val inputId = inputIds.single()
        return NotGate(inputId)
    }
}

class BuffGateFactory private constructor(): GateFactory<BuffGate> {
    companion object {
        val default = BuffGateFactory()
    }

    override fun create(inputIds: List<Int>): BuffGate {
        val inputId = inputIds.first()
        return BuffGate(inputId)
    }
}



val stGateFactories: Map<String, GateFactory<*>> = mapOf(
    "AND" to AndGateFactory.default,
    "NAND" to NandGateFactory.default,
    "OR" to OrGateFactory.default,
    "NOR" to NorGateFactory.default,
    "XOR" to XorGateFactory.default,
    "XNOR" to XnorGateFactory.default,
    "NOT" to NotGateFactory.default,
    "BUFF" to BuffGateFactory.default
)

interface MTGateFactory<T: MTGate>{
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
        return MTXorGate(inputId1, inputId2,  threadSize)
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
        return MTXnorGate(inputId1, inputId2,  threadSize)
    }
}

class MTNotGateFactory private constructor(): MTGateFactory<MTNotGate> {
    companion object {
        val default = MTNotGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTNotGate {
        require(inputIds.size == 1)
        val inputId1 = inputIds.single()
        return MTNotGate(inputId1,  threadSize)
    }
}

class MTBuffGateFactory private constructor(): MTGateFactory<MTBuffGate> {
    companion object {
        val default = MTBuffGateFactory()
    }

    override fun create(inputIds: List<Int>, threadSize: Int): MTBuffGate {
        require(inputIds.size == 1)
        val inputId1 = inputIds.single()
        return MTBuffGate(inputId1,  threadSize)
    }
}

val mtGateFactories: Map<String, MTGateFactory<*>> = mapOf(
    "AND" to MTAndGateFactory.default,
    "NAND" to MTNandGateFactory.default,
    "OR" to MTOrGateFactory.default,
    "NOR" to MTNorGateFactory.default,
    "XOR" to MTXorGateFactory.default,
    "XNOR" to MTXnorGateFactory.default,
    "NOT" to MTNotGateFactory.default,
    "BUFF" to MTBuffGateFactory.default
)