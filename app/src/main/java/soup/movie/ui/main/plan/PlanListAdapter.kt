package soup.movie.ui.main.plan

import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.item_movie_plan.view.*
import soup.movie.R
import soup.movie.data.helper.hasOpenDate
import soup.movie.data.helper.saveTo
import soup.movie.data.model.Movie
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.widget.recyclerview.callback.AlwaysDiffCallback

class PlanListAdapter(private val host: FragmentActivity) :
        DataBindingListAdapter<Movie>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Movie> =
            super.onCreateViewHolder(parent, viewType).apply {
                itemView.setOnClickListener { _ ->
                    val intent = Intent(host, DetailActivity::class.java)
                    val movie: Movie = getItem(adapterPosition).apply {
                        saveTo(intent)
                    }
                    host.startActivity(intent, options(movie).toBundle())
                }
            }

    private fun DataBindingViewHolder<Movie>.options(movie: Movie): ActivityOptions {
        return if (movie.hasOpenDate()) {
            ActivityOptions.makeSceneTransitionAnimation(host,
                    Pair.create(itemView.backgroundView, host.getString(R.string.transition_background)),
                    Pair.create(itemView.posterView, host.getString(R.string.transition_poster)),
                    Pair.create(itemView.ageBgView, host.getString(R.string.transition_age_bg)),
                    Pair.create(itemView.dDayView, host.getString(R.string.transition_d_day)))
        } else {
            ActivityOptions.makeSceneTransitionAnimation(host,
                    Pair.create(itemView.backgroundView, host.getString(R.string.transition_background)),
                    Pair.create(itemView.posterView, host.getString(R.string.transition_poster)),
                    Pair.create(itemView.ageBgView, host.getString(R.string.transition_age_bg)))
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_movie_plan
}
