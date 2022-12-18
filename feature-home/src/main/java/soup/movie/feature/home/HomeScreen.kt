/*
 * Copyright 2022 SOUP
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
package soup.movie.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.showToast
import soup.movie.feature.home.filter.HomeFilterScreen
import soup.movie.feature.home.now.HomeNowList
import soup.movie.feature.home.plan.HomePlanList
import soup.movie.model.Movie
import soup.movie.resources.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onMovieItemClick: (Movie) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val selectedTab by viewModel.selectedHomeTab.collectAsState()

    val homeTabs = HomeTabUiModel.values()
    val gridStates = homeTabs.map { rememberLazyGridState() }
    val isTopAtCurrentTab by remember {
        derivedStateOf {
            gridStates[selectedTab.ordinal].let {
                it.firstVisibleItemIndex == 0 && it.firstVisibleItemScrollOffset == 0
            }
        }
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState
    val bottomSheetVisible by remember {
        derivedStateOf {
            bottomSheetState.isExpanded
        }
    }
    BackHandler(enabled = bottomSheetVisible || isTopAtCurrentTab.not() || selectedTab != HomeTabUiModel.Now) {
        if (bottomSheetVisible) {
            coroutineScope.launch {
                bottomSheetState.collapse()
            }
        }
        if (isTopAtCurrentTab.not()) {
            val currentGridState = gridStates[selectedTab.ordinal]
            coroutineScope.launch {
                currentGridState.animateScrollToItem(0)
            }
        } else {
            viewModel.onHomeTabSelected(HomeTabUiModel.Now)
        }
    }
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = if (MaterialTheme.colors.isLight) 16.dp else 0.dp,
        sheetContent = {
            HomeFilterScreen(viewModel = hiltViewModel())
        },
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = { onSearchClick() }) {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        homeTabs.forEachIndexed { index, homeTab ->
                            if (index > 0) {
                                Divider(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .requiredSize(2.dp, 16.dp),
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                            val selected = selectedTab == homeTab
                            val text = when (homeTab) {
                                HomeTabUiModel.Now -> stringResource(R.string.menu_now)
                                HomeTabUiModel.Plan -> stringResource(R.string.menu_plan)
                            }
                            Text(
                                text = text,
                                color = if (selected) {
                                    MaterialTheme.colors.onSurface
                                } else {
                                    MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                                },
                                modifier = Modifier
                                    .clickable {
                                        if (selected) {
                                            coroutineScope.launch {
                                                gridStates[homeTab.ordinal].animateScrollToItem(
                                                    0
                                                )
                                            }
                                        } else {
                                            viewModel.onHomeTabSelected(homeTab)
                                        }
                                    }
                                    .fillMaxHeight()
                                    .wrapContentHeight()
                            )
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selectedTab) {
                HomeTabUiModel.Now -> HomeNowList(
                    viewModel = hiltViewModel(),
                    state = gridStates[selectedTab.ordinal],
                    onItemClick = {
                        viewModel.onMovieClick()
                        onMovieItemClick(it)
                    },
                    onItemLongClick = {
                        context.showToast(it.title)
                    },
                )
                HomeTabUiModel.Plan -> HomePlanList(
                    viewModel = hiltViewModel(),
                    state = gridStates[selectedTab.ordinal],
                    onItemClick = {
                        viewModel.onMovieClick()
                        onMovieItemClick(it)
                    },
                    onItemLongClick = {
                        context.showToast(it.title)
                    },
                )
            }
            HomeFilterButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    viewModel.onFilterButtonClick()
                    coroutineScope.launch {
                        bottomSheetState.expand()
                    }
                },
            )
        }
    }
}

@Composable
private fun HomeFilterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            Icons.Rounded.FilterList,
            contentDescription = stringResource(R.string.menu_filter)
        )
    }
}
