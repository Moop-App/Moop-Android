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
package soup.movie.theater

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import soup.metronome.material.chip.ChipDefaults
import soup.metronome.material.chip.FilterChip
import soup.movie.model.Theater

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TheaterFilterChip(
    theater: Theater,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    when (theater.type) {
        Theater.TYPE_CGV -> CgvFilterChip(
            text = theater.name,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Theater.TYPE_LOTTE -> LotteFilterChip(
            text = theater.name,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Theater.TYPE_MEGABOX -> MegaboxFilterChip(
            text = theater.name,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun CgvFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color(0xFFBDBDBD))) {
        FilterChip(
            checked = checked,
            onCheckedChange = onCheckedChange,
            chipIcon = {
                Image(
                    painterResource(R.drawable.ic_logo_cgv),
                    contentDescription = null
                )
            },
            checkedIcon = {
                Image(
                    painterResource(R.drawable.ic_check_cgv),
                    contentDescription = null
                )
            },
            enabled = enabled,
            border = BorderStroke(width = 1.dp, color = Color(0x229E9E9E)),
            colors = ChipDefaults.filterChipColors(
                checkedBackgroundColor = Color.White,
                checkedContentColor = Color(0xFFE51F20),
                uncheckedBackgroundColor = Color(0x55FFFFFF),
                uncheckedContentColor = Color(0x66000000)
            )
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(ChipDefaults.TextPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LotteFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color.White)) {
        FilterChip(
            checked = checked,
            onCheckedChange = onCheckedChange,
            chipIcon = {
                Image(
                    painterResource(R.drawable.ic_logo_lotte),
                    contentDescription = null
                )
            },
            checkedIcon = {
                Image(
                    painterResource(R.drawable.ic_check_lotte),
                    contentDescription = null
                )
            },
            enabled = enabled,
            colors = ChipDefaults.filterChipColors(
                checkedBackgroundColor = Color(0xFFED1D24),
                checkedContentColor = Color.White,
                uncheckedBackgroundColor = Color(0x66ED1D24),
                uncheckedContentColor = Color(0x77FFFFFF)
            )
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(ChipDefaults.TextPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MegaboxFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color.White)) {
        FilterChip(
            checked = checked,
            onCheckedChange = onCheckedChange,
            chipIcon = {
                Image(
                    painterResource(R.drawable.ic_logo_megabox),
                    contentDescription = null
                )
            },
            checkedIcon = {
                Image(
                    painterResource(R.drawable.ic_check_megabox),
                    contentDescription = null
                )
            },
            enabled = enabled,
            colors = ChipDefaults.filterChipColors(
                checkedBackgroundColor = Color(0xFF352263),
                checkedContentColor = Color.White,
                uncheckedBackgroundColor = Color(0x77352263),
                uncheckedContentColor = Color(0x77FFFFFF)
            )
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(ChipDefaults.TextPadding)
            )
        }
    }
}
