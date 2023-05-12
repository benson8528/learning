package secondExam

import expect
import java.io.InputStream

const val EOF = -1

private val NUMBER_RANGE = '0'.code..'9'.code
private val UPPERCASE_ALPHABET_RANGE = 'A'.code..'Z'.code
private val LOWERCASE_ALPHABET_RANGE = 'a'.code..'z'.code

open class Lexer(private val input: InputStream): InputStream() {

    private data class Node(
        val data: Int,
        var next: Node? = null
    )
    private var _putbackHead: Node? = null

    private fun putback(value: Int) {
        _putbackHead = Node(value, _putbackHead)
    }

    fun probe(vararg string: Int): Boolean {
        if (string.isEmpty()) { return false }

        var putback: Node? = null
        try {
            for (element in string) {
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

    fun read(vararg acceptingOptions: Iterable<Int>): String {
        val sb = StringBuilder()

        while (!probeEOF()) {
            val peek = peek() ?: break
            if (acceptingOptions.any { peek in it }) {
                sb.append(Char(read()))
            }
        }

        return sb.toString()
    }

    fun readTill(vararg stoppingString: Int): String {
        val sb = StringBuilder()
        while (!probe(*stoppingString)) {
            val read = read()
            if (read < 0) { break }
            sb.append(Char(read))
        }
        return sb.toString()
    }

    fun skip(vararg skippingOptions: Int) {
        var read = read()
        while (read in skippingOptions && read != EOF) {
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


fun Lexer.readTill(string: String): String {
    return readTill(*string.toIntArray())
}

fun Lexer.skip(vararg options: Char) {
    skip(*options
        .map {
            it.code
        }
        .toIntArray()
    )

//    options
//        .map {
//            it.code
//        }
//        .toIntArray()
//        .let {
//            skip(*it)
//        }
}

fun Lexer.skipSpace() {
    skip(' ')
}
fun Lexer.skipSpCrLfTab() {
    skip(' ', '\r', '\n', '\t')
}

fun Lexer.probe(string: String) = probe(*string.toIntArray())
fun Lexer.expect(string: String) {
    expect(*string.toIntArray())
}

fun Lexer.probeCrLf(): Boolean = probe("\r\n")
fun Lexer.expectCrLf() = expect("\r\n")

fun Lexer.probeEOF(): Boolean = probe(-1)
fun Lexer.expectEOF() = expect(-1)

fun Lexer.probeDigits(): Boolean {
    return peek()?.let { it in NUMBER_RANGE } == true
//    val nextIsDigit = peek()?.let { Char(it).isDigit() }
//    return nextIsDigit == true
}

fun Lexer.readDigits(): Int {
    return read(NUMBER_RANGE).toInt()

//    val sb = StringBuilder()
//    while (probeDigits()) {
//        sb.append(Char(read()))
//    }
//    return sb.toString().toInt()
}

fun Lexer.probeAlphabet(): Boolean {
    return peek()
        ?.let {
            it in LOWERCASE_ALPHABET_RANGE || it in UPPERCASE_ALPHABET_RANGE
        } == true
}
fun Lexer.readAlphabets(): String {
    return read(LOWERCASE_ALPHABET_RANGE, UPPERCASE_ALPHABET_RANGE)
//    val sb = StringBuilder()
//    while (probeAlphabet()) {
//        sb.append(Char(read()))
//    }
//    return sb.toString()
}

private fun String.toIntArray() =
    map { it.code }.toIntArray()