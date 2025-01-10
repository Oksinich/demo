import java.util.concurrent.ConcurrentLinkedQueue

object Looper {

    private val messageQueue: ConcurrentLinkedQueue<Message> = ConcurrentLinkedQueue()
    private var isRunning = true

    fun prepareMainLooper() {
        // инициализируется лупер
    }

    fun loop() {
        println("loop")
        while (isRunning) {
            if (messageQueue.isNotEmpty()) {
                val message = messageQueue.poll()
                handlers[message.what]?.handleMessage(message)
                println("handleMessage")
            }
        }
    }

    private val handlers = mutableMapOf<Int, Handler>()

    fun registerHandler(messageType: Int, handler: Handler) {
        handlers[messageType] = handler
    }

    fun post(message: Message) {
        messageQueue.add(message)
    }
}