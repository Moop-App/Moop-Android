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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import soup.movie.detail.DetailPersonListAdapter.ViewHolder
import soup.movie.detail.databinding.DetailItemCastPersonBinding
import soup.movie.ext.executeWeb
import soup.movie.util.setOnDebounceClickListener

internal class DetailPersonListAdapter : ListAdapter<PersonUiModel, ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DetailItemCastPersonBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding).apply {
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
        private val binding: DetailItemCastPersonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PersonUiModel) {
            binding.nameText.text = item.name
            binding.castText.text = item.cast
            binding.castText.isGone = item.cast.isEmpty()
        }
    }
}
