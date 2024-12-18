
class UiHandler(looper: Looper, val viewManager: ViewManager) : Handler(looper) {

    override fun handleMessage(message: Message) {
        if (message.what == 1) {
            viewManager.updateView()
            println("handleMessage UiHandler")
        }
    }
}