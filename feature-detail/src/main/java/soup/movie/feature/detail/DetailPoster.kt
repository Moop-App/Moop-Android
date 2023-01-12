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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import soup.metronome.zoomable.ExperimentalZoomableApi
import soup.metronome.zoomable.ZoomableBox
import soup.metronome.zoomable.rememberZoomableState
import soup.movie.core.imageloading.AsyncImage
import soup.movie.model.MovieModel

@OptIn(ExperimentalZoomableApi::class)
@Composable
internal fun DetailPoster(
    movie: MovieModel,
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
            onSuccess = {
                zoomableState.contentIntrinsicSize = it.intrinsicSize
            }
        )
    }
}
