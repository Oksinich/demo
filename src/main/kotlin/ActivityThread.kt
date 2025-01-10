object ActivityThread {

    private var activityThread: ActivityThread? = null
    lateinit var viewRootImpl: ViewRootImpl
        private set

    // типо имитация метода мейн в ActivityThread
    fun main() {
        Looper.prepareMainLooper()

        activityThread = this

        // Запускается поток
        Thread {
            Looper.loop()
        }.start()

        handleResumeActivity()
    }

    // Активити тред говорит что готов ебашить, создает вью рут импл
    private fun handleResumeActivity() {
        println("Activity Resumed - Requesting Frame")
        viewRootImpl = ViewRootImpl()
    }
}