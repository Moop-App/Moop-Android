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

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import soup.movie.ext.getAgeLabel
import soup.movie.ext.getDDayLabel
import soup.movie.ext.isBest
import soup.movie.ext.isDDay
import soup.movie.ext.isNew
import soup.movie.home.favorite.MovieAgeTag
import soup.movie.home.favorite.MovieBestTag
import soup.movie.home.favorite.MovieDDayTag
import soup.movie.home.favorite.MovieNewTag
import soup.movie.model.Movie

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DetailHeader(
    uiModel: HeaderUiModel,
    onPosterClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit = {},
) {
    val movie: Movie = uiModel.movie
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = movie.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, top = 12.dp, bottom = 12.dp),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            actions()
        }
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Box(
                contentAlignment = Alignment.TopEnd,
            ) {
                Card(
                    onClick = { onPosterClick(movie) },
                    shape = RoundedCornerShape(4.dp),
                    elevation = 0.dp,
                    modifier = Modifier.padding(end = 18.dp),
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(movie.posterUrl),
                        contentDescription = movie.title,
                        modifier = Modifier
                            .requiredWidthIn(max = 110.dp)
                            .aspectRatio(27 / 40f)
                            .clickable(
                                onClick = { onPosterClick(movie) },
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    MovieAgeTag(
                        age = movie.age,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    if (movie.isDDay()) {
                        MovieDDayTag(
                            text = movie.getDDayLabel().orEmpty(),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    if (movie.isBest()) {
                        MovieBestTag(
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    if (movie.isNew()) {
                        MovieNewTag(
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.fillMaxWidth().padding(start = 12.dp)) {
                if (movie.openDate.isNotEmpty()) {
                    Row {
                        Text(
                            text = "개봉",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.alpha(0.5f),
                        )
                        Text(
                            text = movie.openDate,
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "등급",
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body2,
                        fontSize = 14.sp,
                        modifier = Modifier.alpha(0.5f),
                    )
                    Text(
                        text = movie.getAgeLabel(),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body2,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                val genres = movie.genres
                if (genres != null) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "장르",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.alpha(0.5f),
                        )
                        Text(
                            text = genres.joinToString(separator = ", "),
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                val nations = uiModel.nations
                if (nations.isNotEmpty()) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "국가",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.alpha(0.5f),
                        )
                        Text(
                            text = nations.joinToString(separator = ", "),
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                val showTm = uiModel.showTm
                if (showTm > 0) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "러닝타임",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.alpha(0.5f),
                        )
                        Text(
                            text = stringResource(R.string.time_minute, uiModel.showTm),
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                val companies = uiModel.companies
                    .asSequence()
                    .filter { it.companyPartNm.contains("배급") }
                    .map { it.companyNm }
                    .joinToString(separator = ", ")
                if (companies.isNotBlank()) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "배급",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.alpha(0.5f),
                        )
                        Text(
                            text = companies,
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
internal fun FavoriteButton(
    isFavorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit,
) {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_favorite_selected)
    IconButton(onClick = { onFavoriteChange(!isFavorite) }) {
        Image(
            rememberAnimatedVectorPainter(image, isFavorite),
            contentDescription = null,
            modifier = Modifier.requiredSize(48.dp),
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
internal fun ShareButton(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Image(
            Icons.Default.Share,
            contentDescription = null,
            modifier = Modifier.requiredSize(48.dp),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground),
        )
    }
}
