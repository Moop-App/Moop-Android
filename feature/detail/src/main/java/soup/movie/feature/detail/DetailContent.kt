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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun DetailContent(
    viewModel: DetailViewModel,
    uiModel: DetailUiModel,
    onPosterClick: () -> Unit,
    onShareClick: () -> Unit,
    onItemClick: (ContentItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (uiModel) {
            is DetailUiModel.None -> {}
            is DetailUiModel.Success -> {
                DetailList(
                    header = {
                        val isFavorite by viewModel.isFavorite.collectAsState()
                        DetailHeader(
                            uiModel = uiModel.header,
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
                    },
                    items = uiModel.items,
                    viewModel = viewModel,
                    onItemClick = { item -> onItemClick(item) }
                )
            }
            is DetailUiModel.Failure -> {
                DetailError(
                    onRetryClick = {
                        viewModel.onRetryClick()
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
