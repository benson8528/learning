import java.io.FileInputStream
import java.io.InputStream
import java.util.*

const val EOF = -1

open class Lexer(private val input: InputStream): InputStream() {

    private data class Node(
        val data: Int,
        var next: Node? = null
    )
    private var _putbackHead: Node? = null

    private fun putback(value: Int) {
        _putbackHead = Node(value, _putbackHead)
    }

    fun putbackString(): String {
        val sb = StringBuilder()
        var node = _putbackHead
        while (node != null) {
            sb.append("${node.data} ")
            node = node.next
        }
        return sb.toString()
    }

    fun probe(vararg data: Int): Boolean {
        if (data.isEmpty()) { return false }

        var putback: Node? = null

        try {
            for (element in data) {
                val read: Int = read()

                if (read != EOF) {
                    putback = Node(read, putback)
                }

                if (element != read) {
                    return false
                }
            }
            return true
        }
        finally {
            var node = putback
            while (node != null) {
                putback(node.data)
                node = node.next
            }
        }
    }

    fun peek() : Int? {
        val read = read()
        if (read != EOF) {
            putback(read)
        }
        return read.takeIf { it != EOF }
    }

    fun readTill(vararg data: Int): String {
        val sb = StringBuilder()
        while (!probe(*data)) {
            val read = read()
            if (read < 0) { break }
            sb.append(Char(read))
        }
        return sb.toString()
    }

    fun skip(vararg options: Int) {
        var read = read()
        while (read in options && read != EOF) {
            read = read()
        }
        if (read != EOF) {
            putback(read)
        }
    }

    override fun read(): Int {
        val data = _putbackHead?.data ?: input.read()
        _putbackHead = _putbackHead?.next
        return data
    }
}

const val SPACE = ' '
//const val CR = '\r'
//const val LF = '\n'

fun Lexer.readDigits(): Int {
    val sb = StringBuilder()
    while (probeDigits()) {
        sb.append(Char(read()))
    }
    return sb.toString().toInt()
}

fun Lexer.probeDigits(): Boolean {
    val nextIsDigit = peek()?.let { Char(it).isDigit() }

    return nextIsDigit == true
}

fun Lexer.readTill(string: String): String {
    return readTill(*string.toIntArray())
}

fun Lexer.skip(vararg options: Char) {
    options.map { it.code }.toIntArray().let { skip(*it) }
}

fun Lexer.skipSpace() {
    skip(' ')
}

fun Lexer.skipSpCrLfTab() {
    skip(' ', '\r', '\n', '\t')
}

fun Lexer.expect(string: String) {
    expect(*string.toIntArray())
}
fun Lexer.expectCrLf() = expect("\r\n")
fun Lexer.expectEOF() = expect(-1)

fun Lexer.probe(string: String) = probe(*string.toIntArray())
fun Lexer.probeCrLf(): Boolean = probe("\r\n")
fun Lexer.probeAlphabet(): Boolean {
    val nextIsAlphabet = peek()
        ?.let { it in 'a'.code..'z'.code || it in 'A'.code..'Z'.code }
    return nextIsAlphabet == true
}

fun Lexer.readAlphabet(): String {
    val sb = StringBuilder()
    while (probeAlphabet()) {
        sb.append(Char(read()))
    }
    return sb.toString()
}

fun Lexer.probeEOF(): Boolean = probe(-1)

fun InputStream.fillUpOrZero(array: ByteArray, off: Int, len: Int = array.size - off) {
    var filled = 0
    while (filled < len) {
        val read = read(array, filled + off, len - filled)
        if (read >= 0) {
            filled += read
        }
        else {
            repeat(len - filled) {
                array[it + filled] = 0
            }
            filled = len
        }
    }
//    return filled == len
}



fun InputStream.fillUpOrZero(array: ByteArray) {
    fillUpOrZero(array, 0, array.size)
}

fun InputStream.readInto(list: IntList, sizeLimit: Int) {
    while (list.size < sizeLimit) {
        val read = read()
        if (read < 0) { break }
        list.add(read)
    }
}

fun IntList.startsWith(array: IntArray): Boolean {
    val iter = iterator()
    repeat(array.size) {
        if (iter.hasNext()) {
            if (iter.next() != array[it]) { return false }
        }
        else { return false }
    }
    return true
}

fun IntList.contentEquals(array: IntArray): Boolean {
    if (size != array.size) { return false }
    val iter = iterator()
    repeat(array.size) { i ->
        if (iter.next() != array[i]) { return false }
    }
    return true
}

fun IntList.removeFirst(): Int {
    val value = get(0)
    removeAt(0)
    return value
}


//從 intArray 的 from 開始和 array 比較內容是否相等
private fun IntArray.equalsFrom(from: Int, array: IntArray, size: Int = array.size): Boolean {
    repeat(min(array.size, this.size - from)) {index ->
        if (array[index] != this[from + index]) {
            return false
        }
    }
    return true
}

fun ByteArray.moveForwardFrom(from: Int): ByteArray {
    return ByteArray(size) {
        when (val index = from + it) {
            in from until size -> this[index]
            else -> 0
        }
    }
}

// filter, map
private fun String.toIntArray() =
    map { it.code }.toIntArray()

private fun ByteArray.toIntArray() = IntArray(this.size) { this[it].toInt() }

private const val FILENAME = "C:\\Users\\A273\\Desktop\\chunk.txt"

fun main() {
    val istream = FileInputStream(FILENAME)
    val lexer = Lexer(istream)

    if (lexer.probe("3")) {

    }
    else if (lexer.probe("13")) {

    }
    else {
        error("FAILED")
    }


    print(lexer.readTill('a'.code, 'b'.code, 'c'.code, 'd'.code))
    println(lexer.probe("abcd"))
    lexer.expect("abcd")
    var read = lexer.read()
    while (read != -1) {
        print("$read ")
        read = lexer.read()
    }
}