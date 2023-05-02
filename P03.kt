////class IntArrayList v vv
////class IntTailedLinkedList v
////class IntDLinkedList v vv
////class IntTailedDLinkedList v vv
////class IntCircuitLinkedList v
////class IntCircuitDLinkedList
//
////getSize(): Int
////add(value: Int)
////remove(value: Int)
////get(index: Int): Int
////insert(index: Int, value: Int)
////removeAt(index: Int)
////printList()
////contains(value: Int): Boolean
//
//enum class ListType {
//    intTailedDLinkedList,
//    intTailedLinkedList,
//    intDLinkedList,
//    intArrayList,
//    intCircuitLinkedList,
//    intCircuitDLinkedList
//}
//
//fun secondExam.main() {
//
////    var a = IntTailedDLinkedList()
//
//    for (i in ListType.values()) {
//        println(i.name)
//        test(i)
//        println()
//        println("-----------------------------------------")
//    }
//}
//
//fun test(type: ListType) {
//    when (type) {
//        ListType.intTailedLinkedList -> { intTailedLinkedListTest() }
//        ListType.intTailedDLinkedList -> { intTailedDLinkedListTest() }
//        ListType.intArrayList -> { intArrayListTest() }
//        ListType.intCircuitDLinkedList -> { intCircuitDLinkedListTest() }
//        ListType.intCircuitLinkedList -> { intCircuitLinkedListTest() }
//        ListType.intDLinkedList -> { intDLinkedListTest() }
//    }
//}
//fun intTailedLinkedListTest() {
//    val list = IntTailedLinkedList()
//    list.insert(0,5)
//    print("insert 5 at first:\t")
//    list.printList()
//    list.remove(5)
//    print("remove 5:\t")
//    list.printList()
//    list.insert(list.getSize(),6)
//    print("insert 6 at last:\t")
//    list.printList()
//    for (i in 0 until 3) {
//        list.add(3)
//    }
//    for (i in 0 until 5) {
//        list.add(i)
//    }
//    for (i in 0 until 2) {
//        list.insert(0,3)
//    }
//    print("make a list:\t")
//    list.printList()
//    list.remove(3)
//    print("remove 3:\t")
//    list.printList()
//    list.removeAt(0)
//    print("remove first number:\t")
//    list.printList()
//    list.removeAt(list.getSize() - 1)
//    print("remove last number:\t")
//    list.printList()
//}
//fun intTailedDLinkedListTest() {
//    val list = IntTailedDLinkedList()
//    list.insert(0,5)
//    print("insert 5 at first:\t")
//    list.printList()
//    list.remove(5)
//    print("remove 5:\t")
//    list.printList()
//    list.insert(list.getSize(),6)
//    print("insert 6 at last:\t")
//    list.printList()
//    for (i in 0 until 3) {
//        list.add(3)
//    }
//    for (i in 0 until 5) {
//        list.add(i)
//    }
//    for (i in 0 until 2) {
//        list.insert(0,3)
//    }
//    print("make a list:\t")
//    list.printList()
//    list.remove(3)
//    print("remove 3:\t")
//    list.printList()
//    list.removeAt(0)
//    print("remove first number:\t")
//    list.printList()
//    list.removeAt(list.getSize() - 1)
//    print("remove last number:\t")
//    list.printList()
//}
//
//fun intDLinkedListTest() {
//    val list = IntDLinkedList()
//    list.insert(0,5)
//    print("insert 5 at first:\t")
//    list.printList()
//    list.remove(5)
//    print("remove 5:\t")
//    list.printList()
//    list.insert(list.getSize(),6)
//    print("insert 6 at last:\t")
//    list.printList()
//    for (i in 0 until 3) {
//        list.add(3)
//    }
//    for (i in 0 until 5) {
//        list.add(i)
//    }
//    for (i in 0 until 2) {
//        list.insert(0,3)
//    }
//    print("make a list:\t")
//    list.printList()
//    list.remove(3)
//    print("remove 3:\t")
//    list.printList()
//    list.removeAt(0)
//    print("remove first number:\t")
//    list.printList()
//    list.removeAt(list.getSize() - 1)
//    print("remove last number:\t")
//    list.printList()
//}
//
//fun intCircuitLinkedListTest() {
//    val list = IntCircuitLinkedList()
//    list.insert(0,5)
//    print("insert 5 at first:\t")
//    list.printList()
//    list.remove(5)
//    print("remove 5:\t")
//    list.printList()
//    list.insert(list.getSize(),6)
//    print("insert 6 at last:\t")
//    list.printList()
//    for (i in 0 until 3) {
//        list.add(3)
//    }
//    for (i in 0 until 5) {
//        list.add(i)
//    }
//    for (i in 0 until 2) {
//        list.insert(0,3)
//    }
//    print("make a list:\t")
//    list.printList()
//    list.remove(3)
//    print("remove 3:\t")
//    list.printList()
//    list.removeAt(0)
//    print("remove first number:\t")
//    list.printList()
//    list.removeAt(list.getSize() - 1)
//    print("remove last number:\t")
//    list.printList()
//}
//
//fun intCircuitDLinkedListTest() {
//    val list = IntCircuitDLinkedList()
//    list.insert(0,5)
//    print("insert 5 at first:\t")
//    list.printList()
//    list.remove(5)
//    print("remove 5:\t")
//    list.printList()
//    list.insert(list.getSize(),6)
//    print("insert 6 at last:\t")
//    list.printList()
//    for (i in 0 until 3) {
//        list.add(3)
//    }
//    for (i in 0 until 5) {
//        list.add(i)
//    }
//    for (i in 0 until 2) {
//        list.insert(0,3)
//    }
//    print("make a list:\t")
//    list.printList()
//    list.remove(3)
//    print("remove 3:\t")
//    list.printList()
//    list.removeAt(0)
//    print("remove first number:\t")
//    list.printList()
//    list.removeAt(list.getSize() - 1)
//    print("remove last number:\t")
//    list.printList()
//}
//
//fun intArrayListTest() {
//    val list = IntArrayList()
//    list.insert(0,5)
//    print("insert 5 at first:\t")
//    list.printList()
//    list.remove(5)
//    print("remove 5:\t")
//    list.printList()
//    list.insert(list.size, 6)
//    print("insert 6 at last:\t")
//    list.printList()
//    for (i in 0 until 3) {
//        list.add(3)
//    }
//    for (i in 0 until 5) {
//        list.add(i)
//    }
//    for (i in 0 until 2) {
//        list.insert(0,3)
//    }
//    print("make a list:\t")
//    list.printList()
//    list.remove(3)
//    print("remove 3:\t")
//    list.printList()
//    list.removeAt(0)
//    print("remove first number:\t")
//    list.printList()
//    list.removeAt(list.size - 1)
//    print("remove last number:\t")
//    list.printList()
//}