/*
 * Copyright 2024 SOUP
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
package soup.movie.feature.home.impl.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.resources.R

@Composable
fun CommonError(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(40.dp),
        color = MovieTheme.colorScheme.error,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                MovieIcons.Info,
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.common_network_error),
                modifier = Modifier.padding(start = 4.dp),
                maxLines = 1,
                style = MovieTheme.typography.titleSmall,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun CommonErrorPreview() {
    MovieTheme {
        Surface {
            CommonError(onClick = {})
        }
    }
}
