package soup.movie.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ext.IdBasedDiffCallback
import soup.movie.ui.databinding.DataBindingItemListener

internal class DetailListAdapter(
    itemClickListener: (ContentItemUiModel) -> Unit
) : ListAdapter<ContentItemUiModel, DetailListAdapter.ViewHolder>(IdBasedDiffCallback { it.id }) {

    private val itemListener = DataBindingItemListener<ContentItemUiModel>(
        onClick = { _, position, item ->
            when (item) {
                is PlotItemUiModel -> {
                    item.isExpanded = item.isExpanded.not()
                    notifyItemChanged(position)
                }
                is AdUiModel -> {
                    // do nothing
                }
                else -> {
                    itemClickListener(item)
                }
            }
        }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            if (headerHeight > 0) {
                holder.itemView.updateLayoutParams {
                    height = headerHeight
                }
            }
        } else {
            holder.binding.apply {
                setVariable(BR.item, getItem(position))
                setVariable(BR.position, position)
                setVariable(BR.listener, itemListener)
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HeaderItemUiModel -> R.layout.detail_item_header
        is BoxOfficeItemUiModel -> R.layout.detail_item_box_office
        is CgvItemUiModel -> R.layout.detail_item_cgv
        is LotteItemUiModel -> R.layout.detail_item_lotte
        is MegaboxItemUiModel -> R.layout.detail_item_megabox
        is NaverItemUiModel -> R.layout.detail_item_naver
        is ImdbItemUiModel -> R.layout.detail_item_imdb
        is PlotItemUiModel -> R.layout.detail_item_plot
        is CastItemUiModel -> R.layout.detail_item_cast
        is TrailerHeaderItemUiModel -> R.layout.detail_item_trailer_header
        is TrailerItemUiModel -> R.layout.detail_item_trailer
        is TrailerFooterItemUiModel -> R.layout.detail_item_trailer_footer
        is AdUiModel -> R.layout.detail_item_ad
    }

    fun getSpanSize(position: Int): Int = when (getItem(position)) {
        is CgvItemUiModel,
        is LotteItemUiModel,
        is MegaboxItemUiModel -> 1
        else -> 3
    }

    private var headerHeight: Int = 0

    fun updateHeader(height: Int) {
        headerHeight = height
        notifyItemChanged(0)
    }

    class ViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
