
fun sendPacket(
    content: ByteArray,
    onSuccess: (ByteArray) -> Unit // callback function
) {
    // 開執行緒送封包

    // Receive response
    val response: ByteArray = ByteArray(3)

    onSuccess(response)
}

fun main() {
    sendPacket(ByteArray(0)) { received ->
        // 處理收到的封包
    }
}

