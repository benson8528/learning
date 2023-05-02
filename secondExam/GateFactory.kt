package secondExam

// Factory, Singleton

interface GateFactory<T: LogicGate> {
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
val gateFactories: Map<String, GateFactory<*>> = mapOf(
    "AND" to AndGateFactory.default,
    "NAND" to NandGateFactory.default,
    "OR" to OrGateFactory.default,
    "NOR" to NorGateFactory.default,
    "XOR" to XorGateFactory.default,
    "XNOR" to XnorGateFactory.default,
    "NOT" to NotGateFactory.default,
    "BUFF" to BuffGateFactory.default
)