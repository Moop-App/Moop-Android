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
package soup.movie.core.external

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import soup.movie.log.Logger

private fun Context.executePlayStoreForApp(pkgName: String) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$pkgName"),
            ),
        )
    } catch (e: ActivityNotFoundException) {
        Logger.w(e)
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$pkgName"),
            ),
        )
    }
}

object Moop {

    private const val packageName = "soup.movie"

    fun executePlayStore(ctx: Context) {
        ctx.executePlayStoreForApp(packageName)
    }
}

object Cgv {

    fun executeWeb(ctx: Context, theaterCode: String) {
        ctx.executeWeb(detailWebUrl(theaterCode))
    }

    private fun detailWebUrl(theaterCode: String): String =
        "https://m.cgv.co.kr/WebApp/TheaterV4/TheaterDetail.aspx?tc=$theaterCode"
}

object LotteCinema {

    fun executeWeb(ctx: Context, theaterCode: String) {
        ctx.executeWeb(detailWebUrl(theaterCode))
    }

    private fun detailWebUrl(theaterCode: String): String =
        "https://www.lottecinema.co.kr/NLCMW/Cinema/Detail?cinemaID=$theaterCode"
}

object Megabox {

    fun executeWeb(ctx: Context, theaterCode: String) {
        ctx.executeWeb(detailWebUrl(theaterCode))
    }

    private fun detailWebUrl(theaterCode: String): String =
        "https://m.megabox.co.kr/theater?brchNo=$theaterCode"
}

object YouTube {

    private const val packageName = "com.google.android.youtube"

    fun executeApp(ctx: Context, youtubeId: String) {
        try {
            ctx.startActivity(createTrailerAppIntent(youtubeId))
        } catch (e: ActivityNotFoundException) {
            Logger.w(e)
            ctx.startActivitySafely(
                createTrailerWebIntent(youtubeId),
            )
        }
    }

    private fun createTrailerAppIntent(id: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("vnd.youtube:$id"),
    ).apply {
        flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
        } else {
            Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    private fun createTrailerWebIntent(id: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.youtube.com/watch?v=$id"),
    )

    fun executeAppWithQuery(ctx: Context, movieTitle: String) {
        val query = "$movieTitle 예고편"
        try {
            ctx.startActivity(createSearchAppIntent(query))
        } catch (e: ActivityNotFoundException) {
            Logger.w(e)
            ctx.startActivitySafely(
                createSearchWebIntent(
                    query,
                ),
            )
        }
    }

    private fun createSearchAppIntent(query: String): Intent = Intent(
        Intent.ACTION_SEARCH,
    )
        .setPackage(packageName)
        .putExtra("query", query).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                    Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
            } else {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

    private fun createSearchWebIntent(query: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.youtube.com/results?search_query=$query"),
    )
}
