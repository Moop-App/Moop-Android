package soup.movie.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import androidx.recyclerview.widget.ext.FixedLayoutManager
import soup.movie.BR
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.DetailItemTrailersBinding
import soup.movie.ui.detail.DetailListAdapter.DataBindingViewHolder
import soup.movie.ui.detail.DetailViewState.ListItem

internal class DetailListAdapter(private val listener: DetailListItemListener,
                                 private val analytics: EventAnalytics) :
    ListAdapter<ListItem, DataBindingViewHolder>(AlwaysDiffCallback()) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return when (binding) {
            is DetailItemTrailersBinding -> TrailersViewHolder(binding, analytics).apply {
                binding.listView.setRecycledViewPool(viewPool)
            }
            else -> DataBindingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    override fun getItemViewType(position: Int): Int = getItem(position).layoutRes

    open class DataBindingViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun bind(item: ListItem, listener: DetailListItemListener) {
            binding.setVariable(BR.item, item)
            binding.setVariable(BR.listener, listener)
            binding.executePendingBindings()
        }
    }

    class TrailersViewHolder(
        binding: DetailItemTrailersBinding,
        analytics: EventAnalytics
    ) : DataBindingViewHolder(binding) {

        private val listAdapter = DetailTrailerListAdapter(analytics)

        init {
            binding.listView.layoutManager = FixedLayoutManager(itemView.context)
            binding.listView.adapter = listAdapter
        }

        override fun bind(item: ListItem, listener: DetailListItemListener) {
            super.bind(item, listener)
            listAdapter.submitList(item.trailers)
        }
    }
}
