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

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBars
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
import soup.movie.model.Movie
import soup.movie.spec.FirebaseLink
import soup.movie.spec.KakaoLink
import soup.movie.ui.detailShareDim

@Composable
internal fun DetailShare(
    movie: Movie,
    onClose: () -> Unit,
    onShareInstagram: (Movie) -> Unit,
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
            .padding(
                WindowInsets.systemBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                    .asPaddingValues(),
            )
            .background(color = MaterialTheme.colors.detailShareDim)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End,
    ) {
        Spacer(modifier = Modifier.requiredSize(48.dp))
        IconButton(
            onClick = {
                context.shareText(movie, "com.facebook.katana")
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(R.drawable.ic_logo_facebook),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                context.shareText(movie, "com.twitter.android")
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(R.drawable.ic_logo_twitter),
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
                    painterResource(R.drawable.ic_logo_instagram),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                context.shareText(movie, "jp.naver.line.android")
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(R.drawable.ic_logo_line),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                KakaoLink.share(context, movie)
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(R.drawable.ic_logo_kakaotalk),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        IconButton(
            onClick = {
                context.shareText(movie)
            },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.requiredSize(48.dp).padding(all = 4.dp),
            ) {
                Image(
                    painterResource(R.drawable.ic_logo_more),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

private fun Context.shareText(movie: Movie, packageName: String? = null) {
    FirebaseLink.createDetailLink(movie) { link ->
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
