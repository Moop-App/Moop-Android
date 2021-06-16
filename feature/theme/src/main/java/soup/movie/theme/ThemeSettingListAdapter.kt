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
package soup.movie.theme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import soup.movie.theme.databinding.ThemeOptionItemOptionBinding
import soup.movie.util.setOnDebounceClickListener

class ThemeSettingListAdapter(
    private val listener: (ThemeSettingItemUiModel) -> Unit
) : ListAdapter<ThemeSettingItemUiModel, ThemeSettingListAdapter.ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ThemeOptionItemOptionBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnDebounceClickListener(delay = 350L) {
                getItem(bindingAdapterPosition)?.run(listener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ThemeOptionItemOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ThemeSettingItemUiModel) {
            binding.root.setThemeOptionLabel(item.themeOption)
        }
    }
}
