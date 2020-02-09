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
                getItem(adapterPosition)?.run(listener)
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
            binding.item = item
            binding.executePendingBindings()
        }
    }
}
