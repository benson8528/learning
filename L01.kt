
// `fun` is a keyword
// `val` ...
// `var` ...
// `while`, `for`, `if`, `else`, `do`
fun main() {
    var arr = IntArray(5) { it * 1 }
    for (num in arr){
        println(num)
    }
    arr.apply { size }
//    print("Hello world") // 無換行
//    println("Hello world") // 有換行

    // Literal
    // 3: Integer literal
    // "Hello": String literal
    // 'c': Character literal
    // 3L: Long literal
    // 1.5: Double literal
    // 1.5f: Float literal

    // val -> value (給了預設值後，不可變的變數)
    val a = 3 // Int
//    a = 4 // ERROR

    // var -> variable (給了預設值後，可變的變數)
    var b = 4 // Int
    b = 5

    val c = 1.5 // Double
    val d: Long = 3

    val e = 3L
    val f = 1.5F

    val s/* : String */ = "Hello"

    // Control flows

    // Conditional statement
    //      - if-else
    //  [BLOCK A]
    //  if (<conditional-expression C1>) {
    //      [BLOCK B]
    //  }
    //  else if (<conditional-expression> C2) {
    //      [BLOCK C]
    //  }
    //  else { // 可以沒有 'else'-part
    //      [BLOCK D]
    //  }
    //  [BLOCK E]

    //      - when (switch in C#)
    // C# version
    // switch (<expression>) {
    //  case <expr01>:
    //      [BLOCK01]
    //      break
    //  case <expr02>:
    //      [BLOCK02]
    //      break
    //  default:
    //      [BLOCK_D]
    // }

    // Loop statement
    //      - while
    //  [BLOCK A]
    //  while (<conditional-statement C1>) {
    //      [BLOCK B]
    //  }
    //  [BLOCK C]

    //      - for
    //======  C# version
    //  [BLOCK A]
    //  for (<stmt-01>; <conditional-statement C1>; <stmt-02>) {
    //      [BLOCK B]
    //  }
    //  [BLOCK C]

    //====== C# version
    // [BLOCK A]
    // foreach (a in list/array/dict) {
    //      [BLOCK B]
    // }
    // [BLOCK C]

    // Kotlin version - range
    // [BLOCK A]
//    for (i in 1..3) { --> i runs from 1 to 3 (inclusive)，前者必小於後者
//    for (i in 1 until 4) { --> i runs from 1 until 4 (exclusive)，前者必小於後者
//        [BLOCK B]
//    }
//    for (i in 3 downTo 1) { --> i runs from 3 to 1 (inclusive)，前者必大於後者
//        [BLOCK B]
//    }
//    for (i in 1 until 4 step 3) { --> i runs from 1 until 4 (exclusive)，前者必小於後者，每跑一次增加3
//        [BLOCK B]
//    }
    // [BLOCK C]

    //      - do-while
    //  [BLOCK A]
    //  do {
    //      [BLOCK B]
    //  } while (<conditional-statement C1>)
    //  [BLOCK C]

    // Function declaration (函數宣告)
    //  - 名字 (功能描述)
    //  - 參數 (你給它的東西，為要完成功能)
    //  - 回傳值 (它給你的東西)
    // fun <name>(<參數1: 參數1的型態, 參數2: 參數2的型態, ....>): <回傳值型態> {
    // }

    // Function invocation (函數呼叫)
    // <function-name>(<參數>)
    greeting("SH")
}

fun greeting(name: String): Unit /* 等同 void */ {
//    "Hello " + name // string interpolation
    println("Hello, $name")
}


