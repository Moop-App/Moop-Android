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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.showToast
import soup.movie.feature.home.impl.favorite.HomeFavoriteScreen
import soup.movie.model.MovieModel
import soup.movie.resources.R

@Composable
fun HomeNavGraph(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieItemClick: (MovieModel) -> Unit,
) {
    val currentMainTab by viewModel.selectedMainTab.collectAsState()
    HomeScaffold(
        currentTab = currentMainTab,
        tabs = MainTabUiModel.entries.toTypedArray(),
        onTabSelected = { mainTab ->
            viewModel.onMainTabSelected(mainTab)
        },
    ) {
        Box {
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

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun HomeScaffold(
    currentTab: MainTabUiModel,
    tabs: Array<MainTabUiModel>,
    onTabSelected: (MainTabUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onTabReselected: (MainTabUiModel) -> Unit = onTabSelected,
    content: @Composable () -> Unit,
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            tabs.forEach { tab ->
                val selected = currentTab == tab
                item(
                    icon = {
                        Icon(
                            rememberAnimatedVectorPainter(
                                animatedImageVector = when (tab) {
                                    MainTabUiModel.Home ->
                                        AnimatedImageVector.animatedVectorResource(MovieIcons.AvdHomeNowSelected)

                                    MainTabUiModel.Favorite ->
                                        AnimatedImageVector.animatedVectorResource(MovieIcons.AvdFavoriteSelected)
                                },
                                atEnd = selected,
                            ),
                            contentDescription = null,
                        )
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
                )
            }
        },
        modifier = modifier,
        content = content,
    )
}
