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
package soup.movie.feature.settings.impl.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.designsystem.util.debounce
import soup.movie.feature.settings.impl.theme.stringResIdOf
import soup.movie.feature.theme.ThemeOption
import soup.movie.resources.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onThemeEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme by viewModel.themeUiModel.collectAsState()
    SettingsScreen(
        themeUiModel = theme,
        onThemeEditClick = onThemeEditClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    themeUiModel: ThemeSettingUiModel?,
    onThemeEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.menu_settings)) },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingsThemeItem(theme = themeUiModel, onClick = onThemeEditClick)
            HorizontalDivider()
        }
    }
}

@Composable
private fun SettingsThemeItem(
    theme: ThemeSettingUiModel?,
    onClick: () -> Unit,
) {
    val text = if (theme != null) {
        stringResource(stringResIdOf(theme.themeOption))
    } else {
        ""
    }
    Column(
        modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SettingsCategory(
                text = stringResource(R.string.settings_category_theme),
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { debounce(onClick) },
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    MovieIcons.Palette,
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.requiredHeight(48.dp)) {
            FilledTonalButton(
                onClick = { debounce(onClick) },
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 17.sp,
                    style = MovieTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun SettingsCategory(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium,
    )
}

@Preview
@Composable
private fun SettingsCategoryPreview() {
    MaterialTheme {
        Surface {
            SettingsCategory(text = "Category")
        }
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() {
    MovieTheme {
        SettingsScreen(
            themeUiModel = ThemeSettingUiModel(
                themeOption = ThemeOption.System,
            ),
            onThemeEditClick = {},
        )
    }
}
