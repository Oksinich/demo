import androidx.compose.runtime.*
import view.UiUpdate
import view.ViewGroupData
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Классик, должен был быть прослойкой между UiHandler и View
 * который делает типо view.draw, когда UiHandler говорит обновить вьюшку
 */
class ViewRootImpl {

    private var viewHierarchy: MutableState<ViewGroupData>? = null
    private var choreographer: Choreographer? = null
    val buffer = ConcurrentLinkedQueue<UiUpdate>()

    private val onViewUpdated: MutableList<OnViewUpdatedListener> = mutableListOf()

    fun setViewHierarchy(views: MutableState<ViewGroupData>) {
        viewHierarchy = views
    }

    fun scheduleTraversals() {
        choreographer?.postFrameCallback { updateView() }
    }

    // тут я представляла что вью будет обновляться, parent будет меняться и это изменение отобращиться на экране, но
    // пока не отображается :ded:
    fun updateView() {
        println("updateView ${buffer.size}")
        if (buffer.isNotEmpty()) {
            viewHierarchy?.value = buffer.poll().update(viewHierarchy!!.value)
            println("updateView")
        }
        onViewUpdated.forEach { viewHierarchy?.let { it1 -> it.onViewUpdated(it1) } }
    }

    fun setChoreographer(choreographer: Choreographer) {
        this.choreographer = choreographer
    }

    fun addOnViewUpdatedListener(listener: OnViewUpdatedListener) {
        onViewUpdated.add(listener)
    }
}

interface OnViewUpdatedListener {

    fun onViewUpdated(updatedViews: MutableState<ViewGroupData>)
}