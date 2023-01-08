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
import coil.compose.AsyncImage
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.domain.movie.getDDayLabel
import soup.movie.domain.movie.isBest
import soup.movie.domain.movie.isDDay
import soup.movie.domain.movie.isNew
import soup.movie.feature.home.favorite.MovieAgeTag
import soup.movie.feature.home.favorite.MovieBestTag
import soup.movie.feature.home.favorite.MovieDDayTag
import soup.movie.feature.home.favorite.MovieNewTag
import soup.movie.model.MovieModel
import soup.movie.resources.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DetailHeader(
    uiModel: HeaderUiModel,
    onPosterClick: (MovieModel) -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit = {},
) {
    val movie: MovieModel = uiModel.movie
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
                    AsyncImage(
                        movie.posterUrl,
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
                        text = stringResource(
                            when {
                                movie.age >= 19 -> R.string.movie_age_19
                                movie.age >= 15 -> R.string.movie_age_15
                                movie.age >= 12 -> R.string.movie_age_12
                                movie.age >= 0 -> R.string.movie_age_all
                                else -> R.string.movie_age_unknown
                            }
                        ),
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
    val image = AnimatedImageVector.animatedVectorResource(MovieIcons.AvdFavoriteSelected)
    IconButton(
        onClick = {
            onFavoriteChange(!isFavorite)
        }
    ) {
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
            MovieIcons.Share,
            contentDescription = null,
            modifier = Modifier.requiredSize(48.dp),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground),
        )
    }
}
