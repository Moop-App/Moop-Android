package soup.movie.ui.main.home

import android.util.Pair
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import kotlinx.android.synthetic.main.item_movie.view.*
import soup.movie.R
import soup.movie.data.helper.isBest
import soup.movie.data.helper.isDDay
import soup.movie.data.helper.isNew
import soup.movie.data.model.Movie
import soup.movie.ui.databinding.DataBindingListAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.util.consume
import soup.movie.util.setOnDebounceClickListener
import soup.movie.util.showToast
import soup.movie.util.with

class HomeListAdapter(
    private val listener: (Movie, Array<Pair<View, String>>) -> Unit
) : DataBindingListAdapter<Movie>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Movie> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnDebounceClickListener {
                val index = adapterPosition
                if (adapterPosition in 0..itemCount) {
                    val movie: Movie = getItem(index)
                    listener(movie, createSharedElements(movie))
                }
            }
            itemView.setOnLongClickListener {
                consume {
                    val index = adapterPosition
                    if (index in 0..itemCount) {
                        val movie: Movie = getItem(index)
                        it?.context?.showToast(movie.title)
                    }
                }
            }
        }
    }

    private fun DataBindingViewHolder<Movie>.createSharedElements(movie: Movie): Array<Pair<View, String>> {
        itemView.run {
            val sharedElements = mutableListOf(
                backgroundView with R.string.transition_background,
                posterView with R.string.transition_poster,
                ageBgView with R.string.transition_age_bg)
            if (movie.isNew()) {
                sharedElements.add(newView with R.string.transition_new)
            }
            if (movie.isBest()) {
                sharedElements.add(bestView with R.string.transition_best)
            }
            if (movie.isDDay()) {
                sharedElements.add(dDayView with R.string.transition_d_day)
            }
            return sharedElements.toTypedArray()
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_movie
}
