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
package soup.movie.feature.home.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.feature.home.textUnit

@Composable
fun MovieBestTag(
    modifier: Modifier = Modifier,
) {
    MovieTextTag(
        text = "BEST",
        tagBackgroundColor = MovieTheme.colors.tagBest,
        modifier = modifier,
        fontSize = 11.dp.textUnit,
    )
}
