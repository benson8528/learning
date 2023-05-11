package secondExam

import Lexer
import probe
import probeEOF
import readTill
import java.io.ByteArrayInputStream
import java.io.StringReader

private const val CONTENT = "# c17     \r\n# 5"

fun main() {
    val istream = ByteArrayInputStream(CONTENT.toByteArray())
    val lexer = Lexer(istream)

    if (lexer.probe("\r\n")) {
        error("???")
    }
    val firstLine = lexer.readTill("\r\n")
//    println(lexer.read())
//    println(lexer.read())
//    println(lexer.read())
//    println(lexer.putbackString())
    while (!lexer.probeEOF()) {
        val read = lexer.read()

        println("code: $read; ch: '${Char(read)}'")
    }

//    val graph = GraphBuilder.default.fromStream(istream)
}