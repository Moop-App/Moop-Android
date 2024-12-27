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
package soup.movie.feature.home.impl.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme

@Composable
fun CgvFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
                    if (checked) {
                        MovieTheme.colors.onCgv
                    } else {
                        Color(0x66000000)
                    },
                ),
            )
        },
        enabled = enabled,
        border = BorderStroke(width = 1.dp, color = Color(0x229E9E9E)),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MovieTheme.colors.cgv,
            selectedLabelColor = MovieTheme.colors.onCgv,
            containerColor = Color(0x55FFFFFF),
            labelColor = Color(0x66000000),
        ),
        label = {
            Text(
                text = text,
                fontSize = 14.sp,
            )
        },
    )
}

@Composable
fun LotteFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        trailingIcon = {
            Image(
                painterResource(MovieIcons.FilterChipLotteCancel),
                contentDescription = null,
                colorFilter = if (checked) null else ColorFilter.tint(Color(0x77FFFFFF)),
            )
        },
        enabled = enabled,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFED1D24),
            selectedLabelColor = Color.White,
            containerColor = Color(0x66ED1D24),
            labelColor = Color(0x77FFFFFF),
        ),
        label = {
            Text(
                text = text,
                fontSize = 14.sp,
            )
        },
    )
}

@Composable
fun MegaboxFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
                colorFilter = if (checked) null else ColorFilter.tint(Color(0x77FFFFFF)),
            )
        },
        enabled = enabled,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF352263),
            selectedLabelColor = Color.White,
            containerColor = Color(0x77352263),
            labelColor = Color(0x77FFFFFF),
        ),
        label = {
            Text(
                text = text,
                fontSize = 14.sp,
            )
        },
    )
}
