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
package soup.movie.theater

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ReorderableList(
    listState: ReorderableListState,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    var overscrollJob by remember { mutableStateOf<Job?>(null) }
    LazyColumn(
        modifier = modifier.pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset -> listState.onDragStart(offset) },
                onDrag = { change, offset ->
                    change.consumeAllChanges()
                    listState.onDrag(offset)

                    if (overscrollJob?.isActive == true)
                        return@detectDragGesturesAfterLongPress

                    listState.checkForOverScroll()
                        .takeIf { it != 0f }
                        ?.let {
                            overscrollJob =
                                scope.launch { listState.lazyListState.scrollBy(it) }
                        }
                        ?: run { overscrollJob?.cancel() }
                },
                onDragEnd = { listState.onDragInterrupted() },
                onDragCancel = { listState.onDragInterrupted() }
            )
        },
        state = listState.lazyListState,
        content = content
    )
}

@Composable
fun rememberReorderableListState(
    lazyListState: LazyListState = rememberLazyListState(),
    onMove: (Int, Int) -> Unit,
): ReorderableListState {
    return remember { ReorderableListState(lazyListState = lazyListState, onMove = onMove) }
}

class ReorderableListState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit
) {

    private fun LazyListState.getVisibleItemInfoFor(absoluteIndex: Int): LazyListItemInfo? {
        return layoutInfo.visibleItemsInfo.getOrNull(absoluteIndex - layoutInfo.visibleItemsInfo.first().index)
    }

    private val LazyListItemInfo.offsetEnd: Int
        get() = offset + size

    private var draggedDistance by mutableStateOf(0f)

    // used to obtain initial offsets on drag start
    private var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)

    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)

    private val initialOffsets: Pair<Int, Int>?
        get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }

    val elementDisplacement: Float?
        get() = currentIndexOfDraggedItem
            ?.let { lazyListState.getVisibleItemInfoFor(absoluteIndex = it) }
            ?.let { item ->
                (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset
            }

    private val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absoluteIndex = it)
        }

    private var overscrollJob by mutableStateOf<Job?>(null)

    fun offsetOf(index: Int): Float {
        return elementDisplacement.takeIf {
            index == currentIndexOfDraggedItem
        } ?: 0f
    }

    fun zIndexOf(index: Int): Float {
        return if (index == currentIndexOfDraggedItem) {
            1f
        } else {
            0f
        }
    }

    fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
            ?.also {
                currentIndexOfDraggedItem = it.index
                initiallyDraggedElement = it
            }
    }

    fun onDragInterrupted() {
        draggedDistance = 0f
        currentIndexOfDraggedItem = null
        initiallyDraggedElement = null
        overscrollJob?.cancel()
    }

    fun onDrag(offset: Offset) {
        draggedDistance += offset.y

        initialOffsets?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo
                    .filterNot { item -> item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index }
                    .firstOrNull { item ->
                        val delta = startOffset - hovered.offset
                        when {
                            delta > 0 -> (endOffset > item.offsetEnd)
                            else -> (startOffset < item.offset)
                        }
                    }
                    ?.also { item ->
                        currentIndexOfDraggedItem?.let { current ->
                            onMove.invoke(
                                current,
                                item.index
                            )
                        }
                        currentIndexOfDraggedItem = item.index
                    }
            }
        }
    }

    fun checkForOverScroll(): Float {
        return initiallyDraggedElement?.let {
            val startOffset = it.offset + draggedDistance
            val endOffset = it.offsetEnd + draggedDistance

            return@let when {
                draggedDistance > 0 -> (endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
                draggedDistance < 0 -> (startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
                else -> null
            }
        } ?: 0f
    }
}
