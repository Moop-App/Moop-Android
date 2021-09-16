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
package soup.movie.theater.sort

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soup.movie.model.Theater
import soup.movie.theater.CgvChip
import soup.movie.theater.LotteChip
import soup.movie.theater.MegaboxChip
import soup.movie.theater.R
import soup.movie.theater.draggableList
import soup.movie.theater.rememberDraggableListState
import soup.movie.util.debounce

@Composable
internal fun TheaterSortScreen(viewModel: TheaterSortViewModel, onAddItemClick: () -> Unit) {
    ProvideWindowInsets {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(start = false, end = false),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.theater_sort_title)) }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { debounce(onAddItemClick) }) {
                    Icon(
                        painterResource(R.drawable.ic_round_add),
                        contentDescription = stringResource(R.string.theater_select_action_confirm)
                    )
                }
            }
        ) { paddingValues ->
            val selectedTheaters = viewModel.selectedTheaters
            val modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

            if (selectedTheaters.isEmpty()) {
                TheaterSortNoItem(modifier)
            } else {
                TheaterSortReorderList(
                    selectedTheaters,
                    modifier = modifier,
                    onMove = { fromPosition, toPosition ->
                        viewModel.onItemMove(fromPosition, toPosition)
                    }
                )
            }
        }
    }
}

@Composable
private fun TheaterSortNoItem(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painterResource(R.drawable.ic_round_no_theaters),
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(68.dp)
        )
        Text(
            stringResource(R.string.theater_empty_description),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun TheaterSortReorderList(
    selectedTheaters: List<Theater>,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val draggableListState = rememberDraggableListState(lazyListState, onMove = onMove)
    LazyColumn(
        modifier = modifier.draggableList(draggableListState),
        state = lazyListState
    ) {
        itemsIndexed(selectedTheaters) { index, theater ->
            val itemState = draggableListState.getItemState(index)
            val transY by animateFloatAsState(targetValue = itemState.offset.y)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 12.dp)
                    .graphicsLayer { translationY = transY }
                    .zIndex(itemState.zIndex)
            ) {
                when (theater.type) {
                    Theater.TYPE_CGV -> CgvChip(text = theater.name)
                    Theater.TYPE_LOTTE -> LotteChip(text = theater.name)
                    Theater.TYPE_MEGABOX -> MegaboxChip(text = theater.name)
                    else -> throw IllegalArgumentException("This is not valid type.")
                }
                Spacer(modifier = Modifier.weight(1f))
                // TODO: Touch Down 시, Drag가 시작하도록 수정 필요
                Image(
                    painterResource(R.drawable.ic_round_drag_handle),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}
