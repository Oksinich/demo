package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.concurrent.ConcurrentLinkedQueue

interface Child
data class ViewData(val id: Int, var width: Int, var height: Int, val color: Color) : Child
data class ViewGroupData(val parent: ViewData, val childrenData: List<Child>) : Child
data class UiUpdate(val update: (ViewGroupData) -> ViewGroupData)

@Composable
fun ChildView(data: ViewData, updatesBuffer: ConcurrentLinkedQueue<UiUpdate>) {
    Box(
        modifier = Modifier
            .background(data.color, RoundedCornerShape(8.dp))
            .width(data.width.dp)
            .height(data.height.dp)
            .clickable {
                println("ChildView ${updatesBuffer.size}")
                updatesBuffer.add(UiUpdate { parentGroupData ->
                    updateChild(parentGroupData, data.id)
                })
            }
    ) {
        Text("Child View ${data.id}", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ViewGroup(data: ViewGroupData, updatesBuffer: ConcurrentLinkedQueue<UiUpdate>) {
    Box(
        modifier = Modifier
            .background(data.parent.color, RoundedCornerShape(8.dp))
            .width(data.parent.width.dp)
            .height(data.parent.height.dp)
            .clickable {
                println("ViewGroup ${updatesBuffer.size}")
                updatesBuffer.add(UiUpdate { parentGroupData ->
                    updateParent(parentGroupData, data.parent.id)
                })
            }
    ) {
        Text("ViewGroup ${data.parent.id}", modifier = Modifier.align(Alignment.Center))
        data.childrenData.forEach { child ->
            when (child) {
                is ViewData -> ChildView(child, updatesBuffer)
                is ViewGroupData -> ViewGroup(child, updatesBuffer)
            }
        }
    }
}

fun updateChild(parentGroupData: ViewGroupData, id: Int): ViewGroupData {
    val newChildren = parentGroupData.childrenData.map { child ->
        when (child) {
            is ViewData -> {
                if (child.id == id) {
                    child.copy(
                        width = maxOf(1, child.width - 20),
                        height = maxOf(1, child.height - 20)
                    )
                } else {
                    child
                }
            }

            is ViewGroupData -> updateChild(child, id)
            else -> child
        }
    }.toMutableList()
    return parentGroupData.copy(childrenData = newChildren)
}

fun updateParent(parentGroupData: ViewGroupData, id: Int): ViewGroupData {
    if (parentGroupData.parent.id == id) {
        val newParent = parentGroupData.parent.copy(
            width = maxOf(1, parentGroupData.parent.width - 20),
            height = maxOf(1, parentGroupData.parent.height - 20)
        )
        if (checkNeedResizeChildren(parentGroupData)) {
            val newChildren = resizeChildren(parentGroupData)
            return parentGroupData.copy(parent = newParent, childrenData = newChildren)
        }
        return parentGroupData.copy(parent = newParent)

    } else {
        val newChildren = parentGroupData.childrenData.map { child ->
            when (child) {
                is ViewGroupData -> updateParent(child, id)
                else -> child
            }
        }.toMutableList()
        return parentGroupData.copy(childrenData = newChildren)
    }
}

fun resizeChildren(parentGroupData: ViewGroupData): MutableList<Child> {
    return parentGroupData.childrenData.map { child ->
        when (child) {
            is ViewData -> child.copy(
                width = maxOf(1, (child.width / 2)),
                height = maxOf(1, (child.height / 2))
            )

            is ViewGroupData -> {
                val newParent = child.parent.copy(
                    width = maxOf(1, (child.parent.width / 2)),
                    height = maxOf(1, (child.parent.height / 2))
                )
                val newChild = resizeChildren(child)
                child.copy(parent = newParent, childrenData = newChild)
            }

            else -> child
        }
    }.toMutableList()
}

fun checkNeedResizeChildren(parentGroupData: ViewGroupData): Boolean {
    parentGroupData.childrenData.forEach { child: Child ->
        when (child) {
            is ViewData -> {
                if (child.height >= parentGroupData.parent.height - 20 || child.width >= parentGroupData.parent.width - 20) {
                    return true
                }
            }

            is ViewGroupData -> return checkNeedResizeChildren(child)
        }
    }
    return false
}