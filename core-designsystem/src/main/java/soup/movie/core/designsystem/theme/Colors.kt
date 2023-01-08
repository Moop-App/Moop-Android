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

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
data class MovieColors(
    internal val material: Colors,
    val divider: Color,
    val star: Color,
    val dim: Color,
    val naver: Color,
    val onNaver: Color,
    val cgv: Color,
    val onCgv: Color,
    val lotte: Color,
    val onLotte: Color,
    val megabox: Color,
    val onMegabox: Color,
    val tagNew: Color,
    val tagBest: Color,
    val tagDDay: Color,
    val ageTag12: Color,
    val ageTag15: Color,
    val ageTag19: Color,
    val ageTagAll: Color,
    val ageTagUnknown: Color,
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight

    val primarySurface: Color get() = material.primarySurface
}

fun lightMovieColors(
    material: Colors = lightColors(),
    divider: Color = Color(0xFFF5F5F5),
    star: Color = Color(0xFFFFC107),
    dim: Color = Color(0xDDFFFFFF),
    naver: Color = Color(0xFF1EC800),
    onNaver: Color = material.surface,
    cgv: Color = Color.White,
    onCgv: Color = Color(0xFFE51F20),
    lotte: Color = Color(0xFFED1D24),
    onLotte: Color = Color.White,
    megabox: Color = Color(0xFF352263),
    onMegabox: Color = Color.White,
    tagNew: Color = Color(0xFFF44336),
    tagBest: Color = Color(0xFFFF9800),
    tagDDay: Color = Color(0xFF9E9E9E),
    ageTag12: Color = Color(0xFF2196F3),
    ageTag15: Color = Color(0xFFFFC107),
    ageTag19: Color = Color(0xFFF44336),
    ageTagAll: Color = Color(0xFF4CAF50),
    ageTagUnknown: Color = Color(0xFF9E9E9E),
): MovieColors = MovieColors(
    material = material,
    divider = divider,
    star = star,
    dim = dim,
    naver = naver,
    onNaver = onNaver,
    cgv = cgv,
    onCgv = onCgv,
    lotte = lotte,
    onLotte = onLotte,
    megabox = megabox,
    onMegabox = onMegabox,
    tagNew = tagNew,
    tagBest = tagBest,
    tagDDay = tagDDay,
    ageTag12 = ageTag12,
    ageTag15 = ageTag15,
    ageTag19 = ageTag19,
    ageTagAll = ageTagAll,
    ageTagUnknown = ageTagUnknown,
)

fun darkMovieColors(
    material: Colors = darkColors(),
    divider: Color = Color(0xFF212121),
    star: Color = Color(0xFFFFC107),
    dim: Color = Color(0xAA000000),
    naver: Color = Color(0xFF1EC800),
    onNaver: Color = material.surface,
    cgv: Color = Color.White,
    onCgv: Color = Color(0xFFE51F20),
    lotte: Color = Color(0xFFED1D24),
    onLotte: Color = Color.White,
    megabox: Color = Color(0xFF352263),
    onMegabox: Color = Color.White,
    tagNew: Color = Color(0xFFF44336),
    tagBest: Color = Color(0xFFFFB74D),
    tagDDay: Color = Color(0xFFE0E0E0),
    ageTag12: Color = Color(0xFF64B5F6),
    ageTag15: Color = Color(0xFFFFD54F),
    ageTag19: Color = Color(0xFFE57373),
    ageTagAll: Color = Color(0xFF81C784),
    ageTagUnknown: Color = Color(0xFFE0E0E0),
): MovieColors = MovieColors(
    material = material,
    divider = divider,
    star = star,
    dim = dim,
    naver = naver,
    onNaver = onNaver,
    cgv = cgv,
    onCgv = onCgv,
    lotte = lotte,
    onLotte = onLotte,
    megabox = megabox,
    onMegabox = onMegabox,
    tagNew = tagNew,
    tagBest = tagBest,
    tagDDay = tagDDay,
    ageTag12 = ageTag12,
    ageTag15 = ageTag15,
    ageTag19 = ageTag19,
    ageTagAll = ageTagAll,
    ageTagUnknown = ageTagUnknown,
)

internal val LocalMovieColors = staticCompositionLocalOf { lightMovieColors() }
