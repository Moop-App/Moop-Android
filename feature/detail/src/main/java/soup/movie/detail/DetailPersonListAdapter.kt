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

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import com.google.android.material.composethemeadapter.MdcTheme
import soup.movie.detail.DetailPersonListAdapter.ViewHolder
import soup.movie.ext.executeWeb
import soup.movie.util.setOnDebounceClickListener

internal class DetailPersonListAdapter :
    ListAdapter<PersonUiModel, ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ComposeView(parent.context)).apply {
            itemView.setOnDebounceClickListener {
                val query = getItem(bindingAdapterPosition).query
                it.context.executeWeb("https://m.search.naver.com/search.naver?query=$query")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val composeView: ComposeView
    ) : RecyclerView.ViewHolder(composeView) {

        fun bind(item: PersonUiModel) {
            composeView.setContent {
                MdcTheme {
                    CastPerson(item)
                }
            }
        }
    }
}

@Composable
private fun CastPerson(item: PersonUiModel) {
    Card(
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
                text = item.name,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
            )
            if (item.cast.isNotEmpty()) {
                Text(
                    text = item.cast,
                    maxLines = 1,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }
    }
}
