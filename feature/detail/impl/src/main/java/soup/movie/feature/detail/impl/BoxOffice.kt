/*
 * Copyright 2024 SOUP
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.resources.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxOffice(
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
            modifier = Modifier
                .padding(top = 12.dp, bottom = 14.dp)
                .fillMaxWidth(),
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
        }
    }
}

@Preview
@Composable
private fun BoxOfficePreview() {
    MovieTheme {
        BoxOffice(
            uiModel = BoxOfficeItemUiModel(
                rank = 1,
                rankDate = "2024.11.10.",
                audience = 1000,
                screenDays = 7,
            ),
            onClick = {},
        )
    }
}
