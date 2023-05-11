package secondExam


fun main() {
    case1()
}

fun case1() {
    val threads = List<Thread>(1) {
        TestThreadFast()
    }
//    measure {
        for (thread in threads) {
            thread.start()
        }
//    }
}

fun case2() {
    val perm = InputPermutation(28)

    while (perm.hasNext()) {
        perm.next()
    }
}

class TestThreadFast: Thread() {
    companion object {
        val perm = InputPermutation(28)
    }
    override fun run() {
        measure {
            while (perm.hasNext()) {
                perm.next()
            }
        }
    }
}
