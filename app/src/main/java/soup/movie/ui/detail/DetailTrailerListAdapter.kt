package soup.movie.ui.detail

import android.view.ViewGroup
import soup.movie.R
import soup.movie.data.helper.executeYouTube
import soup.movie.data.model.Trailer
import soup.movie.ui.helper.EventAnalytics
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.widget.recyclerview.callback.AlwaysDiffCallback

internal class DetailTrailerListAdapter(private val analytics: EventAnalytics) :
        DataBindingListAdapter<Trailer>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Trailer> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                val trailer = getItem(adapterPosition)
                analytics.clickItem(trailer)
                it.context.executeYouTube(trailer)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_trailer
}
