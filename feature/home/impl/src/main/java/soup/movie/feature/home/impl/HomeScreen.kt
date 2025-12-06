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
package soup.movie.feature.home.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.feature.home.impl.filter.HomeFilterScreen
import soup.movie.feature.home.impl.now.HomeNowList
import soup.movie.feature.home.impl.plan.HomePlanList
import soup.movie.model.MovieModel
import soup.movie.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onMovieItemClick: (MovieModel) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val selectedTab by viewModel.selectedHomeTab.collectAsState()

    val homeTabs = remember { HomeTabUiModel.entries.toTypedArray() }
    val gridStates = homeTabs.map { rememberLazyGridState() }
    val isTopAtCurrentTab by remember {
        derivedStateOf {
            gridStates[selectedTab.ordinal].let {
                it.firstVisibleItemIndex == 0 && it.firstVisibleItemScrollOffset == 0
            }
        }
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState
    val bottomSheetVisible by remember {
        derivedStateOf {
            bottomSheetState.currentValue == SheetValue.Expanded
        }
    }
    BackHandler(enabled = bottomSheetVisible || isTopAtCurrentTab.not() || selectedTab != HomeTabUiModel.Now) {
        if (bottomSheetVisible) {
            coroutineScope.launch {
                bottomSheetState.partialExpand()
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
        containerColor = MovieTheme.colorScheme.surfaceContainerLowest,
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            HomeFilterScreen(viewModel = hiltViewModel())
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MovieTheme.colorScheme.surfaceContainerLowest,
                ),
                actions = {
                    IconButton(onClick = { onSearchClick() }) {
                        Icon(
                            MovieIcons.Search,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        homeTabs.forEachIndexed { index, homeTab ->
                            if (index > 0) {
                                VerticalDivider(
                                    thickness = 2.dp,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .requiredHeight(16.dp),
                                    color = MovieTheme.colorScheme.onSurface,
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
                                    MovieTheme.colorScheme.onSurface
                                } else {
                                    MovieTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                },
                                modifier = Modifier
                                    .clickable {
                                        if (selected) {
                                            coroutineScope.launch {
                                                gridStates[homeTab.ordinal].animateScrollToItem(
                                                    0,
                                                )
                                            }
                                        } else {
                                            viewModel.onHomeTabSelected(homeTab)
                                        }
                                    }
                                    .fillMaxHeight()
                                    .wrapContentHeight(),
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (selectedTab) {
                HomeTabUiModel.Now -> HomeNowList(
                    viewModel = hiltViewModel(),
                    state = gridStates[selectedTab.ordinal],
                    onItemClick = {
                        onMovieItemClick(it)
                    },
                    onItemLongClick = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.snackbarHostState.showSnackbar(message = it.title)
                        }
                    },
                )
                HomeTabUiModel.Plan -> HomePlanList(
                    viewModel = hiltViewModel(),
                    state = gridStates[selectedTab.ordinal],
                    onItemClick = {
                        onMovieItemClick(it)
                    },
                    onItemLongClick = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.snackbarHostState.showSnackbar(message = it.title)
                        }
                    },
                )
            }
            HomeFilterButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
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
            MovieIcons.FilterList,
            contentDescription = stringResource(R.string.menu_filter),
        )
    }
}
