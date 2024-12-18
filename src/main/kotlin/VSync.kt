class VSync {

    private var onVSync:(()->Unit)? = null

    fun start(onVSync: () -> Unit) {
        this.onVSync = onVSync
        Thread{
            while (true) {
                Thread.sleep(16)
                this.onVSync?.invoke()
            }
        }.start()
    }
}