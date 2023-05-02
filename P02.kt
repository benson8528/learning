//class Matrix3_3 {
//    var entries = arrayOf(
//        arrayOf(0, 0, 0),
//        arrayOf(0, 0, 0),
//        arrayOf(0, 0, 0)
//    )
//}
//
//fun println(matrix: Matrix3_3) {
//    /*
//    0 0 0
//    1 1 1
//    2 2 2
//     */
//    for (i in 0 until matrix.entries.size) {
//        for (j in 0 until matrix.entries[i].size) {
//            print("${matrix.entries[i][j]}\t")
//        }
//        println()
//    }
//}
//fun secondExam.main() {
//    val x = -13
//    val y = 5
//    print(x / y * y + x % y)
//    val a: Matrix3_3 = Matrix3_3()
//    a.entries = arrayOf(
//        arrayOf(1, 1, 3),
//        arrayOf(1, 1, 1),
//        arrayOf(2, 2, 2))
//
//    val b: Matrix3_3 = Matrix3_3()
//    b.entries = arrayOf(
//        arrayOf(1, 1, 1),
//        arrayOf(3, 4, 3),
//        arrayOf(5, 5, 5))
//
////    var c: Matrix3_3 = multiply(a,b)
////    println(c)
//    val time: Time = Time()
//    time.hour = 5
//    time.minute = 5
//    time.second = 5
//
////    val twoHoursLater = addHours(time, 2)
////    if (time.hours != 7 || time.minutes != 5 || time.seconds != 5) {
////        println("ERROR")
////    }
//
//
////    print(addSeconds(time, 115))
////    println()
////    print(time)
////    print(addMonths(Date(), -25))
//}
//
//fun add(a: Matrix3_3, b: Matrix3_3): Matrix3_3 {
//    val o: Matrix3_3 = Matrix3_3()
//    for (i in 0 until a.entries.size) {
//        for (j in 0 until a.entries[i].size) {
//            o.entries[i][j] = a.entries[i][j] + b.entries[i][j]
//        }
//    }
//    return o
//}
//
//fun subtract(a: Matrix3_3, b: Matrix3_3): Matrix3_3 {
//    var o: Matrix3_3 = Matrix3_3()
//    for (i in 0 until a.entries.size) {
//        for (j in 0 until a.entries[i].size) {
//            o.entries[i][j] = a.entries[i][j] - b.entries[i][j]
//        }
//    }
//    return o
//}
//
//fun multiply(a: Matrix3_3, b: Matrix3_3): Matrix3_3 {
//    val o: Matrix3_3 = Matrix3_3()
//    var x: Int =0
////    println(o)
//    for (i in 0 until o.entries.size) {
//
//        for (j in 0 until o.entries[i].size) {
//            for (k in 0 until o.entries[j].size) {
//                var ta = a.entries[i][k]
//                var tb = b.entries[k][j]
//                o.entries[i][j] += a.entries[i][k] * b.entries[k][j]
//            }
//                // 00 01 02 00*00 + 01*10 + 02*20 | 00*01 + 01*11 + 02*21 | 00*02 + 01*12 + 02*22
//                // 10 11 12
//                // 20 21 22
//        }
//    }
//    return o
//}
//
//class Time( // primary constructor
//    hour: Int,
//    minute: Int,
//    second: Int
//) {
////    var hours: Int? = 0
////    var minutes: Int = 0
////    var seconds: Int = 0
//
//    var hour: Int
//        private set
//    init {
//        if (hour == null || hour >= 24 || hour < 0) {
//            // error
//        }
//        this.hour = hour
//    }
//
//    var minute: Int
//    init {
//        this.minute = minute
//    }
//
//    var second: Int
//    init {
//        this.second = second
//    }
//
//    // secondary constructor
//    constructor(time: Time): this(time.hour, time.minute, time.second)
//    constructor(): this(0, 0, 0)
//
//    fun print() {
//        print(String.format("%02d:%02d:%02d",this.hour,this.minute,this.second))
//    }
//}
//
//fun print(time: Time) {
//    time.print()
//
//    // HH:mm:ss (24H)
//    val out = String.format("%02d:%02d:%02d",time.hour,time.minute,time.second)
//    print(out)
////    print("${time.hours}:${time.minutes}:${time.seconds}")
//}
//
//fun addSeconds(time: Time, amount: Int): Time {
//    val sec = time.second + amount
//
//    val timeT = Time(time)
//
////    val timeT: Time = Time()
////    timeT.second = time.second
////    timeT.minute = time.minute
////    timeT.hour = time.hour
//
//    return if (sec >= 60){
//        // a >= 0 -> a = (a / b) * b + (a % b)
//        // a < 0 -> ???
//        timeT.second = sec % 60
//        addMinutes(timeT, sec / 60)
//    } else if (sec < 0) {
//        if (sec % 60  == 0) {
//            timeT.second = 0
//            addMinutes(timeT,sec / 60)
//        } else {
//            timeT.second = 60 + sec % 60
//            addMinutes(timeT,sec / 60 - 1)
//        }
//
//    } else {
//        timeT.second = sec
//        timeT
//    }
////    return time
//}
//
//fun addMinutes(time: Time, amount: Int): Time {
//    val min = time.minute + amount
//    var timeT: Time = Time()
//    timeT.second = time.second
//    timeT.minute = time.minute
//    timeT.hour = time.hour
//    return if (min >= 60) {
//        timeT.minute = min % 60
//        addHours(timeT, min / 60)
//    } else if (min < 0) {
//        if (min % 60 == 0) {
//            timeT.minute = 0
//            addHours(timeT,min / 60)
//        } else {
//            timeT.minute = 60 + min % 60
//            addHours(timeT, min / 60 - 1)
//        }
//    } else {
//        timeT.minute = min
//        timeT
//    }
//}
//
//fun addHours(time: Time, amount: Int): Time {
//    val hour = time.hour!! + amount
//    var timeT: Time = Time()
//    timeT.second = time.second
//    timeT.minute = time.minute
//    timeT.hour = time.hour
//    return if (hour >= 0) {
//        timeT.hour = hour % 24
//        timeT
//    } else {
//        when (hour % 24) {
//            0 -> timeT.hour = 0
//            else -> timeT.hour = (hour % 24) + 24
//        }
//        timeT
//    }
//}
//
//class Date private constructor(
//    date: Date
//) {
//    // Copy constructor
////    constructor(date: Date) {
////        this.year = date.year
////        this.month = date.month
////        this.day = date.day
////    }
//
////    var year: Int = 1970
//
//    var year: Int
//    init {
//        year = 1970
//    }
//
//    // 取值 getter; 設值 setter
//    var month: Int = 1
////        private set
//
//    var day: Int = 1
//
//    init {
//        this.year = date.year
//        this.month = date.month
//        this.day = date.day
//    }
//
//    fun addYear() {
//        this
//    }
//
//    fun addYears(amount: Int): Date {
//        val years = this.year + amount
//        val dateT: Date = Date(this)
////        dateT.day = this.day
////        dateT.month = this.month
//        dateT.year = years
//
//        return if (isLeapYear(this.year) && !isLeapYear(years) ) {
//            if (dateT.month == 2 && dateT.day == 29) {
//                dateT.day = 28
//            }
//            dateT
//        }
//        else {
//            dateT
//        }
//    }
//
//}
//
//fun addYear(`this`: Date) {
//
//}
//
//fun print(date: Date) {
//    if (date.year < 0) {
//        print(String.format("BC %04d/%02d/%02d", -date.year, date.month, date.day))
//    }
//    else {
//        print(String.format("%04d/%02d/%02d", date.year, date.month, date.day))
//    }
//}
//
//fun isLeapYear(year: Int): Boolean {
//    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
//}
////var isLeapYear: Boolean = false
//val a = 100 // Global variable 全域變數
//
//fun addYears(date: Date, amount: Int): Date{
//
//    val years = date.year + amount
//    var dateT: Date = Date() // 呼叫沒有參數的建構子
//    dateT.day = date.day
//    dateT.month = date.month
//    dateT.year = years
//    return if (isLeapYear(date.year) && !isLeapYear(years) ) {
//        if (dateT.month == 2 && dateT.day == 29) {
//            dateT.day = 28
//        }
//        dateT
//    } else {
//        dateT
//    }
//}
//
//fun addMonths(date: Date, amount: Int): Date {
//    val months = date.month+ amount
//    var dateT: Date = Date()
//    dateT.day = date.day
//    dateT.month = date.month
//    dateT.year = date.year
//    return if (months > 12) {
//        dateT.month = months % 12
//        if (dateT.month == 0 ) {
//            dateT.month = 12
//        }
//        addYears(dateT, months / 12)
//    } else if (months <= 0) {
//        dateT.month = 12 + months % 12
//        addYears(dateT,months / 12 - 1)
//    } else {
//        dateT.month = months
//        dateT
//    }
//}
//
//fun numOfDays(year: Int, month: Int): Int {
//    return when (month) {
//        1, 3, 5, 7, 8, 10, 12 -> 31
//        4, 6, 9, 11 -> 30
//        else -> {
//            if (isLeapYear(year)) {
//                29
//            } else {
//                28
//            }
//        }
//    }
//}
//
//fun addDays(date: Date, amount: Int): Date {
//    var days = amount + date.day
//    var dateT: Date = Date()
//    dateT.day = date.day
//    dateT.month = date.month
//    dateT.year = date.year
//    while (days > numOfDays(dateT.year, dateT.month)) {
//        days -= numOfDays(dateT.year, dateT.month)
//        dateT = addMonths(dateT, 1)
//    }
//
//    while (days <= 0) {
//        dateT = addMonths(dateT, -1)
//        days += numOfDays(dateT.year, dateT.month)
//    }
//
//    dateT.day = days
//
//    return dateT
////    return date
//}
