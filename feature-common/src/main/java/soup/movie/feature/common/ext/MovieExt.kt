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
package soup.movie.feature.common.ext

import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.model.CgvInfo
import soup.movie.model.LotteInfo
import soup.movie.model.MegaboxInfo
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.feature.common.util.today

fun Movie.getDDay(): Long = openDate.toLocalDate()?.let {
    ChronoUnit.DAYS.between(today(), it)
} ?: 999

fun Movie.getDDayLabel(): String? = openDate.toLocalDate()?.let {
    val dDay = ChronoUnit.DAYS.between(today(), it)
    return when {
        dDay <= 0 -> "NOW"
        else -> "D-$dDay"
    }
}

fun Movie.hasOpenDate(): Boolean = openDate.toLocalDate() != null

fun Movie.isDDay(): Boolean = isPlan and hasOpenDate()

fun Movie.isBest(): Boolean {
    return theater.cgv.eggIsOver(96) or
        theater.lotte.starIsOver(8.8) or
        theater.megabox.starIsOver(8.5)
}

fun Movie.isNew(): Boolean = isNow and isInThePastWeek()

fun Movie.isInThePastWeek(): Boolean = isIn(-6..0)

fun Movie.isInTheThreeDays(): Boolean = isIn(-2..0)

fun Movie.isInTheNextWeek(): Boolean = isIn(0..6)

fun Movie.isIn(dayRange: IntRange): Boolean {
    val openDate = openDate.toLocalDate()
    if (openDate != null) {
        return ChronoUnit.DAYS.between(today(), openDate) in dayRange
    }
    return false
}

fun MovieDetail.screenDays(): Int {
    val openDate = openDate.toLocalDate()
    if (openDate != null) {
        return ChronoUnit.DAYS.between(openDate, today()).toInt()
    }
    return 0
}

/**
 * Valid format: YYYY.MM.DD
 */
private fun String.toLocalDate(): LocalDate? = split(".").let {
    return if (it.size == 3) {
        LocalDate.of(it[0].toInt(), it[1].toInt(), it[2].toInt())
    } else {
        null
    }
}

private fun CgvInfo?.eggIsOver(target: Int): Boolean {
    return this?.star.eggIsOver(target)
}

private fun String?.eggIsOver(target: Int): Boolean {
    return if (this == null || isBlank() || this == "?") {
        false
    } else {
        this >= target.toString()
    }
}

private fun LotteInfo?.starIsOver(target: Double): Boolean {
    return this?.star.starIsOver(target)
}

private fun MegaboxInfo?.starIsOver(target: Double): Boolean {
    return this?.star.starIsOver(target)
}

private fun String?.starIsOver(target: Double): Boolean {
    return if (this == null || isBlank()) {
        false
    } else {
        this >= target.toString()
    }
}
