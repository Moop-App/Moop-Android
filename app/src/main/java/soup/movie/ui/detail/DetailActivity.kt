package soup.movie.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ShareCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.spanSizeLookup
import com.stfalcon.imageviewer.StfalconImageViewer
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.model.Movie
import soup.movie.databinding.DetailActivityBinding
import soup.movie.spec.KakaoLink
import soup.movie.ui.base.BaseActivity
import soup.movie.ui.home.MovieSelectManager
import soup.movie.util.*
import soup.movie.util.helper.Cgv
import soup.movie.util.helper.LotteCinema
import soup.movie.util.helper.Megabox
import soup.movie.util.helper.YouTube
import soup.widget.elastic.ElasticDragDismissFrameLayout
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class DetailActivity : BaseActivity(), DetailViewRenderer, DetailViewAnimation {

    private val movie: Movie by lazyFast {
        MovieSelectManager.getSelectedItem()!!
    }

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: DetailActivityBinding

    private val viewModel: DetailViewModel by viewModels()

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            binding.header.root.run {
                val maxOffset = max(
                    height,
                    recyclerView.resources.getDimensionPixelSize(R.dimen.detail_header_height)
                )
                val headerIsShown = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() == 0
                val offset = if (headerIsShown) {
                    min(maxOffset, recyclerView.computeVerticalScrollOffset()).toFloat()
                } else {
                    maxOffset.toFloat()
                }
                translationY = -offset
            }
        }
    }

    private val chromeFader = object : ElasticDragDismissFrameLayout.ElasticDragDismissCallback() {

        override fun onDragDismissed() {
            finishAfterTransition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.detail_activity)
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
        binding.root.doOnApplyWindowInsets { _, windowInsets, initialPadding ->
            val topPadding = initialPadding.top + windowInsets.systemWindowInsetTop
            val bottomPadding = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            binding.header.root.updatePadding(top = topPadding)
            binding.listView.updatePadding(top = topPadding, bottom = bottomPadding)
            binding.share.root.updatePadding(top = topPadding, bottom = bottomPadding)
        }

        postponeEnterTransition()
        initViewState(binding)

        viewModel.shareAction.observeEvent(this) {
            executeShareAction(it)
        }
    }

    private fun initViewState(binding: DetailActivityBinding) {
        binding.header.apply {
            posterView.loadAsync(movie.posterUrl, withKey = true, doOnEnd = {
                startPostponedEnterTransition()
            })
            posterCard.setOnDebounceClickListener(delay = 150L) {
                analytics.clickPoster()
                showPosterViewer(from = posterView)
            }
            favoriteButton.setOnDebounceClickListener {
                val isFavorite = favoriteButton.isSelected.not()
                viewModel.onFavoriteButtonClick(isFavorite)
            }
            shareButton.setOnDebounceClickListener {
                analytics.clickShare()
                binding.toggleShareButton()
            }
        }
        binding.errorRetryButton.setOnDebounceClickListener {
            Timber.d("retry")
            viewModel.onRetryClick()
        }
        binding.share.apply {
            fun onShareClick(target: ShareTarget) {
                viewModel.requestShareImage(target, movieCard.drawToBitmap())
            }
            root.setOnDebounceClickListener {
                binding.toggleShareButton()
            }
            facebookShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.Facebook)
            }
            twitterShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.Twitter)
            }
            instagramShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.Instagram)
            }
            lineShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.LINE)
            }
            kakaoTalkShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.KakaoLink)
            }
            etcShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.Others)
            }
        }
        val listAdapter = DetailListAdapter { item ->
            val ctx: Context = this@DetailActivity
            when (item) {
                is BoxOfficeItemUiModel -> {
                    ctx.executeWeb(item.webLink)
                }
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
                is ImdbItemUiModel -> {
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
                supportsChangeAnimations = false
            }
        }
        viewModel.favoriteUiModel.observe(this) { isFavorite ->
            binding.header.favoriteButton.isSelected = isFavorite
        }
        viewModel.headerUiModel.observe(this) {
            binding.render(it)
        }
        viewModel.contentUiModel.observe(this) {
            listAdapter.submitList(it.items)
            listAdapter.updateHeader(height = binding.header.root.measuredHeight)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.draggableFrame.addListener(chromeFader)
        binding.listView.addOnScrollListener(scrollListener)
    }

    override fun onPause() {
        binding.draggableFrame.removeListener(chromeFader)
        binding.listView.removeOnScrollListener(scrollListener)
        super.onPause()
    }

    override fun onBackPressed() {
        if (binding.share.root.isActivated) {
            binding.toggleShareButton()
        } else {
            finishAfterTransition()
        }
    }

    private fun DetailActivityBinding.toggleShareButton() {
        share.root.let {
            if (it.isActivated) {
                it.isActivated = false
                it.hideShareViewTo(header.shareButton)
            } else {
                it.isActivated = true
                it.showShareViewFrom(header.shareButton)
            }
        }
    }

    //TODO: Re-implements this
    private fun showPosterViewer(from: ImageView) {
        StfalconImageViewer
            .Builder(from.context, listOf(movie.posterUrl)) { view, imageUrl ->
                view.loadAsync(imageUrl, withKey = true)
            }
            .withTransitionFrom(from)
            .withHiddenStatusBar(false)
            .show()
    }

    private fun executeShareAction(action: ShareAction) {
        if (action.target == ShareTarget.KakaoLink) {
            KakaoLink.share(this, movie)
        } else {
            ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(R.string.action_share_poster)
                .setStream(action.imageUri)
                .setType(action.mimeType)
                .apply {
                    when (action.target) {
                        ShareTarget.Facebook -> "com.facebook.katana"
                        ShareTarget.Twitter -> "com.twitter.android"
                        ShareTarget.Instagram -> "com.instagram.android"
                        ShareTarget.LINE -> "jp.naver.line.android"
                        else -> null
                    }?.let {
                        intent.setPackage(it)
                    }
                }
                .startChooser()
        }
    }
}
