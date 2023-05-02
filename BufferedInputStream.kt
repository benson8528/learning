import java.io.InputStream
import kotlin.math.max

private const val BUFFER_SIZE = 1024

class BufferedInputStream2(
   val input: InputStream,
   capacity: Int = BUFFER_SIZE
): InputStream() {
   private var buf: ByteArray // To support marking, this buffer should allow resetting.

   private inline val capacity get() = buf.size - size

   private var nextIndex: Int = 0
   private var size: Int = 0

   private inline val Int.isValidIndex get() = this in 0 until size

   init {
      require(capacity > 0)

      buf = ByteArray(max(BUFFER_SIZE, capacity))
   }

   //<editor-fold desc="Supportive properties and methods for marking and resetting">
   override fun markSupported(): Boolean = true

   // Note that when mark() is called,
   //    a new buffer will be created and the marking index will always be 0 (the first element of the buffer).

   private var readLimit: Int? = null

   private fun invalidateMarking() {
      readLimit = null
   }

   private inline val canReset: Boolean
      get() {
         val readLimit = readLimit ?: return false
         return nextIndex <= readLimit
      }

   override fun mark(readLimit: Int) {
      this.buf = ByteArray(max(BUFFER_SIZE, readLimit)) {
         val index = it + nextIndex

         if (index.isValidIndex) {
            buf[index]
         }
         else {
            0
         }
      }

      this.size -= this.nextIndex
      this.nextIndex = 0
      this.readLimit = readLimit
   }

   override fun reset() {
      require(canReset)
      nextIndex = 0
   }
   //</editor-fold>

   //<editor-fold desc="Supportive properties and methods for reading">
   private inline val refillRequired: Boolean get() = nextIndex >= size

   private fun refill() {
      if (capacity > 0) {
         size += max(input.read(buf, size, capacity), 0)
      }
      else { // No space for refilling
         invalidateMarking()

         nextIndex = 0
         size = input.read(buf)
      }
   }

   override fun read(): Int {
      if (refillRequired) {
         refill()
      }

      return if (nextIndex.isValidIndex) {
         buf[nextIndex++].toInt()
      }
      else {
         -1
      }
   }
   //</editor-fold>

   override fun close() {
      input.close()
   }
}

class ArrayInputStream(vararg values: Int): InputStream() {
   private val data = values

   private var nextIndex = 0

   override fun read(): Int {
      return if (nextIndex in data.indices) {
         data[nextIndex++]
      }
      else {
         -1
      }
   }
}



fun InputStream.fillUp(array: ByteArray): Boolean {
   var filled = 0
   while (filled < array.size) {
      val read = read(array, filled, array.size - filled)
      if (read <= 0) { break } // Nothing left
      filled += read
   }
   return filled == array.size
}

fun ByteArray.expect(vararg values: Int) {
   require(size == values.size)
   for (i in indices) {
      require(this[i].toInt() == values[i])
   }
}

fun main() {
   val array = arrayOf(0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9).toIntArray()
   val source = ArrayInputStream(*array)
   val buffered = BufferedInputStream2(source)

   val zeros = ByteArray(5)
   require(buffered.fillUp(zeros))
   zeros.expect(0, 0, 0, 0, 0)

   buffered.mark(5)
   val steps = ByteArray(5)
   require(buffered.fillUp(steps))
   steps.expect(1, 2, 3, 4, 5)

   buffered.reset()
   require(buffered.fillUp(steps))
   steps.expect(1, 2, 3, 4, 5)

   buffered.reset()

   val remaining = ByteArray(10)
   require(!buffered.fillUp(remaining))
   remaining.expect(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

   println("PASS")
}



