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

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.R
import soup.movie.config.Config
import soup.movie.config.RemoteConfig
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.work.LegacyWorker
import soup.movie.work.OpenDateAlarmWorker
import soup.movie.work.OpenDateSyncWorker

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        // https://issuetracker.google.com/issues/147937971
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Moop)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // TODO: Improve this please
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        handleDeepLink(intent)

        val config: Config = RemoteConfig()
        config.fetchAndActivate {
            if (config.allowToRunLegacyWorker) {
                LegacyWorker.enqueueWork(this)
            } else {
                LegacyWorker.cancelWork(this)
            }
        }
        OpenDateAlarmWorker.enqueuePeriodicWork(this)
        OpenDateSyncWorker.enqueuePeriodicWork(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
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
