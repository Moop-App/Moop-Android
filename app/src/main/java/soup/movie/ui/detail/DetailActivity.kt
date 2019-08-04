package soup.movie.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.model.Movie
import soup.movie.databinding.DetailActivityBinding
import soup.movie.spec.KakaoLink
import soup.movie.spec.share
import soup.movie.ui.base.BaseActivity
import soup.movie.ui.home.MovieSelectManager
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

    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var analytics: EventAnalytics

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
        val binding = DataBindingUtil.setContentView<DetailActivityBinding>(this, R.layout.detail_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        if (isPortrait) {
            binding.root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            binding.root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        binding.root.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            binding.header.root.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop
            )
            binding.listView.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop,
                bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            )
        }

        postponeEnterTransition()
        initViewState(binding)

        viewModel.shareAction.observeEvent(this) {
            executeShareAction(it)
        }
    }

    private fun initViewState(binding: DetailActivityBinding) {
        binding.header.apply {
            posterView.loadAsync(movie.posterUrl, endAction = {
                startPostponedEnterTransition()
            })
            posterCard.setOnDebounceClickListener(delay = 150L) {
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
        val listAdapter = DetailListAdapter { item ->
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
        viewModel.contentUiModel.observe(this) {
            listAdapter.submitList(it.items)
        }
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

    //TODO: Re-implements this
    private fun showPosterViewer(from: ImageView) {
        StfalconImageViewer
            .Builder(from.context, listOf(movie.posterUrl)) { view, imageUrl ->
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
