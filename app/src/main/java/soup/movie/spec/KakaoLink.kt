package soup.movie.spec

import android.content.Context
import android.content.Intent
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import soup.movie.model.Movie
import soup.movie.domain.model.getAgeLabel
import timber.log.Timber

object KakaoLink {

    private const val MOVIE_ID = "movieId"

    private val NoResponseCallback = object : ResponseCallback<KakaoLinkResponse>() {
        override fun onSuccess(result: KakaoLinkResponse) {}
        override fun onFailure(errorResult: ErrorResult) {
            Timber.e(errorResult.toString())
        }
    }

    fun extractMovieId(intent: Intent): String? {
        return if (intent.action == Intent.ACTION_VIEW) {
            intent.data?.getQueryParameter(MOVIE_ID)
        } else {
            null
        }
    }

    fun share(context: Context, movie: Movie) {
        val movieLink = LinkObject.newBuilder()
            .setAndroidExecutionParams("$MOVIE_ID=${movie.id}")
            .build()
        val params = FeedTemplate.newBuilder(
            ContentObject.newBuilder(movie.title, movie.posterUrl, movieLink)
                .setDescrption(movie.description)
                .build())
            .build()
        KakaoLinkService.getInstance().sendDefault(context, params, NoResponseCallback)
    }

    private val Movie.description: String
        get() = "$openDate / ${getAgeLabel()}"
}
