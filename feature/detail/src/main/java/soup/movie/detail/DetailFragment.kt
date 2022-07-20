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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import soup.compose.material.motion.circularReveal
import soup.metronome.zoomable.ExperimentalZoomableApi
import soup.metronome.zoomable.ZoomableBox
import soup.metronome.zoomable.rememberZoomableState
import soup.movie.analytics.EventAnalytics
import soup.movie.ext.observeEvent
import soup.movie.ext.showToast
import soup.movie.model.Movie
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.ui.MovieTheme
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var analytics: EventAnalytics

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieTheme {
                    DetailNavGraph(
                        movie = args.movie,
                        viewModel = viewModel,
                        analytics = analytics,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(args.movie)
        viewModel.uiEvent.observeEvent(viewLifecycleOwner) {
            when (it) {
                is ShareAction -> view.context.executeShareAction(it)
                is ToastAction -> view.context.showToast(it.resId)
            }
        }
    }

    private fun Context.executeShareAction(action: ShareAction, movie: Movie = args.movie) {
        when (action) {
            is ShareAction.Text -> {
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DetailNavGraph(
    movie: Movie,
    viewModel: DetailViewModel,
    analytics: EventAnalytics,
) {
    Box {
        var showShare by remember { mutableStateOf(false) }
        var showPoster by remember { mutableStateOf(false) }
        BackHandler(
            enabled = showShare,
            onBack = { showShare = false }
        )
        DetailScreen(
            movie = movie,
            viewModel = viewModel,
            analytics = analytics,
            onShareClick = {
                showShare = true
            },
            onPosterClick = {
                showPoster = true
            },
        )
        DetailShare(
            onClick = { showShare = false },
            onShareClick = { target ->
                if (target == ShareTarget.Instagram) {
                    viewModel.requestShareImage(
                        target,
                        imageUrl = movie.posterUrl
                    )
                } else {
                    viewModel.requestShareText(target)
                }
            },
            modifier = Modifier.circularReveal(
                visible = showShare,
                center = { Offset(x = it.width, y = 0f) }
            ),
        )
        if (showPoster) {
            DetailPoster(movie)
        }
    }
}

@OptIn(ExperimentalZoomableApi::class)
@Composable
private fun DetailPoster(
    movie: Movie,
    upPress: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val zoomableState = rememberZoomableState()
    BackHandler {
        coroutineScope.launch {
            if (zoomableState.isScaled) {
                zoomableState.animateToInitialState()
            } else {
                upPress()
            }
        }
    }
    ZoomableBox(
        modifier = Modifier.background(Color.Black),
        state = zoomableState,
    ) {
        AsyncImage(
            movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            transform = {
                if (it is AsyncImagePainter.State.Success) {
                    zoomableState.contentIntrinsicSize = it.painter.intrinsicSize
                }
                it
            }
        )
    }
}
