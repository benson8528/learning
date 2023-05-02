import kotlin.math.sqrt

fun main() {
    print(p01_8(15))
//    p01_d(100000)
//    println(p01_e(100000))
}

fun printSymbol(symbol: String, times: Int) {
    for (i in 0 until times) {
        print(symbol)
    }
}

// Practice 01-1
// n = 4:
// *
// **
// ***
// ****
//

fun p01_1(n: Int) {
    for (i in 1 .. n) {
        printSymbol("*",i)
        println()
    }
}

// Practice 01-2
// n = 4:
// ****
// ***
// **
// *
//
fun p01_2(n:Int){
    for (i in n downTo 1){
        printSymbol("*",i)
        println()
    }
}
// Practice 01-3
// n = 4:
// ****
//  ***
//   **
//    *
//
fun p01_3(n:Int){
    for (i in n downTo 1) {
        // The ith row (line).
        printSymbol(" ",n - i)
        printSymbol("*", i)
        println()
    }

//    for(i in n downTo 1){
////        for(k in 0 until n-i)
////            print(" ")
//        printSpace(n - i)
//        printStar(i)
//        println()
//    }
}
// Practice 01-4
// n = 4:
//    *
//   **
//  ***
// ****
//
fun p01_4(n: Int){
    for(i in 1..n){
        printSymbol(" ", n - i)
        printSymbol("*", i)
//        for(k in n-i downTo 1)
//            print(" ")
//        for(j in 1..i)
//            print("*")
        println()
    }
}
// Practice 01-5
// n = 4:
//    *
//   ***
//  *****
// *******
//
fun p01_5(n: Int) {
    for (i in 1 .. n) {
        printSymbol(" ", n - i)
        printSymbol("*", i * 2 - 1)
//        for(k in n-i downTo 1)
//            print(" ")
//        for(j in 1..i*2-1)
//            print("*")
        println()
    }
}
// Practice 01-6: 費式數列
// n = 5:
// 1 1 2 3 5
fun p01_6(n: Int) {
    var a: Int = 0
    var b: Int = 1
//    var c:Int
    print("$b ")
    for (i in 0 until n - 1) {
//    for (i in 1..n-1){
        val c = a + b
        print("$c ")
        a = b
        b = c
    }
}

// Practice 01-7: 印出99乘法表
fun p01_7(){
    for (i in 1..9) {
        for (j in 1 .. 9) {
//            print("$j x $i = ${i * j}\t")
            print("${i * j}\t")
        }
        println()
    }
}

fun isPrime(n: Int): Boolean {
    //TODO:
    // 1. n is even: false
    // 2. 3 ~ sqrt(n), step 2
    if (n % 2 == 0) {
        return true
    }
    var sqrtN: Int = n
    if (n >= 9) {
        sqrtN = sqrt(n.toFloat()).toInt()+1
    }
    for (i in 3 until sqrtN step 2) {
        if (n % i == 0) {
            return false
        }
    }
    return true
}

// Practice 01-8: 印出n個質數
fun p01_8(n: Int) {
    if (n <= 0) { return }
    print("2 ")
    if (n == 1) { return }
    print("3 ")

    var count = 2
    var num = 5

    while (count < n) {
        if (isPrime(num)) {
            count += 1
            print("$num ")
        }
        num += 2
    }

//    var a:Int=2
//    var count:Int=0
//    while(n>count){
//        var b:Int=2
//        while(a>=b){
//            if(a==b){
//                print("$a ")
//                count++
//            }
//            if(a%b==0)break
//            b++
//        }
//        a++
//    }
}
// Practice 01-9: 印出n的所有因數
fun p01_9(n: Int){
    for (i in 1..n) {
        if (n % i == 0) {
            print("$i ")
        }
    }
}

fun min(a: Int, b: Int): Int {
    // expression: 運算式
    //  Ex:
    //      a + b
    //      function call
    //      if-else
    //      when

    // statement: 敘述
    //  Ex: for-loop, while-loop

    // C# version
    // int min = (a > b) ? b : a;
    return if (a > b) {
            b
        }
        else {
            a
        }

//    var t:Int
//    if(a>b){
//        t=b
//    }else{
//        t=a
//    }
//    return t
}

// Practice 01-a: 印出a, b的所有公因數
fun p01_a(a:Int,b:Int){
    val min = min(a, b)
    for(i in 1..min){
        if (a % i == 0 && b % i == 0) {
            print("$i ")
        }
    }
    println()

}
// Practice 01-b: 印出a, b的最大公因數 (輾轉相除法)
fun p01_b(a: Int,b: Int){
    var n1:Int
    var n2:Int
    var n3:Int=1
    if(a>b){
        n1=a
        n2=b
    }else{
        n1=b
        n2=a
    }
    while(n3>0){
        n3=n1%n2
        n1=n2
        n2=n3
    }
    print(n1)
}
// Practice 01-c: 印出a, b的最小公倍數
fun p01_c(a: Int,b: Int){
    fun gcd(a:Int,b: Int):Int{
        var n1:Int
        var n2:Int
        var n3:Int=-1
        if(a>b){
            n1=a
            n2=b
        }else{
            n1=b
            n2=a
        }
        do{
            n3=n1%n2
            n1=n2
            n2=n3
        }while(n3!=0)
        return n1
    }
    print(a * b / gcd(a ,b))
}

fun p01_d(n: Int) { // n!, loop version
    var t :Int = 1
    for (i in 2 .. n) {
        t *= i
    }
    print(t)
}

fun p01_e(n: Int): Int { // n!, recursive version
    //n * (n - 1)
    return when (n) {
        1, 2 -> n
        else -> n * p01_e(n - 1)
    }
//    return if (n - 2 == 0) {
//        n
//    } else {
//        n * p01_e(n - 1)
//    }
}