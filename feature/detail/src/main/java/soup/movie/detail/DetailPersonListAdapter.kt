package soup.movie.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import soup.movie.detail.databinding.DetailItemCastPersonBinding
import soup.movie.ext.executeWeb
import soup.movie.util.setOnDebounceClickListener

internal class DetailPersonListAdapter(
) : ListAdapter<PersonUiModel, DetailPersonListAdapter.ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DetailItemCastPersonBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnDebounceClickListener {
                val query = getItem(adapterPosition).query
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
