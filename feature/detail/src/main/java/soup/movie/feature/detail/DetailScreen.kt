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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.external.YouTube
import soup.movie.core.external.executeWeb
import soup.movie.resources.R

@Composable
internal fun DetailScreen(
    viewModel: DetailViewModel,
    uiModel: DetailUiModel,
    onShareClick: () -> Unit,
    onPosterClick: () -> Unit,
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    DetailContent(
        viewModel = viewModel,
        uiModel = uiModel,
        onPosterClick = {
            viewModel.clickPoster()
            onPosterClick()
        },
        onShareClick = {
            viewModel.clickShare()
            onShareClick()
        },
        onItemClick = { item ->
            when (item) {
                is BoxOfficeItemUiModel -> {
                    context.executeWeb(item.webLink)
                }
                is NaverItemUiModel -> {
                    context.executeWeb(item.webLink)
                }
                is ImdbItemUiModel -> {
                    context.executeWeb(item.webLink)
                }
                is TrailerHeaderItemUiModel -> {
                    showPrivacyDialog = true
                }
                is TrailerItemUiModel -> {
                    viewModel.clickTrailer()
                    YouTube.executeApp(context, item.trailer.youtubeId)
                }
                is TrailerFooterItemUiModel -> {
                    viewModel.clickMoreTrailers()
                    YouTube.executeAppWithQuery(context, item.movieTitle)
                }
                else -> {}
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Text(text = stringResource(R.string.trailer_dialog_title))
            },
            text = {
                Column {
                    Text(text = stringResource(R.string.trailer_dialog_message))

                    val url = "https://policies.google.com/privacy"
                    ClickableText(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = MovieTheme.colors.secondary,
                                    textDecoration = TextDecoration.Underline,
                                )
                            ) {
                                append(url)
                            }
                        },
                    ) {
                        context.executeWeb(url)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MovieTheme.colors.secondary
                    ),
                ) {
                    Text(text = stringResource(R.string.trailer_dialog_button))
                }
            },
        )
    }
}
