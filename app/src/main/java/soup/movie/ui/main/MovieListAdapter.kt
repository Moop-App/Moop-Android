package soup.movie.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.support.annotation.ColorRes
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import soup.movie.R
import soup.movie.data.model.Movie
import soup.movie.ui.detail.DetailActivity
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.ImageUtil
import soup.movie.util.MovieUtil


class MovieListAdapter(
        private val host: FragmentActivity)
    : ListAdapter<Movie, MovieListAdapter.ViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_vertical, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val intent = Intent(host, DetailActivity::class.java)
            MovieUtil.saveTo(intent, getItem(holder.adapterPosition))
            val options = ActivityOptions.makeSceneTransitionAnimation(host,
                    Pair.create(holder.backgroundView, host.getString(R.string.transition_background)),
                    Pair.create(holder.posterView, host.getString(R.string.transition_poster)),
                    Pair.create(holder.ageBgView, host.getString(R.string.transition_age_bg)),
                    Pair.create(holder.ageBgOuterView, host.getString(R.string.transition_age_bg_outer)))
            host.startActivity(intent, options.toBundle())
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.background)
        lateinit var backgroundView: View
        @BindView(R.id.movie_poster)
        lateinit var posterView: ImageView
        @BindView(R.id.age_bg)
        lateinit var ageBgView: View
        @BindView(R.id.age_bg_outer)
        lateinit var ageBgOuterView: View

        init {
            ButterKnife.bind(this, view)
        }

        fun bindItem(movie: Movie) {
            val ctx = itemView.context
            ImageUtil.loadAsync(ctx, posterView, movie.poster)
            updateAgeText(ageBgView, movie.age)
        }

        private fun updateAgeText(ageBgView: View, ageText: String) {
            if (TextUtils.isEmpty(ageText)) {
                ageBgView.visibility = View.GONE
            } else {
                ageBgView.backgroundTintList =
                        ContextCompat.getColorStateList(ageBgView.context, getColorAsAge(ageText))
                ageBgView.visibility = View.VISIBLE
            }
        }

        @ColorRes
        private fun getColorAsAge(age: String): Int {
            return when (age) {
                "전체 관람가" -> R.color.green
                "12세 관람가" -> R.color.blue
                "15세 관람가" -> R.color.amber
                "청소년관람불가" -> R.color.red
                else -> R.color.grey
            }
        }
    }
}
