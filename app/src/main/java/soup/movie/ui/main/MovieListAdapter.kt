package soup.movie.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_movie_vertical.view.*
import soup.movie.R
import soup.movie.data.getColorAsAge
import soup.movie.data.model.Movie
import soup.movie.ui.detail.DetailActivity
import soup.movie.util.*

class MovieListAdapter(
        private val host: FragmentActivity)
    : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
            MovieViewHolder(parent).apply {
                itemView.setOnClickListener { _ ->
                    val intent = Intent(host, DetailActivity::class.java).also {
                        getItem(adapterPosition).saveTo(it)
                    }
                    val options = ActivityOptions.makeSceneTransitionAnimation(host,
                            Pair.create(itemView.backgroundView, host.getString(R.string.transition_background)),
                            Pair.create(itemView.posterView, host.getString(R.string.transition_poster)),
                            Pair.create(itemView.ageBgView, host.getString(R.string.transition_age_bg)),
                            Pair.create(itemView.ageBgOuterView, host.getString(R.string.transition_age_bg_outer)))
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
            itemView.ageBgView.backgroundTintList = ctx.getColorStateListCompat(movie.getColorAsAge())
        }
    }
}
