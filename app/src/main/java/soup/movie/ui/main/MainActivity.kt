package soup.movie.ui.main

import android.content.Intent
import android.content.Intent.ACTION_VIEW
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
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.MainDirections
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.MainActivityBinding
import soup.movie.spec.KakaoLink
import soup.movie.ui.base.BaseActivity
import soup.movie.ui.base.consumeBackEventInChildFragment
import soup.movie.ui.home.MovieSelectManager
import soup.movie.util.consume
import soup.movie.util.isPortrait
import soup.movie.util.observeEvent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: MainActivityBinding

    private val viewModel: MainViewModel by viewModels()

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

        viewModel.uiEvent.observeEvent(this) {
            execute(it)
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
        when (action) {
            ACTION_VIEW -> {
                KakaoLink.extractMovieId(this)?.let {
                    viewModel.requestMovie(it)
                }
            }
        }
    }

    private fun execute(action: MainUiEvent) {
        when (action) {
            is OpenDrawerMenuUiEvent -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            is ShowDetailUiEvent -> {
                MovieSelectManager.select(action.movie)
                navHostFragment.findNavController().navigate(
                    MainDirections.actionToDetail()
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
