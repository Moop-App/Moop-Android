package soup.movie.ui.detail

import android.animation.ValueAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Pair
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.app.ShareCompat
import androidx.core.view.postOnAnimationDelayed
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_header.*
import soup.movie.R
import soup.movie.data.helper.*
import soup.movie.data.model.Movie
import soup.movie.databinding.ActivityDetailBinding
import soup.movie.settings.impl.UseWebLinkSetting
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailViewState.DoneState
import soup.movie.ui.detail.DetailViewState.LoadingState
import soup.movie.ui.detail.timetable.TimetableActivity
import soup.movie.ui.helper.EventAnalytics
import soup.movie.util.*
import soup.movie.util.IntentUtil.createShareIntent
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import soup.widget.elastic.ElasticDragDismissFrameLayout
import soup.widget.util.AnimUtils.getFastOutSlowInInterpolator
import soup.widget.util.ColorUtils
import soup.widget.util.ViewUtils
import soup.widget.util.getBitmap
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max

class DetailActivity :
        BaseActivity<DetailContract.View, DetailContract.Presenter>(),
        DetailContract.View {

    override val binding by contentView<DetailActivity, ActivityDetailBinding>(
            R.layout.activity_detail
    )

    private var windowBackground: Int = Color.WHITE

    private lateinit var movie: Movie

    @Inject
    override lateinit var presenter: DetailContract.Presenter

    @Inject
    lateinit var useWebLinkSetting: UseWebLinkSetting

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazy {
        DetailListAdapter(object : DetailListItemListener {

            override fun onInfoClick(item: Movie) {
                analytics.clickCgvInfo(item)
                Cgv.executeMobileWeb(this@DetailActivity, item)
            }

            override fun onTicketClick(item: Movie) {
                analytics.clickCgvTicket(item)
                executeTicketLink(item)
            }

            private fun executeTicketLink(item: Movie) {
                if (useWebLinkSetting.get()) {
                    Cgv.executeWebForSchedule(this@DetailActivity, item)
                } else {
                    Cgv.executeAppForSchedule(this@DetailActivity)
                }
            }

            override fun onMoreTrailersClick(item: Movie) {
                analytics.clickMoreTrailers("${item.title} 예고편")
                YouTube.executeAppWithQuery(this@DetailActivity, item)
            }
        }, analytics)
    }

    private val chromeFader by lazy {
        object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() = setResultAndFinish()
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        private var wasScrolled: Boolean = false

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val offset: Int = recyclerView.computeVerticalScrollOffset()
            detailHeaderView.translationZ = max(3f, offset / 800f)

            val isScrolled: Boolean = offset != 0
            if (wasScrolled != isScrolled) {
                wasScrolled = isScrolled
                if (isScrolled) {
                    detailHeaderView.setBackgroundColor(windowBackground)
                } else {
                    detailHeaderView.setBackgroundColorResource(android.R.color.transparent)
                }
            }
        }
    }

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
                    resources.displayMetrics
            ).toInt()

            if (presenter.usePaletteTheme()) {
                Palette.from(bitmap)
                        .maximumColorCount(3)
                        .clearFilters()
                        .setRegion(0, 0, bitmap.width - 1, twentyFourDip) /* - 1 to work around
                        https://code.google.com/p/android/issues/detail?id=191013 */
                        .generate { palette -> applyTopPalette(bitmap, palette) }
            } else {
                applyTheme(ThemeData(windowBackground, isDark = false))
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
        binding.item = movie
    }

    private fun doStartPostponedEnterTransition() {
        startPostponedEnterTransition()
        window.decorView.postOnAnimationDelayed(300) {
            presenter.requestData(movie)
        }
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)

        windowBackground = ctx.getColorCompat(R.color.windowBackground)

        posterView.loadAsync(movie.posterUrl, shotLoadListener)
        posterView.setOnClickListener {
            analytics.clickPoster(movie)
            presenter.requestShareImage(movie.posterUrl)
        }
        timetableButton.setOnClickListener {
            analytics.clickTimetable(movie)
            val intent = Intent(this, TimetableActivity::class.java)
            movie.saveTo(intent)
            startActivity(intent, ActivityOptions
                    .makeSceneTransitionAnimation(this, *createSharedElements())
                    .toBundle())
        }
        shareButton.setOnClickListener {
            analytics.clickShare(movie)
            share(movie)
        }

        listView.apply {
            adapter = listAdapter
            itemAnimator = FadeInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
    }

    private fun createSharedElements(): Array<Pair<View, String>> = arrayOf(
            ageBgView with R.string.transition_age_bg,
            ageView with R.string.transition_age)

    override fun onResume() {
        super.onResume()
        draggableFrame.addListener(chromeFader)
        listView.addOnScrollListener(scrollListener)
    }

    override fun onPause() {
        draggableFrame.removeListener(chromeFader)
        listView.removeOnScrollListener(scrollListener)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        movie.saveTo(outState)
    }

    override fun render(viewState: DetailViewState) {
        printRenderLog { viewState }
        when (viewState) {
            is LoadingState -> {
                //TODO: show loading state
            }
            is DoneState -> {
                listAdapter.submitList(viewState.items)
            }
        }
    }

    override fun onBackPressed() {
        setResultAndFinish()
    }

    private fun applyTopPalette(bitmap: Bitmap, palette: Palette?) {
        val isDark = isDark(bitmap, palette)

        // color the status bar.
        windowBackground = ColorUtils.getMostPopulousSwatch(palette)?.let {
            ColorUtils.scrimify(it.rgb, isDark, SCRIM_ADJUSTMENT)
        } ?: run {
            window.statusBarColor
        }
        applyTheme(ThemeData(windowBackground, isDark))
    }

    private fun isDark(bitmap: Bitmap, palette: Palette?): Boolean {
        val lightness = ColorUtils.isDark(palette)
        return if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
            ColorUtils.isDark(bitmap, bitmap.width / 2, 0)
        } else {
            lightness == ColorUtils.IS_DARK
        }
    }

    private fun applyTheme(theme: ThemeData) {
        if (theme.isDark.not()) { // make back icon dark on light images
            val darkColor = getColorCompat(R.color.dark_icon)
            titleView.setTextColor(darkColor)
            openDateView.setTextColor(darkColor)
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
        detailHeaderView.setBackgroundColorResource(android.R.color.transparent)
        finishAfterTransition()
    }

    override fun doShareImage(imageUri: Uri, mimeType: String) {
        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(R.string.action_share_poster)
                .setSubject(movie.title)
                .setStream(imageUri)
                .setType(mimeType)
                .startChooser()
    }

    private fun share(movie: Movie) {
        if (Kakao.isInstalled(this)) {
            val params = FeedTemplate.newBuilder(
                    ContentObject.newBuilder(movie.title, movie.posterUrl,
                            LinkObject.newBuilder()
                                    .setWebUrl(Cgv.detailWebUrl(movie))
                                    .setMobileWebUrl(Cgv.detailMobileWebUrl(movie))
                                    .setAndroidExecutionParams("id=${movie.id}")
                                    .build())
                            .setDescrption(movie.toDescription())
                            .build())
                    .build()
            KakaoLinkService.getInstance().sendDefault(this, params,
                    object : ResponseCallback<KakaoLinkResponse>() {
                        override fun onFailure(errorResult: ErrorResult) {
                            Timber.e(errorResult.toString())
                        }

                        override fun onSuccess(result: KakaoLinkResponse) {}
                    })
        } else {
            startActivitySafely(createShareIntent("공유하기", movie.toShareDescription()))
        }
    }

    companion object {

        private const val SCRIM_ADJUSTMENT = 0.075f
    }

    data class ThemeData(
            @ColorInt
            val bgColor: Int,
            val isDark: Boolean)
}
