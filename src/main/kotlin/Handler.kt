

// Абстракция для Handler, который отправляет сообщения в Looper
abstract class Handler(private val looper: Looper) {

    abstract fun handleMessage(message: Message)

    fun sendMessage(message: Message) {
        looper.post(message)
    }
}