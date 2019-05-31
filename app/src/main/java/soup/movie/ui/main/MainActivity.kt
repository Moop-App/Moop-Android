package soup.movie.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.spec.KakaoLink
import soup.movie.ui.BaseActivity
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.detail.DetailActivity
import soup.movie.util.observeEvent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: Improve this please
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        intent?.handleDeepLink()

        viewModel.uiEvent.observeEvent(this) {
            execute(it)
        }
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
                val intent = Intent(this, DetailActivity::class.java)
                startActivityForResult(intent, 0, ActivityOptions
                    .makeSceneTransitionAnimation(this)
                    .toBundle())
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
