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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import soup.movie.core.designsystem.showToast

@Composable
fun DetailNavGraph(
    viewModel: DetailViewModel,
) {
    val context = LocalContext.current
    val uiModel: DetailUiModel by viewModel.uiModel.collectAsState()
    Box {
        val movie = (uiModel as? DetailUiModel.Success)?.header?.movie
        var showPoster by remember { mutableStateOf(false) }
        DetailScreen(
            viewModel = viewModel,
            uiModel = uiModel,
            onPosterClick = {
                showPoster = true
            },
        )
        if (movie != null) {
            AnimatedVisibility(
                visible = showPoster,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                DetailPoster(
                    movie = movie,
                    upPress = { showPoster = false },
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ToastAction -> context.showToast(event.resId)
            }
        }
    }
}
