package soup.movie.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.support.DaggerAppCompatActivity
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.R
import soup.movie.core.MainDirections
import soup.movie.databinding.MainActivityBinding
import soup.movie.ext.assistedActivityViewModels
import soup.movie.ext.consume
import soup.movie.ext.isPortrait
import soup.movie.ext.observeEvent
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.system.SystemEvent
import soup.movie.system.SystemViewModel
import soup.movie.ui.base.consumeBackEventInChildFragment
import soup.movie.work.LegacyWorker
import soup.movie.work.OpenDateAlarmWorker
import soup.movie.work.OpenDateSyncWorker
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory
    private val viewModel: MainViewModel by assistedActivityViewModels {
        viewModelFactory.create()
    }

    private val listener = NavController.OnDestinationChangedListener {
        _, destination, _ ->
        binding.navigationView.setCheckedItem(destination.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Moop_Main)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this
        if (isPortrait) {
            binding.root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            binding.root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        binding.navigationView.doOnApplyWindowInsets { navigationView, insets, initialState ->
            navigationView.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop
            )
        }

        //TODO: Improve this please
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

        LegacyWorker.enqueueWork(this)
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
        get() = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
}
