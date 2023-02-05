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
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.designsystem.util.debounce
import soup.movie.model.TheaterModel
import soup.movie.model.TheaterTypeModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TheaterChip(
    theater: TheaterModel,
    onTheaterClick: (TheaterModel) -> Unit = {}
) {
    when (theater.type) {
        TheaterTypeModel.CGV -> CgvChip(
            text = theater.name,
            onClick = { debounce { onTheaterClick(theater) } }
        )
        TheaterTypeModel.LOTTE -> LotteChip(
            text = theater.name,
            onClick = { debounce { onTheaterClick(theater) } }
        )
        TheaterTypeModel.MEGABOX -> MegaboxChip(
            text = theater.name,
            onClick = { debounce { onTheaterClick(theater) } }
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun CgvChip(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Chip(
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(width = 1.dp, color = Color(0x229E9E9E)),
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.White,
            contentColor = MovieTheme.colors.onCgv,
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LotteChip(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Chip(
        onClick = onClick,
        enabled = enabled,
        colors = ChipDefaults.chipColors(
            backgroundColor = Color(0xFFED1D24),
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MegaboxChip(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Chip(
        onClick = onClick,
        enabled = enabled,
        colors = ChipDefaults.chipColors(
            backgroundColor = Color(0xFF352263),
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
        )
    }
}
