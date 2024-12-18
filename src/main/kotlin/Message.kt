// Абстракция для сообщения, которое обрабатывается в Looper
data class Message(val what: Int, val data: Any? = null)