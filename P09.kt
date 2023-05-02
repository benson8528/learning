import java.io.FileInputStream
import java.io.InputStream

private const val FILENAME = "C:\\Users\\A273\\Desktop\\test.txt"
private const val BUFFER_SIZE = 10
class BufferedInputStream(private val input: InputStream, val size: Int = BUFFER_SIZE): InputStream() {
    private var buf = ByteArray(size)
    private var dataCount = 0
    private var index = 0
    private var markIndex = 0
    private var marklimit = -1
    private val isMarked get() = marklimit != -1
    private inline val bufferSpace get() = buf.size - dataCount

    init {
        dataCount = input.read(buf)
    }

    private fun refill() {

    }

    override fun read(): Int {
        if (bufferSpace > 0) {
            refill()
        }

        return if (index < dataCount) {
            println("index: $index")
            if (isMarked && index >= markIndex + marklimit) {
                println("set markLimit to -1")
                marklimit = -1
            }
            val result = buf[index].toInt()
            index += 1
            result
        }
        else if (index >= size) {
            if (marklimit >= 0) {
                buf = ByteArray(buf.size) {
                    if (it + markIndex < buf.size) {
                        buf[it+markIndex]
                    }
                    else {
                        -1
                    }
                }
                dataCount = index - markIndex
                markIndex = 0
                dataCount += input.read(buf, dataCount, size - dataCount)
            }
            else {
                dataCount = input.read(buf)
            }
            index = 0
            buf[index].toInt()
        }
        else {
            -1
        }
    }

    override fun mark(readlimit: Int) {
        require(readlimit <= size)
        markIndex = index
        marklimit = readlimit
        println("mark at index: $index")
    }

    override fun reset() {
        if (marklimit == -1) {
            error("mark limit is -1")
        }
        index = markIndex
    }




}




fun main() {
    val inputStream = FileInputStream(FILENAME)
    val bufferedInputStream = BufferedInputStream(inputStream)

    var read = bufferedInputStream.read()
//    while (read != -1) {
//        println(read)
//        read = bufferedInputStream.read()
//    }

    repeat(5) {
        println(read)
        read = bufferedInputStream.read()
    }
    bufferedInputStream.mark(6)

    repeat(4) {
        println(read)
        read = bufferedInputStream.read()
    }
    bufferedInputStream.reset()
    repeat(4) {
        println(read)
        read = bufferedInputStream.read()
    }
    println(read)
}