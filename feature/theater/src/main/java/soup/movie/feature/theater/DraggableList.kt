/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.feature.theater

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

interface DraggableListState {
    fun onDragStart(offset: Offset)
    fun onDragStart(index: Int)
    fun onDrag(offset: Offset)
    fun onDragEnd()
    fun onDragCancel()
    fun getItemState(index: Int): DraggableItemState
}

@Stable
data class DraggableItemState(
    val dragging: Boolean,
    val offset: Offset,
    val zIndex: Float,
)

@Composable
fun rememberDraggableListState(
    lazyListState: LazyListState,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
): DraggableListState {
    val onMoveState = rememberUpdatedState(onMove)
    return remember(lazyListState) {
        DefaultDraggableListState(lazyListState = lazyListState, onMove = onMoveState.value)
    }
}

fun Modifier.draggableList(
    listState: DraggableListState,
): Modifier = pointerInput(Unit) {
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            listState.onDragStart(offset)
        },
        onDrag = { change, offset ->
            change.consume()
            listState.onDrag(offset)
        },
        onDragEnd = {
            listState.onDragEnd()
        },
        onDragCancel = {
            listState.onDragCancel()
        },
    )
}

fun Modifier.draggableItem(
    index: Int,
    listState: DraggableListState,
): Modifier = pointerInput(Unit) {
    detectDragGestures(
        onDragStart = {
            listState.onDragStart(index)
        },
        onDrag = { change, offset ->
            change.consume()
            listState.onDrag(offset)
        },
        onDragEnd = {
            listState.onDragEnd()
        },
        onDragCancel = {
            listState.onDragCancel()
        },
    )
}

class DefaultDraggableListState(
    private val lazyListState: LazyListState,
    private val onMove: (fromIndex: Int, toIndex: Int) -> Unit,
) : DraggableListState {

    private var draggingDistance by mutableStateOf(0f)
    private var draggingItem by mutableStateOf<LazyListItemInfo?>(null)
    private var currentIndexOfDraggingItem by mutableStateOf<Int?>(null)

    private inline val LazyListItemInfo.offsetEnd: Int
        get() = offset + size

    private val visibleItemsInfo: List<LazyListItemInfo>
        get() = lazyListState.layoutInfo.visibleItemsInfo

    override fun onDragStart(offset: Offset) {
        visibleItemsInfo
            .firstOrNull { offset.y.toInt() in it.offset..it.offsetEnd }
            ?.also {
                draggingItem = it
                currentIndexOfDraggingItem = it.index
            }
    }

    override fun onDragStart(index: Int) {
        visibleItemsInfo
            .find(absoluteIndex = index)
            ?.also {
                draggingItem = it
                currentIndexOfDraggingItem = it.index
            }
    }

    override fun onDrag(offset: Offset) {
        draggingDistance += offset.y

        val draggingItem = draggingItem ?: return
        val currentIndex = currentIndexOfDraggingItem ?: return
        val hovered = visibleItemsInfo.find(absoluteIndex = currentIndex) ?: return
        val startOffset = draggingItem.offset + draggingDistance
        val endOffset = draggingItem.offsetEnd + draggingDistance
        visibleItemsInfo
            .filterNot { item -> startOffset > item.offsetEnd || item.offset > endOffset || hovered.index == item.index }
            .firstOrNull { item ->
                if (startOffset <= hovered.offset) {
                    startOffset < item.offset
                } else {
                    endOffset > item.offsetEnd
                }
            }?.also { item ->
                onMove(currentIndex, item.index)
                currentIndexOfDraggingItem = item.index
            }
    }

    override fun onDragEnd() {
        resetDraggingState()
    }

    override fun onDragCancel() {
        resetDraggingState()
    }

    override fun getItemState(index: Int): DraggableItemState {
        val isDraggingItem = index == currentIndexOfDraggingItem
        val offset = if (isDraggingItem) {
            val hovered = visibleItemsInfo.find(absoluteIndex = index)
            if (hovered != null) {
                val offsetY = (draggingItem?.offset ?: 0) + draggingDistance - hovered.offset
                Offset(x = 0f, y = offsetY)
            } else {
                Offset.Zero
            }
        } else {
            Offset.Zero
        }
        return DraggableItemState(
            dragging = isDraggingItem,
            offset = offset,
            zIndex = if (isDraggingItem) 1f else 0f,
        )
    }

    private fun resetDraggingState() {
        draggingDistance = 0f
        draggingItem = null
        currentIndexOfDraggingItem = null
    }

    private fun List<LazyListItemInfo>.find(absoluteIndex: Int): LazyListItemInfo? {
        return find { it.index == absoluteIndex }
    }
}
