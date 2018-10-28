package soup.movie.ui.main.movie

import android.util.Pair
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_movie.view.*
import soup.movie.R
import soup.movie.R.string.*
import soup.movie.data.helper.isBest
import soup.movie.data.helper.isDDay
import soup.movie.data.helper.isNew
import soup.movie.data.model.Movie
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.setOnDebounceClickListener
import soup.movie.util.with
import soup.widget.recyclerview.callback.AlwaysDiffCallback

class MovieListAdapter(private val listener: (Int, Movie, Array<Pair<View, String>>) -> Unit) :
        DataBindingListAdapter<Movie>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Movie> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnDebounceClickListener {
                val index = adapterPosition
                if (index in 0..itemCount) {
                    val movie: Movie = getItem(index)
                    listener(index, movie, createSharedElements(movie))
                }
            }
        }
    }

    private fun DataBindingViewHolder<Movie>.createSharedElements(movie: Movie): Array<Pair<View, String>> {
        itemView.run {
            val sharedElements = mutableListOf(
                    backgroundView with transition_background,
                    posterView with transition_poster,
                    ageBgView with transition_age_bg)
            if (movie.isNew()) {
                sharedElements.add(newView with transition_new)
            }
            if (movie.isBest()) {
                sharedElements.add(bestView with transition_best)
            }
            if (movie.isDDay()) {
                sharedElements.add(dDayView with transition_d_day)
            }
            return sharedElements.toTypedArray()
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_movie
}
