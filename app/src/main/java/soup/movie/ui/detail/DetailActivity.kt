package soup.movie.ui.detail

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.app.ShareCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.spanSizeLookup
import com.stfalcon.imageviewer.StfalconImageViewer
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import kotlinx.android.synthetic.main.detail_activity.*
import kotlinx.android.synthetic.main.detail_header.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.ui.home.MovieSelectManager
import soup.movie.data.model.Movie
import soup.movie.databinding.DetailActivityBinding
import soup.movie.spec.KakaoLink
import soup.movie.spec.share
import soup.movie.ui.base.BaseActivity
import soup.movie.util.*
import soup.movie.util.helper.Cgv
import soup.movie.util.helper.LotteCinema
import soup.movie.util.helper.Megabox
import soup.movie.util.helper.YouTube
import soup.widget.elastic.ElasticDragDismissFrameLayout
import javax.inject.Inject
import kotlin.math.min

class DetailActivity : BaseActivity() {

    private val movie: Movie by lazyFast {
        MovieSelectManager.getSelectedItem()!!
    }

    private val viewModel: DetailViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazyFast {
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
            val maxOffset = min(
                header.height,
                recyclerView.resources.getDimensionPixelSize(R.dimen.detail_header_height)
            )
            val headerIsShown = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() == 0
            val offset = if (headerIsShown) {
                min(maxOffset, recyclerView.computeVerticalScrollOffset()).toFloat()
            } else {
                maxOffset.toFloat()
            }
            header.translationZ = if (offset < 10f) 1f else 0f
            header.translationY = -offset
            header.alpha = 1f - offset / maxOffset
        }
    }

    private val chromeFader = object : ElasticDragDismissFrameLayout.ElasticDragDismissCallback() {

        override fun onDragDismissed() {
            finishAfterTransition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        val binding = DataBindingUtil.setContentView<DetailActivityBinding>(this, R.layout.detail_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            binding.header.root.updatePadding(
                top = windowInsets.systemWindowInsetTop
            )
            binding.listView.updatePadding(
                top = windowInsets.systemWindowInsetTop,
                bottom = windowInsets.systemWindowInsetBottom
            )
            windowInsets
        }

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
        binding.header.apply {
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
        val isDark: Boolean = isDark(themeBgColor)
        if (isDark.not()) { // make back icon dark on light images
            val darkColor = getColorAttr(R.attr.moop_iconColorDark)
            titleView.setTextColor(darkColor)
            openDateView.setTextColor(darkColor)
            shareButton.setColorFilter(darkColor)

            // set a light status bar
            window.decorView.setLightStatusBar()
        }
        backgroundView.setBackgroundColor(themeBgColor)
    }

    private fun View.setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * Check if a color is dark (convert to XYZ & check Y component)
     */
    private fun isDark(@ColorInt color: Int): Boolean {
        return androidx.core.graphics.ColorUtils.calculateLuminance(color) < 0.5
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
