/*
 * Copyright 2022 SOUP
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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightMovieColors = lightMovieColors(
    material = lightColors(
        primary = Color.White,
        primaryVariant = Color(0xFF9E9E9E),
        onPrimary = Color.Black,
        secondary = Color(0xFF2D7AF6),
        onSecondary = Color.White
    )
)

private val DarkMovieColors = lightMovieColors(
    material = darkColors(
        primary = Color.Black,
        primaryVariant = Color.Black,
        onPrimary = Color.White,
        secondary = Color(0xFF8EB5F0),
        onSecondary = Color.Black,
    )
)

private val LightElevations = Elevations(
    card = 10.dp,
    bottomSheet = 16.dp,
)

private val DarkElevations = Elevations(
    card = 1.dp,
    bottomSheet = 0.dp,
)

@Composable
fun MovieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkMovieColors
    } else {
        LightMovieColors
    }
    val elevation = if (darkTheme) {
        DarkElevations
    } else {
        LightElevations
    }
    CompositionLocalProvider(
        LocalMovieColors provides colors,
        LocalElevations provides elevation,
    ) {
        MaterialTheme(
            colors = colors.material,
            content = content,
        )
    }
}

object MovieTheme {

    val colors: MovieColors
        @Composable
        get() = LocalMovieColors.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val elevations: Elevations
        @Composable
        get() = LocalElevations.current
}
