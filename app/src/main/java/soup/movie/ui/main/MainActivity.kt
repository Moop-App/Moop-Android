package soup.movie.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.spec.KakaoLink
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.home.HomeFragment
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
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, HomeFragment.newInstance())
                .commit()
        }

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
}
