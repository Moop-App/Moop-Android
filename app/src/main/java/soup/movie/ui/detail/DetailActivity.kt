package soup.movie.ui.detail

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
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
import soup.movie.databinding.ActivityDetailBinding
import soup.movie.settings.impl.UseWebLinkSetting
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailViewState.DoneState
import soup.movie.ui.detail.DetailViewState.LoadingState
import soup.movie.ui.detail.timetable.TimetableActivity
import soup.movie.util.*
import soup.movie.util.IntentUtil.createShareIntentWithText
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
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

    override val binding by contentView<DetailActivity, ActivityDetailBinding>(
            R.layout.activity_detail
    )

    @Inject
    override lateinit var presenter: DetailContract.Presenter

    @Inject
    lateinit var useWebLinkSetting: UseWebLinkSetting

    private val listAdapter by lazy {
        DetailListAdapter(this@DetailActivity)
    }

    private val chromeFader by lazy {
        object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() = setResultAndFinish()
        }
    }

    private lateinit var movie: Movie

    private val shotLoadListener = object : RequestListener<Drawable> {
        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            val bitmap = resource.getBitmap() ?: return false

            val twentyFourDip = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    24f,
                    this@DetailActivity.resources.displayMetrics
            ).toInt()
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.width - 1, twentyFourDip) /* - 1 to work around
                        https://code.google.com/p/android/issues/detail?id=191013 */
                    .generate { palette -> applyTopPalette(bitmap, palette) }
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
        posterView.setOnClickListener {
            presenter.requestShareImage(movie.poster)
        }
        titleView.text = movie.title
        ageView.text = movie.getSimpleAgeLabel()
        ageBgView.backgroundTintList = ctx.getColorStateListCompat(movie.getColorAsAge())
        eggView.text = movie.egg

        ticketButton.setOnClickListener {
            if (useWebLinkSetting.get()) {
                //executeCgvWebPage(movie)
                startCgvWebPageUsingCustomTab()
            } else {
                executeCgvApp()
            }
        }
        timetableButton.setOnClickListener {
            startActivity(Intent(this, TimetableActivity::class.java)
                    .apply { movie.saveTo(this) })
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
        viewState.printRenderLog()
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

    private fun applyTopPalette(bitmap: Bitmap, palette: Palette?) {
        if (presenter.usePaletteTheme()) {
            val lightness = ColorUtils.isDark(palette)
            val isDark = if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                ColorUtils.isDark(bitmap, bitmap.width / 2, 0)
            } else {
                lightness == ColorUtils.IS_DARK
            }

            // color the status bar.
            var statusBarColor = window.statusBarColor
            ColorUtils.getMostPopulousSwatch(palette)?.let {
                statusBarColor = ColorUtils.scrimify(it.rgb, isDark, SCRIM_ADJUSTMENT)
            }
            applyTheme(ThemeData(statusBarColor, isDark))
        } else {
            applyTheme(ThemeData(Color.WHITE, isDark = false))
        }
    }

    private fun applyTheme(theme: ThemeData) {
        if (theme.isDark.not()) { // make back icon dark on light images
            val darkColor = this@DetailActivity.getColorCompat(R.color.dark_icon)
            titleView.setTextColor(darkColor)
            eggView.setTextColor(darkColor)
            favoriteButton.setColorFilter(darkColor)
            ticketButton.setColorFilter(darkColor)
            timetableButton.setColorFilter(darkColor)
            shareButton.setColorFilter(darkColor)

            // set a light status bar
            ViewUtils.setLightStatusBar(window.decorView)
        }

        if (theme.bgColor != window.statusBarColor) {
            backgroundView.setBackgroundColor(theme.bgColor)
            ValueAnimator.ofArgb(window.statusBarColor, theme.bgColor).apply {
                addUpdateListener { animation ->
                    window.statusBarColor = animation.animatedValue as Int
                }
                duration = 500L
                interpolator = getFastOutSlowInInterpolator(this@DetailActivity)
            }.start()
        }
    }

    private fun setResultAndFinish() {
        finishAfterTransition()
    }

    override fun doShareImage(imageUri: Uri, mimeType: String) {
        ShareCompat.IntentBuilder.from(this@DetailActivity)
                .setChooserTitle(R.string.action_share_poster)
                .setSubject(movie.title)
                .setStream(imageUri)
                .setType(mimeType)
                .startChooser()
    }

    private fun startCgvWebPageUsingCustomTab() {
        CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setSecondaryToolbarColor(Color.BLACK)
                .setShowTitle(true)
                .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
                .setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
                .build()
                .launchUrl(this, Uri.parse("http://m.cgv.co.kr/quickReservation/Default.aspx?MovieIdx=${movie.id}"))
    }

    companion object {

        private const val SCRIM_ADJUSTMENT = 0.075f
    }

    data class ThemeData(
            @ColorInt
            val bgColor: Int,
            val isDark: Boolean)
}
