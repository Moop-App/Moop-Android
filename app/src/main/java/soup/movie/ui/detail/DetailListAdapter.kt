package soup.movie.ui.detail

import android.content.Context
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_trailer.view.*
import soup.movie.R
import soup.movie.data.model.Trailer
import soup.movie.ui.helper.databinding.DataBindingAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.executeYoutube
import soup.movie.util.loadAsync

internal class DetailListAdapter(private val ctx: Context) :
        DataBindingAdapter<Trailer>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Trailer> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                ctx.executeYoutube(getItem(adapterPosition).youtubeId)
            }
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<Trailer>, position: Int) {
        val trailer = getItem(position)
        holder.itemView.apply {
            trailerThumbnailView.loadAsync(trailer.getThumbnailUrl())
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_trailer
}
