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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import soup.movie.analytics.EventAnalytics

@Composable
internal fun DetailScreen(
    viewModel: DetailViewModel,
    analytics: EventAnalytics,
    onPosterClick: () -> Unit,
    onShareClick: () -> Unit,
    onItemClick: (ContentItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val headerUiModel by viewModel.headerUiModel.observeAsState()
    val isFavorite by viewModel.favoriteUiModel.observeAsState(initial = false)
    val contentUiModel by viewModel.contentUiModel.observeAsState()
    val isError by viewModel.isError.observeAsState(initial = false)
    Box(modifier = modifier) {
        if (isError) {
            DetailError(
                onRetryClick = {
                    viewModel.onRetryClick()
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
        contentUiModel?.let {
            DetailList(
                header = {
                    headerUiModel?.let { uiModel ->
                        DetailHeader(
                            uiModel = uiModel,
                            onPosterClick = {
                                onPosterClick()
                            },
                            modifier = Modifier.padding(bottom = 8.dp),
                            actions = {
                                FavoriteButton(
                                    isFavorite = isFavorite,
                                    onFavoriteChange = { isFavorite ->
                                        viewModel.onFavoriteButtonClick(isFavorite)
                                    }
                                )
                                ShareButton(
                                    onClick = {
                                        onShareClick()
                                    }
                                )
                            }
                        )
                    }
                },
                items = it.items,
                analytics = analytics,
                onItemClick = { item -> onItemClick(item) }
            )
        }
    }
}
