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
import soup.movie.domain.model.getAgeLabel
import soup.movie.data.model.Movie
import soup.movie.data.model.MovieId
import soup.movie.util.IntentUtil
import soup.movie.util.fromJson
import soup.movie.util.startActivitySafely
import soup.movie.util.toJson
import timber.log.Timber

object KakaoLink {

    private const val MOVIE_ID = "movieId"

    private val NoResponseCallback = object : ResponseCallback<KakaoLinkResponse>() {
        override fun onSuccess(result: KakaoLinkResponse) {}
        override fun onFailure(errorResult: ErrorResult) {
            Timber.e(errorResult.toString())
        }
    }

    fun extractMovieId(intent: Intent): MovieId? {
        return intent.data?.getQueryParameter(MOVIE_ID)?.fromJson()
    }

    fun share(context: Context, movie: Movie) {
        val movieLink = LinkObject.newBuilder()
            .setAndroidExecutionParams("$MOVIE_ID=${movie.toMovieId().toJson()}")
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

fun Context.share(movie: Movie) {
    startActivitySafely(IntentUtil.createShareIntent("영화 공유하기", movie.shareDescription))
}

private val Movie.shareDescription: String
    get() = "제목: $title\n개봉일: $openDate\n${getAgeLabel()}"
