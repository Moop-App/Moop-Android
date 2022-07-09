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
package soup.movie.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController
import soup.movie.ext.showToast
import soup.movie.ext.startActivitySafely
import soup.movie.model.Theater
import soup.movie.theater.edit.TheaterEditScreen
import soup.movie.theater.edit.TheaterEditViewModel
import soup.movie.theater.sort.TheaterSortScreen
import soup.movie.theater.sort.TheaterSortViewModel
import soup.movie.theme.ThemeOptionScreen
import soup.movie.theme.ThemeOptionViewModel
import soup.movie.util.Cgv
import soup.movie.util.LotteCinema
import soup.movie.util.Megabox
import soup.movie.util.Moop

private enum class Screen(val route: String) {
    Settings("SettingsScreen"),
    ThemeOption("ThemeEditScreen"),
    TheaterSort("TheaterSortScreen"),
    TheaterEdit("TheaterEditScreen"),
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun SettingsNavGraph(
    onNavigationOnClick: () -> Unit,
) {
    val navController = rememberMaterialMotionNavController()
    MaterialMotionNavHost(
        navController,
        startDestination = Screen.Settings.route,
        enterTransition = { materialSharedAxisZIn(forward = true) },
        exitTransition = { materialSharedAxisZOut(forward = true) },
        popEnterTransition = { materialSharedAxisZIn(forward = false) },
        popExitTransition = { materialSharedAxisZOut(forward = false) },
    ) {
        composable(Screen.Settings.route) {
            val context = LocalContext.current
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onNavigationOnClick = onNavigationOnClick,
                onThemeEditClick = {
                    navController.navigate(Screen.ThemeOption.route)
                },
                onTheaterItemClick = { theater ->
                    context.executeWeb(theater)
                },
                onTheaterEditClick = {
                    navController.navigate(Screen.TheaterSort.route)
                },
                onVersionClick = {
                    if (it.isLatest) {
                        context.showToast(R.string.settings_version_toast)
                    } else {
                        Moop.executePlayStore(context)
                    }
                },
                onMarketIconClick = {
                    Moop.executePlayStore(context)
                },
                onBugReportClick = {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:")
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(HELP_EMAIL))
                    intent.putExtra(
                        Intent.EXTRA_SUBJECT,
                        "뭅 v${BuildConfig.VERSION_NAME} 버그리포트"
                    )
                    context.startActivitySafely(intent)
                }
            )
        }
        composable(Screen.ThemeOption.route) {
            val viewModel = hiltViewModel<ThemeOptionViewModel>()
            ThemeOptionScreen(viewModel = viewModel)
        }
        composable(Screen.TheaterSort.route) {
            val viewModel = hiltViewModel<TheaterSortViewModel>()
            TheaterSortScreen(
                viewModel = viewModel,
                upPress = {
                    navController.navigateUp()
                },
                onAddItemClick = {
                    navController.navigate(Screen.TheaterEdit.route)
                }
            )
        }
        composable(Screen.TheaterEdit.route) {
            val viewModel = hiltViewModel<TheaterEditViewModel>()
            TheaterEditScreen(
                viewModel = viewModel,
                upPress = { navController.navigateUp() }
            )
        }
    }
}

private fun Context.executeWeb(theater: Theater) {
    return when (theater.type) {
        Theater.TYPE_CGV -> Cgv.executeWeb(this, theater)
        Theater.TYPE_LOTTE -> LotteCinema.executeWeb(this, theater)
        Theater.TYPE_MEGABOX -> Megabox.executeWeb(this, theater)
        else -> throw IllegalArgumentException("${theater.type} is not valid type.")
    }
}

private const val HELP_EMAIL = "help.moop@gmail.com"
