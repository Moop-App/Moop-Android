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
package soup.movie.feature.home.impl.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.feature.home.impl.textUnit

@Composable
fun MovieAgeTag(
    age: Int,
    modifier: Modifier = Modifier,
) {
    val (text, color) = when {
        age >= 19 -> "청불" to MovieTheme.colors.ageTag19
        age >= 15 -> "15" to MovieTheme.colors.ageTag15
        age >= 12 -> "12" to MovieTheme.colors.ageTag12
        age >= 0 -> "전체" to MovieTheme.colors.ageTagAll
        else -> "미정" to MovieTheme.colors.ageTagUnknown
    }
    MovieTextTag(
        text = text,
        tagBackgroundColor = color,
        modifier = modifier,
        fontSize = 12.dp.textUnit,
    )
}
