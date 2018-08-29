package soup.movie.util

import android.content.Intent

object IntentUtil {

    fun createShareIntentWithText(title: String, text: String): Intent =
            Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, text)
                    .setType("text/plain")
                    .run { Intent.createChooser(this, title) }
}
