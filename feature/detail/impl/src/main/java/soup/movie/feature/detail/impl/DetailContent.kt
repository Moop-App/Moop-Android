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
package soup.movie.feature.detail.impl

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.resources.R

@Composable
internal fun DetailContent(
    viewModel: DetailViewModel,
    uiModel: DetailUiModel,
    onPosterClick: (String) -> Unit,
    onItemClick: (ContentItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MovieTheme.colorScheme.surfaceContainerLowest,
    ) { paddingValues ->
        when (uiModel) {
            is DetailUiModel.None -> {}
            is DetailUiModel.Success -> {
                DetailList(
                    header = {
                        val isFavorite by viewModel.isFavorite.collectAsState()
                        DetailHeader(
                            uiModel = uiModel.header,
                            onPosterClick = {
                                onPosterClick(uiModel.header.movie.posterUrl)
                            },
                            modifier = Modifier.padding(bottom = 8.dp),
                            actions = {
                                FavoriteButton(
                                    isFavorite = isFavorite,
                                    onFavoriteChange = { isFavorite ->
                                        viewModel.onFavoriteButtonClick(isFavorite)
                                    },
                                )
                            },
                        )
                    },
                    items = uiModel.items,
                    onItemClick = { item -> onItemClick(item) },
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is DetailUiModel.Failure -> {
                DetailError(
                    onRetryClick = {
                        viewModel.onRetryClick()
                    },
                    modifier = Modifier.padding(paddingValues).fillMaxSize(),
                )
            }
        }
    }

    val context = LocalContext.current
    val showOpenDateAlarmMessage by viewModel.showOpenDateAlarmMessage.collectAsState()
    LaunchedEffect(showOpenDateAlarmMessage) {
        if (showOpenDateAlarmMessage) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.action_toast_opendate_alarm),
                )
                viewModel.onOpenDateAlarmMessageShown()
            }
        }
    }
}
