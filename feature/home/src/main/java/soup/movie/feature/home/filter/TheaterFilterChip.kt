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
package soup.movie.feature.home.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun CgvFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilterChip(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        trailingIcon = {
            Image(
                painterResource(MovieIcons.FilterChipCgvCancel),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (checked) MovieTheme.colors.onCgv
                    else Color(0x66000000)
                )
            )
        },
        enabled = enabled,
        border = BorderStroke(width = 1.dp, color = Color(0x229E9E9E)),
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = MovieTheme.colors.cgv,
            selectedContentColor = MovieTheme.colors.onCgv,
            backgroundColor = Color(0x55FFFFFF),
            contentColor = Color(0x66000000)
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun LotteFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilterChip(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        trailingIcon = {
            Image(
                painterResource(MovieIcons.FilterChipLotteCancel),
                contentDescription = null,
                colorFilter = if (checked) null else ColorFilter.tint(Color(0x77FFFFFF))
            )
        },
        enabled = enabled,
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = Color(0xFFED1D24),
            selectedContentColor = Color.White,
            backgroundColor = Color(0x66ED1D24),
            contentColor = Color(0x77FFFFFF)
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MegaboxFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilterChip(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        trailingIcon = {
            Image(
                painterResource(MovieIcons.FilterChipMegaboxCancel),
                contentDescription = null,
                modifier = Modifier,
                colorFilter = if (checked) null else ColorFilter.tint(Color(0x77FFFFFF))
            )
        },
        enabled = enabled,
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = Color(0xFF352263),
            selectedContentColor = Color.White,
            backgroundColor = Color(0x77352263),
            contentColor = Color(0x77FFFFFF)
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
        )
    }
}
