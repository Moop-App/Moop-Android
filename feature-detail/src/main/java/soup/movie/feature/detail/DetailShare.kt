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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.detailShareDim
import soup.movie.feature.deeplink.FirebaseLink
import soup.movie.feature.deeplink.KakaoLink
import soup.movie.model.MovieModel
import soup.movie.resources.R

@Composable
internal fun DetailShare(
    movie: MovieModel,
    onClose: () -> Unit,
    onShareInstagram: (MovieModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClose,
            )
            .fillMaxSize()
            .systemBarsPadding()
            .background(color = MaterialTheme.colors.detailShareDim)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End,
    ) {
        Spacer(modifier = Modifier.requiredSize(48.dp))
        IconButton(
            onClick = {
                context.shareToFacebook(movie)
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(MovieIcons.Facebook),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                context.shareToTwitter(movie)
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(MovieIcons.Twitter),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = { onShareInstagram(movie) },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(MovieIcons.Instagram),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                context.shareToLine(movie)
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(MovieIcons.Line),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                context.shareToKakaoTalk(movie)
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(MovieIcons.KakaoTalk),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                context.shareToOthers(movie)
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(MovieIcons.More),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

private fun Context.shareToKakaoTalk(movie: MovieModel) {
    KakaoLink.share(
        context = this,
        movieId = movie.id,
        imageUrl = movie.posterUrl,
        title = movie.title,
        description = buildString {
            append(movie.openDate)
            val ageLabel = getString(
                when {
                    movie.age >= 19 -> R.string.movie_age_19
                    movie.age >= 15 -> R.string.movie_age_15
                    movie.age >= 12 -> R.string.movie_age_12
                    movie.age >= 0 -> R.string.movie_age_all
                    else -> R.string.movie_age_unknown
                }
            )
            append(" / $ageLabel")
        }
    )
}

private fun Context.shareToFacebook(movie: MovieModel) {
    shareText(movie, "com.facebook.katana")
}

private fun Context.shareToTwitter(movie: MovieModel) {
    shareText(movie, "com.twitter.android")
}

private fun Context.shareToLine(movie: MovieModel) {
    shareText(movie, "jp.naver.line.android")
}

private fun Context.shareToOthers(movie: MovieModel) {
    shareText(movie, null)
}

private fun Context.shareText(movie: MovieModel, packageName: String?) {
    FirebaseLink.createDetailLink(
        movieId = movie.id,
        imageUrl = movie.posterUrl,
        title = movie.title,
        description = buildString {
            if (movie.isNow) {
                append("현재상영중")
            } else {
                append("${movie.openDate}개봉")
            }
            val ageLabel = getString(
                when {
                    movie.age >= 19 -> R.string.movie_age_19
                    movie.age >= 15 -> R.string.movie_age_15
                    movie.age >= 12 -> R.string.movie_age_12
                    movie.age >= 0 -> R.string.movie_age_all
                    else -> R.string.movie_age_unknown
                }
            )
            append(" / $ageLabel")
            movie.genres?.let { genres ->
                append(" / ${genres.joinToString()}")
            }
        },
    ) { link ->
        ShareCompat.IntentBuilder(this)
            .setChooserTitle(R.string.action_share)
            .setText("[뭅] ${movie.title}\n$link")
            .setType("text/plain")
            .apply {
                if (packageName != null) {
                    intent.setPackage(packageName)
                }
            }
            .startChooser()
    }
}
