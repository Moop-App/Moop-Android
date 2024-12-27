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

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.webtoonscorp.android.readmore.material.ReadMoreText
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.external.executeWeb
import soup.movie.core.imageloading.AsyncImage
import soup.movie.resources.R

@Composable
internal fun DetailList(
    header: @Composable () -> Unit,
    items: List<ContentItemUiModel>,
    onItemClick: (ContentItemUiModel) -> Unit,
) {
    LazyColumn(
        contentPadding = WindowInsets.systemBars.asPaddingValues(),
    ) {
        item {
            header()
        }
        items(items, key = { it.id }) { item ->
            when (item) {
                is BoxOfficeItemUiModel -> {
                    BoxOffice(
                        uiModel = item,
                        onClick = { onItemClick(item) },
                    )
                }
                is TheatersItemUiModel -> {
                    val ctx = LocalContext.current
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Cgv(
                            uiModel = item.cgv,
                            onClick = {
                                ctx.executeWeb(item.cgv.webLink)
                            },
                            modifier = Modifier.weight(1f),
                        )
                        Lotte(
                            uiModel = item.lotte,
                            onClick = {
                                ctx.executeWeb(item.lotte.webLink)
                            },
                            modifier = Modifier.weight(1f),
                        )
                        Megabox(
                            uiModel = item.megabox,
                            onClick = {
                                ctx.executeWeb(item.megabox.webLink)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                is ImdbItemUiModel -> {
                    Imdb(
                        uiModel = item,
                        onClick = { onItemClick(item) },
                    )
                }
                is PlotItemUiModel -> {
                    Plot(
                        uiModel = item,
                        onClick = { onItemClick(item) },
                    )
                }
                is CastItemUiModel -> {
                    Cast(
                        uiModel = item,
                    )
                }
                is TrailerHeaderItemUiModel -> {
                    TrailerHeader(
                        uiModel = item,
                        onPrivacyTipClick = { onItemClick(item) },
                    )
                }
                is TrailerItemUiModel -> {
                    TrailerItem(
                        uiModel = item,
                        onClick = { onItemClick(item) },
                    )
                }
                is TrailerFooterItemUiModel -> {
                    TrailerFooter(
                        onClick = { onItemClick(item) },
                    )
                }
            }
        }
    }
}

@Composable
private fun Cgv(
    uiModel: CgvItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        onClick = onClick,
        enabled = uiModel.hasInfo,
        modifier = modifier.padding(start = 12.dp, end = 4.dp, bottom = 8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 4.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AssistChip(
                onClick = onClick,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MovieTheme.colors.cgv,
                    labelColor = MovieTheme.colors.onCgv,
                ),
                label = {
                    Text(
                        text = "CGV",
                        style = MovieTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StarIcon(modifier = Modifier.requiredSize(width = 24.dp, height = 36.dp))
                Text(
                    text = uiModel.rating,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun Lotte(
    uiModel: LotteItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        onClick = onClick,
        enabled = uiModel.hasInfo,
        modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 4.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AssistChip(
                onClick = onClick,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MovieTheme.colors.lotte,
                    labelColor = MovieTheme.colors.onLotte,
                ),
                label = {
                    Text(
                        text = "롯데시네마",
                        style = MovieTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StarIcon(modifier = Modifier.requiredSize(width = 24.dp, height = 36.dp))
                Text(
                    text = uiModel.rating,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun Megabox(
    uiModel: MegaboxItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        onClick = onClick,
        enabled = uiModel.hasInfo,
        modifier = modifier.padding(start = 4.dp, end = 12.dp, bottom = 8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 4.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AssistChip(
                onClick = onClick,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MovieTheme.colors.megabox,
                    labelColor = MovieTheme.colors.onMegabox,
                ),
                label = {
                    Text(
                        text = "메가박스",
                        style = MovieTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StarIcon(modifier = Modifier.requiredSize(width = 24.dp, height = 36.dp))
                Text(
                    text = uiModel.rating,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun Plot(
    uiModel: PlotItemUiModel,
    onClick: () -> Unit,
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    MovieIcons.Subject,
                    contentDescription = null,
                    modifier = Modifier.requiredSize(20.dp),
                )
                Text(
                    text = "줄거리",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            var isExpanded by remember { mutableStateOf(false) }
            ReadMoreText(
                text = uiModel.plot,
                expanded = isExpanded,
                color = MovieTheme.colors.onSurface,
                style = MovieTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable {
                        isExpanded = !isExpanded
                    }
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(durationMillis = 100)),
                readMoreText = "더보기",
                readMoreColor = MovieTheme.colors.secondary,
                readMoreFontWeight = FontWeight.Bold,
                readMoreMaxLines = 3,
            )
        }
    }
}

@Composable
private fun Imdb(
    uiModel: ImdbItemUiModel,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(MovieIcons.Imdb),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentWidth()
                        .requiredHeight(24.dp),
                )
                Text(
                    text = uiModel.imdb,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(MovieIcons.RottenTomatoes),
                    contentDescription = null,
                    modifier = Modifier.requiredSize(24.dp),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // TOMATOMETER: https://www.rottentomatoes.com/about
                    val rottenTomatoes = uiModel.rottenTomatoes
                    val showTomatoIcon = rottenTomatoes.contains('%')
                    if (showTomatoIcon) {
                        val score = rottenTomatoes.substring(0, rottenTomatoes.lastIndex)
                            .toIntOrNull() ?: 0
                        Image(
                            if (score >= 60) {
                                painterResource(MovieIcons.RottenTomatoesFresh)
                            } else {
                                painterResource(MovieIcons.RottenTomatoesRotten)
                            },
                            contentDescription = null,
                            modifier = Modifier.requiredSize(16.dp),
                        )
                    }
                    Text(
                        text = uiModel.rottenTomatoes,
                        color = MovieTheme.colors.onSurface,
                        style = MovieTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(MovieIcons.Metacritic),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentWidth()
                        .requiredHeight(24.dp),
                )
                Text(
                    text = uiModel.metascore,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun Cast(
    uiModel: CastItemUiModel,
) {
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(uiModel.persons) { item ->
            Person(
                uiModel = item,
                onClick = {
                    context.executeWeb("https://m.search.naver.com/search.naver?query=${item.query}")
                },
            )
        }
    }
}

@Composable
private fun Person(
    uiModel: PersonUiModel,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = uiModel.name,
                maxLines = 1,
                style = MovieTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            if (uiModel.cast.isNotEmpty()) {
                Text(
                    text = uiModel.cast,
                    maxLines = 1,
                    style = MovieTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun TrailerHeader(
    uiModel: TrailerHeaderItemUiModel,
    onPrivacyTipClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(MovieIcons.YouTube),
                contentDescription = null,
                modifier = Modifier
                    .requiredWidth(48.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Inside,
            )
            Text(
                text = stringResource(R.string.trailer_search_result, uiModel.movieTitle),
                maxLines = 1,
                style = MovieTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f),
            )
            IconButton(
                modifier = Modifier
                    .requiredWidth(48.dp)
                    .fillMaxHeight(),
                onClick = onPrivacyTipClick,
            ) {
                Image(
                    MovieIcons.PrivacyTip,
                    contentDescription = null,
                    modifier = Modifier.requiredSize(18.dp),
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(color = MovieTheme.colors.onSurface),
                )
            }
        }
    }
}

@Composable
private fun TrailerItem(
    uiModel: TrailerItemUiModel,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            AsyncImage(
                uiModel.trailer.thumbnailUrl,
                placeholder = ColorPainter(color = MovieTheme.colors.onSurface.copy(alpha = 0.1f)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredWidth(140.dp)
                    .fillMaxHeight(),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = uiModel.trailer.title,
                    style = MovieTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    text = uiModel.trailer.author,
                    style = MovieTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun TrailerFooter(
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "더보기",
                color = MovieTheme.colors.secondary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun StarIcon(modifier: Modifier = Modifier) {
    Image(
        MovieIcons.Star,
        contentDescription = null,
        colorFilter = ColorFilter.tint(MovieTheme.colors.star),
        modifier = modifier,
    )
}
