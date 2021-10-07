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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import soup.compose.material.chip.Chip
import soup.compose.material.chip.ChipDefaults

@Composable
private fun rippleTheme(color: Color) = object : RippleTheme {

    @Composable
    override fun defaultColor(): Color {
        return color
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleAlpha(1f, 1f, 1f, 1f)
    }
}

@Composable
fun CgvChip(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color(0xFFBDBDBD))) {
        Chip(
            onClick = onClick,
            enabled = enabled,
            border = BorderStroke(width = 1.dp, color = Color(0x229E9E9E)),
            colors = ChipDefaults.chipColors(
                backgroundColor = Color.White,
                contentColor = Color(0xFFE51F20)
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LotteChip(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color.White)) {
        Chip(
            onClick = onClick,
            enabled = enabled,
            colors = ChipDefaults.chipColors(
                backgroundColor = Color(0xFFED1D24),
                contentColor = Color.White
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MegaboxChip(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color.White)) {
        Chip(
            onClick = onClick,
            enabled = enabled,
            colors = ChipDefaults.chipColors(
                backgroundColor = Color(0xFF352263),
                contentColor = Color.White
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}
