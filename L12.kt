import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

// Serializable
//      - A marker interface

class Director(val name: String): Serializable

data class Movie(val director: Director, val title: String): Serializable {
    constructor(director: String, title: String): this(Director(director), title)
}
data class Movie2(val director: String, val title: String): Serializable

fun main() {
    val movie = Movie("Unknown", "Die Hard")
    val data = ByteArrayOutputStream()

    val ostream = ObjectOutputStream(data)
    ostream.writeObject(movie)

    val istream = ObjectInputStream(ByteArrayInputStream(data.toByteArray()))
    val movie2 = istream.readObject() as? Movie
        ?: error("???")

    println(movie)
    println("---")
    println(movie2)
    
    println("---")
    println(movie === movie2)
}

