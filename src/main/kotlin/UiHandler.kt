class UiHandler(
    looper: Looper,
    val viewRootImpl: ViewRootImpl,
) : Handler(looper) {

    override fun handleMessage(message: Message) {
        if (message.what == 1) {
            viewRootImpl.scheduleTraversals()
        }
    }
}