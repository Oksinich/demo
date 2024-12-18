class Choreographer {
    private var frameCallback: FrameCallback? = null
    private var vsync:VSync? = null

    fun connectVSync(vsync: VSync){
        this.vsync = vsync
    }


    fun postFrameCallback(callback: FrameCallback) {
        frameCallback = callback
    }

    fun scheduleFrame() {
        println("scheduleFrame")
        vsync?.start{
            frameCallback?.doFrame()
        }
    }
}

// Интерфейс для Callback, который вызывается при отрисовке кадра
fun interface FrameCallback {
    fun doFrame()
}