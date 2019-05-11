package soup.movie.ui.detail

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import androidx.recyclerview.widget.ext.FixedLayoutManager
import soup.movie.BR
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.DetailItemTrailersBinding
import soup.movie.ui.databinding.DataBindingListAdapter
import soup.movie.ui.databinding.DataBindingViewHolder

internal class DetailListAdapter(
    private val listener: DetailListItemListener,
    private val analytics: EventAnalytics
) : DataBindingListAdapter<ContentItemUiModel>(AlwaysDiffCallback()) {

    override fun createViewHolder(binding: ViewDataBinding): DataBindingViewHolder<ContentItemUiModel> {
        return when (binding) {
            is DetailItemTrailersBinding -> TrailersViewHolder(binding, analytics)
            else -> DataBindingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<ContentItemUiModel>, position: Int) {
        holder.binding.setVariable(BR.listener, listener)
        super.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CgvItemUiModel -> R.layout.detail_item_cgv
        is LotteItemUiModel -> R.layout.detail_item_lotte
        is MegaboxItemUiModel -> R.layout.detail_item_megabox
        is NaverItemUiModel -> R.layout.detail_item_naver
        is TrailersItemUiModel -> R.layout.detail_item_trailers
    }

    class TrailersViewHolder(
        binding: DetailItemTrailersBinding,
        analytics: EventAnalytics
    ) : DataBindingViewHolder<ContentItemUiModel>(binding) {

        private val listAdapter = DetailTrailerListAdapter(analytics)

        init {
            binding.listView.layoutManager = FixedLayoutManager(itemView.context)
            binding.listView.adapter = listAdapter
        }

        override fun bind(item: ContentItemUiModel) {
            super.bind(item)
            if (item is TrailersItemUiModel) {
                listAdapter.submitList(item.trailers)
            }
        }
    }

    fun getSpanSize(position: Int): Int = when (getItem(position)) {
        is CgvItemUiModel,
        is LotteItemUiModel,
        is MegaboxItemUiModel -> 1
        is NaverItemUiModel,
        is TrailersItemUiModel -> 3
    }
}
