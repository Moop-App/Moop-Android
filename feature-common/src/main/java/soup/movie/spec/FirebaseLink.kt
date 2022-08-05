/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.spec

import android.content.Intent
import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import soup.movie.ext.getAgeLabel
import soup.movie.model.Movie
import timber.log.Timber

object FirebaseLink {

    private const val PATH_DETAIL = "detail"
    private const val MOVIE_ID = "movieId"

    fun extractMovieId(intent: Intent?, onResult: (String?) -> Unit) {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener {
                val deepLink = it?.link
                if (deepLink?.lastPathSegment == PATH_DETAIL) {
                    onResult(deepLink.getQueryParameter(MOVIE_ID))
                    return@addOnSuccessListener
                }
                onResult(null)
            }
            .addOnFailureListener {
                Timber.w(it)
                onResult(null)
            }
    }

    fun createDetailLink(movie: Movie, onResult: (Uri?) -> Unit) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://moop.link/$PATH_DETAIL?$MOVIE_ID=${movie.id}"))
            .setDomainUriPrefix("https://moop.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("soup.movie")
                    .setMinimumVersion(92)
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder("com.kor45cw.Moop")
                    .build()
            )
            .setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setImageUrl(Uri.parse(movie.posterUrl))
                    .setTitle(movie.title)
                    .setDescription(movie.description)
                    .build()
            )
            .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
            .addOnSuccessListener {
                it.shortLink?.let { link -> onResult(link) }
            }
            .addOnFailureListener {
                Timber.w(it)
            }
    }

    private val Movie.description: String
        get() {
            return buildString {
                if (isNow) {
                    append("현재상영중")
                } else {
                    append("${openDate}개봉")
                }
                append(" / ${getAgeLabel()}")
                genres?.let { genres ->
                    append(" / ${genres.joinToString()}")
                }
            }
        }
}
