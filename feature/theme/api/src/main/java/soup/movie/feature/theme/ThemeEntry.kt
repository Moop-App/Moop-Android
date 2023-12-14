/*
 * Copyright 2023 SOUP
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
package soup.movie.feature.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

interface ThemeEntry {
    @Composable
    fun ThemeOptionScreen()
}

@Composable
fun rememberThemeEntry(): ThemeEntry {
    val context = LocalContext.current
    return remember(context) {
        EntryPointAccessors
            .fromApplication(context, ThemeEntryPoint::class.java)
            .providesThemeEntry()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface ThemeEntryPoint {
    fun providesThemeEntry(): ThemeEntry
}
