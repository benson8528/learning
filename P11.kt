import secondExam.Lexer
import secondExam.expect
import secondExam.probe
import java.io.FileInputStream
import java.io.InputStream

//const val CR = '\r'
//const val LF = '\n'

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