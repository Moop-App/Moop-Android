package soup.movie.ui.detail

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.spanSizeLookup
import com.stfalcon.imageviewer.StfalconImageViewer
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import kotlinx.android.synthetic.main.detail_activity.*
import kotlinx.android.synthetic.main.detail_header.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.data.helper.Cgv
import soup.movie.data.helper.LotteCinema
import soup.movie.data.helper.Megabox
import soup.movie.data.helper.YouTube
import soup.movie.data.model.Movie
import soup.movie.databinding.DetailActivityBinding
import soup.movie.spec.KakaoLink
import soup.movie.spec.share
import soup.movie.ui.BaseActivity
import soup.movie.util.*
import soup.widget.elastic.ElasticDragDismissFrameLayout.SystemChromeFader
import soup.widget.util.AnimUtils.getFastOutSlowInInterpolator
import soup.widget.util.ColorUtils
import soup.widget.util.ViewUtils
import javax.inject.Inject
import kotlin.math.min

class DetailActivity : BaseActivity() {

    private val movie: Movie by lazyFast {
        MovieSelectManager.getSelectedItem()!!
    }

    private val viewModel: DetailViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazy {
        DetailListAdapter { item ->
            val ctx: Context = this@DetailActivity
            when (item) {
                is CgvItemUiModel -> {
                    analytics.clickCgvInfo()
                    Cgv.executeMobileWeb(ctx, item.movieId)
                }
                is LotteItemUiModel -> {
                    analytics.clickLotteInfo()
                    LotteCinema.executeMobileWeb(ctx, item.movieId)
                }
                is MegaboxItemUiModel -> {
                    analytics.clickMegaboxInfo()
                    Megabox.executeMobileWeb(ctx, item.movieId)
                }
                is NaverItemUiModel -> {
                    ctx.executeWeb(item.webLink)
                }
                is TrailerItemUiModel -> {
                    analytics.clickTrailer()
                    YouTube.executeApp(ctx, item.trailer)
                }
                is TrailerFooterItemUiModel -> {
                    analytics.clickMoreTrailers()
                    YouTube.executeAppWithQuery(ctx, item.movieTitle)
                }
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            //TODO: Improve this please
            val maxOffset = min(200, detailHeaderView.height)
            val offset = min(maxOffset, recyclerView.computeVerticalScrollOffset()).toFloat()
            detailHeaderView.translationZ = if (offset < 10f) 1f else 0f
            detailHeaderView.translationY = -offset
            detailHeaderView.alpha = 1f - offset / maxOffset
        }
    }

    private val chromeFader: SystemChromeFader by lazyFast {
        object : SystemChromeFader(this) {
            override fun onDragDismissed() {
                finishAfterTransition()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<DetailActivityBinding>(this, R.layout.detail_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        postponeEnterTransition()
        initViewState(binding)

        viewModel.contentUiModel.observe(this) {
            render(it)
        }
        viewModel.shareAction.observeEvent(this) {
            executeShareAction(it)
        }
    }

    private fun initViewState(binding: DetailActivityBinding) {
        binding.detailHeaderView.apply {
            posterView.loadAsync(movie.posterUrl, endAction = {
                startPostponedEnterTransition()
            })
            posterView.setOnDebounceClickListener {
                analytics.clickPoster()
                showPosterViewer(from = posterView)
            }
            kakaoTalkButton.setOnDebounceClickListener {
                analytics.clickShare()
                KakaoLink.share(it.context, movie)
            }
            shareButton.setOnDebounceClickListener {
                analytics.clickShare()
                share(movie)
            }
        }
        binding.listView.apply {
            layoutManager = GridLayoutManager(this@DetailActivity, 3).apply {
                spanSizeLookup = spanSizeLookup(listAdapter::getSpanSize)
            }
            adapter = listAdapter
            itemAnimator = FadeInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
        //TODO: Please improve this more
        applyTheme(themeBgColor = getColorAttr(R.attr.moop_bgColor))
    }

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

    override fun onBackPressed() {
        finishAfterTransition()
    }

    private fun render(uiModel: ContentUiModel) {
        listAdapter.submitList(uiModel.items)
    }

    private fun applyTheme(@ColorInt themeBgColor: Int) {
        val isDark: Boolean = ColorUtils.isDark(themeBgColor)
        if (isDark.not()) { // make back icon dark on light images
            val darkColor = getColorAttr(R.attr.moop_iconColorDark)
            titleView.setTextColor(darkColor)
            openDateView.setTextColor(darkColor)
            shareButton.setColorFilter(darkColor)

            // set a light status bar
            ViewUtils.setLightStatusBar(window.decorView)
        }

        if (themeBgColor != window.statusBarColor) {
            backgroundView.setBackgroundColor(themeBgColor)
            ValueAnimator.ofArgb(window.statusBarColor, themeBgColor).apply {
                addUpdateListener { animation ->
                    window.statusBarColor = animation.animatedValue as Int
                }
                duration = 500L
                interpolator = getFastOutSlowInInterpolator(this@DetailActivity)
            }.start()
        }
    }

    //TODO: Re-implements this
    private fun showPosterViewer(from: ImageView) {
        StfalconImageViewer
            .Builder<String>(from.context, listOf(movie.posterUrl)) { view, imageUrl ->
                view.loadAsync(imageUrl)
            }
            .withTransitionFrom(from)
            .withHiddenStatusBar(false)
            .show()
    }

    private fun executeShareAction(action: ShareAction) {
        ShareCompat.IntentBuilder.from(this)
            .setChooserTitle(R.string.action_share_poster)
            .setSubject(movie.title)
            .setStream(action.imageUri)
            .setType(action.mimeType)
            .startChooser()
    }
}
