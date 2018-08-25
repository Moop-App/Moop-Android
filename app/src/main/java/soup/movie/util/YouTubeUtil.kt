package soup.movie.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object YouTubeUtil {

    enum class Quality(val key: String) {
        LOW("default"),
        MEDIUM("mqdefault"),
        HIGH("hqdefault"),
        STANDARD("sddefault"),
        MAX("maxresdefault");
    }

    @JvmStatic
    @JvmOverloads
    fun getThumbnailUrl(id: String, quality: Quality = Quality.STANDARD): String {
        return String.format("https://img.youtube.com/vi/%s/%s.jpg", id, quality.key)
    }

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
