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
package soup.movie.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import soup.movie.analytics.EventAnalytics
import soup.movie.model.Movie

@Composable
internal fun DetailScreen(
    movie: Movie,
    viewModel: DetailViewModel,
    analytics: EventAnalytics,
    onPosterClick: () -> Unit,
    onItemClick: (ContentItemUiModel) -> Unit,
) {
    var showShare by remember { mutableStateOf(false) }
    BackHandler(
        enabled = showShare,
        onBack = { showShare = false }
    )
    DetailContent(
        viewModel = viewModel,
        analytics = analytics,
        onPosterClick = {
            analytics.clickPoster()
            onPosterClick()
        },
        onShareClick = {
            analytics.clickShare()
            showShare = true
        },
        onItemClick = { onItemClick(it) },
        modifier = Modifier.fillMaxSize(),
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
            center = Offset(x = 1f, y = 0f)
        ),
    )
}
