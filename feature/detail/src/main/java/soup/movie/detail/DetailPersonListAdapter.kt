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
package soup.movie.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import soup.movie.ext.executeWeb

@Composable
internal fun Cast(item: CastItemUiModel) {
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
    ) {
        items(item.persons) { item ->
            Person(
                uiModel = item,
                onClick = {
                    context.executeWeb("https://m.search.naver.com/search.naver?query=${item.query}")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Person(
    uiModel: PersonUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = dimensionResource(R.dimen.detail_card_elevation),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = uiModel.name,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
            )
            if (uiModel.cast.isNotEmpty()) {
                Text(
                    text = uiModel.cast,
                    maxLines = 1,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }
    }
}
