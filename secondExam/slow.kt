package secondExam

fun main() {

    val threads = List<Thread>(1) {
        TestThreadSlow()
    }
//    measure {
        for (thread in threads) {
            thread.start()
        }
//    }
}

class TestThreadSlow: Thread() {
    companion object {
        val perm = InputPermutation(28)
    }
    override fun run() {
        val start = System.currentTimeMillis()
//        println("start running")
        while (perm.hasNext()) {
//            print(".")
            perm.next()
        }
//        println("finish running")
        val end = System.currentTimeMillis()
//
        println("Total execution time (in ms): ${(end - start) }")


    }
}