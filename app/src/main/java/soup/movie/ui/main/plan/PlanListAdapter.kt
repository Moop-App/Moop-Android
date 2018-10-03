package soup.movie.ui.main.plan

import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.item_movie_plan.view.*
import soup.movie.R
import soup.movie.R.string.*
import soup.movie.data.helper.saveTo
import soup.movie.data.helper.showDDay
import soup.movie.data.model.Movie
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.helper.EventAnalytics
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.with
import soup.widget.recyclerview.callback.AlwaysDiffCallback

class PlanListAdapter(private val host: FragmentActivity,
                      private val analytics: EventAnalytics) :
        DataBindingListAdapter<Movie>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Movie> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener { _ ->
                val intent = Intent(host, DetailActivity::class.java)
                val movie: Movie = getItem(adapterPosition).apply {
                    saveTo(intent)
                }
                analytics.clickItem(adapterPosition, movie)
                host.startActivity(intent, ActivityOptions
                        .makeSceneTransitionAnimation(host, *createSharedElements(movie))
                        .toBundle())
            }
        }
    }

    private fun DataBindingViewHolder<Movie>.createSharedElements(movie: Movie): Array<Pair<View, String>> {
        return if (movie.showDDay()) itemView.run {
            arrayOf(
                    backgroundView with transition_background,
                    posterView with transition_poster,
                    ageBgView with transition_age_bg,
                    dDayView with transition_d_day)
        } else itemView.run {
            arrayOf(
                    backgroundView with transition_background,
                    posterView with transition_poster,
                    ageBgView with transition_age_bg)
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_movie_plan
}
