import androidx.compose.runtime.*
import view.UiUpdate
import view.ViewGroupData
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Классик, должен был быть прослойкой между UiHandler и View
 * который делает типо view.draw, когда UiHandler говорит обновить вьюшку
 */
class ViewManager(val parent: MutableState<ViewGroupData>) {

    val buffer = ConcurrentLinkedQueue<UiUpdate>()

    // тут я представляла что вью будет обновляться, parent будет меняться и это изменение отобращиться на экране, но
    // пока не отображается :ded:
    fun updateView() {
        println("updateView ${buffer.size}")
        if (buffer.isNotEmpty()) {
            parent.value = buffer.poll().update(parent.value)
            println("updateView")
        }
    }
}