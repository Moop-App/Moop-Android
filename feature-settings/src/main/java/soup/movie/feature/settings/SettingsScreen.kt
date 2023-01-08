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
package soup.movie.feature.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import soup.metronome.material.UnelevatedButton
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.designsystem.util.debounce
import soup.movie.core.external.Cgv
import soup.movie.core.external.LotteCinema
import soup.movie.core.external.Megabox
import soup.movie.core.external.Moop
import soup.movie.core.external.startActivitySafely
import soup.movie.feature.theater.TheaterChip
import soup.movie.feature.theme.stringResIdOf
import soup.movie.model.TheaterModel
import soup.movie.model.TheaterTypeModel
import soup.movie.resources.R

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel,
    onThemeEditClick: () -> Unit,
    onTheaterEditClick: () -> Unit,
    onVersionClick: (VersionSettingUiModel) -> Unit,
    onMarketIconClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.menu_settings)) }
            )
        }
    ) { paddingValues ->
        val context = LocalContext.current
        val theme by viewModel.themeUiModel.collectAsState()
        val theater by viewModel.theaterUiModel.collectAsState()
        val version by viewModel.versionUiModel
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
                onItemClick = { theater -> context.executeWeb(theater) },
                onEditClick = onTheaterEditClick
            )
            SettingsDivider()
            SettingsVersionItem(
                version = version,
                onClick = onVersionClick,
                onActionClick = onMarketIconClick
            )
            SettingsDivider()
            SettingsFeedbackItem(onClick = { context.goToEmail() })
        }
    }
    if (viewModel.showVersionUpdateDialog) {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { viewModel.showVersionUpdateDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        MovieIcons.NewReleases,
                        contentDescription = null,
                        tint = MovieTheme.colors.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.settings_version_update_title))
                }
            },
            text = { Text(text = stringResource(R.string.settings_version_update_message)) },
            confirmButton = {
                TextButton(
                    onClick = { Moop.executePlayStore(context) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MovieTheme.colors.secondary
                    ),
                ) {
                    Text(text = stringResource(R.string.settings_version_update_button_positive))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.showVersionUpdateDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MovieTheme.colors.secondary
                    ),
                ) {
                    Text(text = stringResource(R.string.settings_version_update_button_negative))
                }
            },
        )
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
                    MovieIcons.Palette,
                    contentDescription = null,
                    tint = MovieTheme.colors.onBackground,
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.requiredHeight(48.dp)) {
            UnelevatedButton(
                onClick = { debounce(onClick) },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MovieTheme.colors.surface
                )
            ) {
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 17.sp,
                    style = MovieTheme.typography.body2
                )
            }
        }
    }
}

@Composable
private fun SettingsTheaterItem(
    theaterList: List<TheaterModel>,
    onItemClick: (TheaterModel) -> Unit,
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
                    MovieIcons.Edit,
                    contentDescription = null,
                    tint = MovieTheme.colors.onBackground,
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box {
            if (theaterList.isEmpty()) {
                Text(
                    text = stringResource(R.string.settings_theater_empty_description),
                    textAlign = TextAlign.Center,
                    style = MovieTheme.typography.body2
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
private fun SettingsVersionItem(
    version: VersionSettingUiModel?,
    onClick: (VersionSettingUiModel) -> Unit,
    onActionClick: () -> Unit = {}
) {
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
                    text = version?.let { version ->
                        if (version.isLatest) {
                            stringResource(R.string.settings_version_latest, version.versionName)
                        } else {
                            stringResource(R.string.settings_version_current, version.versionName)
                        }
                    }.orEmpty(),
                    textAlign = TextAlign.Center,
                    style = MovieTheme.typography.body2
                )
            }
            IconButton(
                onClick = { debounce(onActionClick) },
                modifier = Modifier
                    .width(48.dp)
                    .padding(end = 4.dp)
                    .align(Alignment.CenterEnd)
            ) {
                if (version?.isLatest == true) {
                    Icon(
                        MovieIcons.Shop,
                        contentDescription = null,
                        tint = MovieTheme.colors.onSurface,
                    )
                } else {
                    Icon(
                        MovieIcons.NewReleases,
                        contentDescription = null,
                        tint = MovieTheme.colors.onError,
                    )
                }
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
                    style = MovieTheme.typography.body2
                )
            }
            Icon(
                MovieIcons.BugReport,
                contentDescription = null,
                tint = MovieTheme.colors.onSurface,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun SettingsCategory(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        color = MovieTheme.colors.onBackground,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SettingsDivider() {
    Divider(color = MovieTheme.colors.divider)
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
            backgroundColor = MovieTheme.colors.onSurface.copy(alpha = 0.1f),
            disabledBackgroundColor = MovieTheme.colors.onSurface.copy(alpha = 0.05f)
        ),
        content = content
    )
}

private fun Context.executeWeb(theater: TheaterModel) {
    return when (theater.type) {
        TheaterTypeModel.CGV -> Cgv.executeWeb(this, theater.code)
        TheaterTypeModel.LOTTE -> LotteCinema.executeWeb(this, theater.code)
        TheaterTypeModel.MEGABOX -> Megabox.executeWeb(this, theater.code)
    }
}

private fun Context.goToEmail() {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("help.moop@gmail.com"))
    intent.putExtra(
        Intent.EXTRA_SUBJECT,
        "뭅 v${BuildConfig.VERSION_NAME} 버그리포트"
    )
    startActivitySafely(intent)
}
