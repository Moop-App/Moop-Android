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
package soup.movie.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soup.metronome.material.UnelevatedButton
import soup.movie.model.Theater
import soup.movie.system.SystemViewModel
import soup.movie.theater.TheaterChip
import soup.movie.theme.stringResIdOf
import soup.movie.util.debounce

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel,
    systemViewModel: SystemViewModel,
    onThemeEditClick: () -> Unit,
    onTheaterItemClick: (Theater) -> Unit,
    onTheaterEditClick: () -> Unit,
    onVersionClick: (VersionSettingUiModel) -> Unit,
    onMarketIconClick: () -> Unit,
    onBugReportClick: () -> Unit
) {
    ProvideWindowInsets {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(start = false, end = false),
            topBar = {
                Toolbar(
                    text = stringResource(R.string.menu_settings),
                    onNavigationOnClick = { systemViewModel.openNavigationMenu() }
                )
            }
        ) { paddingValues ->
            val theme by viewModel.themeUiModel.observeAsState()
            val theater by viewModel.theaterUiModel.observeAsState()
            val version by viewModel.versionUiModel.observeAsState()
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                SettingsThemeItem(theme, onClick = onThemeEditClick)
                SettingsDivider()
                SettingsTheaterItem(
                    theater?.theaterList.orEmpty(),
                    onItemClick = onTheaterItemClick,
                    onEditClick = onTheaterEditClick
                )
                SettingsDivider()
                SettingsTheaterModeItem()
                SettingsDivider()
                SettingsVersionItem(
                    version = version,
                    onClick = onVersionClick,
                    onActionClick = onMarketIconClick
                )
                SettingsDivider()
                SettingsFeedbackItem(onClick = onBugReportClick)
            }
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
        modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsCategory(
                text = stringResource(R.string.settings_category_theme),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { debounce(onClick) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painterResource(R.drawable.ic_round_palette),
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.requiredHeight(48.dp)) {
            UnelevatedButton(
                onClick = { debounce(onClick) },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.surface
                )
            ) {
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 17.sp,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
private fun SettingsTheaterItem(
    theaterList: List<Theater>,
    onItemClick: (Theater) -> Unit,
    onEditClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsCategory(
                text = stringResource(R.string.settings_category_theater),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { debounce(onEditClick) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painterResource(R.drawable.ic_round_edit),
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box {
            if (theaterList.isEmpty()) {
                Text(
                    text = stringResource(R.string.settings_theater_empty_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )
            } else {
                FlowRow(mainAxisSpacing = 8.dp) {
                    theaterList.forEach { theater ->
                        TheaterChip(theater, onItemClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsTheaterModeItem() {
    Column(
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        SettingsCategory(text = stringResource(R.string.settings_category_theater_mode))
        Spacer(modifier = Modifier.height(12.dp))
        SettingsButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(48.dp),
            enabled = false,
        ) {
            Text(
                text = "준비 중입니다",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
private fun SettingsVersionItem(
    version: VersionSettingUiModel?,
    onClick: (VersionSettingUiModel) -> Unit,
    onActionClick: () -> Unit = {}
) {
    val text = if (version != null) {
        if (version.isLatest) {
            stringResource(R.string.settings_version_latest, version.versionName)
        } else {
            stringResource(R.string.settings_version_current, version.versionName)
        }
    } else {
        ""
    }
    val actionIcon = if (version?.isLatest == true) {
        painterResource(R.drawable.ic_round_shop)
    } else {
        painterResource(R.drawable.ic_round_new_releases)
    }
    Column(
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        SettingsCategory(text = stringResource(R.string.settings_category_version))
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.requiredHeight(48.dp)) {
            SettingsButton(
                onClick = { debounce { version?.run(onClick) } },
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )
            }
            IconButton(
                onClick = { debounce(onActionClick) },
                modifier = Modifier
                    .width(48.dp)
                    .padding(end = 4.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    actionIcon,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun SettingsFeedbackItem(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        SettingsCategory(text = stringResource(R.string.settings_category_feedback))
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.requiredHeight(48.dp)) {
            SettingsButton(
                onClick = onClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "개발자에게 버그 신고하기",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )
            }
            Icon(
                painterResource(id = R.drawable.ic_round_bug_report),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun Toolbar(text: String, onNavigationOnClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationOnClick) {
                Icon(
                    Icons.Outlined.Menu,
                    contentDescription = null
                )
            }
        },
        title = { Text(text = text) }
    )
}

@Composable
private fun SettingsCategory(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colors.onBackground,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SettingsDivider() {
    Divider(color = MaterialTheme.colors.divider)
}

@Composable
private fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    UnelevatedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
            disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
        ),
        content = content
    )
}
