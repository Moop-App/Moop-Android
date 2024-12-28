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
package soup.movie.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
data class MovieColors(
    val star: Color,
    val dim: Color,
    val cgv: Color,
    val onCgv: Color,
    val lotte: Color,
    val onLotte: Color,
    val megabox: Color,
    val onMegabox: Color,
    val tagDDay: Color,
    val ageTag12: Color,
    val ageTag15: Color,
    val ageTag19: Color,
    val ageTagAll: Color,
    val ageTagUnknown: Color,
)

fun lightMovieColors(
    star: Color = Color(0xFFFFC107),
    dim: Color = Color(0xDDFFFFFF),
    cgv: Color = Color.White,
    onCgv: Color = Color(0xFFE51F20),
    lotte: Color = Color(0xFFED1D24),
    onLotte: Color = Color.White,
    megabox: Color = Color(0xFF352263),
    onMegabox: Color = Color.White,
    tagDDay: Color = Color(0xFF9E9E9E),
    ageTag12: Color = Color(0xFF2196F3),
    ageTag15: Color = Color(0xFFFFC107),
    ageTag19: Color = Color(0xFFF44336),
    ageTagAll: Color = Color(0xFF4CAF50),
    ageTagUnknown: Color = Color(0xFF9E9E9E),
): MovieColors = MovieColors(
    star = star,
    dim = dim,
    cgv = cgv,
    onCgv = onCgv,
    lotte = lotte,
    onLotte = onLotte,
    megabox = megabox,
    onMegabox = onMegabox,
    tagDDay = tagDDay,
    ageTag12 = ageTag12,
    ageTag15 = ageTag15,
    ageTag19 = ageTag19,
    ageTagAll = ageTagAll,
    ageTagUnknown = ageTagUnknown,
)

fun darkMovieColors(
    star: Color = Color(0xFFFFC107),
    dim: Color = Color(0xAA000000),
    cgv: Color = Color.White,
    onCgv: Color = Color(0xFFE51F20),
    lotte: Color = Color(0xFFED1D24),
    onLotte: Color = Color.White,
    megabox: Color = Color(0xFF352263),
    onMegabox: Color = Color.White,
    tagDDay: Color = Color(0xFFE0E0E0),
    ageTag12: Color = Color(0xFF64B5F6),
    ageTag15: Color = Color(0xFFFFD54F),
    ageTag19: Color = Color(0xFFE57373),
    ageTagAll: Color = Color(0xFF81C784),
    ageTagUnknown: Color = Color(0xFFE0E0E0),
): MovieColors = MovieColors(
    star = star,
    dim = dim,
    cgv = cgv,
    onCgv = onCgv,
    lotte = lotte,
    onLotte = onLotte,
    megabox = megabox,
    onMegabox = onMegabox,
    tagDDay = tagDDay,
    ageTag12 = ageTag12,
    ageTag15 = ageTag15,
    ageTag19 = ageTag19,
    ageTagAll = ageTagAll,
    ageTagUnknown = ageTagUnknown,
)

internal val LocalMovieColors = staticCompositionLocalOf { lightMovieColors() }
