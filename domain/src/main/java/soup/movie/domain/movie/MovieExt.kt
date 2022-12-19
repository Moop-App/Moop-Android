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
package soup.movie.domain.movie

import soup.movie.model.CgvInfoModel
import soup.movie.model.LotteInfoModel
import soup.movie.model.MegaboxInfoModel
import soup.movie.model.MovieDetailModel
import soup.movie.model.MovieModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun MovieModel.getDDay(): Long = openDate.toLocalDate()?.let {
    ChronoUnit.DAYS.between(today(), it)
} ?: 999

fun MovieModel.getDDayLabel(): String? = openDate.toLocalDate()?.let {
    val dDay = ChronoUnit.DAYS.between(today(), it)
    return when {
        dDay <= 0 -> "NOW"
        else -> "D-$dDay"
    }
}

private fun MovieModel.hasOpenDate(): Boolean = openDate.toLocalDate() != null

fun MovieModel.isDDay(): Boolean = isPlan and hasOpenDate()

fun MovieModel.isBest(): Boolean {
    return theater.cgv.eggIsOver(96) or
        theater.lotte.starIsOver(8.8) or
        theater.megabox.starIsOver(8.5)
}

fun MovieModel.isNew(): Boolean = isNow and isInThePastWeek()

fun MovieModel.isInThePastWeek(): Boolean = isIn(-6..0)

fun MovieModel.isIn(dayRange: IntRange): Boolean {
    val openDate = openDate.toLocalDate()
    if (openDate != null) {
        return ChronoUnit.DAYS.between(today(), openDate) in dayRange
    }
    return false
}

fun MovieDetailModel.screenDays(): Int {
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

private fun CgvInfoModel?.eggIsOver(target: Int): Boolean {
    return this?.star.eggIsOver(target)
}

private fun String?.eggIsOver(target: Int): Boolean {
    return if (this == null || isBlank() || this == "?") {
        false
    } else {
        this >= target.toString()
    }
}

private fun LotteInfoModel?.starIsOver(target: Double): Boolean {
    return this?.star.starIsOver(target)
}

private fun MegaboxInfoModel?.starIsOver(target: Double): Boolean {
    return this?.star.starIsOver(target)
}

private fun String?.starIsOver(target: Double): Boolean {
    return if (this == null || isBlank()) {
        false
    } else {
        this >= target.toString()
    }
}
