package soup.movie.ui.main.now

import android.util.Pair
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_movie_now.view.*
import soup.movie.R
import soup.movie.R.string.*
import soup.movie.data.helper.isBest
import soup.movie.data.helper.isNew
import soup.movie.data.model.Movie
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.with
import soup.widget.recyclerview.callback.AlwaysDiffCallback

class NowListAdapter(private val listener: (Int, Movie, Array<Pair<View, String>>) -> Unit) :
        DataBindingListAdapter<Movie>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Movie> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                val movie: Movie = getItem(adapterPosition)
                listener(adapterPosition, movie, createSharedElements(movie))
            }
        }
    }

    private fun DataBindingViewHolder<Movie>.createSharedElements(movie: Movie): Array<Pair<View, String>> {
        return when {
            movie.isNew() -> itemView.run {
                arrayOf(
                        backgroundView with transition_background,
                        posterView with transition_poster,
                        ageBgView with transition_age_bg,
                        newView with transition_new)
            }
            movie.isBest() -> itemView.run {
                arrayOf(
                        backgroundView with transition_background,
                        posterView with transition_poster,
                        ageBgView with transition_age_bg,
                        bestView with transition_best)
            }
            else -> itemView.run {
                arrayOf(
                        backgroundView with transition_background,
                        posterView with transition_poster,
                        ageBgView with transition_age_bg)
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_movie_now
}
