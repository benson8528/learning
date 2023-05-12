import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

private const val FILENAME = "C:\\Users\\A273\\Desktop\\chunk.txt"

private const val a = 'a'.code
private const val b = 'b'.code
private const val c = 'c'.code
private const val e = 'e'.code
private const val f = 'f'.code
private const val g = 'g'.code

private const val CR = '\r'
private const val LF = '\n'

private const val BUFFER_SIZE = 1024

class ChunkInputStream(
    private val input: InputStream
): InputStream() {
    init {
        require(input.markSupported())
    }

    constructor(file: File): this(BufferedInputStream(FileInputStream(file)))
    constructor(filename: String): this(File(filename))

    private var buffer: ByteArray = ByteArray(BUFFER_SIZE)

    /**
     * The index of byte when read() is called.
     */
    private var nextIndex: Int = 0

    /**
     * The actual size of the buffed data.
     */
    private var size: Int = 0

    private inline val hasBufferedData: Boolean get() = nextIndex < size

    private fun readTillCrLf(): String {
        var result = ""
        //<editor-fold desc="Version2">
//        var crReached = false
//        while (true) {
//            val read = Char(input.read())
//            if (read == CR) {
//                crReached = true
//                continue
//            }
//            if (crReached) {
//                when (read) {
//                    LF -> { break }
//                    else -> {
//                        crReached = false
//                        result += CR
//                    }
//                }
//            }
//            result += read
//        }
        //</editor-fold>

        //<editor-fold desc="Version1">
        //TODO: BUGGY WHEN READ STRING LIKE 'abc\rdef', IN WHICH '\r' WILL BE SKIPPED
        var read = Char(input.read())
        var lastRead = read
        while (read != '\n' && lastRead != '\r') {
            lastRead = read
            if (lastRead != '\r') {
                result += lastRead.toString()
            }
            read = Char(input.read())
            if (read != '\n') { result += lastRead }
        }
        //</editor-fold>
        return result
    }
    private fun fillUp(size: Int): Unit {
        println("fill up  size: $size")
        var filled = 0
        while (filled < size) {
            val read = input.read(buffer, filled, size - filled)
            if (read < 0) { return }
            filled += read
        }
    }
    private fun expectCrLf(): Unit {
        require(Char(input.read()) == CR)
        require(Char(input.read()) == LF)
    }

    private fun readChunk() {
        println("-------------read chunk-------------")
        val numOfBytesString = input.readTillCrLf()
        if (numOfBytesString != "") {
            val numOfBytes = numOfBytesString.toIntOrNull()
                ?: error("Invalid chunk format")

            buffer = buffer.takeIf { it.size > numOfBytes } ?: ByteArray(numOfBytes)

            fillUp(numOfBytes)

//            repeat(numOfBytes) {
//                print(Char(buffer[it].toInt()))
//            }
//            println()
            input.expectCRLF()

            nextIndex = 0
            size = numOfBytes
        }
        else {
            expectEOF()
        }
    }

    override fun read(): Int {
        print("reading data")
        if (!hasBufferedData) {
            readChunk()
        }
        return when {
            nextIndex < size -> buffer[nextIndex++].toInt()
            else -> -1
        }
    }
}

fun InputStream.expectCRLF() {
    expect('\r'.code, '\n'.code)
}
fun InputStream.expectEOF() { // secondExam.EOF: end of file
    expect(-1)
}
fun InputStream.expect(vararg data: Int) {
    expect(data, 0, data.size)
}

fun InputStream.expect(data: IntArray, offset: Int, len: Int) {
    for (i in offset until offset + len) {
        require(read() == data[i])
    }
}

fun InputStream.readTillCrLf() = readTill(CR.code, LF.code)

fun InputStream.readTill(value: Int): String {
    val sb = StringBuilder() // StringBuffer (thread-safe)
    var read = read()
    while (read != value && read != -1) {
        sb.append(Char(read))
        read = read()
    }
    return sb.toString()
}

fun InputStream.readTill(vararg data: Int): String {
    require(markSupported())
    require(data.isNotEmpty())

    if (data.size == 1) {
        return readTill(data[0])
    }

    val sb = StringBuilder()
    while (true) {
        sb.append(readTill(data[0]))

        // 往後偷看 data.size - 1 個
        if (peek(data, 1, data.size - 1)) {
            expect(data, 1, data.size - 1)
            break
        }
        else {
            sb.append(Char(data[0]))
        }
    }
    return sb.toString()


    var result = ""
    while (true) {
        var read = read()
        if (read != data[0]) {
            result += Char(read)
        }
        else {
            // 讀byte存入array裡
            val array = IntArray(data.size)
            array[0] = read
            for (i in 1 until array.size) {
                read = read()
                array[i] = read
            }

            // 檢查讀到的內容是否跟要求的內容相符
            if (array.contentEquals(data)) {
                break
            }
            else { //[\r,d,c]
                //TODO: BUGGY WHEN THE STREAM REACHES secondExam.EOF.
                array.forEach { result += Char(it) }
            }
        }
    }
    return result
}

fun InputStream.peek(data: IntArray, offset: Int, len: Int): Boolean {
    require(markSupported())

    mark(len)
    try {
        for (i in offset until offset + len) {
            val read = read()
            if (read != data[i]) {
                return false
            }
        }
        return true
    }
    finally {
        reset()
    }
}

private fun InputStream.testChunkReading() {
    val data = ByteArray(16)
    require(fillUp(data))
    data.expect(a, b, c, a, b, c, a, b, c, a, b, c, a, e, f, g)
}

fun main() {
    val istream = ChunkInputStream(FILENAME)
    istream.testChunkReading()

//    var read = istream.read()
//    while (true) {
//        print("$read ")
//        read = istream.read()
//    }
//
    println("PASS")

}