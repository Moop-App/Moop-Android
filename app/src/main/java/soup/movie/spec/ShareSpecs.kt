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
import soup.movie.data.helper.Cgv
import soup.movie.data.helper.Kakao
import soup.movie.data.helper.toDescription
import soup.movie.data.helper.toShareDescription
import soup.movie.data.model.Movie
import soup.movie.data.model.MovieId
import soup.movie.util.IntentUtil
import soup.movie.util.fromJson
import soup.movie.util.startActivitySafely
import soup.movie.util.toJson
import timber.log.Timber

object ShareSpecs {

    internal const val MOVIE_ID = "movieId"

    fun extractMovieId(intent: Intent): MovieId? {
        return intent.data?.getQueryParameter(MOVIE_ID)?.fromJson()
    }
}

fun Context.share(movie: Movie) {
    if (Kakao.isInstalled(this)) {
        val params = FeedTemplate.newBuilder(
                ContentObject.newBuilder(movie.title, movie.posterUrl,
                        LinkObject.newBuilder()
                                .setWebUrl(Cgv.detailWebUrl(movie))
                                .setMobileWebUrl(Cgv.detailMobileWebUrl(movie))
                                .setAndroidExecutionParams("${ShareSpecs.MOVIE_ID}=${movie.toMovieId().toJson()}")
                                .build())
                        .setDescrption(movie.toDescription())
                        .build())
                .build()
        KakaoLinkService.getInstance().sendDefault(this, params,
                object : ResponseCallback<KakaoLinkResponse>() {
                    override fun onFailure(errorResult: ErrorResult) {
                        Timber.e(errorResult.toString())
                    }

                    override fun onSuccess(result: KakaoLinkResponse) {}
                })
    } else {
        startActivitySafely(IntentUtil.createShareIntent("공유하기", movie.toShareDescription()))
    }
}
