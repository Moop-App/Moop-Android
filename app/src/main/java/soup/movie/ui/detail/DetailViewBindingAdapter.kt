package soup.movie.ui.detail

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import soup.movie.R

/**
 * TOMATOMETER: https://www.rottentomatoes.com/about
 */
@BindingAdapter("tomatoMeterIcon")
fun setTomatoMeterIcon(view: ImageView, rottenTomatoes: String) {
    if (rottenTomatoes.contains('%')) {
        val score = rottenTomatoes.substring(0, rottenTomatoes.lastIndex).toIntOrNull() ?: 0
        if (score >= 60) {
            view.setImageResource(R.drawable.ic_rt_fresh)
        } else {
            view.setImageResource(R.drawable.ic_rt_rotten)
        }
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("submitPersonList")
fun submitPersonList(view: RecyclerView, items: List<PersonUiModel>) {
    view.adapter = DetailPersonListAdapter().apply {
        submitList(items)
    }
}
