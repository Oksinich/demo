class Choreographer(private val vsyncSource: VSync, private val uiHandler: UiHandler) {

    private var frameCallback: FrameCallback? = null
    private var vsync:VSync? = null

    init {
        vsync = vsyncSource
        scheduleVsyncFrame()
    }


    fun postFrameCallback(callback: FrameCallback) {
        frameCallback = callback
    }

    private fun scheduleVsyncFrame() {
        println("scheduleFrame")
        vsync?.start{
            uiHandler.sendMessage(Message(1, "do frame"))
            frameCallback?.doFrame()
        }
    }
}

// Интерфейс для Callback, который вызывается при отрисовке кадра
fun interface FrameCallback {
    fun doFrame()
}