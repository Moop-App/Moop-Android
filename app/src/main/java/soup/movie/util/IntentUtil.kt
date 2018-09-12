package soup.movie.util

import android.content.Intent

object IntentUtil {

    fun createShareIntent(title: String, text: String): Intent =
            Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, text)
                    .setType("text/plain")
                    .let { Intent.createChooser(it, title) }
}
