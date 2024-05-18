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

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.R
import soup.movie.config.Config
import soup.movie.config.RemoteConfig
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.designsystem.windowsizeclass.calculateWindowSizeClass
import soup.movie.feature.deeplink.FirebaseLink
import soup.movie.feature.deeplink.KakaoLink
import soup.movie.feature.tasks.RecommendMoviesTasks
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var recommendMoviesTasks: RecommendMoviesTasks

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Moop)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MovieTheme {
                MainNavGraph(
                    mainViewModel = viewModel,
                    widthSizeClass = calculateWindowSizeClass(this).widthSizeClass,
                )
            }
        }

        // TODO: Improve this please
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        handleDeepLink(intent)

        val config: Config = RemoteConfig()
        config.fetchAndActivate {
            if (config.allowToRunLegacyWorker) {
                recommendMoviesTasks.fetch()
            } else {
                recommendMoviesTasks.cancel()
            }
        }
        viewModel.onInit()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        FirebaseLink.extractMovieId(intent) { movieId ->
            if (movieId != null) {
                viewModel.requestMovie(movieId)
            } else {
                KakaoLink.extractMovieId(intent)?.let {
                    viewModel.requestMovie(it)
                }
            }
        }
    }
}
