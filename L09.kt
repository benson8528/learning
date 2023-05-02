import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private const val FILENAME = "C:\\Users\\A273\\Desktop\\test.txt"
private const val OUTPUT_FILENAME = "C:\\Users\\A273\\Desktop\\test2.txt"

fun main() {

//    val input = FileInputStream(FILENAME)
//    val inputBuffer = BufferedInputStream(input)
//
//    var readBuffer = inputBuffer.read()
//    while (readBuffer != -1) {
//        println(Char(readBuffer))
//        readBuffer = inputBuffer.read()
//    }
//    val inputStream = FileInputStream(FILENAME)
//    val inputStreamReader = InputStreamReader(inputStream)
//    val input = BufferedReader(inputStreamReader)
//    var read: Int = input.read()
//    while (read != -1) {
//        if (read == 99) {
//            input.mark(1)
//        }
//        println(Char(read))
//
//        read = input.read()
//    }

    val outputStream = FileOutputStream(OUTPUT_FILENAME)

//    outputStream.write(99)
//    outputStream.close()

    val streamWriter = OutputStreamWriter(outputStream,)
    streamWriter.write('c'.code)
    streamWriter.flush()
//    streamWriter.close()

}
