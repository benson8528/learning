
fun main() {
    // Array
    val a: Array<Int> = arrayOf(1, 2, 3, 4, 5)
    a.size // The length of the array `a`.
    // 格數 (index) 從 0 ~ size - 1
    a[0] // 取值，拿到第 0 格
    a[0] = 3 // 設值，將第 0 格設為 3

    val b: Array<Array<Int>> = arrayOf(
        arrayOf(1, 0, 0), // type: Array<Int>
        arrayOf(0, 1, 0),
        arrayOf(0, 0, 1)
    )
    b[0][1]

    // 自訂資料型態 `class`
    val name = Name()
    name.first
    name.last
    name.middle

    val name2 = Name()
    name2.first = "Shang"
}

class Name {
    var first: String = ""
    var last: String = ""
    var middle: String = ""
}

inline val Name.fullname: String get() = "$first $middle $last"
