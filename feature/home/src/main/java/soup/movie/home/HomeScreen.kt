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
package soup.movie.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import soup.movie.ext.showToast
import soup.movie.home.favorite.HomeFavoriteList
import soup.movie.home.filter.HomeFilterScreen
import soup.movie.home.now.HomeNowList
import soup.movie.home.plan.HomePlanList
import soup.movie.model.Movie
import soup.movie.ui.windowsizeclass.WindowWidthSizeClass

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    widthSizeClass: WindowWidthSizeClass,
    viewModel: HomeViewModel,
    onNavigationClick: () -> Unit,
    onMovieItemClick: (Movie) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val currentTab by viewModel.selectedTab.observeAsState(HomeTabUiModel.Now)
    val tabs = HomeTabUiModel.values()
    val gridStates = tabs.map { rememberLazyGridState() }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState
    val bottomSheetVisible by remember {
        derivedStateOf {
            bottomSheetState.isExpanded
        }
    }
    val isTopAtCurrentTab by remember {
        derivedStateOf {
            gridStates[currentTab.ordinal].let {
                it.firstVisibleItemIndex == 0 && it.firstVisibleItemScrollOffset == 0
            }
        }
    }
    BackHandler(enabled = bottomSheetVisible || isTopAtCurrentTab.not() || currentTab != HomeTabUiModel.Now) {
        if (bottomSheetVisible) {
            coroutineScope.launch {
                bottomSheetState.collapse()
            }
        }
        if (isTopAtCurrentTab.not()) {
            val currentGridState = gridStates[currentTab.ordinal]
            coroutineScope.launch {
                currentGridState.animateScrollToItem(0)
            }
        } else {
            viewModel.onTabSelected(HomeTabUiModel.Now)
        }
    }
    BottomSheetScaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = if (MaterialTheme.colors.isLight) 16.dp else 0.dp,
        sheetContent = {
            HomeFilterScreen(viewModel = hiltViewModel())
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onNavigationClick() }) {
                        Icon(
                            Icons.Outlined.Menu,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = when (currentTab) {
                            HomeTabUiModel.Now -> stringResource(R.string.menu_now)
                            HomeTabUiModel.Plan -> stringResource(R.string.menu_plan)
                            HomeTabUiModel.Favorite -> stringResource(R.string.menu_favorite)
                        }
                    )
                }
            )
        },
    ) {
        HomeScaffold(
            widthSizeClass = widthSizeClass,
            currentTab = currentTab,
            tabs = tabs,
            onTabSelected = { viewModel.onTabSelected(it) },
            onTabReselected = { tab ->
                coroutineScope.launch {
                    gridStates[tab.ordinal].animateScrollToItem(0)
                }
            },
            floatingActionButton = {
                HomeFilterButton(
                    onClick = {
                        viewModel.onFilterButtonClick()
                        coroutineScope.launch {
                            bottomSheetState.expand()
                        }
                    },
                )
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                val context = LocalContext.current
                when (currentTab) {
                    HomeTabUiModel.Now -> HomeNowList(
                        viewModel = hiltViewModel(),
                        state = gridStates[currentTab.ordinal],
                        onItemClick = {
                            viewModel.onMovieClick()
                            onMovieItemClick(it)
                        },
                        onItemLongClick = {
                            context.showToast(it.title)
                        }
                    )
                    HomeTabUiModel.Plan -> HomePlanList(
                        viewModel = hiltViewModel(),
                        state = gridStates[currentTab.ordinal],
                        onItemClick = {
                            viewModel.onMovieClick()
                            onMovieItemClick(it)
                        },
                        onItemLongClick = {
                            context.showToast(it.title)
                        }
                    )
                    HomeTabUiModel.Favorite -> HomeFavoriteList(
                        viewModel = hiltViewModel(),
                        state = gridStates[currentTab.ordinal],
                        onItemClick = {
                            viewModel.onMovieClick()
                            onMovieItemClick(it)
                        },
                        onItemLongClick = {
                            context.showToast(it.title)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScaffold(
    widthSizeClass: WindowWidthSizeClass,
    currentTab: HomeTabUiModel,
    tabs: Array<HomeTabUiModel>,
    onTabSelected: (HomeTabUiModel) -> Unit,
    onTabReselected: (HomeTabUiModel) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactScreen(
                currentTab = currentTab,
                tabs = tabs,
                onTabSelected = onTabSelected,
                onTabReselected = onTabReselected,
                floatingActionButton = floatingActionButton,
                content = content,
            )
        }
        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded -> {
            MediumScreen(
                currentTab = currentTab,
                tabs = tabs,
                onTabSelected = onTabSelected,
                onTabReselected = onTabReselected,
                floatingActionButton = floatingActionButton,
                content = content,
            )
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun CompactScreen(
    currentTab: HomeTabUiModel,
    tabs: Array<HomeTabUiModel>,
    onTabSelected: (HomeTabUiModel) -> Unit,
    onTabReselected: (HomeTabUiModel) -> Unit = onTabSelected,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(
            WindowInsets.navigationBars
                .only(WindowInsetsSides.Bottom)
                .asPaddingValues()
        ),
        bottomBar = {
            BottomNavigation {
                tabs.forEach { tab ->
                    val selected = currentTab == tab
                    BottomNavigationItem(
                        icon = {
                            val imageVector = when (tab) {
                                HomeTabUiModel.Now -> AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_now_selected)
                                HomeTabUiModel.Plan -> AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_plan_selected)
                                HomeTabUiModel.Favorite -> AnimatedImageVector.animatedVectorResource(
                                    R.drawable.avd_favorite_selected
                                )
                            }
                            Icon(
                                rememberAnimatedVectorPainter(imageVector, selected),
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                text = when (tab) {
                                    HomeTabUiModel.Now -> stringResource(R.string.menu_now)
                                    HomeTabUiModel.Plan -> stringResource(R.string.menu_plan)
                                    HomeTabUiModel.Favorite -> stringResource(R.string.menu_favorite)
                                }
                            )
                        },
                        selected = selected,
                        onClick = {
                            if (selected) {
                                onTabReselected(tab)
                            } else {
                                onTabSelected(tab)
                            }
                        },
                        selectedContentColor = MaterialTheme.colors.secondary,
                        unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton,
        content = content,
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun MediumScreen(
    currentTab: HomeTabUiModel,
    tabs: Array<HomeTabUiModel>,
    onTabSelected: (HomeTabUiModel) -> Unit,
    onTabReselected: (HomeTabUiModel) -> Unit = onTabSelected,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(elevation = 24.dp) {
            tabs.forEach { tab ->
                val selected = currentTab == tab
                NavigationRailItem(
                    icon = {
                        val imageVector = when (tab) {
                            HomeTabUiModel.Now -> AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_now_selected)
                            HomeTabUiModel.Plan -> AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_plan_selected)
                            HomeTabUiModel.Favorite -> AnimatedImageVector.animatedVectorResource(R.drawable.avd_favorite_selected)
                        }
                        Icon(
                            rememberAnimatedVectorPainter(imageVector, selected),
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = when (tab) {
                                HomeTabUiModel.Now -> stringResource(R.string.menu_now)
                                HomeTabUiModel.Plan -> stringResource(R.string.menu_plan)
                                HomeTabUiModel.Favorite -> stringResource(R.string.menu_favorite)
                            }
                        )
                    },
                    selected = selected,
                    onClick = {
                        if (selected) {
                            onTabReselected(tab)
                        } else {
                            onTabSelected(tab)
                        }
                    },
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                )
            }
        }
        Scaffold(
            floatingActionButton = floatingActionButton,
            content = content,
        )
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
