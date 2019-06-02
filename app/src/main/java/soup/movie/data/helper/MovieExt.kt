package soup.movie.data.helper

import androidx.annotation.DrawableRes
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.R
import soup.movie.data.model.CgvInfo
import soup.movie.data.model.LotteInfo
import soup.movie.data.model.MegaboxInfo
import soup.movie.data.model.Movie
import soup.movie.util.helper.today

@DrawableRes
fun Movie.getAgeBackground(): Int = when {
    ageValue >= 19 -> R.drawable.bg_tag_age_19
    ageValue >= 15 -> R.drawable.bg_tag_age_15
    ageValue >= 12 -> R.drawable.bg_tag_age_12
    ageValue >= 0 -> R.drawable.bg_tag_age_all
    else -> R.drawable.bg_tag_age_unknown
}

fun Movie.getSimpleAgeLabel(): String = when {
    ageValue >= 19 -> "청불"
    ageValue >= 15 -> "15"
    ageValue >= 12 -> "12"
    ageValue >= 0 -> "전체"
    else -> "미정"
}

fun Movie.getAgeLabel(): String = when {
    ageValue >= 19 -> "청소년관람불가"
    ageValue >= 15 -> "15세관람가"
    ageValue >= 12 -> "12세관람가"
    ageValue >= 0 -> "전체관람가"
    else -> "등급 미지정"
}

fun Movie.getDDay(): Long = openDate()?.let {
    ChronoUnit.DAYS.between(today(), it)
} ?: 999

fun Movie.getDDayLabel(): String? = openDate()?.let {
    val dDay = ChronoUnit.DAYS.between(today(), it)
    return when {
        dDay <= 0 -> "NOW"
        else -> "D-$dDay"
    }
}

fun Movie.hasOpenDate(): Boolean = openDate() != null

fun Movie.isDDay(): Boolean = isPlan and hasOpenDate()

fun Movie.isBest(): Boolean =
        cgv.eggIsOver(96) or
        lotte.starIsOver(8.8) or
        megabox.starIsOver(8.5)

fun Movie.isNew(): Boolean = isNow and isInThePastWeek()

fun Movie.isInThePastWeek(): Boolean = isIn(-6..0)

fun Movie.isInTheThreeDays(): Boolean = isIn(-2..0)

fun Movie.isInTheNextWeek(): Boolean = isIn(0..6)

fun Movie.isIn(dayRange: IntRange): Boolean {
    val openDate = openDate()
    if (openDate != null) {
        return ChronoUnit.DAYS.between(today(), openDate) in dayRange
    }
    return false
}

private fun Movie.openDate(): LocalDate? = openDate.split(".").let {
    return if (it.size == 3) {
        LocalDate.of(it[0].toInt(), it[1].toInt(), it[2].toInt())
    } else {
        null
    }
}

private fun CgvInfo?.eggIsOver(target: Int): Boolean {
    return if (this == null || egg.isBlank() || egg == "?") {
        false
    } else {
        egg >= target.toString()
    }
}

private fun LotteInfo?.starIsOver(target: Double): Boolean {
    return if (this == null || star.isBlank()) {
        false
    } else {
        star >= target.toString()
    }
}

private fun MegaboxInfo?.starIsOver(target: Double): Boolean {
    return if (this == null || star.isBlank()) {
        false
    } else {
        star >= target.toString()
    }
}
