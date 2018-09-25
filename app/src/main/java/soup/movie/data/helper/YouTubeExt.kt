package soup.movie.data.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import soup.movie.data.model.Trailer
import soup.movie.util.startActivitySafely

fun Context.executeYoutube(trailer: Trailer) {
    try {
        startActivity(createAppIntent(trailer.youtubeId))
    } catch (e: ActivityNotFoundException) {
        startActivitySafely(createWebIntent(trailer.youtubeId))
    }
}

private fun createAppIntent(id: String): Intent =
        Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))

private fun createWebIntent(id: String): Intent =
        Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
