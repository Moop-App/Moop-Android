package soup.movie.ui.detail

import android.content.Context
import android.view.ViewGroup
import soup.movie.R
import soup.movie.data.helper.executeYoutube
import soup.movie.data.model.Trailer
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.widget.recyclerview.callback.AlwaysDiffCallback

internal class DetailListAdapter(private val ctx: Context) :
        DataBindingListAdapter<Trailer>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Trailer> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                ctx.executeYoutube(getItem(adapterPosition))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_trailer
}
