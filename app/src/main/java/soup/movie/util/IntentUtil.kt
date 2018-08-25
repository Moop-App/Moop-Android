package soup.movie.util

import android.content.Intent

object IntentUtil {

    @JvmStatic
    fun createShareIntentWithText(title: String, text: String): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, text)
                .setType("text/plain")
        return Intent.createChooser(shareIntent, title)
    }
}
