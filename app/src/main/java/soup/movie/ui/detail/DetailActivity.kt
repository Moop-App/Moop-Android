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
import androidx.core.app.ShareCompat
import androidx.core.view.postDelayed
import androidx.palette.graphics.Palette
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
import soup.movie.util.IntentUtil.createShareIntent
import soup.movie.util.delegates.contentView
import soup.movie.util.getColorCompat
import soup.movie.util.loadAsync
import soup.movie.util.log.printRenderLog
import soup.movie.util.startActivitySafely
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
        DetailListAdapter(object : DetailListItemListener {

            override fun onInfoClick(item: Movie) {
                executeWebPage(Cgv.detailMobileWebUrl(item))
            }

            override fun onTicketClick(item: Movie) {
                executeTicketLink(item)
            }

            override fun onMoreTrailersClick(item: Movie) {
                executeYouTube(item)
            }
        })
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
        binding.item = movie
    }

    private fun doStartPostponedEnterTransition() {
        startPostponedEnterTransition()
        window.decorView.postDelayed(300) {
            presenter.requestData(movie)
        }
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        posterView.loadAsync(movie.posterUrl, shotLoadListener)
        posterView.setOnClickListener {
            presenter.requestShareImage(movie.posterUrl)
        }
        timetableButton.setOnClickListener {
            startActivity(Intent(this, TimetableActivity::class.java)
                    .apply { movie.saveTo(this) })
        }
        shareButton.setOnClickListener { share(movie) }

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
        if (presenter.usePaletteTheme()) {
            val isDark = isDark(bitmap, palette)

            // color the status bar.
            val statusBarColor = ColorUtils.getMostPopulousSwatch(palette)?.let {
                ColorUtils.scrimify(it.rgb, isDark, SCRIM_ADJUSTMENT)
            } ?: run {
                window.statusBarColor
            }
            applyTheme(ThemeData(statusBarColor, isDark))
        } else {
            applyTheme(ThemeData(Color.WHITE, isDark = false))
        }
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
            val darkColor = this@DetailActivity.getColorCompat(R.color.dark_icon)
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

    private fun executeTicketLink(item: Movie) {
        if (useWebLinkSetting.get()) {
            executeWebPage(Cgv.reservationUrl(item))
        } else {
            executeMarketApp(Cgv.PACKAGE_NAME)
        }
    }

    private fun share(movie: Movie) {
        if (isInstalledApp(Kakao.PACKAGE_NAME)) {
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
