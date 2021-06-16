/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.detail

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.spanSizeLookup
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.detail.databinding.DetailActivityBinding
import soup.movie.ext.executeWeb
import soup.movie.ext.loadAsync
import soup.movie.ext.observeEvent
import soup.movie.ext.showToast
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.util.YouTube
import soup.movie.util.clipToOval
import soup.movie.util.setOnDebounceClickListener
import soup.movie.util.viewBindings
import soup.movie.widget.elastic.ElasticDragDismissFrameLayout
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), DetailViewRenderer, DetailViewAnimation {

    private val args: DetailActivityArgs by navArgs()

    private val binding by viewBindings(DetailActivityBinding::inflate)

    @Inject
    lateinit var analytics: EventAnalytics

    private val viewModel: DetailViewModel by viewModels()

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            binding.header.root.run {
                val maxOffset = max(
                    height,
                    recyclerView.resources.getDimensionPixelSize(R.dimen.detail_header_height)
                )
                val headerIsShown =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() == 0
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
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        Insetter.builder()
            .setOnApplyInsetsListener { container, insets, initialState ->
                val systemInsets = insets.getInsets(systemBars())
                container.updatePadding(
                    left = initialState.paddings.left + systemInsets.left,
                    right = initialState.paddings.right + systemInsets.right,
                )
            }
            .applyToView(binding.container)
        Insetter.builder()
            .setOnApplyInsetsListener { header, insets, initialState ->
                header.updatePadding(top = initialState.paddings.top + insets.getInsets(systemBars()).top)
            }
            .applyToView(binding.header.root)
        Insetter.builder()
            .setOnApplyInsetsListener { listView, insets, initialState ->
                val systemWindowInsets = insets.getInsets(systemBars())
                listView.updatePadding(
                    top = initialState.paddings.top + systemWindowInsets.top,
                    bottom = initialState.paddings.bottom + systemWindowInsets.bottom
                )
            }
            .applyToView(binding.listView)
        Insetter.builder()
            .setOnApplyInsetsListener { share, insets, initialState ->
                val systemWindowInsets = insets.getInsets(systemBars())
                share.updatePadding(
                    top = initialState.paddings.top + systemWindowInsets.top,
                    bottom = initialState.paddings.bottom + systemWindowInsets.bottom
                )
            }
            .applyToView(binding.share.root)

        postponeEnterTransition()
        initViewState(binding)

        viewModel.init(args.movie)
        viewModel.uiEvent.observeEvent(this) {
            when (it) {
                is ShareAction -> executeShareAction(it)
                is ToastAction -> showToast(it.resId)
            }
        }
    }

    private fun initViewState(binding: DetailActivityBinding) {
        binding.header.apply {
            posterView.loadAsync(
                args.movie.posterUrl,
                doOnEnd = {
                    startPostponedEnterTransition()
                }
            )
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
            facebookShareButton.clipToOval(true)
            facebookShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.Facebook)
            }
            twitterShareButton.clipToOval(true)
            twitterShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.Twitter)
            }
            instagramShareButton.clipToOval(true)
            instagramShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.Instagram)
            }
            lineShareButton.clipToOval(true)
            lineShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.LINE)
            }
            kakaoTalkShareButton.clipToOval(true)
            kakaoTalkShareButton.setOnDebounceClickListener {
                onShareClick(ShareTarget.KakaoLink)
            }
            etcShareButton.clipToOval(true)
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
                    ctx.executeWeb(item.webLink)
                }
                is LotteItemUiModel -> {
                    analytics.clickLotteInfo()
                    ctx.executeWeb(item.webLink)
                }
                is MegaboxItemUiModel -> {
                    analytics.clickMegaboxInfo()
                    ctx.executeWeb(item.webLink)
                }
                is NaverItemUiModel -> {
                    ctx.executeWeb(item.webLink)
                }
                is ImdbItemUiModel -> {
                    ctx.executeWeb(item.webLink)
                }
                is TrailerHeaderItemUiModel -> {
                    val message = SpannableString(ctx.getText(R.string.trailer_dialog_message))
                    Linkify.addLinks(message, Linkify.WEB_URLS)
                    AlertDialog.Builder(ctx, R.style.AlertDialogTheme)
                        .setTitle(R.string.trailer_dialog_title)
                        .setMessage(message)
                        .setPositiveButton(R.string.trailer_dialog_button, null)
                        .show()
                        .apply {
                            findViewById<TextView>(android.R.id.message)?.movementMethod =
                                LinkMovementMethod.getInstance()
                        }
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
        viewModel.isError.observe(this) {
            binding.errorGroup.isVisible = it
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

    // TODO: Re-implements this
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
                ShareCompat.IntentBuilder(this)
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
                    ShareCompat.IntentBuilder(this)
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
