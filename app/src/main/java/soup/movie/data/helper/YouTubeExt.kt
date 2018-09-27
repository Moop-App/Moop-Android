package soup.movie.data.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import soup.movie.data.model.Movie
import soup.movie.data.model.Trailer
import soup.movie.util.startActivitySafely

fun Context.executeYouTube(trailer: Trailer) {
    val id = trailer.youtubeId
    try {
        startActivity(createTrailerAppIntent(id))
    } catch (e: ActivityNotFoundException) {
        startActivitySafely(createTrailerWebIntent(id))
    }
}

private fun createTrailerAppIntent(id: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("vnd.youtube:$id"))

private fun createTrailerWebIntent(id: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://www.youtube.com/watch?v=$id"))

fun Context.executeYouTube(movie: Movie) {
    val query = "${movie.title} 예고편"
    try {
        startActivity(createSearchAppIntent(query))
    } catch (e: ActivityNotFoundException) {
        startActivitySafely(createSearchWebIntent(query))
    }
}

private fun createSearchAppIntent(query: String): Intent =
        Intent(Intent.ACTION_SEARCH)
                .setPackage(YouTube.PACKAGE_NAME)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("query", query)

private fun createSearchWebIntent(query: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.youtube.com/results?search_query=$query"))
