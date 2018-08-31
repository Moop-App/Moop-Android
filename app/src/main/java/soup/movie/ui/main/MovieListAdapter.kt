package soup.movie.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.item_movie_vertical.view.*
import soup.movie.R
import soup.movie.data.getColorAsAge
import soup.movie.data.model.Movie
import soup.movie.ui.databinding.DataBindingAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.ui.detail.DetailActivity
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.getColorStateListCompat
import soup.movie.util.loadAsync
import soup.movie.util.saveTo

class MovieListAdapter(private val host: FragmentActivity) :
        DataBindingAdapter<Movie>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Movie> =
            super.onCreateViewHolder(parent, viewType).apply {
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

    override fun onBindViewHolder(holder: DataBindingViewHolder<Movie>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.run {
            val item = getItem(position)
            posterView.loadAsync(item.poster)
            ageBgView.backgroundTintList = context.getColorStateListCompat(item.getColorAsAge())
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_movie_vertical
}
