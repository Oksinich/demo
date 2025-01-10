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


    // Создается активити тред
    ActivityThread.main()
    ActivityThread.viewRootImpl.setViewHierarchy(parentData)

    // Создаем хендлер юай потока
    val uiHandler = UiHandler(Looper, ActivityThread.viewRootImpl)

    val vsync = VSync()
    val choreographer = Choreographer(vsync, uiHandler)
    ActivityThread.viewRootImpl.setChoreographer(choreographer)

    // Суем юай хендлео в лупер, чтоб лупер отправлял в этот хендлер нужные сообщения
    Looper.registerHandler(1, uiHandler)

    Window(onCloseRequest = ::exitApplication) {
        ViewGroup(parentData.value, ActivityThread.viewRootImpl.buffer)
        ActivityThread.viewRootImpl.addOnViewUpdatedListener(
           object :OnViewUpdatedListener {
               override fun onViewUpdated(updatedViews: MutableState<ViewGroupData>) {
                   parentData.value = updatedViews.value
               }
           }
        )
    }
}