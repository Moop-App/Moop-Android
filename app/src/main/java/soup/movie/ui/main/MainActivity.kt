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
package soup.movie.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import soup.compose.material.motion.animation.materialSharedAxisZ
import soup.movie.R
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.feature.home.HomeScreenKey
import soup.movie.feature.navigator.EntryProviderInstaller
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: NavigatorImpl

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Moop)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MovieTheme {
                NavigationSuiteScaffold(
                    navigationSuiteItems = {
                        TOP_LEVEL_ROUTES.forEach { (key, value) ->
                            val isSelected = key == navigator.state.topLevelRoute
                            item(
                                icon = {
                                    Icon(
                                        painter = rememberAnimatedVectorPainter(
                                            animatedImageVector = AnimatedImageVector.animatedVectorResource(value.icon),
                                            atEnd = isSelected,
                                        ),
                                        contentDescription = null,
                                    )
                                },
                                label = {
                                    Text(text = stringResource(value.description))
                                },
                                selected = isSelected,
                                onClick = { navigator.navigate(key) },
                            )
                        }
                    },
                ) {
                    val dialogSceneStrategy = remember { DialogSceneStrategy<NavKey>() }
                    val directive = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
                        .copy(horizontalPartitionSpacerSize = 0.dp)
                    NavDisplay(
                        entries = navigator.state.toEntries(
                            entryProvider = entryProvider {
                                entryProviderScopes.forEach { builder -> this.builder() }
                            },
                        ),
                        onBack = { navigator.goBack() },
                        sceneStrategy = rememberListDetailSceneStrategy<NavKey>(
                            backNavigationBehavior = BackNavigationBehavior.PopLatest,
                            directive = directive,
                        ) then dialogSceneStrategy,
                        transitionSpec = { materialSharedAxisZ(forward = true) },
                        popTransitionSpec = { materialSharedAxisZ(forward = false) },
                        predictivePopTransitionSpec = { materialSharedAxisZ(forward = false) },
                    )
                }
            }
        }

        viewModel.onInit()
    }
}

private val TOP_LEVEL_ROUTES = mapOf<NavKey, NavBarItem>(
    HomeScreenKey.Home to NavBarItem(icon = MovieIcons.AvdHomeNowSelected, description = soup.movie.resources.R.string.menu_home),
    HomeScreenKey.Favorite to NavBarItem(icon = MovieIcons.AvdFavoriteSelected, description = soup.movie.resources.R.string.menu_favorite),
)

data class NavBarItem(
    val icon: Int,
    val description: Int,
)
