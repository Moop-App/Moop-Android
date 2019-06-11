package soup.movie.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.util.Pair
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import kotlinx.android.synthetic.main.home_item_movie.view.*
import soup.movie.R
import soup.movie.domain.model.isBest
import soup.movie.domain.model.isDDay
import soup.movie.domain.model.isNew
import soup.movie.data.model.Movie
import soup.movie.ui.databinding.DataBindingListAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.util.consume
import soup.movie.util.setOnDebounceClickListener
import soup.movie.util.showToast

class HomeListAdapter(
    private val listener: (Movie, Array<Pair<View, String>>) -> Unit
) : DataBindingListAdapter<Movie>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Movie> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnDebounceClickListener(delay = 150L) {
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

    override fun getItemViewType(position: Int) = R.layout.home_item_movie

    private fun DataBindingViewHolder<Movie>.createSharedElements(movie: Movie): Array<Pair<View, String>> {
        itemView.run {
            val sharedElements = mutableListOf(
                backgroundView to R.string.transition_background,
                posterView to R.string.transition_poster,
                ageBgView to R.string.transition_age_bg)
            if (movie.isNew()) {
                sharedElements.add(newView to R.string.transition_new)
            }
            if (movie.isBest()) {
                sharedElements.add(bestView to R.string.transition_best)
            }
            if (movie.isDDay()) {
                sharedElements.add(dDayView to R.string.transition_d_day)
            }
            return sharedElements.toTypedArray()
        }
    }

    private infix fun View.to(@StringRes tagId: Int): Pair<View, String> {
        return Pair(this, context.getString(tagId))
    }
}
