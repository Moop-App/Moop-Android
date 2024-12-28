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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import soup.movie.core.designsystem.showToast
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.external.YouTube
import soup.movie.core.external.executeWeb
import soup.movie.resources.R

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onPosterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uiModel: DetailUiModel by viewModel.uiModel.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ToastAction -> context.showToast(event.resId)
            }
        }
    }

    var showPrivacyDialog by remember { mutableStateOf(false) }
    DetailContent(
        viewModel = viewModel,
        uiModel = uiModel,
        onPosterClick = onPosterClick,
        onItemClick = { item ->
            when (item) {
                is ImdbItemUiModel -> {
                    context.executeWeb(item.webLink)
                }
                is TrailerHeaderItemUiModel -> {
                    showPrivacyDialog = true
                }
                is TrailerItemUiModel -> {
                    YouTube.executeApp(context, item.trailer.youtubeId)
                }
                is TrailerFooterItemUiModel -> {
                    YouTube.executeAppWithQuery(context, item.movieTitle)
                }
                else -> {}
            }
        },
        modifier = modifier.fillMaxSize(),
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
                    Text(
                        text = buildAnnotatedString {
                            withLink(
                                LinkAnnotation.Url(
                                    url = url,
                                    styles = TextLinkStyles(
                                        style = SpanStyle(
                                            color = MovieTheme.colorScheme.secondary,
                                            textDecoration = TextDecoration.Underline,
                                        ),
                                    ),
                                ) {
                                    context.executeWeb(url)
                                },
                            ) {
                                append(url)
                            }
                        },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MovieTheme.colorScheme.secondary,
                    ),
                ) {
                    Text(text = stringResource(R.string.trailer_dialog_button))
                }
            },
        )
    }
}
