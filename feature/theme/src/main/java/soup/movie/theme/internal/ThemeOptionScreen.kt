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
package soup.movie.theme.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soup.movie.theme.R
import soup.movie.theme.ThemeSettingItemUiModel
import soup.movie.theme.ThemeSettingViewModel
import soup.movie.theme.stringResIdOf
import soup.movie.util.debounce

@Composable
internal fun ThemeOptionScreen(viewModel: ThemeSettingViewModel) {
    ProvideWindowInsets {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(start = false, end = false),
            topBar = {
                TopAppBar(title = { Text(stringResource(R.string.theme_option_title)) })
            }
        ) { paddingValues ->
            ThemeOptionList(
                items = viewModel.items,
                onItemClick = { viewModel.onItemClick(it) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun ThemeOptionList(
    items: List<ThemeSettingItemUiModel>,
    onItemClick: (ThemeSettingItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        items.forEach {
            ThemeOptionItem(it, onItemClick)
        }
    }
}

@Composable
private fun ThemeOptionItem(
    item: ThemeSettingItemUiModel,
    onItemClick: (ThemeSettingItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { debounce { onItemClick(item) } }
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(stringResIdOf(item.themeOption)),
            fontSize = 17.sp
        )
    }
}
