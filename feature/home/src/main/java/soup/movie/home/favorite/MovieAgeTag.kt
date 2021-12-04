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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Colors.ageTagUnknown: Color
    get() = if (isLight) {
        Color(0xFF9E9E9E)
    } else {
        Color(0xFFE0E0E0)
    }

private val Colors.ageTagAll: Color
    get() = if (isLight) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFF81C784)
    }

private val Colors.ageTag12: Color
    get() = if (isLight) {
        Color(0xFF2196F3)
    } else {
        Color(0xFF64B5F6)
    }

private val Colors.ageTag15: Color
    get() = if (isLight) {
        Color(0xFFFFC107)
    } else {
        Color(0xFFFFD54F)
    }

private val Colors.ageTag19: Color
    get() = if (isLight) {
        Color(0xFFF44336)
    } else {
        Color(0xFFE57373)
    }

@Composable
internal fun MovieAgeTag(
    age: Int,
    modifier: Modifier = Modifier
) {
    val color: Color = when {
        age >= 19 -> MaterialTheme.colors.ageTag19
        age >= 15 -> MaterialTheme.colors.ageTag15
        age >= 12 -> MaterialTheme.colors.ageTag12
        age >= 0 -> MaterialTheme.colors.ageTagAll
        else -> MaterialTheme.colors.ageTagUnknown
    }
    Box(
        modifier = modifier
            .size(10.dp)
            .background(color = color, shape = RoundedCornerShape(5.dp))
            .border(1.dp, MaterialTheme.colors.background, shape = RoundedCornerShape(5.dp))
    )
}
