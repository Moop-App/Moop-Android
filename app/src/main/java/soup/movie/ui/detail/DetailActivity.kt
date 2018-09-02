package soup.movie.ui.detail

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.core.view.postDelayed
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import kotlinx.android.synthetic.main.activity_detail.*
import soup.movie.R
import soup.movie.data.getColorAsAge
import soup.movie.data.getSimpleAgeLabel
import soup.movie.data.model.Movie
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailViewState.DoneState
import soup.movie.ui.detail.DetailViewState.LoadingState
import soup.movie.ui.detail.timetable.TimetableActivity
import soup.movie.util.*
import soup.movie.util.IntentUtil.createShareIntentWithText
import soup.widget.elastic.ElasticDragDismissFrameLayout
import soup.widget.util.AnimUtils.getFastOutSlowInInterpolator
import soup.widget.util.ColorUtils
import soup.widget.util.ViewUtils
import soup.widget.util.getBitmap
import timber.log.Timber
import javax.inject.Inject

class DetailActivity :
        BaseActivity<DetailContract.View, DetailContract.Presenter>(),
        DetailContract.View {

    @Inject
    override lateinit var presenter: DetailContract.Presenter

    private val listAdapter by lazy {
        DetailListAdapter(this@DetailActivity)
    }

    private val chromeFader by lazy {
        object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() = setResultAndFinish()
        }
    }

    private lateinit var movie: Movie

    override val layoutRes: Int
        get() = R.layout.activity_detail

    private val shotLoadListener = object : RequestListener<Drawable> {
        override fun onResourceReady(resource: Drawable,
                                     model: Any,
                                     target: Target<Drawable>,
                                     dataSource: DataSource,
                                     isFirstResource: Boolean): Boolean {
            val bitmap = resource.getBitmap() ?: return false
            val twentyFourDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24f, this@DetailActivity.resources.displayMetrics).toInt()
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.width - 1, twentyFourDip)
                    .generate { palette ->
                        @ColorUtils.Lightness
                        val lightness = ColorUtils.isDark(palette)
                        val isDark: Boolean = if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                            ColorUtils.isDark(bitmap, bitmap.width / 2, 0)
                        } else {
                            lightness == ColorUtils.IS_DARK
                        }

                        val adaptiveColor = this@DetailActivity.getColorCompat(
                                if (isDark) R.color.light_icon else R.color.dark_icon)
                        titleView.setTextColor(adaptiveColor)
                        eggView.setTextColor(adaptiveColor)
                        favoriteButton.setColorFilter(adaptiveColor)
                        timetableButton.setColorFilter(adaptiveColor)
                        shareButton.setColorFilter(adaptiveColor)

                        // color the status bar. Set a complementary dark color on L,
                        // light or dark color on M (with matching status bar icons)
                        var statusBarColor = window.statusBarColor
                        ColorUtils.getMostPopulousSwatch(palette)?.apply {
                            if (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                statusBarColor = ColorUtils.scrimify(rgb, isDark, SCRIM_ADJUSTMENT)
                                // set a light status bar on M+
                                if (!isDark) {
                                    ViewUtils.setLightStatusBar(posterView)
                                }
                            }
                        }

                        if (statusBarColor != window.statusBarColor) {
                            backgroundView.setBackgroundColor(statusBarColor)
                            ValueAnimator.ofArgb(window.statusBarColor, statusBarColor)?.apply {
                                addUpdateListener {
                                    window.statusBarColor = animatedValue as Int
                                }
                                duration = 500L
                                interpolator = getFastOutSlowInInterpolator(this@DetailActivity)
                                start()
                            }
                        }
                    }
            doStartPostponedEnterTransition()
            return false
        }

        override fun onLoadFailed(e: GlideException?, model: Any,
                                  target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            doStartPostponedEnterTransition()
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        movie = if (savedInstanceState == null) {
            intent.restoreFrom()!!
        } else {
            savedInstanceState.restoreFrom()!!
        }
        Timber.d("onCreate: movie=%s", movie)
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    private fun doStartPostponedEnterTransition() {
        startPostponedEnterTransition()
        window.decorView.postDelayed(300) {
            presenter.requestData(movie)
        }
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        posterView.loadAsync(movie.poster, shotLoadListener)
        titleView.text = movie.title
        ageView.text = movie.getSimpleAgeLabel()
        ageBgView.backgroundTintList = ctx.getColorStateListCompat(movie.getColorAsAge())
        eggView.text = movie.egg

        timetableButton.setOnClickListener { _ ->
            startActivity(Intent(this, TimetableActivity::class.java)
                    .also { movie.saveTo(it) })
        }
        shareButton.setOnClickListener {
            startActivity(createShareIntentWithText("공유하기", movie.toShareDescription()))
        }

        listView.apply {
            adapter = listAdapter
            itemAnimator = FadeInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
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
        movie.saveTo(outState)
    }

    override fun render(viewState: DetailViewState) {
        Timber.d("render: %s", viewState)
        when (viewState) {
            is LoadingState -> {
                //TODO: show loading state
            }
            is DoneState -> {
                listAdapter.submitList(viewState.trailers)
            }
        }
    }

    override fun onBackPressed() {
        setResultAndFinish()
    }

    private fun setResultAndFinish() {
        finishAfterTransition()
    }

    companion object {

        private const val SCRIM_ADJUSTMENT = 0.075f
    }
}
