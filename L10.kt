

fun main() {
    val list = listOf<Int>()

//    val positiveEvens = mutableListOf<Int>()
//    for (value in list) {
//        if (value.isEven && value.isPositive) {
//            positiveEvens.add(value)
//        }
//    }

    val positiveEvens =
        list
            .filter {
                it.isEven
            }
            .map {
                it - 2
            }
//            .mapNotNull {
//                it.takeIf { it.isEven }?.let { it - 2 }
//            }
            .filter {
                it.isPositive
            }

//    val iter: IntIterator
//    iter
//        .filter {
//            it.isEven
//        }
//        .filter {
//            it.isPositive
//        }
//        .toList()
}

inline val Int.isEven get() = and(1) == 0
inline val Int.isOdd get() = and(1) == 1

inline val Int.isPositive get() = this > 0
inline val Int.isNegative get() = this < 0

fun IntIterator.filter(accept: (Int) -> Boolean): FilteringIterator =
    FilteringIterator(this, accept)

fun IntIterator.toList(): IntList {
    TODO()
}

class FilteringIterator(
    val iterator: IntIterator,
    private val accept: (Int) -> Boolean
): IntIterator {
    private var next: Int? = null

    private fun readNext(): Int? {
        while (iterator.hasNext()) {
            val value = iterator.next()

            if (accept(value)) {
                return value
            }
        }
        return null
    }

    override fun hasNext(): Boolean {
        if (next == null) {
            next = readNext()
        }
        return next != null
    }

    override fun next(): Int {
        return next
            ?: readNext()
            ?: error("No next element")
    }
}

