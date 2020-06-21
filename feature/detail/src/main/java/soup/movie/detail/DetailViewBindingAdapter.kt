package soup.movie.detail

import android.view.View
import android.widget.ImageView

/**
 * TOMATOMETER: https://www.rottentomatoes.com/about
 */
fun ImageView.setTomatoMeterIcon(rottenTomatoes: String) {
    if (rottenTomatoes.contains('%')) {
        val score = rottenTomatoes.substring(0, rottenTomatoes.lastIndex).toIntOrNull() ?: 0
        if (score >= 60) {
            setImageResource(R.drawable.ic_rt_fresh)
        } else {
            setImageResource(R.drawable.ic_rt_rotten)
        }
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}
