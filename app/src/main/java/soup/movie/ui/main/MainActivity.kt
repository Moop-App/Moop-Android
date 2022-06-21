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
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import soup.movie.R
import soup.movie.config.Config
import soup.movie.config.RemoteConfig
import soup.movie.core.MainDirections
import soup.movie.databinding.MainActivityBinding
import soup.movie.ext.consume
import soup.movie.ext.observeEvent
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.system.SystemEvent
import soup.movie.system.SystemViewModel
import soup.movie.ui.base.consumeBackEventInChildFragment
import soup.movie.util.viewBindings
import soup.movie.work.LegacyWorker
import soup.movie.work.OpenDateAlarmWorker
import soup.movie.work.OpenDateSyncWorker

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBindings(MainActivityBinding::inflate)

    private val systemViewModel: SystemViewModel by viewModels()

    private val viewModel: MainViewModel by viewModels()

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        binding.navigationView.setCheckedItem(destination.id)
    }

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
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        Insetter.builder()
            .setOnApplyInsetsListener { container, insets, initialState ->
                val systemInsets = insets.getInsets(systemBars())
                container.updatePadding(
                    left = initialState.paddings.left + systemInsets.left,
                    right = initialState.paddings.right + systemInsets.right
                )
            }
            .applyToView(binding.root)
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updatePadding(
                    top = initialState.paddings.top + insets.getInsets(systemBars()).top
                )
            }
            .applyToView(binding.navigationView)

        // TODO: Improve this please
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        intent?.handleDeepLink()

        systemViewModel.systemEvent.observeEvent(this) {
            handleEvent(it)
        }
        viewModel.uiEvent.observeEvent(this) {
            handleEvent(it)
        }

        binding.navigationView.setNavigationItemSelectedListener {
            consume {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                val navController = navHostFragment.findNavController()
                when (it.itemId) {
                    R.id.home -> navController.popBackStack(R.id.home, false)
                    else -> NavigationUI.onNavDestinationSelected(it, navController)
                }
            }
        }

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

    override fun onResume() {
        super.onResume()
        navHostFragment.findNavController().addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navHostFragment.findNavController().removeOnDestinationChangedListener(listener)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.handleDeepLink()
    }

    private fun Intent.handleDeepLink() {
        FirebaseLink.extractMovieId(this) { movieId ->
            if (movieId != null) {
                viewModel.requestMovie(movieId)
            } else {
                KakaoLink.extractMovieId(this)?.let {
                    viewModel.requestMovie(it)
                }
            }
        }
    }

    private fun handleEvent(event: SystemEvent) {
        when (event) {
            is SystemEvent.OpenDrawerMenuUiEvent -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun handleEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.ShowDetailUiEvent -> {
                navHostFragment.findNavController().navigate(
                    MainDirections.actionToDetail(event.movie)
                )
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        if (navHostFragment.consumeBackEventInChildFragment()) return
        super.onBackPressed()
    }

    private val navHostFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
}
