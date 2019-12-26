package soup.movie.domain.model

import androidx.annotation.DrawableRes
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.R
import soup.movie.data.model.*
import soup.movie.util.helper.today

@DrawableRes
fun Movie.getAgeBackground(): Int = when {
    age >= 19 -> R.drawable.bg_tag_age_19
    age >= 15 -> R.drawable.bg_tag_age_15
    age >= 12 -> R.drawable.bg_tag_age_12
    age >= 0 -> R.drawable.bg_tag_age_all
    else -> R.drawable.bg_tag_age_unknown
}

fun Movie.getSimpleAgeLabel(): String = when {
    age >= 19 -> "청불"
    age >= 15 -> "15"
    age >= 12 -> "12"
    age >= 0 -> "전체"
    else -> "미정"
}

fun Movie.getAgeLabel(): String = when {
    age >= 19 -> "청소년관람불가"
    age >= 15 -> "15세관람가"
    age >= 12 -> "12세관람가"
    age >= 0 -> "전체관람가"
    else -> "등급 미지정"
}

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

fun Movie.isBest(): Boolean =
        theater.cgv.eggIsOver(96) or
        theater.lotte.starIsOver(8.8) or
        theater.megabox.starIsOver(8.5)

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
