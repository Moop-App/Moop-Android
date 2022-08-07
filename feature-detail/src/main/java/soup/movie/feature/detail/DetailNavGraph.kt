/*
 * Copyright 2022 SOUP
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
package soup.movie.feature.detail

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import soup.compose.material.motion.circularReveal
import soup.movie.feature.common.ext.showToast

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailNavGraph(
    viewModel: DetailViewModel,
) {
    Box {
        var showShare by remember { mutableStateOf(false) }
        var showPoster by remember { mutableStateOf(false) }
        BackHandler(
            enabled = showShare,
            onBack = { showShare = false }
        )
        DetailScreen(
            viewModel = viewModel,
            onShareClick = {
                showShare = true
            },
            onPosterClick = {
                showPoster = true
            },
        )
        val movie by viewModel.movie.observeAsState()
        movie?.let {
            DetailShare(
                movie = it,
                onClose = { showShare = false },
                onShareInstagram = {
                    viewModel.requestShareImage(
                        imageUrl = it.posterUrl
                    )
                },
                modifier = Modifier.circularReveal(
                    visible = showShare,
                    center = { Offset(x = it.width, y = 0f) }
                ),
            )
            AnimatedVisibility(
                visible = showPoster,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                DetailPoster(
                    movie = it,
                    upPress = { showPoster = false },
                )
            }
        }
    }

    val uiEvent by viewModel.uiEvent.observeAsState()
    val event = uiEvent?.getContentIfNotHandled()
    if (event != null) {
        val context = LocalContext.current
        when (event) {
            is ShareImageAction -> context.shareImage(event)
            is ToastAction -> context.showToast(event.resId)
        }
    }
}

private fun Context.shareImage(action: ShareImageAction) {
    ShareCompat.IntentBuilder(this)
        .setChooserTitle(R.string.action_share_poster)
        .setStream(action.imageUri)
        .setType(action.mimeType)
        .apply {
            intent.setPackage("com.instagram.android")
        }
        .startChooser()
}
