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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.resources.R

@Composable
fun HomeFilterTheater(viewModel: HomeFilterViewModel) {
    val theaterUiModel by viewModel.theaterUiModel.collectAsState()
    HomeFilterTheater(
        theaterUiModel = theaterUiModel,
        onCgvFilterChanged = { viewModel.onCgvFilterChanged(it) },
        onLotteFilterChanged = { viewModel.onLotteFilterChanged(it) },
        onMegaboxFilterChanged = { viewModel.onMegaboxFilterChanged(it) },
    )
}

@Composable
private fun HomeFilterTheater(
    theaterUiModel: TheaterFilterUiModel?,
    onCgvFilterChanged: (Boolean) -> Unit,
    onLotteFilterChanged: (Boolean) -> Unit,
    onMegaboxFilterChanged: (Boolean) -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
        HomeFilterCategory(text = stringResource(R.string.filter_category_theater))
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
        ) {
            theaterUiModel?.let { uiModel ->
                CgvFilterChip(
                    text = "CGV",
                    checked = uiModel.hasCgv,
                    onCheckedChange = { isChecked ->
                        onCgvFilterChanged(isChecked)
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                LotteFilterChip(
                    text = "롯데시네마",
                    checked = uiModel.hasLotteCinema,
                    onCheckedChange = { isChecked ->
                        onLotteFilterChanged(isChecked)
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                MegaboxFilterChip(
                    text = "메가박스",
                    checked = uiModel.hasMegabox,
                    onCheckedChange = { isChecked ->
                        onMegaboxFilterChanged(isChecked)
                    },
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeFilterTheaterPreview() {
    MovieTheme {
        HomeFilterTheater(
            theaterUiModel = TheaterFilterUiModel(
                hasCgv = true,
                hasLotteCinema = true,
                hasMegabox = true,
            ),
            onCgvFilterChanged = {},
            onLotteFilterChanged = {},
            onMegaboxFilterChanged = {},
        )
    }
}
