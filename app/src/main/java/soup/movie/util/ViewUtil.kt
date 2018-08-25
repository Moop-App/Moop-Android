package soup.movie.util

import android.view.View

object ViewUtil {

    @JvmStatic
    fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    @JvmStatic
    fun hide(view: View) {
        view.visibility = View.GONE
    }
}
