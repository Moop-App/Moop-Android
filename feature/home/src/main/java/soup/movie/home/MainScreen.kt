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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import soup.movie.ext.showToast
import soup.movie.home.favorite.HomeFavoriteList
import soup.movie.model.Movie
import soup.movie.settings.SettingsNavGraph
import soup.movie.ui.windowsizeclass.WindowWidthSizeClass

@Composable
fun MainScreen(
    widthSizeClass: WindowWidthSizeClass,
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onTheaterMapClick: () -> Unit,
    onMovieItemClick: (Movie) -> Unit,
) {
    val currentMainTab by viewModel.selectedMainTab.observeAsState(MainTabUiModel.Home)
    val tabs = MainTabUiModel.values()
    MainScaffold(
        widthSizeClass = widthSizeClass,
        currentTab = currentMainTab,
        tabs = tabs,
        onTabSelected = { mainTab ->
            if (mainTab == MainTabUiModel.TheaterMap) {
                onTheaterMapClick()
            } else {
                viewModel.onMainTabSelected(mainTab)
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentMainTab) {
                MainTabUiModel.Home -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onSearchClick = onSearchClick,
                        onMovieItemClick = onMovieItemClick,
                    )
                }
                MainTabUiModel.Favorite -> {
                    val context = LocalContext.current
                    HomeFavoriteList(
                        viewModel = hiltViewModel(),
                        onItemClick = {
                            viewModel.onMovieClick()
                            onMovieItemClick(it)
                        },
                        onItemLongClick = {
                            context.showToast(it.title)
                        },
                    )
                }
                MainTabUiModel.TheaterMap -> {}
                MainTabUiModel.Settings -> {
                    SettingsNavGraph()
                }
            }
        }
    }
}

@Composable
private fun MainScaffold(
    widthSizeClass: WindowWidthSizeClass,
    currentTab: MainTabUiModel,
    tabs: Array<MainTabUiModel>,
    onTabSelected: (MainTabUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onTabReselected: (MainTabUiModel) -> Unit = onTabSelected,
    content: @Composable (PaddingValues) -> Unit,
) {
    when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactScreen(
                currentTab = currentTab,
                tabs = tabs,
                onTabSelected = onTabSelected,
                onTabReselected = onTabReselected,
                modifier = modifier,
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
                modifier = modifier,
                content = content,
            )
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun CompactScreen(
    currentTab: MainTabUiModel,
    tabs: Array<MainTabUiModel>,
    onTabSelected: (MainTabUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onTabReselected: (MainTabUiModel) -> Unit = onTabSelected,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.padding(
            WindowInsets.systemBars
                .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                .asPaddingValues()
        ),
        bottomBar = {
            BottomNavigation {
                tabs.forEach { tab ->
                    val selected = currentTab == tab
                    BottomNavigationItem(
                        icon = {
                            val painter = when (tab) {
                                MainTabUiModel.Home -> rememberAnimatedVectorPainter(
                                    AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_now_selected),
                                    selected
                                )
                                MainTabUiModel.Favorite -> rememberAnimatedVectorPainter(
                                    AnimatedImageVector.animatedVectorResource(R.drawable.avd_favorite_selected),
                                    selected
                                )
                                MainTabUiModel.TheaterMap -> rememberVectorPainter(Icons.Rounded.Map)
                                MainTabUiModel.Settings -> rememberVectorPainter(Icons.Rounded.Settings)
                            }
                            Icon(
                                painter,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                text = when (tab) {
                                    MainTabUiModel.Home -> stringResource(R.string.menu_home)
                                    MainTabUiModel.Favorite -> stringResource(R.string.menu_favorite)
                                    MainTabUiModel.TheaterMap -> stringResource(R.string.menu_map)
                                    MainTabUiModel.Settings -> stringResource(R.string.menu_settings)
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
        content = content,
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun MediumScreen(
    currentTab: MainTabUiModel,
    tabs: Array<MainTabUiModel>,
    onTabSelected: (MainTabUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onTabReselected: (MainTabUiModel) -> Unit = onTabSelected,
    content: @Composable (PaddingValues) -> Unit
) {
    Row(modifier = modifier.fillMaxSize()) {
        NavigationRail(elevation = 24.dp) {
            tabs.forEach { tab ->
                val selected = currentTab == tab
                NavigationRailItem(
                    icon = {
                        val painter = when (tab) {
                            MainTabUiModel.Home -> rememberAnimatedVectorPainter(
                                AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_now_selected),
                                selected
                            )
                            MainTabUiModel.Favorite -> rememberAnimatedVectorPainter(
                                AnimatedImageVector.animatedVectorResource(R.drawable.avd_favorite_selected),
                                selected
                            )
                            MainTabUiModel.TheaterMap -> rememberVectorPainter(Icons.Rounded.Map)
                            MainTabUiModel.Settings -> rememberVectorPainter(Icons.Rounded.Settings)
                        }
                        Icon(
                            painter,
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = when (tab) {
                                MainTabUiModel.Home -> stringResource(R.string.menu_home)
                                MainTabUiModel.Favorite -> stringResource(R.string.menu_favorite)
                                MainTabUiModel.TheaterMap -> stringResource(R.string.menu_map)
                                MainTabUiModel.Settings -> stringResource(R.string.menu_settings)
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
        Divider(modifier = Modifier.width(1.dp).fillMaxHeight())
        content(WindowInsets.statusBars.asPaddingValues())
    }
}