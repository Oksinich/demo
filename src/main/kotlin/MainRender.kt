import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import view.ViewData
import view.ViewGroup
import view.ViewGroupData

val child1 = view.ViewData(1, 600, 600, Color.Blue)
val child2 = view.ViewData(2, 400, 400, Color.Green)
val child3 = view.ViewGroupData(
    parent = view.ViewData(3, 250, 250, Color.Yellow),
    childrenData = mutableListOf(view.ViewData(4, 100, 100, Color.Red))
)


fun main() = application {
    // Иерархия вьюшек
    val parentData = remember {
        mutableStateOf(
            ViewGroupData(
                parent = ViewData(0, 800, 800, Color.LightGray),
                childrenData = listOf(child1, child2, child3)
            )
        )
    }
    val viewManager = ViewManager(parentData)

    val choreographer = Choreographer()
    val vsync = VSync()
    // Хореографер начинает следить за vsync
    choreographer.connectVSync(vsync)
    val looper = Looper()
    // Создается активити тред
    val activityThread = ActivityThread(looper, choreographer)
    // Запускается поток
    Thread {
        looper.loop()
    }.start()
    // Активити тред говорит что готов ебашить и сообщает хореограферу что надо запланировать кадр
    activityThread.handleResumeActivity()

    // Создаем хендлер юай потока
    val uiHandler = UiHandler(looper, viewManager)
    // Суем его в лупер, чтоб лупер отправлял в этот хендлер нужные сообщения
    looper.registerHandler(1, uiHandler)

    // Когда хореографер получает сигнал, то отправляется событие типо parentData.value надо отобразить
    choreographer.postFrameCallback(FrameCallback {
        uiHandler.sendMessage(Message(1, parentData.value))
    })

    Window(onCloseRequest = ::exitApplication) {
        ViewGroup(parentData.value, viewManager.buffer)
    }
}