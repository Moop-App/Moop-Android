package soup.movie.ui.detail

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_trailer.view.*
import soup.movie.R
import soup.movie.data.model.Trailer
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.ImageUtil
import soup.movie.util.YouTubeUtil
import soup.movie.util.inflate

internal class DetailListAdapter(
        private val host: Context,
        private val timeTableView: View)
    : ListAdapter<Trailer, ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> TimeTableViewHolder(timeTableView)
            TYPE_TRAILER -> TrailerViewHolder(parent).also {
                it.itemView.setOnClickListener { _ ->
                    YouTubeUtil.executeYoutubeApp(host, getItem(it.adapterPosition).youtubeId)
                }
            }
            else -> throw IllegalStateException("The view type($viewType) is unknown.")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TrailerViewHolder -> {
                getItem(position - 1)?.let {
                    ImageUtil.loadAsync(host, holder.itemView.trailerThumbnailView, it.getThumbnailUrl())
                    holder.itemView.titleView.text = it.title
                    holder.itemView.authorView.text = it.author
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_TRAILER
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1

    private class TimeTableViewHolder(view: View) : ViewHolder(view)

    private class TrailerViewHolder(parent: ViewGroup) :
            ViewHolder(parent.inflate(R.layout.item_trailer))

    companion object {

        private const val TYPE_HEADER = 1
        private const val TYPE_TRAILER = 2
    }
}
