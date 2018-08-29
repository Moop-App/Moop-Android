package soup.movie.ui.detail

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_trailer.view.*
import soup.movie.R
import soup.movie.data.model.Trailer
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.ImageUtil
import soup.movie.util.YouTubeUtil
import soup.movie.util.inflate

internal class DetailListAdapter(private val ctx: Context)
    : ListAdapter<Trailer, ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return TrailerViewHolder(parent).also {
            it.itemView.setOnClickListener { _ ->
                YouTubeUtil.executeYoutubeApp(ctx, getItem(it.adapterPosition).youtubeId)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position - 1)?.let {
            ImageUtil.loadAsync(ctx, holder.itemView.trailerThumbnailView, it.getThumbnailUrl())
            holder.itemView.titleView.text = it.title
            holder.itemView.authorView.text = it.author
        }
    }

    private class TrailerViewHolder(parent: ViewGroup) :
            ViewHolder(parent.inflate(R.layout.item_trailer))
}
