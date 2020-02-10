package soup.movie.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ShareCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.spanSizeLookup
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.android.support.DaggerAppCompatActivity
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.detail.databinding.DetailActivityBinding
import soup.movie.ext.*
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.util.*
import soup.movie.widget.elastic.ElasticDragDismissFrameLayout
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class DetailActivity : DaggerAppCompatActivity(),
    DetailViewRenderer,
    DetailViewAnimation {

    private val args: DetailActivityArgs by navArgs()

    private val binding: DetailActivityBinding by lazy {
        DataBindingUtil.setContentView<DetailActivityBinding>(this, R.layout.detail_activity)
    }

    @Inject
    lateinit var analytics: EventAnalytics

    @Inject
    lateinit var detailFactory: DetailViewModel.Factory
    private val viewModel: DetailViewModel by assistedActivityViewModels {
        detailFactory.create()
    }

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
        binding.header.root.doOnApplyWindowInsets { header, insets, initialState ->
            header.updatePadding(top = initialState.paddings.top + insets.systemWindowInsetTop)
        }
        binding.listView.doOnApplyWindowInsets { listView, insets, initialState ->
            listView.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop,
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
        binding.share.root.doOnApplyWindowInsets { share, insets, initialState ->
            share.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop,
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }

        postponeEnterTransition()
        initViewState(binding)

        viewModel.init(args.movie)
        viewModel.shareAction.observeEvent(this) {
            executeShareAction(it)
        }
    }

    private fun initViewState(binding: DetailActivityBinding) {
        binding.header.apply {
            posterView.loadAsync(args.movie.posterUrl, doOnEnd = {
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
            .Builder(from.context, listOf(args.movie.posterUrl)) { view, imageUrl ->
                view.loadAsync(imageUrl)
            }
            .withTransitionFrom(from)
            .withHiddenStatusBar(false)
            .show()
    }

    private fun executeShareAction(action: ShareAction) {
        val movie = args.movie
        when (action.target) {
            ShareTarget.KakaoLink -> {
                KakaoLink.share(this, movie)
            }
            ShareTarget.Instagram -> {
                ShareCompat.IntentBuilder.from(this)
                    .setChooserTitle(R.string.action_share_poster)
                    .setStream(action.imageUri)
                    .setType(action.mimeType)
                    .apply {
                        intent.setPackage("com.instagram.android")
                    }
                    .startChooser()
            }
            ShareTarget.Facebook,
            ShareTarget.Twitter,
            ShareTarget.LINE,
            ShareTarget.Others -> {
                FirebaseLink.createDetailLink(movie) { link ->
                    ShareCompat.IntentBuilder.from(this)
                        .setChooserTitle(R.string.action_share)
                        .setText("[뭅] ${movie.title}\n$link")
                        .setType("text/plain")
                        .apply {
                            when (action.target) {
                                ShareTarget.Facebook -> "com.facebook.katana"
                                ShareTarget.Twitter -> "com.twitter.android"
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
    }
}
