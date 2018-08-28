package soup.movie.ui.detail

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.util.TypedValue
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activity_detail.*
import soup.movie.R
import soup.movie.data.getColorAsAge
import soup.movie.data.getSimpleAgeLabel
import soup.movie.data.model.Movie
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailViewState.DoneState
import soup.movie.ui.detail.DetailViewState.LoadingState
import soup.movie.util.ImageUtil
import soup.movie.util.IntentUtil.createShareIntentWithText
import soup.movie.util.MovieUtil
import soup.movie.util.RecyclerViewUtil.verticalLinearLayoutManager
import soup.movie.util.getBitmap
import soup.widget.elastic.ElasticDragDismissFrameLayout
import soup.widget.util.AnimUtils.getFastOutSlowInInterpolator
import soup.widget.util.ColorUtils
import soup.widget.util.ViewUtils
import timber.log.Timber
import javax.inject.Inject

class DetailActivity : BaseActivity<DetailContract.View, DetailContract.Presenter>(), DetailContract.View {

    @Inject
    override lateinit var presenter: DetailContract.Presenter

    private lateinit var listAdapter: DetailListAdapter

    private lateinit var chromeFader: ElasticDragDismissFrameLayout.SystemChromeFader

    private lateinit var movie: Movie

    override val layoutRes: Int
        get() = R.layout.activity_detail

    private val shotLoadListener = object : RequestListener<Drawable> {
        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>,
                                     dataSource: DataSource, isFirstResource: Boolean): Boolean {
            val bitmap = resource.getBitmap() ?: return false
            val twentyFourDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24f, this@DetailActivity.resources.displayMetrics).toInt()
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters() /* by default palette ignore certain hues
                        (e.g. pure black/white) but we don't want this. */
                    .setRegion(0, 0, bitmap.width - 1, twentyFourDip) /* - 1 to work around
                        https://code.google.com/p/android/issues/detail?id=191013 */
                    .generate { palette ->
                        val isDark: Boolean
                        @ColorUtils.Lightness val lightness = ColorUtils.isDark(palette)
                        if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                            isDark = ColorUtils.isDark(bitmap, bitmap.width / 2, 0)
                        } else {
                            isDark = lightness == ColorUtils.IS_DARK
                        }

                        val adaptiveColor = ContextCompat.getColor(this@DetailActivity,
                                if (isDark) R.color.white else R.color.dark_icon)
                        backButton.setColorFilter(adaptiveColor)
                        titleView.setTextColor(adaptiveColor)
                        eggView.setTextColor(adaptiveColor)
                        favoriteButton.setColorFilter(adaptiveColor)
                        shareButton.setColorFilter(adaptiveColor)

                        // color the status bar. Set a complementary dark color on L,
                        // light or dark color on M (with matching status bar icons)
                        var statusBarColor = window.statusBarColor
                        val topColor = ColorUtils.getMostPopulousSwatch(palette)
                        if (topColor != null && (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                            statusBarColor = ColorUtils.scrimify(topColor.rgb,
                                    isDark, SCRIM_ADJUSTMENT)
                            // set a light status bar on M+
                            if (!isDark) {
                                ViewUtils.setLightStatusBar(posterView)
                            }
                        }

                        if (statusBarColor != window.statusBarColor) {
                            background.setBackgroundColor(statusBarColor)
                            val statusBarColorAnim = ValueAnimator.ofArgb(
                                    window.statusBarColor, statusBarColor)
                            statusBarColorAnim.addUpdateListener { animation ->
                                window.statusBarColor = animation.animatedValue as Int
                            }
                            statusBarColorAnim.duration = 500L
                            statusBarColorAnim.interpolator = getFastOutSlowInInterpolator(this@DetailActivity)
                            statusBarColorAnim.start()
                        }
                    }
            return false
        }

        override fun onLoadFailed(e: GlideException?, model: Any,
                                  target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            movie = MovieUtil.restoreFrom(intent)!!
        } else {
            movie = MovieUtil.restoreFrom(savedInstanceState)!!
        }
        Timber.d("onCreate: movie=%s", movie)
        super.onCreate(savedInstanceState)
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        ImageUtil.loadAsync(this, posterView, shotLoadListener, movie.poster)
        titleView.text = movie.title
        ageView.text = movie.getSimpleAgeLabel()
        ageBgView.backgroundTintList = ContextCompat.getColorStateList(this, movie.getColorAsAge())
        eggView.text = movie.egg
        favoriteButton.setOnClickListener { v -> }
        shareButton.setOnClickListener {
            startActivity(createShareIntentWithText(
                    "공유하기", MovieUtil.createShareDescription(movie)))
        }

        backButton.setOnClickListener { v -> setResultAndFinish() }
        chromeFader = object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() = setResultAndFinish()
        }
        listAdapter = DetailListAdapter(this)
        movie_contents.let {
            it.layoutManager = verticalLinearLayoutManager(ctx)
            it.adapter = listAdapter
            it.itemAnimator = SlideInRightAnimator()
            it.itemAnimator.addDuration = 200
            it.itemAnimator.removeDuration = 200
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.requestData(movie)
    }

    override fun onResume() {
        super.onResume()
        draggableFrame.addListener(chromeFader)
    }

    override fun onPause() {
        draggableFrame.removeListener(chromeFader)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        MovieUtil.saveTo(outState, movie)
    }

    override fun render(viewState: DetailViewState) {
        Timber.d("render: %s", viewState)
        when (viewState) {
            is LoadingState -> renderLoadingState()
            is DoneState -> renderDoneState(viewState)
        }
    }

    private fun renderLoadingState() {
        //TODO: show loading state
    }

    private fun renderDoneState(state: DoneState) {
        listAdapter.updateList(state.timeTable, state.trailers)
    }

    override fun onBackPressed() {
        setResultAndFinish()
    }

    override fun onNavigateUp(): Boolean {
        setResultAndFinish()
        return true
    }

    private fun setResultAndFinish() {
        finishAfterTransition()
    }

    companion object {

        private const val SCRIM_ADJUSTMENT = 0.075f
    }
}
