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
package soup.movie.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import soup.movie.ext.executeWeb
import soup.movie.ext.startActivitySafely
import soup.movie.model.Theater
import soup.movie.model.Trailer
import timber.log.Timber

private fun Context.isInstalledApp(pkgName: String): Boolean {
    return packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        .find { it?.packageName == pkgName } != null
}

private fun Context.executeMarketApp(pkgName: String, className: String? = null) {
    if (executeApp(pkgName, className).not()) {
        executePlayStoreForApp(pkgName)
    }
}

private fun Context.executeApp(pkgName: String, className: String? = null): Boolean {
    if (className != null) {
        val launchIntent = Intent().apply {
            setClassName(pkgName, className)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(launchIntent)
            return true
        } catch (e: ActivityNotFoundException) {
            Timber.w(e)
        }
    }
    val launchIntent = packageManager.getLaunchIntentForPackage(pkgName)
    if (launchIntent != null) {
        startActivity(launchIntent)
        return true
    }
    return false
}

private fun Context.executePlayStoreForApp(pkgName: String) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$pkgName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        Timber.w(e)
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$pkgName")
            )
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

    private const val packageName = "com.cgv.android.movieapp"

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    private fun detailWebUrl(theaterCode: String): String =
        "https://m.cgv.co.kr/WebApp/TheaterV4/TheaterDetail.aspx?tc=$theaterCode"
}

object LotteCinema {

    private const val packageName = "kr.co.lottecinema.lcm"

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    private fun detailWebUrl(theaterCode: String): String =
        "https://www.lottecinema.co.kr/NLCMW/Cinema/Detail?cinemaID=$theaterCode"
}

object Megabox {

    private const val packageName = "com.megabox.mop"

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    private fun detailWebUrl(theaterCode: String): String =
        "https://m.megabox.co.kr/theater?brchNo=$theaterCode"
}

object Kakao {

    private const val packageName = "com.kakao.talk"
}

object YouTube {

    private const val packageName = "com.google.android.youtube"

    fun executeApp(ctx: Context, trailer: Trailer) {
        val id = trailer.youtubeId
        try {
            ctx.startActivity(createTrailerAppIntent(id))
        } catch (e: ActivityNotFoundException) {
            Timber.w(e)
            ctx.startActivitySafely(
                createTrailerWebIntent(
                    id
                )
            )
        }
    }

    private fun createTrailerAppIntent(id: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("vnd.youtube:$id")
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
        } else {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    private fun createTrailerWebIntent(id: String): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.youtube.com/watch?v=$id")
    )

    fun executeAppWithQuery(ctx: Context, movieTitle: String) {
        val query = "$movieTitle 예고편"
        try {
            ctx.startActivity(createSearchAppIntent(query))
        } catch (e: ActivityNotFoundException) {
            Timber.w(e)
            ctx.startActivitySafely(
                createSearchWebIntent(
                    query
                )
            )
        }
    }

    private fun createSearchAppIntent(query: String): Intent = Intent(
        Intent.ACTION_SEARCH
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
        Uri.parse("https://www.youtube.com/results?search_query=$query")
    )
}
