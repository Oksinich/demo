
class ActivityThread(val looper: Looper, val choreographer: Choreographer) {

    fun handleResumeActivity() {
        println("Activity Resumed - Requesting Frame")
        choreographer.scheduleFrame()
    }
}