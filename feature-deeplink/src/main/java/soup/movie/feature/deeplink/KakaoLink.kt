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
package soup.movie.feature.deeplink

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import soup.movie.log.Logger

// TODO: Make this class to private
object KakaoLink {

    private const val MOVIE_ID = "movieId"

    fun extractMovieId(intent: Intent?): String? {
        return if (intent?.action == Intent.ACTION_VIEW) {
            intent.data?.getQueryParameter(MOVIE_ID)
        } else {
            null
        }
    }

    fun share(
        context: Context,
        movieId: String,
        imageUrl: String,
        title: String,
        description: String,
    ) {
        val defaultFeed = FeedTemplate(
            Content(
                title = title,
                description = description,
                imageUrl = imageUrl,
                link = Link(
                    androidExecutionParams = mapOf(MOVIE_ID to movieId)
                )
            )
        )
        ShareClient.instance.shareDefault(context, defaultFeed) { sharingResult, error ->
            if (error != null) {
                Toast.makeText(context, "실행할 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                Logger.e(error)
            } else if (sharingResult != null) {
                context.startActivity(sharingResult.intent)
            }
        }
    }
}
