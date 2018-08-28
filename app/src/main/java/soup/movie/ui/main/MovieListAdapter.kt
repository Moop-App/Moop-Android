package soup.movie.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_movie_vertical.view.*
import soup.movie.R
import soup.movie.data.getColorAsAge
import soup.movie.data.model.Movie
import soup.movie.ui.detail.DetailActivity
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.ImageUtil
import soup.movie.util.MovieUtil

class MovieListAdapter(
        private val host: FragmentActivity)
    : ListAdapter<Movie, MovieListAdapter.ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_vertical, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val intent = Intent(host, DetailActivity::class.java)
            MovieUtil.saveTo(intent, getItem(holder.adapterPosition))
            val options = ActivityOptions.makeSceneTransitionAnimation(host,
                    Pair.create(holder.itemView.backgroundView, host.getString(R.string.transition_background)),
                    Pair.create(holder.itemView.posterView, host.getString(R.string.transition_poster)),
                    Pair.create(holder.itemView.ageBgView, host.getString(R.string.transition_age_bg)),
                    Pair.create(holder.itemView.ageBgOuterView, host.getString(R.string.transition_age_bg_outer)))
            host.startActivity(intent, options.toBundle())
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(movie: Movie) {
            val ctx = itemView.context
            ImageUtil.loadAsync(ctx, itemView.posterView, movie.poster)
            itemView.ageBgView.backgroundTintList =
                    ContextCompat.getColorStateList(ctx, movie.getColorAsAge())
        }
    }
}
