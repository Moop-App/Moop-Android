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

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val LightMovieColors = lightMovieColors()

private val DarkMovieColors = darkMovieColors()

@Composable
fun MovieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val movieColors = if (darkTheme) {
        DarkMovieColors
    } else {
        LightMovieColors
    }
    val context = LocalContext.current
    val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (darkTheme) {
            dynamicDarkColorScheme(context = context)
        } else {
            dynamicLightColorScheme(context = context)
        }
    } else {
        if (darkTheme) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }
    }
    CompositionLocalProvider(LocalMovieColors provides movieColors) {
        MaterialTheme(
            colorScheme = colors,
            content = content,
        )
    }
}

object MovieTheme {

    val colors: MovieColors
        @Composable
        get() = LocalMovieColors.current

    val colorScheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}
