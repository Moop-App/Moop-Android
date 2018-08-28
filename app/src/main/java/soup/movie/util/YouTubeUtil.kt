package soup.movie.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object YouTubeUtil {

    @JvmStatic
    fun executeYoutubeApp(context: Context, id: String) {
        try {
            context.startActivity(createAppIntent(id))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(createWebIntent(id))
        }
    }

    private fun createAppIntent(id: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
    }

    private fun createWebIntent(id: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
    }
}
