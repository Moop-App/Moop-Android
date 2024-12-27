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
package soup.movie.feature.home.impl.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.resources.R

@Composable
fun HomeFilterAge(viewModel: HomeFilterViewModel) {
    val ageUiModel by viewModel.ageUiModel.collectAsState()
    HomeFilterAge(
        ageUiModel = ageUiModel,
        onAgeAllFilterClicked = { viewModel.onAgeAllFilterClicked() },
        onAge12FilterClicked = { viewModel.onAge12FilterClicked() },
        onAge15FilterClicked = { viewModel.onAge15FilterClicked() },
        onAge19FilterClicked = { viewModel.onAge19FilterClicked() },
    )
}

@Composable
fun HomeFilterAge(
    ageUiModel: AgeFilterUiModel?,
    onAgeAllFilterClicked: () -> Unit,
    onAge12FilterClicked: () -> Unit,
    onAge15FilterClicked: () -> Unit,
    onAge19FilterClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
        HomeFilterCategory(text = stringResource(R.string.filter_category_age))
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ageUiModel?.let {
                HomeFilterAgeText(
                    text = "전체",
                    selected = it.hasAll,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .clickable { onAgeAllFilterClicked() }
                        .background(
                            color = if (it.hasAll) Color(0xFF4CAF50) else Color(0x664CAF50),
                            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
                        ),
                )
                HomeFilterAgeText(
                    text = "12",
                    selected = it.has12,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .clickable { onAge12FilterClicked() }
                        .background(
                            color = if (it.has12) Color(0xFF2196F3) else Color(0x662196F3),
                        ),
                )
                HomeFilterAgeText(
                    text = "15",
                    selected = it.has15,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .clickable { onAge15FilterClicked() }
                        .background(
                            color = if (it.has15) Color(0xFFFFC107) else Color(0x66FFC107),
                        ),
                )
                HomeFilterAgeText(
                    text = "청불",
                    selected = it.has19,
                    modifier = Modifier
                        .clickable { onAge19FilterClicked() }
                        .background(
                            color = if (it.has19) Color(0xFFF44336) else Color(0x66F44336),
                            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                        ),
                )
            }
        }
    }
}

@Composable
private fun HomeFilterAgeText(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val textColor = if (selected) {
        Color.White
    } else {
        Color(0xAAFFFFFF)
    }
    Text(
        text = text,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .requiredWidth(72.dp)
            .wrapContentHeight()
            .padding(vertical = 6.dp),
    )
}

@PreviewLightDark
@Composable
private fun HomeFilterAgePreview() {
    MovieTheme {
        Surface {
            Column {
                listOf(true, false).forEach {
                    HomeFilterAge(
                        ageUiModel = AgeFilterUiModel(
                            hasAll = it,
                            has12 = it,
                            has15 = it,
                            has19 = it,
                        ),
                        onAgeAllFilterClicked = {},
                        onAge12FilterClicked = {},
                        onAge15FilterClicked = {},
                        onAge19FilterClicked = {},
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
