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
package soup.movie.feature.theme.impl

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
import androidx.hilt.navigation.compose.hiltViewModel
import soup.movie.core.designsystem.util.debounce
import soup.movie.feature.theme.stringResIdOf
import soup.movie.resources.R

@Composable
fun ThemeOptionScreenImpl() {
    val viewModel: ThemeOptionViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.theme_option_title)) })
        },
    ) { paddingValues ->
        ThemeOptionList(
            items = viewModel.items,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun ThemeOptionList(
    items: List<ThemeSettingItemUiModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        items.forEach {
            ThemeOptionItem(it)
        }
    }
}

@Composable
private fun ThemeOptionItem(
    uiModel: ThemeSettingItemUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { debounce { uiModel.onItemClick() } }
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = stringResource(stringResIdOf(uiModel.themeOption)),
            fontSize = 17.sp,
        )
    }
}
