/*
 * Copyright 2021 SOUP
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
package soup.movie.feature.theater.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.showToast
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.feature.theater.TheaterFilterChip
import soup.movie.model.TheaterModel
import soup.movie.resources.R

@Composable
internal fun CgvScreen(viewModel: TheaterEditViewModel) {
    val uiModel by viewModel.cgvUiModel.collectAsState(TheaterEditChildUiModel(emptyList()))
    TheaterEditChildScreen(
        uiModel = uiModel,
        onAddTheater = { viewModel.add(it) },
        onRemoveTheater = { viewModel.remove(it) },
    )
}

@Composable
internal fun LotteScreen(viewModel: TheaterEditViewModel) {
    val uiModel by viewModel.lotteUiModel.collectAsState(TheaterEditChildUiModel(emptyList()))
    TheaterEditChildScreen(
        uiModel = uiModel,
        onAddTheater = { viewModel.add(it) },
        onRemoveTheater = { viewModel.remove(it) },
    )
}

@Composable
internal fun MegaboxScreen(viewModel: TheaterEditViewModel) {
    val uiModel by viewModel.megaboxUiModel.collectAsState(TheaterEditChildUiModel(emptyList()))
    TheaterEditChildScreen(
        uiModel = uiModel,
        onAddTheater = { viewModel.add(it) },
        onRemoveTheater = { viewModel.remove(it) },
    )
}

@Composable
private fun TheaterEditChildScreen(
    uiModel: TheaterEditChildUiModel,
    onAddTheater: (TheaterModel) -> Boolean,
    onRemoveTheater: (TheaterModel) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 8.dp),
    ) {
        items(uiModel.areaGroupList) { item ->
            TheaterAreaItem(
                item.title,
                item.theaterList,
                onCheckedChange = { theater, checked ->
                    coroutineScope.launch {
                        if (checked) {
                            if (onAddTheater(theater).not()) {
                                context.showToast(
                                    context.getString(
                                        R.string.theater_select_limit_description,
                                        TheaterEditManager.MAX_ITEMS,
                                    ),
                                )
                            }
                        } else {
                            onRemoveTheater(theater)
                        }
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TheaterAreaItem(
    title: String,
    theaterList: List<TheaterEditTheaterUiModel>,
    onCheckedChange: (TheaterModel, Boolean) -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            modifier = Modifier.padding(all = 8.dp),
            fontWeight = FontWeight.Bold,
            color = MovieTheme.colors.onBackground,
            style = MovieTheme.typography.subtitle1,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            modifier = Modifier.padding(all = 4.dp),
        ) {
            theaterList.forEach { item ->
                TheaterFilterChip(
                    item.theater,
                    checked = item.checked,
                    onCheckedChange = { checked -> onCheckedChange(item.theater, checked) },
                )
            }
        }
    }
}
