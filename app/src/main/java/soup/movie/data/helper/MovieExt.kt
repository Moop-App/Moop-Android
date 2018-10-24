package soup.movie.data.helper

import androidx.annotation.DrawableRes
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.R
import soup.movie.data.model.CgvInfo
import soup.movie.data.model.LotteInfo
import soup.movie.data.model.MegaboxInfo
import soup.movie.data.model.Movie

fun Movie.toShareDescription(): String = "제목: $title\n개봉일: $openDate\n연령제한: $age"

fun Movie.toDescription(): String = "$openDate / $age"

@DrawableRes
fun Movie.getAgeBackground(): Int = when (age) {
    "전체 관람가" -> R.drawable.bg_tag_age_all
    "12세 관람가" -> R.drawable.bg_tag_age_12
    "15세 관람가" -> R.drawable.bg_tag_age_15
    "청소년관람불가" -> R.drawable.bg_tag_age_19
    else -> R.drawable.bg_tag_age_unknown
}

fun Movie.getSimpleAgeLabel(): String = when (age) {
    "전체 관람가" -> "전체"
    "12세 관람가" -> "12"
    "15세 관람가" -> "15"
    "청소년관람불가" -> "청불"
    else -> "미정"
}

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
