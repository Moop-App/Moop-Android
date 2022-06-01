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
package soup.movie.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun isPortrait(): Boolean {
    val context = LocalContext.current
    val orientation = context.resources.configuration.orientation
    return orientation == Configuration.ORIENTATION_PORTRAIT
}

@Composable
fun isLandscape(): Boolean {
    val context = LocalContext.current
    val orientation = context.resources.configuration.orientation
    return orientation == Configuration.ORIENTATION_LANDSCAPE
}
