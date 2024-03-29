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

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.unit.sp
import com.webtoonscorp.android.readmore.material.ReadMoreText
import soup.movie.core.ads.NativeAd
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.external.executeWeb
import soup.movie.core.imageloading.AsyncImage
import soup.movie.resources.R

@Composable
internal fun DetailList(
    header: @Composable () -> Unit,
    items: List<ContentItemUiModel>,
    viewModel: DetailViewModel,
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
                                viewModel.clickCgvInfo()
                                ctx.executeWeb(item.cgv.webLink)
                            },
                            modifier = Modifier.weight(1f),
                        )
                        Lotte(
                            uiModel = item.lotte,
                            onClick = {
                                viewModel.clickLotteInfo()
                                ctx.executeWeb(item.lotte.webLink)
                            },
                            modifier = Modifier.weight(1f),
                        )
                        Megabox(
                            uiModel = item.megabox,
                            onClick = {
                                viewModel.clickMegaboxInfo()
                                ctx.executeWeb(item.megabox.webLink)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                is NaverItemUiModel -> {
                    Naver(
                        uiModel = item,
                        onClick = { onItemClick(item) },
                    )
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
                is AdItemUiModel -> {
                    DetailAd(
                        uiModel = item,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxOffice(
    uiModel: BoxOfficeItemUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp, bottom = 14.dp).fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "박스오피스",
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.body2,
                    modifier = Modifier.alpha(0.7f),
                )
                Text(
                    text = stringResource(R.string.rank, uiModel.rank),
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.subtitle1,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    text = stringResource(R.string.rank_date, uiModel.rankDate),
                    color = MovieTheme.colors.surface,
                    style = MovieTheme.typography.body2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .background(
                            color = MovieTheme.colors.onSurface,
                            shape = RoundedCornerShape(percent = 50),
                        )
                        .padding(vertical = 1.dp, horizontal = 8.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "누적 관객수",
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.body2,
                    modifier = Modifier.alpha(0.7f),
                )
                Text(
                    text = stringResource(R.string.audience, uiModel.audience),
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.subtitle1,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    text = stringResource(R.string.screen_days, uiModel.screenDays),
                    color = MovieTheme.colors.surface,
                    style = MovieTheme.typography.body2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .background(
                            color = MovieTheme.colors.onSurface,
                            shape = RoundedCornerShape(percent = 50),
                        )
                        .padding(vertical = 1.dp, horizontal = 8.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "평점",
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.body2,
                    modifier = Modifier.alpha(0.7f),
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StarIcon(modifier = Modifier.requiredSize(16.dp))
                    Text(
                        text = uiModel.rating,
                        color = MovieTheme.colors.onSurface,
                        style = MovieTheme.typography.subtitle1,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp, start = 2.dp, end = 12.dp),
                    )
                }
                Text(
                    text = "NAVER",
                    color = MovieTheme.colors.onNaver,
                    style = MovieTheme.typography.body2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .background(
                            color = MovieTheme.colors.naver,
                            shape = RoundedCornerShape(percent = 50),
                        )
                        .padding(vertical = 1.dp, horizontal = 8.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Cgv(
    uiModel: CgvItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        enabled = uiModel.hasInfo,
        modifier = modifier.padding(start = 12.dp, end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Column(
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Chip(
                onClick = onClick,
                colors = ChipDefaults.chipColors(
                    backgroundColor = MovieTheme.colors.cgv,
                    contentColor = MovieTheme.colors.onCgv,
                ),
                border = BorderStroke(width = 1.dp, color = Color(0x229E9E9E)),
            ) {
                Text(
                    text = "CGV",
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StarIcon(modifier = Modifier.requiredSize(width = 24.dp, height = 36.dp))
                Text(
                    text = uiModel.rating,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Lotte(
    uiModel: LotteItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        enabled = uiModel.hasInfo,
        modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Column(
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Chip(
                onClick = onClick,
                colors = ChipDefaults.chipColors(
                    backgroundColor = MovieTheme.colors.lotte,
                    contentColor = MovieTheme.colors.onLotte,
                ),
            ) {
                Text(
                    text = "롯데시네마",
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StarIcon(modifier = Modifier.requiredSize(width = 24.dp, height = 36.dp))
                Text(
                    text = uiModel.rating,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Megabox(
    uiModel: MegaboxItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        enabled = uiModel.hasInfo,
        modifier = modifier.padding(start = 4.dp, end = 12.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Column(
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Chip(
                onClick = onClick,
                colors = ChipDefaults.chipColors(
                    backgroundColor = MovieTheme.colors.megabox,
                    contentColor = MovieTheme.colors.onMegabox,
                ),
            ) {
                Text(
                    text = "메가박스",
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StarIcon(modifier = Modifier.requiredSize(width = 24.dp, height = 36.dp))
                Text(
                    text = uiModel.rating,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Plot(
    uiModel: PlotItemUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
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
                    style = MovieTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            var isExpanded by remember { mutableStateOf(false) }
            ReadMoreText(
                text = uiModel.plot,
                expanded = isExpanded,
                color = MovieTheme.colors.onSurface,
                style = MovieTheme.typography.body2,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Naver(
    uiModel: NaverItemUiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Chip(
                onClick = onClick,
                modifier = Modifier.padding(horizontal = 12.dp),
                colors = ChipDefaults.chipColors(
                    backgroundColor = MovieTheme.colors.naver,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = "네이버",
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StarIcon(modifier = Modifier.requiredSize(width = 24.dp, height = 36.dp))
                Text(
                    text = uiModel.rating,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
            TextButton(
                onClick = onClick,
                modifier = Modifier.fillMaxHeight(),
            ) {
                Text(
                    text = "자세히보기",
                    color = MovieTheme.colors.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Imdb(
    uiModel: ImdbItemUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
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
                    style = MovieTheme.typography.body2,
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
                        style = MovieTheme.typography.body2,
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
                    style = MovieTheme.typography.body2,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Person(
    uiModel: PersonUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = uiModel.name,
                maxLines = 1,
                color = MovieTheme.colors.onSurface,
                style = MovieTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
            )
            if (uiModel.cast.isNotEmpty()) {
                Text(
                    text = uiModel.cast,
                    maxLines = 1,
                    color = MovieTheme.colors.onSurface,
                    style = MovieTheme.typography.subtitle2,
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
    Card(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().requiredHeight(48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(MovieIcons.YouTube),
                contentDescription = null,
                modifier = Modifier.requiredWidth(48.dp).fillMaxHeight(),
                contentScale = ContentScale.Inside,
            )
            Text(
                text = stringResource(R.string.trailer_search_result, uiModel.movieTitle),
                maxLines = 1,
                color = MovieTheme.colors.onSurface,
                style = MovieTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp).weight(1f),
            )
            IconButton(
                modifier = Modifier.requiredWidth(48.dp).fillMaxHeight(),
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrailerItem(
    uiModel: TrailerItemUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
        shape = RectangleShape,
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
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
                modifier = Modifier.fillMaxSize().padding(start = 12.dp),
            ) {
                Text(
                    text = uiModel.trailer.title,
                    style = MovieTheme.typography.body2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    text = uiModel.trailer.author,
                    style = MovieTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrailerFooter(
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().requiredHeight(48.dp),
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
private fun DetailAd(
    uiModel: AdItemUiModel,
) {
    Card(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MovieTheme.colors.surface,
        elevation = MovieTheme.elevations.card,
    ) {
        NativeAd(uiModel.adInfo)
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
