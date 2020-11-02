package soup.movie.spec

import android.content.Context
import android.content.Intent
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import soup.movie.ext.getAgeLabel
import soup.movie.model.Movie
import timber.log.Timber

object KakaoLink {

    private const val MOVIE_ID = "movieId"

    fun extractMovieId(intent: Intent): String? {
        return if (intent.action == Intent.ACTION_VIEW) {
            intent.data?.getQueryParameter(MOVIE_ID)
        } else {
            null
        }
    }

    fun share(context: Context, movie: Movie) {
        val defaultFeed = FeedTemplate(
            Content(
                title = movie.title,
                description = movie.description,
                imageUrl = movie.posterUrl,
                link = Link(
                    androidExecParams = mapOf(MOVIE_ID to movie.id)
                )
            )
        )
        LinkClient.instance.defaultTemplate(context, defaultFeed) { linkResult, error ->
            if (error != null) {
                Timber.e(error)
            } else if (linkResult != null) {
                context.startActivity(linkResult.intent)
            }
        }
    }

    private val Movie.description: String
        get() = "$openDate / ${getAgeLabel()}"
}
