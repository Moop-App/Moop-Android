package soup.movie.ui.detail

import android.view.ViewGroup
import soup.movie.R
import soup.movie.data.helper.YouTube
import soup.movie.data.model.Trailer
import soup.movie.ui.helper.EventAnalytics
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.setOnDebounceClickListener
import soup.widget.recyclerview.callback.AlwaysDiffCallback

internal class DetailTrailerListAdapter(private val analytics: EventAnalytics) :
        DataBindingListAdapter<Trailer>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Trailer> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnDebounceClickListener {
                val trailer = getItem(adapterPosition)
                analytics.clickItem(trailer)
                YouTube.executeApp(it.context, trailer)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_detail_trailer
}
