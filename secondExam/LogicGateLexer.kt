package secondExam

import Lexer
import expect
import expectCrLf
import probe
import readTill
import java.io.File
import java.io.FileInputStream

class LogicGateLexer(inputStream: FileInputStream): Lexer(inputStream) {
    constructor(file: File): this(FileInputStream(file))
    constructor(fileName: String): this(File(fileName))

    val inputIds = mutableListOf<Int>()
    val outputIds = mutableListOf<Int>()

    fun parseFile() {
        while (true) {
            when {

                probe("INPUT") -> { inputIds.add(parseIOGate("INPUT")) }
                probe("OUTPUT") -> { outputIds.add(parseIOGate("OUTPUT")) }

            }
        }
    }

    private fun parseIOGate(str :String):Int {
        expect(str)
        expect("(")
        val id = readTill(")").trim().toInt()
        expect(")")
        expectCrLf()
        return id
    }
    private fun parseLogicGate() {

    }
}

