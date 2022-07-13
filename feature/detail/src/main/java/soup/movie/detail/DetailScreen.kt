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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import soup.compose.material.motion.circularReveal
import soup.movie.analytics.EventAnalytics
import soup.movie.ext.executeWeb
import soup.movie.model.Movie
import soup.movie.util.YouTube

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun DetailScreen(
    movie: Movie,
    viewModel: DetailViewModel,
    analytics: EventAnalytics,
    onPosterClick: () -> Unit,
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showShare by remember { mutableStateOf(false) }
    BackHandler(
        enabled = showShare,
        onBack = { showShare = false }
    )

    val context = LocalContext.current
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
                    analytics.clickTrailer()
                    YouTube.executeApp(context, item.trailer)
                }
                is TrailerFooterItemUiModel -> {
                    analytics.clickMoreTrailers()
                    YouTube.executeAppWithQuery(context, item.movieTitle)
                }
                else -> {}
            }
        },
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
            center = { Offset(x = it.width, y = 0f) }
        ),
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
                                    color = MaterialTheme.colors.secondary,
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
                        contentColor = MaterialTheme.colors.secondary
                    ),
                ) {
                    Text(text = stringResource(R.string.trailer_dialog_button))
                }
            },
        )
    }
}
