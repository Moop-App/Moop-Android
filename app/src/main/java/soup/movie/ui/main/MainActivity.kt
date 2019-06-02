package soup.movie.ui.main

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.main_activity.*
import soup.movie.MainDirections
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.spec.KakaoLink
import soup.movie.ui.base.BaseActivity
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.home.MovieSelectManager
import soup.movie.util.consume
import soup.movie.util.observeEvent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        //TODO: implements this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //TODO: Improve this please
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        intent?.handleDeepLink()

        viewModel.uiEvent.observeEvent(this) {
            execute(it)
        }

        navigationView.setNavigationItemSelectedListener {
            consume {
                if (!it.isChecked) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    when (it.itemId) {
                        R.id.action_to_home ->
                            navHostFragment.findNavController()
                                .popBackStack(R.id.home, false)
                        R.id.action_to_search ->
                            navHostFragment.findNavController()
                                .navigate(MainDirections.actionToSearch())
                        R.id.action_to_settings ->
                            navHostFragment.findNavController()
                                .navigate(MainDirections.actionToSettings())
                    }
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
            is ShowDetailUiEvent -> {
                MovieSelectManager.select(action.movie)
                navHostFragment.findNavController().navigate(
                    MainDirections.actionToDetail()
                )
            }
        }
    }

    override fun onBackPressed() {
        if (handleBackEventInChildFragment()) return
        super.onBackPressed()
    }

    private fun handleBackEventInChildFragment(): Boolean {
        val current = navHostFragment.childFragmentManager.fragments.elementAtOrNull(0)
        if (current is OnBackPressedListener) {
            return current.onBackPressed()
        }
        return false
    }
}
