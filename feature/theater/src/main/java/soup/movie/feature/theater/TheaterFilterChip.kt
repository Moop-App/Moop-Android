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
package soup.movie.feature.theater

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.model.TheaterModel
import soup.movie.model.TheaterTypeModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TheaterFilterChip(
    theater: TheaterModel,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    when (theater.type) {
        TheaterTypeModel.CGV -> CgvFilterChip(
            text = theater.name,
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        TheaterTypeModel.LOTTE -> LotteFilterChip(
            text = theater.name,
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        TheaterTypeModel.MEGABOX -> MegaboxFilterChip(
            text = theater.name,
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun CgvFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
        selectedIcon = {
            Image(
                MovieIcons.Check,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MovieTheme.colors.onCgv),
            )
        },
        enabled = enabled,
        border = BorderStroke(width = 1.dp, color = Color(0x229E9E9E)),
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = MovieTheme.colors.cgv,
            selectedContentColor = MovieTheme.colors.onCgv,
            backgroundColor = Color(0x55FFFFFF),
            contentColor = Color(0x66000000),
        ),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LotteFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
        selectedIcon = {
            Image(
                MovieIcons.Check,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MovieTheme.colors.onLotte),
            )
        },
        enabled = enabled,
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = Color(0xFFED1D24),
            selectedContentColor = Color.White,
            backgroundColor = Color(0x66ED1D24),
            contentColor = Color(0x77FFFFFF),
        ),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MegaboxFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
        selectedIcon = {
            Image(
                MovieIcons.Check,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MovieTheme.colors.onMegabox),
            )
        },
        enabled = enabled,
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = Color(0xFF352263),
            selectedContentColor = Color.White,
            backgroundColor = Color(0x77352263),
            contentColor = Color(0x77FFFFFF),
        ),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
        )
    }
}
