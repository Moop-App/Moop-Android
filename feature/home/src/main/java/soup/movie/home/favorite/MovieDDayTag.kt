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
package soup.movie.home.favorite

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import soup.movie.home.textUnit

private val Colors.tagDDay: Color
    get() = if (isLight) {
        Color(0xFF9E9E9E)
    } else {
        Color(0xFFE0E0E0)
    }

@Composable
fun MovieDDayTag(
    text: String,
    modifier: Modifier = Modifier
) {
    MovieTextTag(
        text = text,
        tagBackgroundColor = MaterialTheme.colors.tagDDay,
        modifier = modifier,
        fontSize = 12.dp.textUnit
    )
}
