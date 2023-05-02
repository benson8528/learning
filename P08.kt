import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext


fun doTaskA(id: Int) {
    println("[$id][${Thread.currentThread().name}] done task A")
}
suspend fun doTaskB(id: Int) {
    delay(1000) // Simulation do something.
    println("[$id][${Thread.currentThread().name}] done task B")
}
fun doTaskC(id: Int) {
    println("[$id][${Thread.currentThread().name}] done task C")
}
fun main() {

    runBlocking {
        Dispatchers.IO
        Dispatchers.Main

        coroutineScope {
            repeat(10) {
                launch {
                    println("[${Thread.currentThread().name}] coroutine #$it")
                    delay(1000)
                    println("[${Thread.currentThread().name}] #$it completed")
                }
            }
            repeat(5) {
                launch(Dispatchers.IO) {
                    println("[${Thread.currentThread().name}] Fetching data from service #$it")
                    launch(Dispatchers.Main) {
                        // If new data available, then update UI
                    }
                    delay(1000)

                    println("[${Thread.currentThread().name}] IO#$it completed")
                }
            }
        }
//        coroutineScope {
//
//        }
        println("All completed")
    }
}