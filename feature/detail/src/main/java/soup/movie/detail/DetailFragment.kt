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
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.spanSizeLookup
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.detail.databinding.DetailFragmentBinding
import soup.movie.ext.executeWeb
import soup.movie.ext.loadAsync
import soup.movie.ext.observeEvent
import soup.movie.ext.showToast
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.util.YouTube
import soup.movie.util.autoCleared
import soup.movie.util.clipToOval
import soup.movie.util.setOnDebounceClickListener
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment :
    Fragment(R.layout.detail_fragment),
    DetailViewRenderer,
    DetailViewAnimation,
    OnBackPressedListener {

    private val args: DetailFragmentArgs by navArgs()

    private var binding: DetailFragmentBinding by autoCleared()

    @Inject
    lateinit var analytics: EventAnalytics

    private val viewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DetailFragmentBinding.bind(view).apply {
            initViewState(viewModel)
            adaptSystemWindowInset()
        }

        viewModel.init(args.movie)
        viewModel.uiEvent.observeEvent(viewLifecycleOwner) {
            when (it) {
                is ShareAction -> view.context.executeShareAction(it)
                is ToastAction -> view.context.showToast(it.resId)
            }
        }
    }

    private fun DetailFragmentBinding.adaptSystemWindowInset() {
        Insetter.builder()
            .setOnApplyInsetsListener { header, insets, initialState ->
                header.updatePadding(top = initialState.paddings.top + insets.getInsets(systemBars()).top)
            }
            .applyToView(toolbarLayout)
        Insetter.builder()
            .setOnApplyInsetsListener { listView, insets, initialState ->
                val systemWindowInsets = insets.getInsets(systemBars())
                listView.updatePadding(
                    bottom = initialState.paddings.bottom + systemWindowInsets.bottom
                )
            }
            .applyToView(listView)
        Insetter.builder()
            .setOnApplyInsetsListener { share, insets, initialState ->
                val systemWindowInsets = insets.getInsets(systemBars())
                share.updatePadding(
                    top = initialState.paddings.top + systemWindowInsets.top,
                    bottom = initialState.paddings.bottom + systemWindowInsets.bottom
                )
            }
            .applyToView(share.root)
    }

    private fun DetailFragmentBinding.initViewState(viewModel: DetailViewModel) {
        val ctx: Context = this.root.context
        header.apply {
            posterView.loadAsync(args.movie.posterUrl)
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
                toggleShareButton()
            }
        }
        errorRetryButton.setOnDebounceClickListener {
            Timber.d("retry")
            viewModel.onRetryClick()
        }
        share.apply {
            fun onShareClick(target: ShareTarget) {
                viewModel.requestShareText(target)
            }
            root.setOnDebounceClickListener {
                toggleShareButton()
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
                viewModel.requestShareImage(ShareTarget.Instagram, imageUrl = args.movie.posterUrl)
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
                is AdItemUiModel -> {
                }
                is CastItemUiModel -> {
                }
                is PlotItemUiModel -> {
                }
            }
        }
        listView.apply {
            layoutManager = GridLayoutManager(ctx, 3).apply {
                spanSizeLookup = spanSizeLookup(listAdapter::getSpanSize)
            }
            adapter = listAdapter
            itemAnimator = FadeInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
                supportsChangeAnimations = false
            }
        }
        viewModel.favoriteUiModel.observe(viewLifecycleOwner) { isFavorite ->
            header.favoriteButton.isSelected = isFavorite
        }
        viewModel.headerUiModel.observe(viewLifecycleOwner) {
            header.render(it)
        }
        viewModel.contentUiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.items)
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            errorGroup.isVisible = it
        }
    }

    override fun onBackPressed(): Boolean {
        if (binding.share.root.isActivated) {
            binding.toggleShareButton()
            return true
        } else {
            return false
        }
    }

    private fun DetailFragmentBinding.toggleShareButton() {
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

    private fun Context.executeShareAction(action: ShareAction) {
        when (action) {
            is ShareAction.Text -> {
                val movie = args.movie
                when (action.target) {
                    ShareTarget.KakaoLink -> {
                        KakaoLink.share(this, movie)
                    }
                    ShareTarget.Instagram -> {
                        throw IllegalStateException("Instagram does not support to share text.")
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
            is ShareAction.Image -> {
                if (action.target == ShareTarget.Instagram) {
                    ShareCompat.IntentBuilder(this)
                        .setChooserTitle(R.string.action_share_poster)
                        .setStream(action.imageUri)
                        .setType(action.mimeType)
                        .apply {
                            intent.setPackage("com.instagram.android")
                        }
                        .startChooser()
                } else {
                    throw IllegalStateException("This ShareTarget(${action.target}) does not support to share image.")
                }
            }
        }
    }
}
