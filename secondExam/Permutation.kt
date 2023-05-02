package secondExam

interface Permutation {
    fun hasNext(): Boolean
    fun next(): Array<Boolean>
}


class InputPermutation(val size: Int): Permutation {
    var count = 0

    private var nextValue: Array<Boolean>?

    init {
        nextValue = Array(size) { false }
    }

    private inline val Array<Boolean>.isFinal get() = all { it }

    private fun Array<Boolean>.advance(index: Int = 0) {
//        if (this[index] && index != this.lastIndex) {
//            advance(index + 1)
//        }
//        this[index] = !this[index]
        var i = index
        while (i <= lastIndex) {
            this[i] = !this[i]
            if (this[i]) { break }
            i++
        }


    }

    override fun hasNext(): Boolean {
        return nextValue != null && count <= 5000000
    }

    private fun advance() {
        val nextValue = this.nextValue ?: return
        var i = 0
        while (i <= nextValue.lastIndex) {
            nextValue[i] = !nextValue[i]
            if (nextValue[i]) { return }
            i++
        }
//        if (i == nextValue.size) {
            this.nextValue = null
//        }
    }

    override fun next(): Array<Boolean> {
        count ++
//        val nextValue = this.nextValue?.copy()
        val nextValue = this.nextValue
        require(nextValue != null)
        this.advance()
//        if (this.nextValue?.isFinal != false) {
//            this.nextValue = null
//        }
//        else {
//            this.nextValue?.advance()
//        }
        return nextValue
    }
}

fun Array<Boolean>.copy(): Array<Boolean> {
    return copyOfRange(0, size)
//    return Array(size) { index ->
//        this[index]
//    }
}


fun main() {
    repeat(10) {
        val perm = InputPermutation(31)

        measure {
            while (perm.hasNext()) {
                perm.next()

            }
        }
    }
}