package soup.movie.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_movie_vertical.view.*
import soup.movie.R
import soup.movie.data.getColorAsAge
import soup.movie.data.model.Movie
import soup.movie.ui.detail.DetailActivity
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.inflate
import soup.movie.util.loadAsync
import soup.movie.util.saveTo

class MovieListAdapter(
        private val host: FragmentActivity)
    : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
            MovieViewHolder(parent).also { it ->
                it.itemView.setOnClickListener { _ ->
                    val intent = Intent(host, DetailActivity::class.java).also { intent ->
                        getItem(it.adapterPosition).saveTo(intent)
                    }
                    val options = ActivityOptions.makeSceneTransitionAnimation(host,
                            Pair.create(it.itemView.backgroundView, host.getString(R.string.transition_background)),
                            Pair.create(it.itemView.posterView, host.getString(R.string.transition_poster)),
                            Pair.create(it.itemView.ageBgView, host.getString(R.string.transition_age_bg)),
                            Pair.create(it.itemView.ageBgOuterView, host.getString(R.string.transition_age_bg_outer)))
                    host.startActivity(intent, options.toBundle())
                }
            }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_movie_vertical)) {

        fun bindItem(movie: Movie) {
            val ctx = itemView.context
            itemView.posterView.loadAsync(movie.poster)
            itemView.ageBgView.backgroundTintList =
                    ContextCompat.getColorStateList(ctx, movie.getColorAsAge())
        }
    }
}
