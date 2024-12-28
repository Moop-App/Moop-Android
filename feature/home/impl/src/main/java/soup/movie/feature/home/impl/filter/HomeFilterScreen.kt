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
package soup.movie.feature.home.impl.filter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeFilterScreen(
    viewModel: HomeFilterViewModel,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            HomeFilterTheater(viewModel)
        }
        item {
            HomeFilterDivider()
            HomeFilterAge(viewModel)
        }
    }
}

@Composable
private fun HomeFilterDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}
