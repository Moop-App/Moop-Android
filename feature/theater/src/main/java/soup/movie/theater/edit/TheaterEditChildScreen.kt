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
package soup.movie.theater.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch
import soup.movie.ext.showToast
import soup.movie.model.Theater
import soup.movie.theater.R
import soup.movie.theater.TheaterFilterChip

@Composable
internal fun CgvScreen(viewModel: TheaterEditViewModel) {
    val uiModel by viewModel.cgvUiModel.collectAsState(TheaterEditChildUiModel(emptyList()))
    TheaterEditChildScreen(
        uiModel = uiModel,
        onAddTheater = { viewModel.add(it) },
        onRemoveTheater = { viewModel.remove(it) }
    )
}

@Composable
internal fun LotteScreen(viewModel: TheaterEditViewModel) {
    val uiModel by viewModel.lotteUiModel.collectAsState(TheaterEditChildUiModel(emptyList()))
    TheaterEditChildScreen(
        uiModel = uiModel,
        onAddTheater = { viewModel.add(it) },
        onRemoveTheater = { viewModel.remove(it) }
    )
}

@Composable
internal fun MegaboxScreen(viewModel: TheaterEditViewModel) {
    val uiModel by viewModel.megaboxUiModel.collectAsState(TheaterEditChildUiModel(emptyList()))
    TheaterEditChildScreen(
        uiModel = uiModel,
        onAddTheater = { viewModel.add(it) },
        onRemoveTheater = { viewModel.remove(it) }
    )
}

@Composable
private fun TheaterEditChildScreen(
    uiModel: TheaterEditChildUiModel,
    onAddTheater: (Theater) -> Boolean,
    onRemoveTheater: (Theater) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 8.dp)
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
                                        TheaterEditManager.MAX_ITEMS
                                    )
                                )
                            }
                        } else {
                            onRemoveTheater(theater)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun TheaterAreaItem(
    title: String,
    theaterList: List<TheaterEditTheaterUiModel>,
    onCheckedChange: (Theater, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            modifier = Modifier.padding(all = 8.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle1
        )
        FlowRow(mainAxisSpacing = 8.dp, modifier = Modifier.padding(all = 4.dp)) {
            theaterList.forEach { item ->
                TheaterFilterChip(
                    item.theater,
                    checked = item.checked,
                    onCheckedChange = { checked -> onCheckedChange(item.theater, checked) }
                )
            }
        }
    }
}
