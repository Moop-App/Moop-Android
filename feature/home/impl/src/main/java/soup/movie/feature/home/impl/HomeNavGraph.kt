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

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.showToast
import soup.movie.feature.home.impl.favorite.HomeFavoriteScreen
import soup.movie.model.MovieModel
import soup.movie.resources.R

@Composable
fun HomeNavGraph(
    widthSizeClass: WindowWidthSizeClass,
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieItemClick: (MovieModel) -> Unit,
) {
    val currentMainTab by viewModel.selectedMainTab.collectAsState()
    val tabs = MainTabUiModel.values()
    MainScaffold(
        widthSizeClass = widthSizeClass,
        currentTab = currentMainTab,
        tabs = tabs,
        onTabSelected = { mainTab ->
            viewModel.onMainTabSelected(mainTab)
        },
        modifier = Modifier.systemBarsPadding(),
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
                    HomeFavoriteScreen(
                        viewModel = hiltViewModel(),
                        onSettingsClick = onSettingsClick,
                        onItemClick = onMovieItemClick,
                        onItemLongClick = {
                            context.showToast(it.title)
                        },
                    )
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
        WindowWidthSizeClass.Expanded,
        -> {
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
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    val selected = currentTab == tab
                    NavigationBarItem(
                        icon = {
                            when (tab) {
                                MainTabUiModel.Home -> {
                                    Icon(
                                        rememberAnimatedVectorPainter(
                                            AnimatedImageVector.animatedVectorResource(MovieIcons.AvdHomeNowSelected),
                                            selected,
                                        ),
                                        contentDescription = null,
                                    )
                                }
                                MainTabUiModel.Favorite -> {
                                    Icon(
                                        rememberAnimatedVectorPainter(
                                            AnimatedImageVector.animatedVectorResource(MovieIcons.AvdFavoriteSelected),
                                            selected,
                                        ),
                                        contentDescription = null,
                                    )
                                }
                            }
                        },
                        label = {
                            Text(
                                text = when (tab) {
                                    MainTabUiModel.Home -> stringResource(R.string.menu_home)
                                    MainTabUiModel.Favorite -> stringResource(R.string.menu_favorite)
                                },
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
                        colors = NavigationBarItemDefaults.colors(),
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
    content: @Composable (PaddingValues) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        NavigationRail {
            tabs.forEach { tab ->
                val selected = currentTab == tab
                NavigationRailItem(
                    icon = {
                        when (tab) {
                            MainTabUiModel.Home -> {
                                Icon(
                                    rememberAnimatedVectorPainter(
                                        AnimatedImageVector.animatedVectorResource(MovieIcons.AvdHomeNowSelected),
                                        selected,
                                    ),
                                    contentDescription = null,
                                )
                            }
                            MainTabUiModel.Favorite -> {
                                Icon(
                                    rememberAnimatedVectorPainter(
                                        AnimatedImageVector.animatedVectorResource(MovieIcons.AvdFavoriteSelected),
                                        selected,
                                    ),
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                    label = {
                        Text(
                            text = when (tab) {
                                MainTabUiModel.Home -> stringResource(R.string.menu_home)
                                MainTabUiModel.Favorite -> stringResource(R.string.menu_favorite)
                            },
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
                    colors = NavigationRailItemDefaults.colors(),
                )
            }
        }
        VerticalDivider(thickness = 1.dp)
        content(PaddingValues())
    }
}
