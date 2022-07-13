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
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.analytics.EventAnalytics
import soup.movie.ext.loadAsync
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
                    DetailScreen(
                        movie = args.movie,
                        viewModel = viewModel,
                        analytics = analytics,
                        onPosterClick = { showPosterViewer() },
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

    // TODO: Re-implements this
    private fun showPosterViewer() {
        StfalconImageViewer
            .Builder(context, listOf(args.movie.posterUrl)) { view, imageUrl ->
                view.loadAsync(imageUrl)
            }
            .withHiddenStatusBar(false)
            .show()
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
