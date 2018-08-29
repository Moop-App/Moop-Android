package soup.movie.ui.detail

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_trailer.view.*
import soup.movie.R
import soup.movie.data.model.Trailer
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.executeYoutube
import soup.movie.util.inflate
import soup.movie.util.loadAsync

internal class DetailListAdapter(private val ctx: Context)
    : ListAdapter<Trailer, ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            TrailerViewHolder(parent).also {
                it.itemView.setOnClickListener { _ ->
                    ctx.executeYoutube(getItem(it.adapterPosition).youtubeId)
                }
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trailer = getItem(position)
        holder.itemView.let {
            it.trailerThumbnailView.loadAsync(trailer.getThumbnailUrl())
            it.titleView.text = trailer.title
            it.authorView.text = trailer.author
        }
    }

    private class TrailerViewHolder(parent: ViewGroup) :
            ViewHolder(parent.inflate(R.layout.item_trailer))
}
