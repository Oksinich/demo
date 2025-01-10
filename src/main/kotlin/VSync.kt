class VSync {

    fun start(onVSync: () -> Unit) {
        Thread{
            while (true) {
                Thread.sleep(16)
                onVSync.invoke()
            }
        }.start()
    }
}