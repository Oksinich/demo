

class Looper {

    private val messageQueue = mutableListOf<Message>()
    private var isRunning = true

    fun loop() {
        println("loop")
        while (isRunning) {
            if (messageQueue.isNotEmpty()) {
                println("messageQueue.isNotEmpty()")
                val message = messageQueue.removeFirst()
                handlers[message.what]?.handleMessage(message)
                println("handleMessage")
            }
        }
    }

    private val handlers = mutableMapOf<Int, Handler>()

    fun registerHandler(messageType:Int, handler:Handler){
        handlers[messageType] = handler
    }

    fun quit() {
        isRunning = false
    }

    fun post(message: Message) {
        messageQueue.add(message)
    }
}