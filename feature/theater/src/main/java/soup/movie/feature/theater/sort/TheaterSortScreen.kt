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
package soup.movie.feature.theater.sort

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.designsystem.util.debounce
import soup.movie.feature.theater.TheaterChip
import soup.movie.feature.theater.draggableItem
import soup.movie.feature.theater.draggableList
import soup.movie.feature.theater.rememberDraggableListState
import soup.movie.model.TheaterModel
import soup.movie.resources.R

@Composable
fun TheaterSortScreen(
    viewModel: TheaterSortViewModel,
    upPress: () -> Unit,
    onAddItemClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        coroutineScope.launch {
            viewModel.saveSnapshot()
            upPress()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.theater_sort_title)) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { debounce(onAddItemClick) }) {
                Icon(
                    MovieIcons.Add,
                    contentDescription = stringResource(R.string.theater_select_action_confirm),
                )
            }
        },
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
                },
            )
        }
    }
}

@Composable
private fun TheaterSortNoItem(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painterResource(MovieIcons.NoTheaters),
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(68.dp),
            colorFilter = ColorFilter.tint(color = MovieTheme.colors.onBackground),
        )
        Text(
            stringResource(R.string.theater_empty_description),
            color = MovieTheme.colors.onBackground,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Composable
private fun TheaterSortReorderList(
    selectedTheaters: List<TheaterModel>,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    val draggableListState = rememberDraggableListState(lazyListState, onMove = onMove)
    LazyColumn(
        modifier = modifier.draggableList(draggableListState),
        state = lazyListState,
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
                    .zIndex(itemState.zIndex),
            ) {
                TheaterChip(theater)
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    MovieIcons.DragHandle,
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .draggableItem(index, draggableListState),
                    colorFilter = ColorFilter.tint(color = MovieTheme.colors.onBackground),
                )
            }
        }
    }
}
