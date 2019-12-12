package soup.movie.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.util.Pair
import androidx.recyclerview.widget.ext.IdBasedDiffCallback
import soup.movie.R
import soup.movie.data.model.Movie
import soup.movie.databinding.HomeItemMovieBinding
import soup.movie.domain.model.isBest
import soup.movie.domain.model.isDDay
import soup.movie.domain.model.isNew
import soup.movie.ui.databinding.DataBindingListAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.util.consume
import soup.movie.util.setOnDebounceClickListener
import soup.movie.util.showToast

class HomeListAdapter(
    context: Context,
    private val listener: (Movie, Array<Pair<View, String>>) -> Unit
) : DataBindingListAdapter<Movie>(IdBasedDiffCallback { it.moopId }) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = HomeItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(binding).apply {
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

    private fun MovieViewHolder.createSharedElements(movie: Movie): Array<Pair<View, String>> {
        itemView.run {
            val sharedElements = mutableListOf(
                backgroundView to R.string.transition_background,
                posterView to R.string.transition_poster,
                ageBgView to R.string.transition_age_bg
            )
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

    class MovieViewHolder(binding: HomeItemMovieBinding) : DataBindingViewHolder<Movie>(binding) {

        val backgroundView = binding.backgroundView
        val posterView = binding.posterView
        val ageBgView = binding.ageBgView.root
        val newView = binding.newView.root
        val bestView = binding.bestView.root
        val dDayView = binding.dDayView.root
    }
}
