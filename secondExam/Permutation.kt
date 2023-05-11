package secondExam

import kotlin.concurrent.thread
import kotlin.math.pow

interface Permutation {
    fun hasNext(): Boolean
    fun next(): Array<Boolean>
}


fun Permutation.nextOrNull(): Array<Boolean>? =
    if (hasNext()) {
        next()
    }
    else {
        null
    }

//      lock version
//fun Permutation.nextOrNull(): Array<Boolean>? = synchronized(this) {
//        if (hasNext()) {
//            return@synchronized next()
//        }
//        else {
//            return@synchronized null
//        }
//    }

interface FastPermutation {
    fun next(): Array<Boolean>?
}


open class InputPermutation(val size: Int): Permutation {

    private var nextValue: Array<Boolean>?

    init {
        nextValue = Array(size) { false }
    }

    //<editor-fold desc="Obsolete properties and methods">
    @Deprecated("Obsolete property")
    private inline val Array<Boolean>.isFinal get() = all { it }

    @Deprecated("Obsolete method")
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
    //</editor-fold>

    override fun hasNext(): Boolean {
        return nextValue != null
    }

    private fun advance() {
        val nextValue = this.nextValue ?: return
        var i = 0
        while (i <= nextValue.lastIndex) {
            nextValue[i] = !nextValue[i]
            if (nextValue[i]) { return }
            i++
        }

        this.nextValue = null
    }
    override fun next(): Array<Boolean> {
        val nextValue = this.nextValue
        require(nextValue != null)
        this.advance()
        return nextValue
    }
}

class LimitedInputPermutation(
    size: Int,
    private val limit: Int
): InputPermutation(size) {
    private var counter = 0

    override fun hasNext(): Boolean {
        return counter < limit && super.hasNext()
    }

    override fun next(): Array<Boolean> {
        return super.next().also { counter++ }
    }
}

//class ThreadSafeInputPermutation(size: Int): InputPermutation(size) {
//    override fun hasNext(): Boolean {
//        synchronized(this) {
//            return super.hasNext()
//        }
//    }
//
//    override fun next(): Array<Boolean> {
//        synchronized(this) {
//            return super.next()
//        }
//    }
//}

fun Array<Boolean>.copy(): Array<Boolean> {
    return copyOfRange(0, size)
}

class TestThread: Thread() {
    companion object {
        val perm = InputPermutation(28)
    }
    override fun run() {
        while (perm.hasNext()) {
            perm.next()

        }
//        measure {
//            while (perm.hasNext()) {
//                perm.next()
//
//            }
//        }
    }
}